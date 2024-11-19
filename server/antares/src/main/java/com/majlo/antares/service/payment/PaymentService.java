package com.majlo.antares.service.payment;

import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.dtos.reservation.SeatReservationTicketTypeDto;
import com.majlo.antares.model.User;
import com.majlo.antares.model.location.TicketPrice;
import com.majlo.antares.model.location.TicketType;
import com.majlo.antares.model.reservation.EventSeatStatus;
import com.majlo.antares.model.transaction.TransactionEntity;
import com.majlo.antares.model.transaction.TransactionEntityItem;
import com.majlo.antares.repository.location.TicketPriceRepository;
import com.majlo.antares.repository.location.TicketTypeRepository;
import com.majlo.antares.repository.reservation.EventSeatStatusRepository;
import com.majlo.antares.repository.transaction.TransactionEntityItemRepository;
import com.majlo.antares.repository.transaction.TransactionEntityRepository;
import com.majlo.antares.service.TicketService;
import com.majlo.antares.service.UserService;
import com.majlo.antares.service.reservation.EventSeatStatusService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private final TransactionEntityRepository transactionEntityRepository;
    private final TransactionEntityItemRepository transactionEntityItemRepository;
    private final EventSeatStatusRepository eventSeatStatusRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketPriceRepository ticketPriceRepository;

    private final TicketService ticketService;
    private final UserService userService;
    private final EventSeatStatusService eventSeatStatusService;

    public PaymentService(TransactionEntityRepository transactionEntityRepository, TransactionEntityItemRepository transactionEntityItemRepository, EventSeatStatusRepository eventSeatStatusRepository, UserService userService, EventSeatStatusService eventSeatStatusService, TicketTypeRepository ticketTypeRepository, TicketPriceRepository ticketPriceRepository, TicketService ticketService) {
        this.transactionEntityRepository = transactionEntityRepository;
        this.transactionEntityItemRepository = transactionEntityItemRepository;
        this.eventSeatStatusRepository = eventSeatStatusRepository;
        this.userService = userService;
        this.eventSeatStatusService = eventSeatStatusService;
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketPriceRepository = ticketPriceRepository;
        this.ticketService = ticketService;
    }

    /** Payment for multiple seats */
    @Transactional
    public TransactionEntity payForMultipleSeats(
            List<SeatReservationTicketTypeDto> seatReservations,
            Long userId,
            String paymentMethod,
            String discountCode) throws IOException {

        if (seatReservations.isEmpty()) {
            return null;
        }
        TransactionEntity transactionEntity = createTransaction(userId, paymentMethod);
        double totalAmount = 0.0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        for (SeatReservationTicketTypeDto reservation : seatReservations) {
            EventSeatStatus seatStatus = eventSeatStatusRepository
                    .findById(reservation.getEventSeatStatusId())
                    .orElseThrow(() -> new RuntimeException("Seat not found for event"));

            if (seatStatus.isPaid()) {
                throw new RuntimeException("Seat already paid for");
            }

//            TicketType ticketType = ticketTypeRepository.findById(reservation.getTicketTypeId())
//                    .orElseThrow(() -> new RuntimeException("Ticket type not found"));

            TicketPrice ticketPrice = ticketPriceRepository.findById(reservation.getTicketTypeId())
                    .orElseThrow(() -> new RuntimeException("Ticket price not found"));

//            /** Update seat status */
//            eventSeatStatusRepository.save(seatStatus);

            /** Create transaction item */
//            double seatPrice = seatStatus.getSeatPrice(ticketType);
            double seatPrice = ticketPrice.getPrice();



            String eventName = seatStatus.getEvent().getName();
            String seatNumber = seatStatus.getSeat().getSeatNumber().toString();
            String seatRow = seatStatus.getSeat().getRow().getRowNumber().toString();
            String sectorName = seatStatus.getSector().getName();
            String ticketType = ticketPrice.getTicketType().getName();
            String price = String.valueOf(ticketPrice.getPrice());
            String eventDate = seatStatus.getEvent().getEventDateStart().format(formatter);

            String ticketPdfLink = ticketService.generateTicketPdf(
                    eventName,
                    seatNumber,
                    seatRow,
                    sectorName,
                    ticketType,
                    price,
                    eventDate
            );


            TransactionEntityItem transactionItem = TransactionEntityItem.builder()
                    .transactionEntity(transactionEntity)
                    .seatStatus(seatStatus)
                    .originalPrice(seatPrice)
//                    .finalPrice(calculateFinalPrice(seatStatus, discountCode))
                    .finalPrice(seatPrice)
                    .ticketType(ticketPrice.getTicketType().getName())
                    .purchaseDate(LocalDateTime.now())
                    .ticketPdfLink(ticketPdfLink)
                    .build();

            totalAmount += transactionItem.getFinalPrice();

            transactionEntityItemRepository.save(transactionItem);
            seatStatus.setTransactionEntityItem(transactionItem);
            eventSeatStatusRepository.save(seatStatus);


            eventSeatStatusService.markAsPaid(seatStatus.getId());
        }

        transactionEntity.setTotalAmount(totalAmount);

        return transactionEntityRepository.save(transactionEntity);
    }

    /** Create new transaction */
    private TransactionEntity createTransaction(Long userId, String paymentMethod) {

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .user(user)
//                .paymentMethod(paymentMethod)
                .transactionDate(LocalDateTime.now())
                .build();
        return transactionEntityRepository.save(transactionEntity);
    }

    /** Calculate final price with discount */
//    private double calculateFinalPrice(EventSeatStatus seatStatus, String discountCode) {
//        double originalPrice = seatStatus.getTransactionEntityItem().
//        double discount = 0.0;
//
//        // TODO: Implement discount codes
////        if (discountCode != null && !discountCode.isEmpty()) {
////
////        }
//
//        return originalPrice - discount;
//    }
}

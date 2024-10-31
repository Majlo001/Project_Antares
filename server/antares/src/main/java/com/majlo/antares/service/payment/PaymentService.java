package com.majlo.antares.service.payment;

import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.dtos.reservation.SeatReservationTicketTypeDto;
import com.majlo.antares.model.User;
import com.majlo.antares.model.location.TicketType;
import com.majlo.antares.model.reservation.EventSeatStatus;
import com.majlo.antares.model.transaction.TransactionEntity;
import com.majlo.antares.model.transaction.TransactionEntityItem;
import com.majlo.antares.repository.location.TicketTypeRepository;
import com.majlo.antares.repository.reservation.EventSeatStatusRepository;
import com.majlo.antares.repository.transaction.TransactionEntityItemRepository;
import com.majlo.antares.repository.transaction.TransactionEntityRepository;
import com.majlo.antares.service.UserService;
import com.majlo.antares.service.reservation.EventSeatStatusService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final TransactionEntityRepository transactionEntityRepository;
    private final TransactionEntityItemRepository transactionEntityItemRepository;
    private final EventSeatStatusRepository eventSeatStatusRepository;
    private final TicketTypeRepository ticketTypeRepository;

    private final UserService userService;
    private final EventSeatStatusService eventSeatStatusService;

    public PaymentService(TransactionEntityRepository transactionEntityRepository, TransactionEntityItemRepository transactionEntityItemRepository, EventSeatStatusRepository eventSeatStatusRepository, UserService userService, EventSeatStatusService eventSeatStatusService, TicketTypeRepository ticketTypeRepository) {
        this.transactionEntityRepository = transactionEntityRepository;
        this.transactionEntityItemRepository = transactionEntityItemRepository;
        this.eventSeatStatusRepository = eventSeatStatusRepository;
        this.userService = userService;
        this.eventSeatStatusService = eventSeatStatusService;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    /** Payment for multiple seats */
    @Transactional
    public TransactionEntity payForMultipleSeats(
            List<SeatReservationTicketTypeDto> seatReservations,
            Long userId,
            String paymentMethod,
            String discountCode) {
        TransactionEntity transactionEntity = createTransaction(userId, paymentMethod);
        double totalAmount = 0.0;

        for (SeatReservationTicketTypeDto reservation : seatReservations) {
            EventSeatStatus seatStatus = eventSeatStatusRepository
                    .findById(reservation.getEventSeatStatusId())
                    .orElseThrow(() -> new RuntimeException("Seat not found for event"));

            if (seatStatus.isPaid()) {
                throw new RuntimeException("Seat already paid for");
            }

            TicketType ticketType = ticketTypeRepository.findById(reservation.getTicketTypeId())
                    .orElseThrow(() -> new RuntimeException("Ticket type not found"));

//            /** Update seat status */
//            eventSeatStatusRepository.save(seatStatus);

            /** Create transaction item */
            double seatPrice = seatStatus.getSeatPrice(ticketType);


            TransactionEntityItem transactionItem = TransactionEntityItem.builder()
                    .transactionEntity(transactionEntity)
                    .seatStatus(seatStatus)
                    .originalPrice(seatPrice)
//                    .finalPrice(calculateFinalPrice(seatStatus, discountCode))
                    .finalPrice(seatPrice)
                    .purchaseDate(LocalDateTime.now())
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

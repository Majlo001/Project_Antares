package com.majlo.antares.service.payment;

import com.majlo.antares.dtos.reservation.SeatReservationRequestDto;
import com.majlo.antares.model.User;
import com.majlo.antares.model.reservation.EventSeatStatus;
import com.majlo.antares.model.transaction.TransactionEntity;
import com.majlo.antares.model.transaction.TransactionEntityItem;
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

    private final UserService userService;
    private final EventSeatStatusService eventSeatStatusService;

    public PaymentService(TransactionEntityRepository transactionEntityRepository, TransactionEntityItemRepository transactionEntityItemRepository, EventSeatStatusRepository eventSeatStatusRepository, UserService userService, EventSeatStatusService eventSeatStatusService) {
        this.transactionEntityRepository = transactionEntityRepository;
        this.transactionEntityItemRepository = transactionEntityItemRepository;
        this.eventSeatStatusRepository = eventSeatStatusRepository;
        this.userService = userService;
        this.eventSeatStatusService = eventSeatStatusService;
    }

    /** Payment for multiple seats */
    @Transactional
    public TransactionEntity payForMultipleSeats(List<SeatReservationRequestDto> seatRequests, Long userId, String paymentMethod, String discountCode) {
        TransactionEntity transactionEntity = createTransaction(userId, paymentMethod);
        Double totalAmount = 0.0;

        for (SeatReservationRequestDto request : seatRequests) {
            EventSeatStatus seatStatus = eventSeatStatusRepository
                    .findBySeatIdAndEventId(request.getSeatId(), request.getEventId())
                    .orElseThrow(() -> new RuntimeException("Seat not found for event"));

            if (seatStatus.isPaid()) {
                throw new RuntimeException("Seat already paid for");
            }

//            /** Update seat status */
//            eventSeatStatusRepository.save(seatStatus);

            /** Create transaction item */
            TransactionEntityItem transactionItem = TransactionEntityItem.builder()
                    .transactionEntity(transactionEntity)
                    .seatStatus(seatStatus)
//                    .originalPrice(seatStatus.getSeat().getSeatPrice())
//                    .finalPrice(calculateFinalPrice(seatStatus, discountCode))
                    .finalPrice(100.50)
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

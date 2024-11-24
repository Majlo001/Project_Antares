package com.majlo.antares.dtos.tickets;

import com.majlo.antares.model.transaction.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTicketDto {
    private Long ticketId;
    private String eventName;
    private Long eventId;
    private String eventLocation;
    private Long eventLocationId;
    private LocalDateTime eventDate;
    private String sectorName;
    private Integer rowNumber;
    private Integer seatNumber;
    private Double ticketPrice;
    private String ticketTypeName;
    private String ticketPdfLink;
    private Boolean isValidated;

    public static UserTicketDto fromTicket(Ticket ticket) {
        return UserTicketDto.builder()
                .ticketId(ticket.getId())
                .eventName(ticket.getEvent().getName())
                .eventId(ticket.getEvent().getId())
                .eventLocation(ticket.getEvent().getLocation().getName())
                .eventLocationId(ticket.getEvent().getLocation().getId())
                .eventDate(ticket.getEvent().getEventDateStart())
                .sectorName(ticket.getTransactionEntityItem().getSeatStatus().getSector().getName())
                .rowNumber(ticket.getTransactionEntityItem().getSeatStatus().getSeat().getRow().getRowNumber())
                .seatNumber(ticket.getTransactionEntityItem().getSeatStatus().getSeat().getSeatNumber())
                .ticketPrice(ticket.getTransactionEntityItem().getFinalPrice())
                .ticketTypeName(ticket.getTransactionEntityItem().getTicketType().getName())
                .ticketPdfLink(ticket.getTicketPdfLink())
                .isValidated(ticket.getIsValidated())
                .build();
    }
}

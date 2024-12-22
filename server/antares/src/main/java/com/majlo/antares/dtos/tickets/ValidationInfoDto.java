package com.majlo.antares.dtos.tickets;

import com.majlo.antares.model.transaction.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationInfoDto {
    private Long ticketId;
    private String eventName;
    private String eventLocation;
    private LocalDateTime eventDate;
    private String sectorName;
    private Integer rowNumber;
    private Integer seatNumber;
    private String ticketTypeName;

    public static ValidationInfoDto fromTicket(Ticket ticket) {
        return ValidationInfoDto.builder()
                .ticketId(ticket.getId())
                .eventName(ticket.getEvent().getName())
                .eventLocation(ticket.getEvent().getLocation().getName())
                .eventDate(ticket.getEvent().getEventDateStart())
                .sectorName(ticket.getTransactionEntityItem().getSeatStatus().getSector().getName())
                .rowNumber(ticket.getTransactionEntityItem().getSeatStatus().getSeat().getRow().getRowNumber())
                .seatNumber(ticket.getTransactionEntityItem().getSeatStatus().getSeat().getSeatNumber())
                .ticketTypeName(ticket.getTransactionEntityItem().getTicketType().getName())
                .build();


    }
}

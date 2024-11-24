package com.majlo.antares.model.transaction;

import com.majlo.antares.model.User;
import com.majlo.antares.model.events.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "antares_ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User ticketOwner;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private TransactionEntityItem transactionEntityItem;

    private String ticketPdfLink;
    private String validationUuid;
    private Boolean isValidated;
    private LocalDateTime validationDate;

    public void validateTicket() {
        this.isValidated = true;
        this.validationDate = LocalDateTime.now();
    }
}

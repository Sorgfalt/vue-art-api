package com.vueart.api.entity;

import com.vueart.api.core.enums.Code;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", nullable = false)
    private ExhibitionInfo exhibitionInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bought_quantity", nullable = false)
    private int boughtQuantity;

    @Column(name = "reserved_date", nullable = false)
    private LocalDateTime reservedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Code.ReservationStatus status;

    public void cancel() {
        this.status = Code.ReservationStatus.CANCELLED;
    }
}

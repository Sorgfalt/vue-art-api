package com.vueart.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "TICKET_INFO")
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", nullable = false)
    private ExhibitionInfo exhibitionInfo;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "ticket_inventory", nullable = false)
    private Integer ticketInventory;

    @Column(name = "ticket_name", nullable = false)
    private String ticketName; // 원래 number였는데 문자열로 해석

    // 예약 시 호출
    public void reserve(int quantity) {
        if (this.ticketInventory < quantity) {
            throw new IllegalStateException("잔여 수량 부족");
        }
        this.ticketInventory -= quantity;
    }

    // 예약 취소 시 호출
    public void cancelReservation(int quantity) {
        if (this.ticketInventory < quantity) {
            throw new IllegalStateException("예약 취소 수량 오류");
        }
        this.ticketInventory += quantity;
    }

}

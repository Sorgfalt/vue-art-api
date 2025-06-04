package com.vueart.api.dto.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long reservationId;
    private Long ticketId;
    private Long exhibitionId;
    private Long userId;
    private Integer boughtQuantity;
    private String status;
    private LocalDateTime reservedDate;
}


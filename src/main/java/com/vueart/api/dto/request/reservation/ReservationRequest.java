package com.vueart.api.dto.request.reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {
    private Long ticketId;
    private Long exhibitionId;
    private Long userId;
    private Integer boughtQuantity;
}


package com.vueart.api.dto.request.reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationCancelRequest {
    private Long reservationId;
    private Long userId; // 선택적으로 검증용
}

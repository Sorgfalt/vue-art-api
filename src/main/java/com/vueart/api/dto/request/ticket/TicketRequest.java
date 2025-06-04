package com.vueart.api.dto.request.ticket;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TicketRequest(
        @NotNull(message = "ticketName은 필수입니다.")
        String ticketName,
        @NotNull(message = "price는 필수입니다.")
        Integer price,
        @NotNull(message = "startDate는 필수입니다.")
        LocalDateTime startDate,
        @NotNull(message = "endDate는 필수입니다.")
        LocalDateTime endDate,
        @NotNull(message = "totalQuantity는 필수입니다.")
        Integer totalQuantity,
        @NotNull(message = "exhibition 정보는 필수 입니다.")
        Integer exhibitionId
) {
}

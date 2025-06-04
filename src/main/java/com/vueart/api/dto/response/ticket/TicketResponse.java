package com.vueart.api.dto.response.ticket;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TicketResponse {
    private Long ticketId;
    private Long exhibitionId;
    private String ticketName;
    private Integer price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer totalQuantity;
}

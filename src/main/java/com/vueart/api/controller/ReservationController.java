package com.vueart.api.controller;

import com.vueart.api.common.response.SuccessResponse;
import com.vueart.api.core.enums.Code;
import com.vueart.api.dto.request.reservation.ReservationRequest;
import com.vueart.api.dto.response.reservation.ReservationResponse;
import com.vueart.api.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public SuccessResponse reserve(@RequestBody ReservationRequest request) {
        reservationService.reserve(request.getUserId(), request.getTicketId(), request.getBoughtQuantity());
        return new SuccessResponse(Code.ApiResponseCode.CREATED_EXHIBITION_INFO.getMessage());
    }

    @PostMapping("/{reservationId}/cancel")
    public SuccessResponse cancel(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return new SuccessResponse(Code.ApiResponseCode.CREATED_EXHIBITION_INFO.getMessage());
    }

    public List<ReservationResponse> getUserReservations(@RequestParam Long userId) {
        return reservationService.getUserReservations(userId).stream()
                .map(reservation -> ReservationResponse.builder()
                        .reservationId(reservation.getId())
                        .ticketId(reservation.getTicket().getId())
                        .exhibitionId(reservation.getExhibitionInfo().getId())
                        .userId(reservation.getUser().getId())
                        .boughtQuantity(reservation.getBoughtQuantity())
                        .status(reservation.getStatus().name())
                        .reservedDate(reservation.getReservedDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}


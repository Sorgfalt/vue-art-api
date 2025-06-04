package com.vueart.api.service.reservation;

import com.vueart.api.entity.Reservation;

import java.util.List;

public interface ReservationService {
    public List<Reservation> getUserReservations(Long userId);
    public void cancelReservation(Long reservationId);
    public void reserve(Long userId, Long ticketId, int quantity);
}

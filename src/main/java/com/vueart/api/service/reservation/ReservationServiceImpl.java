package com.vueart.api.service.reservation;

import com.vueart.api.core.enums.Code;
import com.vueart.api.core.exception.VueArtApiException;
import com.vueart.api.entity.ExhibitionInfo;
import com.vueart.api.entity.Reservation;
import com.vueart.api.entity.Ticket;
import com.vueart.api.entity.User;
import com.vueart.api.repository.exhibitioninfo.ExhibitionInfoRepository;
import com.vueart.api.repository.reservation.ReservationRepository;
import com.vueart.api.repository.ticket.TicketRepository;
import com.vueart.api.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional
    public void reserve(Long userId, Long ticketId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다"));
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 티켓입니다"));
        ExhibitionInfo exhibitionInfo = ticket.getExhibitionInfo();

        if (ticket.getTicketInventory() < quantity) {
            throw new VueArtApiException(Code.ErrorCode.INSUFFICIENT_TICKET_QUANTITY);
        }

        ticket.reserve(quantity);

        Reservation reservation = Reservation.builder()
                .user(user)
                .ticket(ticket)
                .exhibitionInfo(exhibitionInfo)
                .boughtQuantity(quantity)
                .reservedDate(LocalDateTime.now())
                .status(Code.ReservationStatus.CONFIRMED)
                .build();

        reservationRepository.save(reservation);


    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 예약입니다"));

        reservation.cancel();
        reservation.getTicket().cancelReservation(reservation.getBoughtQuantity());
    }

    @Transactional()
    public List<Reservation> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
}


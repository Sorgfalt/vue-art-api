package com.vueart.api.service.ticket;

import com.vueart.api.core.enums.Code;
import com.vueart.api.core.exception.VueArtApiException;
import com.vueart.api.dto.request.ticket.TicketRequest;
import com.vueart.api.dto.response.ticket.TicketResponse;
import com.vueart.api.entity.ExhibitionInfo;
import com.vueart.api.entity.Ticket;
import com.vueart.api.repository.exhibitioninfo.ExhibitionInfoRepository;
import com.vueart.api.repository.ticket.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final ExhibitionInfoRepository exhibitionInfoRepository;

    @Override
    @Transactional
    public void createTicket(TicketRequest req) {
        System.out.println("ticket debug");

        // 🟢 exhibitionId로 엔티티 조회
        ExhibitionInfo exhibitionInfo = exhibitionInfoRepository.findById(req.exhibitionId().longValue())
                .orElseThrow(() -> new VueArtApiException(Code.ErrorCode.NOT_FOUND_EXHIBITION));

        Ticket ticket = Ticket.builder()
                .ticketName(req.ticketName())
                .price(req.price())
                .startDate(req.startDate())
                .endDate(req.endDate())
                .totalQuantity(req.totalQuantity())
                .ticketInventory(req.totalQuantity())
                .exhibitionInfo(exhibitionInfo)
                .build();

        ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public void updateTicket(Long id, TicketRequest req) {
        Ticket existingTicket = ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("티켓 정보가 존재하지 않습니다. ID : "+id));
        Ticket updatedTicket = existingTicket.toBuilder()
                .ticketName(req.ticketName())
                .price(req.price())
                .startDate(req.startDate())
                .endDate(req.endDate())
                .totalQuantity(req.totalQuantity())
                .build();
        ticketRepository.save(updatedTicket);

    }

    public TicketResponse getTicketById(Long id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        return TicketResponse.builder()
                .ticketId(ticket.getId())
                .exhibitionId(ticket.getExhibitionInfo().getId()) // 관계 맺었다는 가정
                .ticketName(ticket.getTicketName())
                .price(ticket.getPrice())
                .startDate(ticket.getStartDate())
                .endDate(ticket.getEndDate())
                .totalQuantity(ticket.getTotalQuantity())
                .build();
    }
}

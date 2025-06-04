package com.vueart.api.service.ticket;

import com.vueart.api.dto.request.ticket.TicketRequest;
import com.vueart.api.dto.response.ticket.TicketResponse;

public interface TicketService {
    void createTicket(TicketRequest req);
    void updateTicket(Long id, TicketRequest req);

    TicketResponse getTicketById(Long id);
}

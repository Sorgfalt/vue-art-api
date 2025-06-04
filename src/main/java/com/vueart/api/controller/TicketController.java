package com.vueart.api.controller;

import com.vueart.api.common.response.SuccessResponse;
import com.vueart.api.core.enums.Code;
import com.vueart.api.dto.request.ticket.TicketRequest;
import com.vueart.api.dto.response.ticket.TicketResponse;
import com.vueart.api.entity.Ticket;
import com.vueart.api.service.ticket.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
@Slf4j
@Tag(name = "Ticket", description = "티켓 관련")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public SuccessResponse createTicket(@RequestBody TicketRequest req) {
        ticketService.createTicket(req);
        return new SuccessResponse(Code.ApiResponseCode.CREATED_TICKET.getMessage());
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

}

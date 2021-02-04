package org.example;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Controller("/ticket")
public class TicketController {
    private static final String FAKE_TOKEN = "FAKE_TOKEN";

    private final TicketRepository ticketRepository;

    @Inject
    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Get(value = "/id-new", produces = MediaType.TEXT_PLAIN)
    public String newId() {
        try {
            return Long.toHexString(ticketRepository.newTicket());
        } catch (InterruptedException e) {
            return "false";
        }
    }

    @Get(value = "/id-is-valid/{id}", produces = MediaType.TEXT_PLAIN)
    public boolean isIdValid(@PathVariable String id) {
        return ticketRepository.isValid(Long.valueOf(id, 16));
    }

    @Get(value = "/id-count{?token}", produces = MediaType.TEXT_PLAIN)
    public long countIds(@Nullable String token) {
        if (!FAKE_TOKEN.equals(token)) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, "");
        }

        return ticketRepository.count();
    }

    @Post(value = "/id-invalidate/{id}{?token}", produces = MediaType.TEXT_PLAIN)
    public boolean invalidateId(@PathVariable String id, @Nullable String token) {
        if (!FAKE_TOKEN.equals(token)) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, "");
        }

        return ticketRepository.invalidate(Long.valueOf(id, 16));
    }
}
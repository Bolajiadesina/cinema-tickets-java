package uk.gov.dwp.uc.pairtest.domain;

import org.springframework.beans.factory.annotation.Value;
import jakarta.validation.constraints.NotNull;
import uk.gov.dwp.uc.pairtest.exception.InvalidCustomerUserTypeException;

/**
 * Immutable Object
 */

public class TicketTypeRequest {

    @Value("${TYPE_NON_NULL}")
    private String typeNonNull;
    @Value("${MESSAGE_MIN_TICKET}")
    private String messageMinimumTicket;
    @Value("${MESSAGE_MAX_TICKET}")
    private String messageMaximumTicket;

    @NotNull
    private final int noOfTickets;
    @NotNull
    private final Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        if (type == null)
            throw new InvalidCustomerUserTypeException(typeNonNull);
        if (noOfTickets < 1)
            throw new IllegalArgumentException(messageMinimumTicket);
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT, CHILD, INFANT
    }

}

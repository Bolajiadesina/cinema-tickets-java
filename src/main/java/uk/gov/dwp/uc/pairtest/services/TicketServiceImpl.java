package uk.gov.dwp.uc.pairtest.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.Null;
import uk.gov.dwp.uc.pairtest.domain.TicketSummary;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.utils.TicketAndAccountsValidations;

@Service
public class TicketServiceImpl implements TicketService {

    

    @Value("${MIN_TICKET}")
    private int minTicket;
    @Value("${MAX_TICKET}")
    private int maxTicket;
    
    private final TicketProcessor ticketProcessor;
    private final BookTicketAndReserveSeat bookTicketAndReserveSeat;
    private final TicketAndAccountsValidations ticketAndAccountsValidations;
    private TicketSummary ticketSummary = null;

    public TicketServiceImpl(
            TicketProcessor ticketProcessor,
            BookTicketAndReserveSeat bookTicketAndReserveSeat,
            TicketAndAccountsValidations ticketAndAccountsValidations) {

        this.ticketProcessor = ticketProcessor;
        this.bookTicketAndReserveSeat = bookTicketAndReserveSeat;
        this.ticketAndAccountsValidations = ticketAndAccountsValidations;
    }

    public TicketTypeRequest createTicketRequest(TicketTypeRequest.Type type, int noOfTickets) {
        if(type.equals("") || type.equals(null)  ){
            throw new InvalidPurchaseException("Ticket Type cannot be null");
        }



        if (noOfTickets < minTicket || noOfTickets > maxTicket) {
            throw new IllegalArgumentException(
                    "Number of tickets must be between " + minTicket + " and " + maxTicket);
        }
        return new TicketTypeRequest(type, noOfTickets);
    }


    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {

        ticketAndAccountsValidations.validateAccountId(accountId);
        ticketSummary = ticketProcessor.processTickets(ticketTypeRequests);
        ticketAndAccountsValidations.validateTicketPurchase(ticketSummary);
        bookTicketAndReserveSeat.makePaymentAndReserveSeats(accountId, ticketSummary);
    }

}

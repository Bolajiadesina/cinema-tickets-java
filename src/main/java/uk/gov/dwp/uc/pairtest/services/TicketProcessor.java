package uk.gov.dwp.uc.pairtest.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.dwp.uc.pairtest.domain.TicketSummary;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

@Component
public class TicketProcessor {

    @Value("${CHILD_TICKET_PRICE}")
    private int childTicketPrice;
    @Value("${ADULT_TICKET_PRICE}")
    private int adultTicketPrice;

    public TicketSummary processTickets(TicketTypeRequest[] ticketRequests) {


        int totalTickets = 0;
        int adultTickets = 0;
        int childTickets = 0;
        int infantTickets = 0;
        int totalAmountToPay = 0;

        for (TicketTypeRequest request : ticketRequests) {
            int numberOfTickets = request.getNoOfTickets();
            totalTickets += numberOfTickets;

            switch (request.getTicketType()) {
                case ADULT:
                    adultTickets += numberOfTickets;
                    totalAmountToPay += numberOfTickets * adultTicketPrice;
                    break;
                case CHILD:
                    childTickets += numberOfTickets;
                    totalAmountToPay += numberOfTickets * childTicketPrice;
                    break;
                case INFANT:
                    infantTickets += numberOfTickets;
                    break;
            }
        }

        return new TicketSummary(totalTickets, adultTickets, childTickets, infantTickets, totalAmountToPay);
    }

}

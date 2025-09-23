package uk.gov.dwp.uc.pairtest.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import uk.gov.dwp.uc.pairtest.domain.TicketSummary;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

@Component
@PropertySource("classpath:application.properties")
public class TicketAndAccountsValidations {

    @Autowired
    private Environment env;
    public   int minTicket;
    public  int maxTicket;

    @PostConstruct
    public void init() {
        minTicket = Integer.parseInt(env.getProperty("MIN_TICKET"));
        maxTicket = Integer.parseInt(env.getProperty("MAX_TICKET"));
    }
     
    public int getMinTicket() { return minTicket; }
    public int getMaxTicket() { return maxTicket; }


    public void validateAccountId(Long accountId) {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID.");
        }
    }

    public void validateTicketPurchase(TicketSummary summary) {
       
        if (summary.getTotalTickets() > maxTicket) {
            throw new InvalidPurchaseException("Only a maximum of " + maxTicket + " tickets that can be purchased at a time.");
        }

        if (summary.getAdultTickets() == 0 && (summary.getChildTickets() > 0 || summary.getInfantTickets() > 0)) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without purchasing an Adult ticket.");
        }
    }
    
   
}

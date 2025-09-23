package uk.gov.dwp.uc.pairtest.domain;

import jakarta.validation.constraints.NotNull;

public class TicketSummary {
    @NotNull
    private final int totalTickets;
    @NotNull
    private final int adultTickets;
    @NotNull
    private final int childTickets;
    @NotNull
    private final int infantTickets;
    @NotNull
    private final int totalAmountToPay;

    public TicketSummary(int totalTickets, int adultTickets, int childTickets, int infantTickets,
            int totalAmountToPay) {
        this.totalTickets = totalTickets;
        this.adultTickets = adultTickets;
        this.childTickets = childTickets;
        this.infantTickets = infantTickets;
        this.totalAmountToPay = totalAmountToPay;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getAdultTickets() {
        return adultTickets;
    }

    public int getChildTickets() {
        return childTickets;
    }

    public int getInfantTickets() {
        return infantTickets;
    }

    public int getTotalAmountToPay() {
        return totalAmountToPay;
    }
}

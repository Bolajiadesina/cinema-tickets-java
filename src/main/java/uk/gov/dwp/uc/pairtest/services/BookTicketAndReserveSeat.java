package uk.gov.dwp.uc.pairtest.services;

import org.springframework.stereotype.Service;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketSummary;

@Service
public class BookTicketAndReserveSeat {

    // private final ExecutorService executorService;
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    public BookTicketAndReserveSeat(
        // ExecutorService executorService,
            TicketPaymentService ticketPaymentService,
            SeatReservationService seatReservationService) {
        // this.executorService = executorService;
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    public void makePaymentAndReserveSeats(Long accountId, TicketSummary summary) {

        // Future<?> paymentFuture = executorService.submit(() -> {
        // try {
        ticketPaymentService.makePayment(accountId, summary.getTotalAmountToPay());
        // } catch (Exception e) {
        // throw new RuntimeException("Payment failed: " + e.getMessage());
        // }
        // });

        // Future<?> reservationFuture = executorService.submit(() -> {
        // try {
        seatReservationService.reserveSeat(accountId, summary.getAdultTickets() + summary.getChildTickets());
        // } catch (Exception e) {
        // throw new RuntimeException("Seat reservation failed: " + e.getMessage());
        // }
        // });
    }

}

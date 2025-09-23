package uk.gov.dwp.uc.pairtest;

import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidCustomerUserTypeException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.services.BookTicketAndReserveSeat;
import uk.gov.dwp.uc.pairtest.services.TicketProcessor;
import uk.gov.dwp.uc.pairtest.services.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.utils.TicketAndAccountsValidations;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CinemaTicketsApplicationTests {

    @Autowired
    private TicketServiceImpl ticketService;
    @Autowired
    private TicketProcessor ticketProcessor;
    @Autowired
    private BookTicketAndReserveSeat bookTicketAndReserveSeat;
    @Autowired
    private TicketAndAccountsValidations ticketAccountValidations;
    @Autowired
    private TicketPaymentService ticketPaymentService;
    @Autowired
    private SeatReservationService seatReservationService;

    @BeforeEach
    public void setup() {
        // ticketAccountValidations = new TicketAndAccountsValidations(); // Remove this
        // line
        ticketPaymentService = mock(TicketPaymentService.class);
        seatReservationService = mock(SeatReservationService.class);
        ticketProcessor = new TicketProcessor();
        bookTicketAndReserveSeat = new BookTicketAndReserveSeat(ticketPaymentService, seatReservationService);
        ticketService = new TicketServiceImpl(ticketProcessor, bookTicketAndReserveSeat, ticketAccountValidations);
    }

    @Test
    void testPropertiesAreLoadedCorrectly() {
        System.out.println("Min Ticket: " + ticketAccountValidations.getMinTicket());
        System.out.println("Max Ticket: " + ticketAccountValidations.getMaxTicket());
        assertNotNull(ticketAccountValidations);
        assertTrue(ticketAccountValidations.getMaxTicket() > 0, "MAX_TICKET should be positive");
    }

    @Test
    public void testPurchaseTickets_ValidInput() {
        TicketTypeRequest adultTickets = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest childTickets = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infantTickets = new TicketTypeRequest(Type.INFANT, 1);

        assertDoesNotThrow(() -> ticketService.purchaseTickets(12345L, adultTickets, childTickets, infantTickets));
    }

    @Test
    public void testPurchaseTickets_InvalidAccountId() {
        TicketTypeRequest adultTickets = new TicketTypeRequest(Type.ADULT, 2);

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(-1L, adultTickets);
        });

        assertEquals("Invalid account ID.", exception.getMessage());
    }

    @Test
    public void testPurchaseTickets_NoAdultTicket() {
        TicketTypeRequest childTickets = new TicketTypeRequest(Type.CHILD, 2);

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(12345L, childTickets);
        });

        assertEquals("Child and Infant tickets cannot be purchased without purchasing an Adult ticket.",
                exception.getMessage());
    }

    @Test
    public void testPurchaseTickets_TooManyTickets() {
        TicketTypeRequest adultTickets = new TicketTypeRequest(Type.ADULT, 26);

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(12345L, adultTickets);
        });

        assertEquals("Only a maximum of 25 tickets that can be purchased at a time.", exception.getMessage());
    }

    @Test
    public void testPurchaseTickets_InfantsExceedAdults() {
        TicketTypeRequest adultTickets = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest infantTickets = new TicketTypeRequest(Type.INFANT, 2);

        assertDoesNotThrow(() -> ticketService.purchaseTickets(12345L, adultTickets, infantTickets));

    }

    @Test
    void testValidTicketTypeRequest() {
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        assertEquals(TicketTypeRequest.Type.ADULT, request.getTicketType());
        assertEquals(2, request.getNoOfTickets());
    }

    

}

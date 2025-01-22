package coworking;
import coworking.databases.DAO.ReservationDAO;
import coworking.databases.DAO.WorkspaceDAO;
import coworking.databases.models.Reservation;
import coworking.databases.models.User;
import coworking.databases.models.Workspace;
import coworking.databases.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerTest {
    private Customer customer;
    @Captor
    ArgumentCaptor<Workspace> workspaceCaptor;
    @Mock
    private Scanner mockScanner;
    @Mock
    private WorkspaceDAO mockWorkspaceDAO;
    @Mock
    private ReservationDAO mockReservationDAO;
    @Mock
    private ReservationService mockReservationService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer(mockScanner, mockWorkspaceDAO, mockReservationDAO, mockReservationService);
    }

    @Test
    void givenDifferentScenarios_whenBrowseAvailableSpaces_thenVerifyBehavior() {
        // Given
        List<Workspace> workspaces = List.of(
                new Workspace("Art", 50.0),
                new Workspace("Tech", 40.0)
        );

        List<Workspace> emptyWorkspaces = Collections.emptyList();

        when(mockWorkspaceDAO.findAvailableWorkspaces())
                .thenReturn(workspaces)
                .thenReturn(emptyWorkspaces);

        // When
        List<Workspace> result1 = customer.browseAvailableSpaces();
        List<Workspace> result2 = customer.browseAvailableSpaces();

        // Then
        assertNotNull(result1);
        assertFalse(result1.isEmpty());
        assertEquals("Art", result1.get(0).getType());
        assertEquals(result2, emptyWorkspaces);

        verify(mockWorkspaceDAO, times(2)).findAvailableWorkspaces();
    }


    @Test
    void givenDifferentScenarios_whenMakeAReservation_thenVerifyBehavior() {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            // Given
            List<Workspace> workspaces = List.of(
                    new Workspace("Art", 50.0),
                    new Workspace("Tech", 40.0)
            );

            when(mockWorkspaceDAO.findAvailableWorkspaces()).thenReturn(workspaces);

            mockedCheckMethods
                    .when(() -> CheckMethods.checkDate(anyString(), anyString()))
                    .thenAnswer(invocation -> {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String s = invocation.getArgument(0);
                        try {
                            LocalDate.parse(s, formatter);
                            return true;
                        } catch (DateTimeParseException e) {
                            System.out.println("Wrong date format. Please enter another one: ");
                            return false;
                        }
                    });

            when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(0);

            when(mockScanner.next())
                    .thenReturn("John")
                    .thenReturn("03-03-2025")
                    .thenReturn("05-03-2025");
            // When
            customer.makeAReservation();

            // Then
            verify(mockScanner, times(2)).nextInt();
            verify(mockScanner, times(3)).next();
            verify(mockReservationService, times(1)).makeReservation(workspaces.get(0), "John", "03-03-2025", "05-03-2025");
            mockedCheckMethods.verify(() -> CheckMethods.checkDate(anyString(), anyString()), times(2));
        }
    }

    @Test
    void givenDifferentScenarios_whenViewMyReservations_thenVerifyBehavior() {
        // Given
        List<Workspace> workspaces = List.of(
                new Workspace("Art", 50.0),
                new Workspace("Tech", 40.0)
        );

        List<User> users = List.of(
                new User("John"),
                new User("Adam")
        );
        List<Reservation> reservations = List.of(
                new Reservation(workspaces.get(0), users.get(0), LocalDate.parse("03-05-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")), LocalDate.parse("05-05-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy"))),
                new Reservation(workspaces.get(1), users.get(0), LocalDate.parse("03-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")), LocalDate.parse("05-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")))
        );

        List<Reservation> emptyReservations = Collections.emptyList();

        when(mockScanner.next()).thenReturn("John");

        when(mockReservationDAO.findMyReservations("John"))
                .thenReturn(reservations)
                .thenReturn(emptyReservations);

        // When
        List<Reservation> result1 = customer.viewMyReservations();
        List<Reservation> result2 = customer.viewMyReservations();

        // Then
        assertNotNull(result1);
        assertFalse(result1.isEmpty());
        assertEquals("John", result1.get(0).getUser().getName());
        assertEquals("Tech", result1.get(1).getWorkspace().getType());
        assertNull(result2);

        verify(mockReservationDAO, times(2)).findMyReservations("John");
    }

    @Test
    void givenDifferentScenarios_whenCancelMyReservation_thenVerifyBehavior() {
        // Given
        List<Workspace> workspaces = List.of(
                new Workspace("Art", 50.0),
                new Workspace("Tech", 40.0)
        );

        List<User> users = List.of(
                new User("John"),
                new User("Adam")
        );
        List<Reservation> reservations = List.of(
                new Reservation(workspaces.get(0), users.get(0), LocalDate.parse("03-05-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")), LocalDate.parse("05-05-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy"))),
                new Reservation(workspaces.get(1), users.get(1), LocalDate.parse("03-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")), LocalDate.parse("05-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")))
        );

        when(mockScanner.next()).thenReturn("John");

        when(mockReservationDAO.findMyReservations("John")).thenReturn(reservations);

        when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(0);

        // When
        customer.cancelMyReservation();

        // Then
        verify(mockScanner, times(2)).nextInt();
        verify(mockScanner, times(1)).next();
        verify(mockReservationService, times(1)).cancelMyReservation(reservations.get(0));

    }
    }




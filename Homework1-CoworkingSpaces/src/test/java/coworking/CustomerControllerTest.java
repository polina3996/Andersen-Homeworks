package coworking;
import coworking.controller.CustomerController;
import coworking.repository.ReservationRepository;
import coworking.repository.UserRepository;
import coworking.repository.WorkspaceRepository;
import coworking.model.Reservation;
import coworking.model.User;
import coworking.model.Workspace;
import coworking.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {
    private CustomerController customerController;
    @Captor
    ArgumentCaptor<Workspace> workspaceCaptor;
    @Mock
    private Scanner mockScanner;
    @Mock
    private WorkspaceRepository mockWorkspaceRepository;
    @Mock
    private ReservationRepository mockReservationRepository;
    @Mock
    private ReservationService mockReservationService;
    @Mock
    private UserRepository mockUserRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerController = new CustomerController(mockScanner, mockWorkspaceRepository, mockReservationRepository, mockReservationService, mockUserRepository);
    }

    @Test
    void givenDifferentScenarios_whenBrowseAvailableSpaces_thenVerifyBehavior() {
        // Given
        List<Workspace> workspaces = List.of(
                new Workspace("Art", 50.0),
                new Workspace("Tech", 40.0)
        );

        List<Workspace> emptyWorkspaces = Collections.emptyList();

        when(mockWorkspaceRepository.findAvailableWorkspaces())
                .thenReturn(workspaces)
                .thenReturn(emptyWorkspaces);

        // When
        List<Workspace> result1 = customerController.browseAvailableSpaces();
        List<Workspace> result2 = customerController.browseAvailableSpaces();

        // Then
        assertNotNull(result1);
        assertFalse(result1.isEmpty());
        assertEquals("Art", result1.get(0).getType());
        assertTrue(result2.isEmpty());

        verify(mockWorkspaceRepository, times(2)).findAvailableWorkspaces();
    }


    @Test
    void givenDifferentScenarios_whenMakeAReservation_thenVerifyBehavior() {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            // Given
            List<Workspace> workspaces = List.of(
                    new Workspace("Art", 50.0),
                    new Workspace("Tech", 40.0)
            );

            when(mockWorkspaceRepository.findAvailableWorkspaces()).thenReturn(workspaces);

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
            customerController.makeAReservation();

            // Then
            verify(mockScanner, times(2)).nextInt();
            verify(mockScanner, times(3)).next();
            verify(mockReservationService, times(1)).makeReservation(mockUserRepository, workspaces.get(0), "John", "03-03-2025", "05-03-2025");
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

        when(mockReservationRepository.findMyReservations("John"))
                .thenReturn(reservations)
                .thenReturn(emptyReservations);

        // When
        List<Reservation> result1 = customerController.viewMyReservations();
        List<Reservation> result2 = customerController.viewMyReservations();

        // Then
        assertNotNull(result1);
        assertFalse(result1.isEmpty());
        assertEquals("John", result1.get(0).getUser().getName());
        assertEquals("Tech", result1.get(1).getWorkspace().getType());
        assertTrue(result2.isEmpty());

        verify(mockReservationRepository, times(2)).findMyReservations("John");
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

        when(mockReservationRepository.findMyReservations("John")).thenReturn(reservations);

        when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(0);

        // When
        customerController.cancelMyReservation();

        // Then
        verify(mockScanner, times(2)).nextInt();
        verify(mockScanner, times(1)).next();
        verify(mockReservationService, times(1)).removeReservation(reservations.get(0));

    }
    }




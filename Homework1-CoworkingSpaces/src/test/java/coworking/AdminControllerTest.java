package coworking;
import coworking.controller.AdminController;
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
import java.util.*;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


class AdminControllerTest {
    private AdminController adminController;
    @Captor ArgumentCaptor<Workspace> workspaceCaptor;
    @Mock private Scanner mockScanner;
    @Mock private WorkspaceRepository mockWorkspaceRepository;
    @Mock private ReservationRepository mockReservationRepository;
    @Mock private ReservationService mockReservationService;
    @Mock private UserRepository mockUserRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(mockScanner, mockWorkspaceRepository, mockReservationRepository, mockReservationService, mockUserRepository);
    }

    @Test
    void givenWrongAndCorrectStubbing_whenAddCoworkingSpace_thenWorkspaceAdded() {
        //Given
        when(mockScanner.next()).thenReturn("Art");
        when(mockScanner.nextDouble())
                .thenThrow(new InputMismatchException())
                .thenReturn(50.0);

        // When
        adminController.addCoworkingSpace();

        // Then
        verify(mockScanner, times(1)).next();
        verify(mockScanner, times(2)).nextDouble();

        verify(mockWorkspaceRepository).save(workspaceCaptor.capture());
        Workspace capturedWorkspace = workspaceCaptor.getValue();
        assertEquals("Art", capturedWorkspace.getType());
        assertEquals(50.00, capturedWorkspace.getPrice(), 0.01);
        assertTrue(capturedWorkspace.getAvailabilityStatus());
    }

    @Test
    void givenDifferentScenarios_whenBrowseCoworkingSpaces_thenVerifyBehavior() {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            // Given
            List<Workspace> workspaces = List.of(
                    new Workspace("Art", 50.0),
                    new Workspace("Tech", 40.0)
            );
            List<Workspace> emptyWorkspaces = Collections.emptyList();

            when(mockWorkspaceRepository.findAll())
                    .thenReturn(workspaces)
                    .thenReturn(emptyWorkspaces);

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(List.class), eq("coworking spaces")))
                    .thenAnswer(invocation -> {
                        List<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No coworking spaces available.");
                        }
                        return false;
                    });

            // When
            List<Workspace> result1 = adminController.browseCoworkingSpaces();
            List<Workspace> result2 = adminController.browseCoworkingSpaces();

            // Then
            assertNotNull(result1);
            assertFalse(result1.isEmpty());
            assertEquals("Art", result1.get(0).getType());
            assertTrue(result2.isEmpty());

            verify(mockWorkspaceRepository, times(2)).findAll();
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(List.class), eq("coworking spaces")), times(2));
        }
    }

    @Test
    void givenDifferentScenarios_whenRemoveCoworkingSpace_thenVerifyBehavior()  {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            // Given
            List<Workspace> workspaces = List.of(
                    new Workspace("Art", 50.0),
                    new Workspace("Tech", 40.0)
            );

            when(mockWorkspaceRepository.findAll()).thenReturn(workspaces);

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(List.class), eq("coworking spaces")))
                    .thenAnswer(invocation -> {
                        List<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No coworking spaces available.");
                        }
                        return false;
                    });

            when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(0);

            // Act
            adminController.removeCoworkingSpace();

            // Assert
            verify(mockScanner, times(2)).nextInt();
            verify(mockWorkspaceRepository, times(1)).delete(workspaces.get(0));
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(List.class), eq("coworking spaces")), times(1));

        }
    }


    @Test
    void givenDifferentScenarios_whenViewAllReservations_thenVerifyBehavior() {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
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

            List<Reservation> emptyReservations = Collections.emptyList();

            when(mockReservationRepository.findReservations())
                    .thenReturn(reservations)
                    .thenReturn(emptyReservations);

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(List.class), eq("reservations")))
                    .thenAnswer(invocation -> {
                        List<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No reservations.");
                        }
                        return false;
                    });

            // When
            List<Reservation> result1 = adminController.viewAllReservations();
            List<Reservation> result2 = adminController.viewAllReservations();

            // Then
            assertNotNull(result1);
            assertFalse(result1.isEmpty());
            assertEquals("John", result1.get(0).getUser().getName());
            assertEquals("Tech", result1.get(1).getWorkspace().getType());
            assertTrue(result2.isEmpty());

            verify(mockReservationRepository, times(2)).findReservations();
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(List.class), eq("reservations")), times(2));
        }
    }


    @Test
    void givenDifferentScenarios_whenUpdateCoworkingSpace_thenVerifyBehavior() {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            // Given
            List<Workspace> workspaces = List.of(
                    new Workspace("Art", 50.0),
                    new Workspace("Tech", 40.0)
            );

            when(mockWorkspaceRepository.findAll()).thenReturn(workspaces);

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(List.class), eq("coworking spaces")))
                    .thenAnswer(invocation -> {
                        List<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No coworking spaces available.");
                        }
                        return false;
                    });

            when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(0);
            when(mockScanner.next()).thenReturn("Open");
            when(mockScanner.nextDouble()).thenReturn(20.0);


            // Act
            adminController.updateCoworkingSpace();

            // Assert
            verify(mockScanner, times(2)).nextInt();
            verify(mockScanner, times(1)).next();
            verify(mockScanner, times(1)).nextDouble();
            verify(mockWorkspaceRepository, times(1)).update(argThat(updatedWorkspace -> updatedWorkspace.getId() == 0 &&
                    updatedWorkspace.getType().equals("Open") &&
                    updatedWorkspace.getPrice() == 20.0));
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(List.class), eq("coworking spaces")), times(1));

        }
    }
    @Test
    void givenDifferentScenarios_whenRemoveReservation_thenVerifyBehavior() {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
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


            when(mockReservationRepository.findReservations()).thenReturn(reservations);

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(List.class), eq("reservations")))
                    .thenAnswer(invocation -> {
                        List<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No reservations.");
                        }
                        return false;
                    });

            when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(0);

            // When
            adminController.removeReservation();

            // Then
            verify(mockScanner, times(2)).nextInt();
            verify(mockReservationService, times(1)).removeReservation(reservations.get(0));
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(List.class), eq("reservations")), times(1));
        }
        }
}

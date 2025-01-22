package coworking;
import coworking.databases.DB;
import coworking.databases.models.Reservation;
import coworking.databases.models.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class AdminTest {
    private Admin admin;
    @Mock private DB mockDb;
    @Mock private Scanner mockScanner;


    @BeforeEach
    void setUp() {
        mockDb = mock(DB.class);
        mockScanner = mock(Scanner.class);
        admin = new Admin(mockDb, mockScanner);
    }

    @Test
    void givenWrongAndCorrectStubbing_whenAddCoworkingSpace_thenWorkspaceAddedOnce() {
        //Given
        when(mockDb.insertIntoWorkspaces(anyString(), anyDouble())).thenReturn(1);

        when(mockScanner.next()).thenReturn("Art");
        when(mockScanner.nextDouble())
                .thenThrow(new InputMismatchException())
                .thenReturn(50.0);

        // When
        admin.addCoworkingSpace();

        // Then
        verify(mockDb, times(1)).insertIntoWorkspaces("Art", 50.0);
        verify(mockScanner, times(1)).next();
        verify(mockScanner, times(2)).nextDouble();
    }

    @Test
    void givenDifferentScenarios_whenBrowseCoworkingSpaces_thenVerifyBehavior() throws SQLException, CheckEmptinessException {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            //Given
            List<Workspace> workspaces = List.of(
                    new Workspace(1, "Art", 50.0, true),
                    new Workspace(2, "Tech", 40.0, true)
            );
            when(mockDb.selectFromWorkspaces())
                    .thenReturn(new ArrayList<>(workspaces))
                    .thenReturn(new ArrayList<>())
                    .thenThrow(new SQLException("Database error."));

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("coworking spaces")))
                    .thenAnswer(invocation -> {
                        ArrayList<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No coworking spaces available.");
                        }
                        return false;
                    });

            // When
            boolean result1 = admin.browseCoworkingSpaces();
            boolean result2 = admin.browseCoworkingSpaces();
            boolean result3 = admin.browseCoworkingSpaces();

            // Then
            assertTrue(result1);
            assertFalse(result2);
            assertFalse(result3);

            verify(mockDb, times(3)).selectFromWorkspaces();
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("coworking spaces")), times(2));
        }
    }

    @Test
    void givenDifferentScenarios_whenRemoveCoworkingSpace_thenVerifyBehavior() throws SQLException {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            //Given
            List<Workspace> workspaces = List.of(
                    new Workspace(1, "Art", 50.0, true),
                    new Workspace(2, "Tech", 40.0, true)
            );
            when(mockDb.selectFromWorkspaces()).thenReturn(new ArrayList<>(workspaces));

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("coworking spaces")))
                    .thenAnswer(invocation -> {
                        ArrayList<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No coworking spaces available.");
                        }
                        return false;
                    });


        when(mockScanner.nextInt())
                .thenThrow(new InputMismatchException())
                .thenReturn(1);

        when(mockDb.removeFromWorkspaces(1)).thenReturn(1);

        // When
        admin.removeCoworkingSpace();

        //Then
        verify(mockDb, times(1)).selectFromWorkspaces();
        verify(mockDb, times(1)).removeFromWorkspaces(1);
        verify(mockScanner, times(2)).nextInt();
        mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("coworking spaces")), times(1));
    }
    }

    @Test
    void givenDifferentScenarios_whenViewAllReservations_thenVerifyBehavior() throws SQLException, CheckEmptinessException {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            //Given
            List<Reservation> reservations = List.of(
                    new Reservation(1, 1,"Art", "John", "2025-03-03", "2025-03-04", 50.0, "2025-01-01"),
                    new Reservation(2, 1,"Art", "Matt", "2025-03-05", "2025-03-06", 50.0, "2025-01-02")
            );
            when(mockDb.selectFromReservations())
                    .thenReturn(new ArrayList<>(reservations))
                    .thenReturn(new ArrayList<>());

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("reservations")))
                    .thenAnswer(invocation -> {
                        ArrayList<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No reservations");
                        }
                        return false;
                    });

            // When
            ArrayList<Reservation> result1 = admin.viewAllReservations();
            ArrayList<Reservation> result2 = admin.viewAllReservations();

            // Then
            assertNotNull(result1);
            assertNull(result2);

            verify(mockDb, times(2)).selectFromReservations();
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("reservations")), times(2));
        }
    }


    @Test
    void givenDifferentScenarios_whenUpdateCoworkingSpace_thenVerifyBehavior() throws SQLException {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            //Given
            List<Workspace> workspaces = List.of(
                    new Workspace(1, "Art", 50.0, true),
                    new Workspace(2, "Tech", 40.0, true)
            );
            when(mockDb.selectFromWorkspaces()).thenReturn(new ArrayList<>(workspaces));

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("coworking spaces")))
                    .thenAnswer(invocation -> {
                        ArrayList<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No coworking spaces available.");
                        }
                        return false;
                    });

            when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(1);

            when(mockDb.selectFromWorkspacesById(1)).thenReturn(new ArrayList<>(workspaces).get(0));

            when(mockScanner.next())
                    .thenReturn("Art");

            when(mockScanner.nextDouble())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(20.0);

            when(mockDb.updateWorkspace("Art", 20.0, 1)).thenReturn(1);

            // When
            admin.updateCoworkingSpace();

            //Then
            verify(mockDb, times(1)).selectFromWorkspaces();
            verify(mockDb, times(1)).selectFromWorkspacesById(1);
            verify(mockDb, times(1)).updateWorkspace("Art", 20.0, 1);
            verify(mockScanner, times(2)).nextInt();
            verify(mockScanner, times(1)).next();
            verify(mockScanner, times(2)).nextDouble();
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("coworking spaces")), times(1));
        }
    }
    @Test
    void givenDifferentScenarios_whenRemoveReservation_thenVerifyBehavior() throws SQLException, CheckEmptinessException {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)) {
            //Given
            List<Reservation> reservations = List.of(
                    new Reservation(1, 1,"Art", "John", "2025-03-03", "2025-03-04", 50.0, "2025-01-01"),
                    new Reservation(2, 1,"Art", "Matt", "2025-03-05", "2025-03-06", 50.0, "2025-01-02")
            );
            when(mockDb.selectFromReservations())
                    .thenReturn(new ArrayList<>(reservations));

            mockedCheckMethods
                    .when(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("reservations")))
                    .thenAnswer(invocation -> {
                        ArrayList<?> list = invocation.getArgument(0);
                        if (list.isEmpty()) {
                            throw new CheckEmptinessException("No reservations");
                        }
                        return false;
                    });

            when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                    .thenReturn(1);

            when(mockDb.updateAvailabilityStatus(true, 1)).thenReturn(1);
            when(mockDb.removeFromMyReservations(1)).thenReturn(1);

            // When
            admin.removeReservation();

            // Then
            verify(mockDb, times(1)).selectFromReservations();
            verify(mockDb, times(1)).updateAvailabilityStatus(true, 1);
            verify(mockDb, times(1)).removeFromMyReservations(1);
            mockedCheckMethods.verify(() -> CheckMethods.checkEmptiness(any(ArrayList.class), eq("reservations")), times(1));
        }
    }
}

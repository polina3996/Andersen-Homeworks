package coworking;
import coworking.databases.DB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerTest {
    private Customer customer;
    @Mock private DB mockDb;
    @Mock private Scanner mockScanner;


    @BeforeEach
    void setUp() {
        mockDb = mock(DB.class);
        mockScanner = mock(Scanner.class);
        customer = new Customer(mockDb, mockScanner);
    }

    @Test
    void givenDifferentScenarios_whenBrowseAvailableSpaces_thenVerifyBehavior() throws SQLException {
        //Given
        List<Workspace> workspaces = List.of(
                new Workspace(1, "Art", 50.0, true),
                new Workspace(2, "Tech", 40.0, true),
                new Workspace(3, "Open", 30.0, true)
        );
        when(mockDb.selectAvailableWorkspaces()).
                thenReturn(new ArrayList<>(workspaces))
                .thenReturn(new ArrayList<>());

        //When
        ArrayList<Workspace> result1 = customer.browseAvailableSpaces();
        ArrayList<Workspace> result2 = customer.browseAvailableSpaces();

        //Then
        assertNotNull(result1);
        assertEquals(result2, new ArrayList<Workspace>());
        verify(mockDb, times(2)).selectAvailableWorkspaces();
    }

    @Test
    void givenDifferentScenarios_whenMakeAReservation_thenVerifyBehavior() throws SQLException {
        try (MockedStatic<CheckMethods> mockedCheckMethods = mockStatic(CheckMethods.class)){
            //Given
            List<Workspace> workspaces = List.of(
                new Workspace(1, "Art", 50.0, true),
                new Workspace(2, "Tech", 40.0, true),
                new Workspace(3, "Open", 30.0, true)
            );
            when(mockDb.selectAvailableWorkspaces()).
                thenReturn(new ArrayList<>(workspaces))
                .thenReturn(new ArrayList<>());

            when(mockScanner.nextInt())
                    .thenThrow(new InputMismatchException())
                            .thenReturn(1);

            when(mockScanner.next())
                    .thenReturn("John")
                    .thenReturn("2025-03-03")
                    .thenReturn("2025-03-05");

            mockedCheckMethods
                    .when(() -> CheckMethods.checkDate(anyString(), anyString()))
                    .thenAnswer(invocation -> {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String s = invocation.getArgument(0);
                        try {
                            LocalDate.parse(s, formatter);
                            return true;
                        } catch (DateTimeParseException e) {
                            System.out.println("Wrong date format. Please enter another one: ");
                            return false;
                        }
                    });


            when(mockDb.insertIntoReservations(1, "John","2025-03-03", "2025-03-05")).thenReturn(1);

            when(mockDb.updateAvailabilityStatus(false, 1)).thenReturn(1);


            //When
            customer.makeAReservation();

            //Then
            verify(mockDb, times(1)).selectAvailableWorkspaces();
            verify(mockDb, times(1)).insertIntoReservations(1, "John","2025-03-03", "2025-03-05" );
            verify(mockDb, times(1)).updateAvailabilityStatus(false,1);
            verify(mockScanner, times(2)).nextInt();
            verify(mockScanner, times(3)).next();
            mockedCheckMethods.verify(() -> CheckMethods.checkDate(anyString(), anyString()), times(2));

    }
    }

    @Test
    void givenDifferentScenarios_whenViewMyReservations_thenVerifyBehavior() throws SQLException {
        //Given
        List<Reservation> reservations = List.of(
                new Reservation(1, 1,"Art", "John", "2025-03-03", "2025-03-04", 50.0, "2025-01-01"),
                new Reservation(2, 2,"Open", "John", "2025-03-05", "2025-03-06", 60.0, "2025-01-02")
        );
        when(mockScanner.next()).thenReturn("John");

        when(mockDb.selectFromMyReservations("John")).
                    thenReturn(new ArrayList<>(reservations))
                    .thenReturn(new ArrayList<>());

        //When
        ArrayList<Reservation> result1 = customer.viewMyReservations();
        ArrayList<Reservation> result2 = customer.viewMyReservations();

        //Then
        verify(mockDb, times(2)).selectFromMyReservations("John");
        verify(mockScanner, times(2)).next();
        assertEquals(result1, reservations);
        assertNull(result2);
        }

    @Test
    void givenDifferentScenarios_whenCancelMyReservation_thenVerifyBehavior() throws SQLException {
        //Given
        List<Reservation> reservations = List.of(
                new Reservation(1, 1,"Art", "John", "2025-03-03", "2025-03-04", 50.0, "2025-01-01"),
                new Reservation(2, 2,"Open", "John", "2025-03-05", "2025-03-06", 60.0, "2025-01-02")
        );
        when(mockScanner.next()).thenReturn("John");

        when(mockDb.selectFromMyReservations("John")).
                thenReturn(new ArrayList<>(reservations));

        when(mockScanner.nextInt())
                .thenThrow(new InputMismatchException())
                .thenReturn(1);

        when(mockDb.updateAvailabilityStatus(true, 1)).thenReturn(1);

        when(mockDb.removeFromMyReservations(1)).thenReturn(1);


        //When
        customer.cancelMyReservation();

        //Then
        verify(mockDb, times(1)).selectFromMyReservations("John");
        verify(mockScanner, times(1)).next();
        verify(mockScanner, times(2)).nextInt();
        verify(mockDb, times(1)).updateAvailabilityStatus(true, 1);
        verify(mockDb, times(1)).removeFromMyReservations(1);
    }
}




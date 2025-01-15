package coworking;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import static junit.framework.TestCase.*;

public class CustomerTest {
    private Customer customer;
    private FakeFileSaverReader fakeFileSaverReader;
    private ArrayList<Workspace> workspaceArray;
    private ArrayList<Reservation> reservationsArray;


    @BeforeEach
    void setUp() {
        fakeFileSaverReader = new FakeFileSaverReader();
        workspaceArray = new ArrayList<>();
        reservationsArray = new ArrayList<>();

        workspaceArray.add(new Workspace(1, "Type1", 20.0));
        workspaceArray.add(new Workspace(2, "Type2", 30.0));
        workspaceArray.get(0).setAvailabilityStatus(true);
        workspaceArray.get(1).setAvailabilityStatus(false);

        fakeFileSaverReader.saveToFile(workspaceArray, "workspaces.ser");

        customer = new Customer(fakeFileSaverReader, new Scanner(""));
        customer.workspaceArray = workspaceArray;
        customer.reservationsArray = reservationsArray;
    }

    @Test
    void givenWorkspaces_whenBrowseAvailableSpaces_thenGetOneWorkspace() {
        //When
        ArrayList<Workspace> availableWorkspaces = customer.browseAvailableSpaces();

        //Then
        assertEquals(1, availableWorkspaces.size());
        assertTrue(availableWorkspaces.stream().allMatch(Workspace::getAvailabilityStatus));

        Workspace workspace = availableWorkspaces.get(0);
        assertEquals(1, workspace.getId());
        assertEquals("Type1", workspace.getType());
        assertEquals(20.0, workspace.getPrice());

    }

    @Test
    void givenInput_whenMakeAReservation_thenReservationIsAddedAndWorkspaceStatusIsChanged() {
        //Given
        customer.scanner = new Scanner(new ByteArrayInputStream("1\nJohn\n01-01-2025\n03-01-2025\n".getBytes()));

        //When
        customer.makeAReservation();

        //Then
        assertEquals(1, fakeFileSaverReader.readFromReservationsFile().size());

        Reservation reservation = fakeFileSaverReader.readFromReservationsFile().get(0);
        assertEquals("John", reservation.getName());
        assertEquals(1, reservation.getWorkspaceId());
        assertEquals(LocalDate.parse("01-01-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")), reservation.getStart());
        assertEquals(LocalDate.parse("03-01-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")), reservation.getEnd());
        assertFalse(fakeFileSaverReader.readFromWorkspacesFile().get(0).getAvailabilityStatus());
    }

    @Test
    void givenReservationAndInput_whenViewMyReservations_thenReservationIsReturned() {
        //Given
        customer.scanner = new Scanner(new ByteArrayInputStream("1\nJohn\n01-01-2025\n03-01-2025\n".getBytes()));
        customer.makeAReservation();
        fakeFileSaverReader.saveToFile(customer.reservationsArray, "reservations.ser");

        customer.scanner = new Scanner("John\n");

        //When+Then
        assertTrue(customer.viewMyReservations());
        assertEquals(1, fakeFileSaverReader.readFromReservationsFile().size());

        Reservation reservation = fakeFileSaverReader.readFromReservationsFile().get(0);
        assertEquals("John", reservation.getName());
        assertEquals(1, reservation.getWorkspaceId());
        assertEquals(LocalDate.parse("01-01-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")), reservation.getStart());
        assertEquals(LocalDate.parse("03-01-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")), reservation.getEnd());
        assertFalse(fakeFileSaverReader.readFromWorkspacesFile().get(0).getAvailabilityStatus());
    }

    @Test
    void givenReservationAndInput_whenCancelMyReservations_thenReservationIsRemovedAndWorkspaceStatusIsChanged() {
        //Given
        customer.scanner = new Scanner(new ByteArrayInputStream("1\nJohn\n01-01-2025\n03-01-2025\n".getBytes()));
        customer.makeAReservation();
        UUID uuid = customer.reservationsArray.get(0).getId();
        fakeFileSaverReader.saveToFile(customer.reservationsArray, "reservations.ser");

        customer.scanner = new Scanner("John\n" + uuid);

        //When
        customer.cancelMyReservation();

        //Then
        assertTrue(fakeFileSaverReader.readFromReservationsFile().isEmpty());
        assertTrue(fakeFileSaverReader.readFromWorkspacesFile().get(0).getAvailabilityStatus());
    }

}

package coworking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import static junit.framework.TestCase.*;
import java.util.ArrayList;
import java.util.UUID;


class AdminTest {
    private Admin admin;
    private Customer customer;
    private FakeFileSaverReader fakeFileSaverReader;
    private ArrayList<Workspace> workspaceArray;
    private ArrayList<Reservation> reservationsArray;


    @BeforeEach
    void setUp() {
        fakeFileSaverReader = new FakeFileSaverReader();
        workspaceArray = new ArrayList<>();
        reservationsArray = new ArrayList<>();
        Scanner scanner = new Scanner("");

        admin = new Admin(fakeFileSaverReader, scanner);
        admin.workspaceArray = workspaceArray;
        admin.reservationsArray = reservationsArray;

        customer = new Customer(fakeFileSaverReader, scanner);
        customer.workspaceArray = workspaceArray;
        customer.reservationsArray = reservationsArray;

    }

    @Test
    void givenInput_whenAddCoworkingSpace_thenWorkspaceAdded() {
        //Given
        admin.scanner = new Scanner(new ByteArrayInputStream("1\nTestType\n20\n".getBytes()));

        // When
        admin.addCoworkingSpace();

        // Then
        assertEquals(1, admin.workspaceArray.size());

        Workspace workspace = admin.workspaceArray.get(0);
        assertEquals(1, workspace.getId());
        assertEquals("TestType", workspace.getType());
        assertEquals(20.0, workspace.getPrice());

        ArrayList<Workspace> loadedWorkspaces = fakeFileSaverReader.readFromWorkspacesFile();
        assertEquals(1, loadedWorkspaces.size());
        Workspace savedWorkspace = loadedWorkspaces.get(0);
        assertEquals(1, savedWorkspace.getId());
        assertEquals("TestType", savedWorkspace.getType());
        assertEquals(20.0, savedWorkspace.getPrice());
    }

    @Test
    void givenCoworkingSpaces_whenBrowseCoworkingSpaces_thenReturnTrue() {
        //Given Coworking space
        admin.scanner = new Scanner(new ByteArrayInputStream("1\nTestType\n20\n".getBytes()));
        admin.addCoworkingSpace();

        // When + Then
        assertTrue(admin.browseCoworkingSpaces());
    }

    @Test
    void givenNoCoworkingSpaces_whenBrowseCoworkingSpaces_thenReturnFalse() {
        //Given(nothing) + When + Then
        assertFalse(admin.browseCoworkingSpaces());
    }


    @Test
    void givenCoworkingSpaces_whenRemoveCoworkingSpace_thenNoWorkspaceAndReservation() {
        //Given
        admin.scanner = new Scanner(new ByteArrayInputStream("1\nTestType\n20\n".getBytes()));
        admin.addCoworkingSpace();

        admin.scanner = new Scanner(new ByteArrayInputStream("1\n".getBytes()));

        // When
        admin.removeCoworkingSpace();

        // Then
        assertEquals(0, admin.workspaceArray.size());
        assertEquals(0, admin.reservationsArray.size());
        assertEquals(0, fakeFileSaverReader.readFromWorkspacesFile().size());
        assertEquals(0, fakeFileSaverReader.readFromReservationsFile().size());

    }
    @Test
    void givenReservation_whenViewAllReservations_thenReturnTrue() {
        //Given
        admin.scanner = new Scanner(new ByteArrayInputStream("1\nTestType\n20\n".getBytes()));
        admin.addCoworkingSpace();

        customer.scanner = new Scanner(new ByteArrayInputStream("1\nJohn\n01-01-2025\n03-01-2025\n".getBytes()));
        customer.makeAReservation();

        //When + Then
        assertTrue(admin.viewAllReservations());

    }

    @Test
    void givenNoReservation_whenViewAllReservations_thenReturnFalse() {
        //Given
        admin.scanner = new Scanner(new ByteArrayInputStream("1\nTestType\n20\n".getBytes()));
        admin.addCoworkingSpace();

        customer.scanner = new Scanner(new ByteArrayInputStream("1\nJohn\n01-01-2025\n03-01-2025\n".getBytes()));

        //When + Then
        assertFalse(admin.viewAllReservations());
    }

    @Test
    void givenCoworkingSpacesAndReservation_whenUpdateCoworkingSpace_thenWorkspaceAndReservationUpdated(){
        //Given
        admin.scanner = new Scanner(new ByteArrayInputStream("1\nTestType\n20\n".getBytes()));
        admin.addCoworkingSpace();
        customer.scanner = new Scanner(new ByteArrayInputStream("1\nJohn\n01-01-2025\n03-01-2025\n".getBytes()));
        customer.makeAReservation();

        admin.scanner = new Scanner(new ByteArrayInputStream("1\n2\nNewType\n30\n".getBytes()));

        // When
        admin.updateCoworkingSpace();

        // Then
        Workspace updatedWorkspace = fakeFileSaverReader.readFromWorkspacesFile().get(0);
        assertNotNull(updatedWorkspace);
        assertEquals(2, updatedWorkspace.getId());
        assertEquals("NewType", updatedWorkspace.getType());
        assertEquals(30.0, updatedWorkspace.getPrice());

        Reservation updatedReservation = fakeFileSaverReader.readFromReservationsFile().stream()
                .filter(reservation -> reservation.getWorkspaceId() == 2)
                .findFirst()
                .orElse(null);

        if (updatedReservation != null) {
            assertEquals("NewType", updatedReservation.getType());
            assertEquals(30.0, updatedReservation.getPrice());
        }
    }

    @Test
    void givenReservation_whenRemoveReservation_thenReservationRemovedAndCoworkingUpdated(){
        //Given
        admin.scanner = new Scanner(new ByteArrayInputStream("1\nTestType\n20\n".getBytes()));
        admin.addCoworkingSpace();
        customer.scanner = new Scanner(new ByteArrayInputStream("1\nJohn\n01-01-2025\n03-01-2025\n".getBytes()));
        customer.makeAReservation();

        UUID uuid = customer.reservationsArray.get(0).getId();

        ArrayList<Reservation> reservationsBefore = fakeFileSaverReader.readFromReservationsFile();
        assertEquals(1, reservationsBefore.size());

        admin.scanner = new Scanner(String.valueOf(uuid));

        //When
        admin.removeReservation();

        //Then
        ArrayList<Reservation> reservationsAfter = fakeFileSaverReader.readFromReservationsFile();
        assertEquals(0, reservationsAfter.size());

        ArrayList<Workspace> workspaces = fakeFileSaverReader.readFromWorkspacesFile();
        assertTrue(workspaces.get(0).getAvailabilityStatus());
    }
}

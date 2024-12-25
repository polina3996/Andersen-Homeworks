import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

/**
 * Browse available spaces, make reservations and cancel bookings
 * - select a workspace
 * - enter booking details(ex. name, date, time(start/end))
 * - store the reservation details in memory
 * - see their reservations and cancel them by selecting the reservation ID
 */
public class Customer {
    FileSaverReader fileSaverReader;
    Scanner scanner;
    ArrayList<Workspace> workspaceArray;
    ArrayList<Reservation> reservationsArray;

    public Customer(FileSaverReader fileSaverReader, Scanner scanner, ArrayList<Workspace> workspaceArray, ArrayList<Reservation> reservationsArray) {
        this.fileSaverReader = fileSaverReader;
        this.scanner = scanner;
        this.workspaceArray = workspaceArray;
        this.reservationsArray = reservationsArray;

    }

    public ArrayList<Workspace> browseAvailableSpaces() {
        //conversion WORKING SPACES into Stream-collection, that executes next method
        this.workspaceArray = this.fileSaverReader.readWorkspacesFromFile();

        ArrayList<Workspace> availableWorkspaces = new ArrayList<Workspace>(this.workspaceArray.stream()
                .filter(item -> item.getAvailabilityStatus())
                .toList());

        if (availableWorkspaces.isEmpty()) {
            System.out.println("There are no available spaces");
            return new ArrayList<Workspace>(); // empty array
        }

        System.out.println("Here are available coworking spaces for you:");
        System.out.println(availableWorkspaces);
//        for (Workspace item : availableWorkspaces) {
//            System.out.println(item);
        return availableWorkspaces;
    }

    public boolean checkPresence(int i, ArrayList<Workspace> availableWorkspaces) {
        for (Workspace workspace: availableWorkspaces) {
            if (workspace.getId() == i) {
                return true;
            };
        }
        System.out.println("No such workspace in a list of available workspaces. Please enter another ID: ");
        return false;
    }

    public void makeAReservation() {
        ArrayList<Workspace> availableWorkspaces = browseAvailableSpaces();
        if (!Objects.equals(availableWorkspaces, new ArrayList<>())) { //shows available spaces(with id+messages) and returns true/false
            System.out.println("Choose the id of any available space: ");
            System.out.println("id - ");
            int id;
            do {
                id = this.scanner.nextInt();
            } while (!checkPresence(id, availableWorkspaces));

            System.out.println("Type in all data to make a reservation: ");
            System.out.println("your name - ");
            String name = this.scanner.next();
            System.out.println("date of start in dd-mm-yyyy format - ");
            String start = this.scanner.next();
            System.out.println("date of end in dd-mm-yyyy format - ");
            String end = this.scanner.next();

            for (Workspace item : this.workspaceArray) {
                if (item.getId() == id) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    UUID uniqueKey = UUID.randomUUID();
                    Reservation reservation = new Reservation(uniqueKey, id, item.getType(), name,
                        LocalDate.parse(start, formatter), LocalDate.parse(end, formatter),
                        item.getPrice());
                    item.setAvailabilityStatus(false);
                    this.reservationsArray.add(reservation);
                    this.fileSaverReader.saveReservationsToFile(this.reservationsArray);
                    this.fileSaverReader.saveWorkspacesToFile(this.workspaceArray);
                }
            }
            System.out.println("New reservation was made successfully!");
        }
    }

    public boolean viewMyReservations() {
        this.reservationsArray = this.fileSaverReader.readReservationsFromFile();
        this.workspaceArray = this.fileSaverReader.readWorkspacesFromFile();
        System.out.println("Type in your name: ");
        System.out.println("name - ");
        String name = this.scanner.next();

        if (this.reservationsArray.isEmpty()) {
            System.out.println("You have no reservations yet");
            return false;
        }
        else {
            System.out.println("Here are your reservations: ");
            for (Reservation item: this.reservationsArray) {
                if (item.getName().equals(name)) {
                    System.out.println(item);
                }
                else {
                    System.out.println("You have no reservations yet");
                    return false;
                }
            }
            return true;
        }
    }

    public void cancelMyReservation() {
        if (!viewMyReservations()) { //shows reservations(with id+messages) and returns true/false
            return;
        }

        System.out.println("Choose the reservation you want to cancel by id: ");
        System.out.println("id(just copy it) - ");
        String id = this.scanner.nextLine();

        //conversion ALL RESERVATIONS into Stream-collection, that executes next methods 1 by 1
        // and creates a var reservationToBeCancelled for further action(Workspace..)
        Reservation reservationToBeCancelled = this.reservationsArray.stream()
                .filter(item -> item.getId().toString().equals(id)) //uuid -> id(string) and compares
                .findFirst() // finds 1st one reservation and writes to reservationToBeCancelled
                .orElse(null); //if nothing was found - reservationToBeCancelled = null

        if (reservationToBeCancelled == null) {
            System.out.println("No reservations with such id");
            return;
        }
        // this line is enough to remove item(without previous 4 lines)
        this.reservationsArray.removeIf(item -> item.getId().toString().equals(id));


        for (Workspace item : this.workspaceArray) {
            if (item.getId() == reservationToBeCancelled.getWorkspaceId()) {
                item.setAvailabilityStatus(true);
            }
        }
        this.fileSaverReader.saveReservationsToFile(this.reservationsArray);
        this.fileSaverReader.saveWorkspacesToFile(this.workspaceArray);
        System.out.println("Your reservation was cancelled successfully!");
    }
}

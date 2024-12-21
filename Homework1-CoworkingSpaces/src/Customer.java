import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    Scanner scanner;
    ArrayList<Workspace> workspaceArray;
    ArrayList<Reservation> reservationsArray;

    public Customer(Scanner scanner, ArrayList<Workspace> workspaceArray, ArrayList<Reservation> reservationsArray) {
        this.scanner = scanner;
        this.workspaceArray = workspaceArray;
        this.reservationsArray = reservationsArray;

    }

    public boolean browseAvailableSpaces() {
        //conversion WORKING SPACES into Stream-collection, that executes next method
        ArrayList<Workspace> availableWorkspaces = new ArrayList<Workspace>(this.workspaceArray.stream()
                .filter(item -> item.availabilityStatus)
                .toList());

        if (availableWorkspaces.isEmpty()) {
            System.out.println("There are no coworking spaces");
            return false;
        }

        System.out.println("Here are available coworking spaces for you:");
        for (Workspace item : availableWorkspaces) {
            System.out.println(item);
        }
        return true;
    }

    public void makeAReservation() {
        if (browseAvailableSpaces()) {
            System.out.println("Choose the id of any available space: ");
            System.out.println("id - ");
            int id = this.scanner.nextInt();
            System.out.println("Type in all data to make a reservation: ");
            System.out.println("your name - ");
            String name = this.scanner.next();
            System.out.println("date of start in dd-mm-yyyy format - ");
            String start = this.scanner.next();
            System.out.println("date of end in dd-mm-yyyy format - ");
            String end = this.scanner.next();

            for (Workspace item : this.workspaceArray) {
                if (item.id == id) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); //create a format of date
                    UUID uniqueKey = UUID.randomUUID();
                    Reservation reservation = new Reservation(uniqueKey, id, item.type, name,
                        LocalDate.parse(start, formatter), LocalDate.parse(end, formatter),
                        item.price);
                    item.availabilityStatus = false;
                    this.reservationsArray.add(reservation);
                }
            }
            System.out.println("New reservation was made successfully!");
        }
    }

    public boolean viewMyReservations() {
        System.out.println("Type in your name: ");
        System.out.println("name - ");
        String name = this.scanner.nextLine();

        if (this.reservationsArray.isEmpty()) {
            System.out.println("You have no reservations yet");
            return false;
        }
        else {
            System.out.println("Here are your reservations: ");
            for (Reservation item: this.reservationsArray) {
                if (item.name.equals(name)) {
                    System.out.println(item);
                }
            }
            return true;
        }
    }

    public void cancelMyReservation() {
        if (!viewMyReservations()) {
            return;
        }

        System.out.println("Choose the reservation you want to cancel by id: ");
        System.out.println("id(just copy it) - ");
        String id = this.scanner.nextLine();

        //conversion ALL RESERVATIONS into Stream-collection, that executes next methods 1 by 1
        // and creates a var reservationToBeCancelled for further action(Workspace..)
        Reservation reservationToBeCancelled = this.reservationsArray.stream()
                .filter(item -> item.id.toString().equals(id))
                .findFirst() // finds 1st one reservation and writes to reservationToBeCancelled
                .orElse(null); //if nothing was found - reservationToBeCancelled = null

        if (reservationToBeCancelled == null) {
            System.out.println("No reservations with such id");
            return;
        }
        // this line is enough to remove item(without previous 4 lines)
        this.reservationsArray.removeIf(item -> item.id.toString().equals(id));


        for (Workspace item : this.workspaceArray) {
            if (item.id == reservationToBeCancelled.workspaceId) {
                item.availabilityStatus = true;
            }
        }
        System.out.println("Your reservation was cancelled successfully!");
    }
    }

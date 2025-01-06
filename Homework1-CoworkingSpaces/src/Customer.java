import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public Customer(FileSaverReader fileSaverReader, Scanner scanner) {
        this.fileSaverReader = fileSaverReader;
        this.scanner = scanner;
        this.workspaceArray = this.fileSaverReader.readFromFile("workspaces.ser");
        this.reservationsArray = this.fileSaverReader.readFromFile("reservations.ser");
    }

    public ArrayList<Workspace> browseAvailableSpaces() {
        ArrayList<Workspace> availableWorkspaces = new ArrayList<Workspace>(this.workspaceArray.stream()
                .filter(Workspace::getAvailabilityStatus)
                .toList());

        try {
            CheckMethods.checkEmptiness(availableWorkspaces, "available workspaces");
            System.out.println("Here are available coworking spaces for you:");
            System.out.println(availableWorkspaces);
            return availableWorkspaces;
        }
        catch (CheckEmptinessException e){
            System.out.println(e.getMessage());
            return new ArrayList<Workspace>(); // empty array
        }
    }

    public void makeAReservation() {
        ArrayList<Workspace> availableWorkspaces = browseAvailableSpaces();//shows available spaces(with id+messages) and returns true/false
        if (availableWorkspaces == null || availableWorkspaces.isEmpty()){
            return;
        }
        try {
            CheckMethods.checkEmptiness(availableWorkspaces, "available workspaces");
            System.out.println("Choose the id of any available space: ");
            System.out.println("id - ");
            int id;

            while (true) {
                try{
                    id = this.scanner.nextInt();
                    CheckMethods.checkCoworkPresence(id, availableWorkspaces);}
                catch (InputMismatchException e) {
                    System.out.println("It's not a number!");
                    this.scanner.nextLine();
                    continue;
                }
                catch (NonPresentException e) {
                    System.out.println(e.getMessage());
                    continue; //caught an Exception - > again asks for id
                }
                break; // id is unique and number -> no exception -> stops asking for id and accepts last one
            }

            System.out.println("Type in all data to make a reservation: ");
            System.out.println("your name - ");
            String name = this.scanner.next();
            System.out.println("date of start in dd-mm-yyyy format - ");
            // TODO: process Exception if wrong format
            String start = this.scanner.next();
            System.out.println("date of end in dd-mm-yyyy format - ");
            // TODO: process Exception if wrong format
            String end = this.scanner.next();

            for (Workspace item : this.workspaceArray) {
                if (item.getId() == id) { //look for workspace which user chose
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    UUID uniqueKey = UUID.randomUUID();
                    Reservation reservation = new Reservation(uniqueKey, id, item.getType(), name,
                            LocalDate.parse(start, formatter), LocalDate.parse(end, formatter),
                            item.getPrice());
                    this.reservationsArray.add(reservation);
                    item.setAvailabilityStatus(false);
                    this.fileSaverReader.saveToFile(this.reservationsArray, "reservations.ser");
                    this.fileSaverReader.saveToFile(this.workspaceArray, "workspaces.ser");
                }
            }
            System.out.println("New reservation was made successfully!");
        }
        catch (CheckEmptinessException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean viewMyReservations() {
        System.out.println("Type in your name: ");
        System.out.println("name - ");
        String name = this.scanner.next();

        try {
            CheckMethods.checkEmptiness(this.reservationsArray, "reservations");
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
        catch (CheckEmptinessException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void cancelMyReservation() {
        if (!viewMyReservations()) { //shows reservations(with id+messages) and returns true/false
            return;
        }

        System.out.println("Choose the reservation you want to cancel by id: ");
        System.out.println("id(just copy it) - ");
        String id;
        while (true) {
            try {
                id = this.scanner.next();
                CheckMethods.checkReservPresence(id, this.reservationsArray);
            }
            catch (NonPresentException e) {
                System.out.println(e.getMessage());
                continue; //caught an Exception - > again asks for id
            }
            break; // coworking exists -> no exception -> stops asking for id and accepts last one
        }

        String finalId = id;
        Reservation reservationToBeCancelled = this.reservationsArray.stream()
                .filter(item -> item.getId().toString().equals(finalId))
                .findFirst()
                .orElse(null);

        if (reservationToBeCancelled == null) {
            System.out.println("No reservations with such id");
            return;
        }

        String finalId1 = id;
        this.reservationsArray.removeIf(item -> item.getId().toString().equals(finalId1));


        for (Workspace item : this.workspaceArray) {
            if (item.getId() == reservationToBeCancelled.getWorkspaceId()) {
                item.setAvailabilityStatus(true);
            }
        }
        this.fileSaverReader.saveToFile(this.reservationsArray, "reservations.ser");
        this.fileSaverReader.saveToFile(this.workspaceArray, "workspaces.ser");
        System.out.println("Your reservation was cancelled successfully!");
    }
}

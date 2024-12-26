import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Manages spaces and view all bookings
 * Add, remove or update coworking spaces
 */
public class Admin {
    FileSaverReader fileSaverReader;
    Scanner scanner;
    ArrayList<Workspace> workspaceArray;
    ArrayList<Reservation> reservationsArray;

    public Admin(FileSaverReader fileSaverReader, Scanner scanner,
                 ArrayList<Workspace> workspaceArray,
                 ArrayList<Reservation> reservationsArray) {
         this.fileSaverReader = fileSaverReader;
         this.scanner = scanner;
         this.workspaceArray = workspaceArray;
         this.reservationsArray = reservationsArray;
    }

    public void addCoworkingSpace() {
        System.out.println("Type in the new coworking space data: ");
        System.out.println("id(number) - ");
        int id;

        while (true) {
            try{
                id = this.scanner.nextInt();
                CheckMethods.checkNonUniquiness(id, this.workspaceArray);}
            catch (InputMismatchException e) {
                System.out.println("It's not a number!");
                this.scanner.nextLine();
                continue;
            }
            catch (NonUniqueException e) {
                System.out.println(e.getMessage());
                continue; //caught an Exception - > again asks for id
            }
            break; // id is unique and number -> no exception -> stops asking for id and accepts last one
        }

        System.out.println("type of coworking(1 word) - ");
        String type = this.scanner.next();
        System.out.println("price in $(number) - ");
        double price;

        while (true) {
            try{
                price = this.scanner.nextDouble();
            }
            catch (InputMismatchException e) {
                System.out.println("It's not a number!");
                this.scanner.nextLine();
                continue;
            }
            break; // price is number -> no exception -> stops asking for price and accepts last one
        }

        Workspace workspace = new Workspace(id, type, price);
        this.workspaceArray.add(workspace);
        this.fileSaverReader.saveWorkspacesToFile(this.workspaceArray);
        System.out.println("New coworking space added successfully!");
    }

    public boolean browseCoworkingSpaces(){
        this.workspaceArray = this.fileSaverReader.readWorkspacesFromFile();

        if (!this.workspaceArray.isEmpty()) {
            System.out.println("Here are all coworking spaces :");
            for (Workspace item : this.workspaceArray) {
                System.out.println(item);
            }
            return true;
        }
        else {
            System.out.println("There are no coworking spaces yet");
            return false;
        }
    }

    public void removeCoworkingSpace() {
        if (browseCoworkingSpaces()) { //shows all coworkings(with id) and returns true/false
            System.out.println("Type in the id of coworking space you want to remove: ");
            System.out.println("id(number) - ");
            int id;

            while (true) {
                try {
                    id = this.scanner.nextInt();
                    CheckMethods.checkCoworkPresence(id, this.workspaceArray);
                }
                catch (InputMismatchException e) {
                    System.out.println("This is not a number!");
                    this.scanner.nextLine();
                    continue;
                }
                catch (NonPresentException e) {
                    System.out.println(e.getMessage());
                    continue; //caught an Exception - > again asks for id
                }
                break; // coworking exists and id is a number-> no exception -> stops asking for id and accepts last one
            }
            int finalId = id;
            this.workspaceArray.removeIf(item -> item.getId()== finalId);
            int finalId1 = id;
            this.reservationsArray.removeIf(item -> item.getWorkspaceId()== finalId1);
            this.fileSaverReader.saveWorkspacesToFile(this.workspaceArray);
            this.fileSaverReader.saveReservationsToFile(this.reservationsArray);
            System.out.printf("Coworking space %d removed successfully", id);
        }
    }

    public boolean viewAllReservations() {
        this.reservationsArray = this.fileSaverReader.readReservationsFromFile();
        if (this.reservationsArray.isEmpty()) {
            System.out.println("There are no reservations yet");
            return false;
        }
        else {
            System.out.println(this.reservationsArray);
            return true;
        }
    }

    public void updateCoworkingSpace() {
        if (browseCoworkingSpaces()) { //shows all coworkings(with id) and returns true/false
            System.out.println("Type in the id of coworking space you want to update: ");
            System.out.println("id(number) - ");
            int id;
            while (true) {
                id = this.scanner.nextInt();
                try {
                    CheckMethods.checkCoworkPresence(id, this.workspaceArray);
                }
                catch (NonPresentException e) {
                    System.out.println(e.getMessage());
                    continue; //caught an Exception - > again asks for id
                }
                break; // coworking exists -> no exception -> stops asking for id and accepts last one
            }
            System.out.println("Type in the new id: ");
            int newId;
            while (true) {
                newId = this.scanner.nextInt();
                try {
                    CheckMethods.checkNonUniquiness(newId, this.workspaceArray);
                }
                catch (InputMismatchException e) {
                    System.out.println("This is not a number!");
                    this.scanner.nextLine();
                    continue;
                }
                catch (NonUniqueException e) {
                    System.out.println(e.getMessage());
                    continue; //caught an Exception - > again asks for newId
                }
                break; // newId is unique and number -> no exception -> stops asking for id and accepts last one
            }
            System.out.println("Type in new type(1 word) - ");
            String newType = this.scanner.next();
            System.out.println("Type in new price in $(number) - ");
            double newPrice;

            while (true) {
                try{
                    newPrice = this.scanner.nextDouble();
                }
                catch (InputMismatchException e) {
                    System.out.println("It's not a number!");
                    this.scanner.nextLine();
                    continue;
                }
                break; // newPrice is number -> no exception -> stops asking for price and accepts last one
            }
            for (Workspace workspace: this.workspaceArray) {
                if (workspace.getId() == id) {
                    workspace.setId(newId);
                    workspace.setType(newType);
                    workspace.setPrice(newPrice);
                }
            }
            for (Reservation reservation: this.reservationsArray) {
                if (reservation.getWorkspaceId() == id) {
                    reservation.setWorkspaceId(newId);
                    reservation.setType(newType);
                    reservation.setPrice(newPrice);
                } //nortification for user?
            }
            this.fileSaverReader.saveWorkspacesToFile(this.workspaceArray);
            this.fileSaverReader.saveReservationsToFile(this.reservationsArray);
            System.out.println("Coworking space changed successfully");
        }
    }

    public void removeReservation() {
        if (!viewAllReservations()) { //shows reservations(with id+messages) and returns true/false
            return; //no reservations -> message from viewAllReservations
        }

        System.out.println("Choose the reservation you want to remove by id: ");
        System.out.println("id(just copy it) - ");
        String id;
        while (true) {
            id = this.scanner.next();
            try {
                CheckMethods.checkReservPresence(id, this.reservationsArray);
            }
            catch (NonPresentException e) {
                System.out.println(e.getMessage());
                continue; //caught an Exception - > again asks for id
            }
            break; // coworking exists -> no exception -> stops asking for id and accepts last one
        }

        String finalId1 = id;
        Reservation reservationToBeCancelled = this.reservationsArray.stream()
                .filter(item -> item.getId().toString().equals(finalId1)) //uuid -> id(string) and compares
                .findFirst() // finds 1st one reservation and writes to reservationToBeCancelled
                .orElse(null); //if nothing was found - reservationToBeCancelled = null

        if (reservationToBeCancelled == null) {
            System.out.println("No reservations with such id");
            return;
        }
        String finalId2 = id;
        this.reservationsArray.removeIf(item -> item.getId().toString().equals(finalId2));

        for (Workspace item : this.workspaceArray) {
            if (item.getId() == reservationToBeCancelled.getWorkspaceId()) {
                item.setAvailabilityStatus(true);
            }
        }
        this.fileSaverReader.saveReservationsToFile(this.reservationsArray);
        this.fileSaverReader.saveWorkspacesToFile(this.workspaceArray);
        System.out.println("Reservation was cancelled successfully!");
    }
}




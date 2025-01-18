package coworking;
import coworking.databases.DB;

import java.sql.SQLException;
import java.util.*;

/**
 * Browse available spaces, make reservations and cancel bookings
 * - select a workspace
 * - enter booking details(ex. name, date, time(start/end))
 * - store the reservation details in memory
 * - see their reservations and cancel them by selecting the reservation ID
 */
public class Customer {
    Scanner scanner;
    DB db;

    public Customer(DB db, Scanner scanner) {
        this.scanner = scanner;
        this.db = db;
    }

    public ArrayList<Workspace> browseAvailableSpaces() {
        try{
            ArrayList<Workspace> availableWorkspaces = this.db.selectAvailableWorkspaces();
            System.out.println("Here are available coworking spaces for you:");
            for (Workspace workspace:availableWorkspaces){
                System.out.println(workspace);
            }
            return availableWorkspaces;
        }
        catch (NullPointerException e){
            System.out.println("No available workspaces yet");
    }
        return null;
    }

    public void makeAReservation() {
        ArrayList<Workspace> availableWorkspaces = browseAvailableSpaces();
        if (availableWorkspaces == null){
            return;
        }

        System.out.println("Choose the id of any available space: ");
        System.out.println("id - ");
        int id;
        Workspace workspaceToBeReserved = null;

        while (true) {
            try {
                id = this.scanner.nextInt();
                for (Workspace workspace : availableWorkspaces){
                    if (workspace.getId() == id){
                        workspaceToBeReserved = workspace;
                        break;
                    }
                }
                //workspaceToBeReserved = this.db.selectFromWorkspacesById(id);
                if (workspaceToBeReserved == null){
                    System.out.println("No such coworking space. Please enter another one: ");
                    continue;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("It's not a number!");
                this.scanner.nextLine();
                continue;
            }
            break; // id exists and it's number -> no exception -> stops asking for id and accepts last one
        }

        System.out.println("Type in all data to make a reservation: ");
        System.out.println("your name - ");
        String name = this.scanner.next();
        System.out.println("date of start in yyyy-MM-dd format - ");
        String start;
        while (true) {
            start = this.scanner.next();
            if (CheckMethods.checkDate(start, "yyyy-MM-dd")){
                break;
            }
            this.scanner.nextLine();
        }

        System.out.println("date of end in yyyy-mm-dd format - ");
        String end;
        while (true) {
            end = this.scanner.next();
            if (CheckMethods.checkDate(end, "yyyy-MM-dd")){
                break;
            }
            this.scanner.nextLine();
        }

        int reservationsAdded = this.db.insertIntoReservations(workspaceToBeReserved.getId(), name, start, end);
        int workspUpdated = this.db.updateAvailabilityStatus(false, workspaceToBeReserved.getId());
        System.out.println("New reservation was made successfully!");
    }

    public ArrayList<Reservation> viewMyReservations() {
        System.out.println("Type in your name: ");
        System.out.println("name - ");
        String name = this.scanner.next();

        ArrayList<Reservation> reservations = null;
        try {
            System.out.println("Here are your reservations: ");
            reservations = this.db.selectFromMyReservations(name);
            if (reservations == null || reservations.isEmpty()) {
                System.out.println("You have no reservations yet");
                return null;
            }
            for (Reservation item : reservations) {
                System.out.println(item);
            }
        } catch (CheckEmptinessException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    public void cancelMyReservation() {
        ArrayList<Reservation> reservations = viewMyReservations();
        if (reservations == null) {
            return;
        }

        System.out.println("Choose the reservation you want to cancel by id: ");
        System.out.println("id - ");
        int id;
        int workspaceToBeUpdatedId = -1;
        Reservation reservationToBeCancelled = null;

        while (true) {
            try {
                id = this.scanner.nextInt();
                for (Reservation reservation:reservations){
                    if (reservation.getId() == id){
                        reservationToBeCancelled = reservation;
                        workspaceToBeUpdatedId = reservationToBeCancelled.getWorkspaceId();
                        break;
                    }
                }
                if (reservationToBeCancelled == null){
                    System.out.println("No such reservation. Please enter another one: ");
                    continue;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("It's not a number!");
                this.scanner.nextLine();
                continue;
            }
            break; // id exists and it's number -> no exception -> stops asking for id and accepts last one
        }
        int workspaceUpdated = this.db.updateAvailabilityStatus(true, workspaceToBeUpdatedId);
        int reservationsRemoved = this.db.removeFromMyReservations(reservationToBeCancelled.getId());

        System.out.println("Your reservation was cancelled successfully!");

    }
}




package coworking;

import coworking.databases.DB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Manages spaces and view all bookings
 * Add, remove or update coworking spaces
 */
public class Admin {
    Scanner scanner;
    DB db;

    public Admin(DB db, Scanner scanner) {
         this.scanner = scanner;
         this.db = db;
    }

    public void addCoworkingSpace() {
        System.out.println("Type in the new coworking space data: ");
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

        int workspacesAdded = this.db.insertIntoWorkspaces(type, price);
        System.out.println("New coworking space added successfully!");
    }

    public boolean browseCoworkingSpaces(){
        try {
            ArrayList<Workspace> workspaces = this.db.selectFromWorkspaces();
            CheckMethods.checkEmptiness(workspaces, "coworking spaces");
            System.out.println("Here are all coworking spaces :");
            for (Workspace item : workspaces) {
                System.out.println(item);
            }
        }
        catch (CheckEmptinessException | SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void removeCoworkingSpace() {
        if (!browseCoworkingSpaces()){
            return;
        }
        System.out.println("Type in the id of coworking space you want to remove: ");
        System.out.println("id(number) - ");
        int id;

        while (true) {
            try {
                id = this.scanner.nextInt();
                int workspacesRemoved = this.db.removeFromWorkspaces(id);
                if (workspacesRemoved == 0){
                    System.out.println("No such coworking space. Please enter another one: ");
                    continue;
                }
            }
            catch (NullPointerException e) {
                System.out.println("No such coworking space. Please enter another one: ");
                this.scanner.nextLine();
                continue;
            }
            catch (InputMismatchException e) {
                System.out.println("This is not a number!");
                this.scanner.nextLine();
                continue;
            }
            break; // coworking exists and id is a number-> no exception -> stops asking for id and accepts last one
        }
        System.out.printf("Coworking space and associated reservation %d removed successfully", id);
    }


    public ArrayList<Reservation> viewAllReservations() {
        try {
            ArrayList<Reservation> reservations = this.db.selectFromReservations();
            CheckMethods.checkEmptiness(reservations, "reservations");
            System.out.println("Here are all reservations :");
            for (Reservation item : reservations) {
                System.out.println(item);
            }
            return reservations;
        }
        catch (CheckEmptinessException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void updateCoworkingSpace() {
        if (!browseCoworkingSpaces()){
            return;
        }
        System.out.println("Type in the id of coworking space you want to update: ");
        System.out.println("id(number) - ");
        int id;
        while (true) {
            try {
                id = this.scanner.nextInt();
                Workspace workspaceToBeUpdated = this.db.selectFromWorkspacesById(id);

                if (workspaceToBeUpdated == null) {
                    System.out.println("No such workspaces. Please enter another one: ");
                    continue;
            }
            }catch (NullPointerException e) {
                System.out.println("No such workspace. Please enter another one: ");
                continue;
            }
            catch (InputMismatchException | SQLException e) {
                System.out.println("This is not a number!");
                this.scanner.nextLine();
                continue;
            }
            break; // id is a number -> no exception -> stops asking for id and accepts last one
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

        int workspacesUpdated = this.db.updateWorkspace(newType, newPrice, id);
        System.out.println("Coworking space changed successfully");
        }

    public void removeReservation(){
        ArrayList<Reservation> reservations = viewAllReservations();
        if (reservations == null) {
            return;
        }

        int workspaceToBeUpdatedId = -1;
        System.out.println("Choose the reservation you want to remove by id: ");
        System.out.println("id - ");
        int id;
        Reservation reservationToBeCancelled = null;
        while (true) {
            try {
                id = this.scanner.nextInt();
                for (Reservation reservation : reservations) {
                    if (reservation.getId() == id) {
                        reservationToBeCancelled = reservation;
                        workspaceToBeUpdatedId = reservationToBeCancelled.getWorkspaceId();
                        break;
                    }
                }
                if (reservationToBeCancelled == null) {
                    System.out.println("No such reservation. Please enter another one: ");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("It's not a number!");
                this.scanner.nextLine();
                continue;
            }
            break;
        }
        int workspaceUpdated = this.db.updateAvailabilityStatus(true, workspaceToBeUpdatedId);
        int reservationsRemoved = this.db.removeFromMyReservations(reservationToBeCancelled.getId());

        System.out.println("Reservation was cancelled successfully!");
    }
}




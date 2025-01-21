package coworking;

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

    public Admin(FileSaverReader fileSaverReader, Scanner scanner) {
         this.fileSaverReader = fileSaverReader;
         this.scanner = scanner;
         this.workspaceArray = this.fileSaverReader.readFromFile("workspaces.ser") ;
         this.reservationsArray = this.fileSaverReader.readFromFile("reservations.ser");
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
        this.fileSaverReader.saveToFile(this.workspaceArray, "workspaces.ser");
        System.out.println("New coworking space added successfully!");
    }


    public boolean browseCoworkingSpaces(){
        try {
            CheckMethods.checkEmptiness(this.workspaceArray, "coworking spaces");
            System.out.println("Here are all coworking spaces :");
            for (Workspace item : this.workspaceArray) {
                System.out.println(item);
            }
        }
        catch (CheckEmptinessException e) {
            System.out.println(e.getMessage());
            return false;
            }
        return true;
    }

    public void removeCoworkingSpace() {
        if (!browseCoworkingSpaces()) {//shows all coworkings(with id) OR Exception and false
            return;
        }
        System.out.println("Type in the id of coworking space you want to remove: ");
        System.out.println("id(number) - ");
        int id;

        while (true) {
            try {
                id = this.scanner.nextInt();
                int finalId2 = id;
                Workspace workspaceToBeRemoved = workspaceArray.stream()
                        .filter(item -> item.getId()== finalId2)
                        .findFirst()
                        .orElse(null);
                if (workspaceToBeRemoved == null){
                    System.out.println("No such coworking space. Please enter another one: ");
                    continue;
                }
                //CheckMethods.checkCoworkPresence(id, this.workspaceArray);
            }
            catch (InputMismatchException e) {
                System.out.println("This is not a number!");
                this.scanner.nextLine();
                continue;
            }
            break; // coworking exists and id is a number-> no exception -> stops asking for id and accepts last one
        }
        int finalId = id;
        this.workspaceArray.removeIf(item -> item.getId()== finalId);
        int finalId1 = id;
        this.reservationsArray.removeIf(item -> item.getWorkspaceId()== finalId1);
        this.fileSaverReader.saveToFile(this.workspaceArray, "workspaces.ser");
        this.fileSaverReader.saveToFile(this.reservationsArray, "reservations.ser");
        System.out.printf("Coworking space %d removed successfully", id);
    }


    public boolean viewAllReservations() {
        try {
            CheckMethods.checkEmptiness(this.reservationsArray, "reservations");
            System.out.println("Here are all reservations :");
            for (Reservation item : this.reservationsArray) {
                System.out.println(item);
            }
        }
        catch (CheckEmptinessException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


    public void updateCoworkingSpace() {
        if (!browseCoworkingSpaces()){  //shows all coworkings(with id) OR Exception and false
            return;
        }
        System.out.println("Type in the id of coworking space you want to update: ");
        System.out.println("id(number) - ");
        int id;
        while (true) {
            try {
                id = this.scanner.nextInt();
                int finalId = id;
                Workspace workspaceToBeUpdated = this.workspaceArray.stream()
                        .filter(item -> item.getId() == finalId)
                        .findFirst()
                        .orElse(null);
                if (workspaceToBeUpdated == null) {
                    System.out.println("No workspaces with such id. Please enter another one: ");
                    continue;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("This is not a number!");
                this.scanner.nextLine();
                continue;
            }
            break; // coworking exists -> no exception -> stops asking for id and accepts last one
        }
        System.out.println("Type in the new id: ");
        int newId;
        while (true) {
            try {
                newId = this.scanner.nextInt();
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

        //updating it in an array of CoworkingSpaces
        int finalId1 = id;
        Workspace workspaceToBeUpdated = this.workspaceArray.stream()
                .filter(item -> item.getId()==finalId1)
                .findFirst()
                .orElse(null);

        workspaceToBeUpdated.setId(newId);
        workspaceToBeUpdated.setType(newType);
        workspaceToBeUpdated.setPrice(newPrice);

        //updating it in an array of Reservations (if such reservation exists)
        int finalId2 = id;
        Reservation reservationToBeUpdated = this.reservationsArray.stream()
                .filter(item -> item.getWorkspaceId() ==finalId2)
                .findFirst()
                .orElse(null);
        if (reservationToBeUpdated != null){
            reservationToBeUpdated.setWorkspaceId(newId);
            reservationToBeUpdated.setType(newType);
            reservationToBeUpdated.setPrice(newPrice);
        }

        this.fileSaverReader.saveToFile(this.workspaceArray, "workspaces.ser");
        this.fileSaverReader.saveToFile(this.reservationsArray, "reservations.ser");
        System.out.println("Coworking space changed successfully");
}

    public void removeReservation() {
        if (!viewAllReservations()) { //shows all reservations(with id) OR Exception and false
            return;
        }

        System.out.println("Choose the reservation you want to remove by id: ");
        System.out.println("id(just copy it) - ");
        String id;
        while (true) {
            id = this.scanner.next();
            String finalId = id;
            Reservation reservationToBeCancelled = reservationsArray.stream()
                    .filter(item -> item.getId().toString().equals(finalId))
                    .findFirst()
                    .orElse(null);
            if (reservationToBeCancelled == null){
                System.out.println("No such reservation. Please enter another one: ");
                continue;
            }
            break; // coworking exists -> no exception -> stops asking for id and accepts last one
        }

        String finalId1 = id;
        Reservation reservationToBeCancelled = this.reservationsArray.stream()
                .filter(item -> item.getId().toString().equals(finalId1))
                .findFirst()
                .orElse(null);

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
        this.fileSaverReader.saveToFile(this.reservationsArray, "reservations.ser");
        this.fileSaverReader.saveToFile(this.workspaceArray, "workspaces.ser");
        System.out.println("Reservation was cancelled successfully!");
    }
}




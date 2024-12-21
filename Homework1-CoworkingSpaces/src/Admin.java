import java.util.ArrayList;
import java.util.Scanner;
/**
 * Manages spaces and view all bookings
 * Add, remove or update coworking spaces
 */
public class Admin {
    Scanner scanner;
    ArrayList<Workspace> workspaceArray;
    ArrayList<Reservation> reservationsArray;

    public Admin(Scanner scanner, ArrayList<Workspace> workspaceArray, ArrayList<Reservation> reservationsArray) {
         this.workspaceArray = workspaceArray;
         this.reservationsArray = reservationsArray;
         this.scanner = scanner;
    }

    public void addCoworkingSpace() {
        System.out.println("Type in the new coworking space data: ");
        System.out.println("id(number) - ");
        int id = this.scanner.nextInt();
        System.out.println("type of coworking(1 word) - ");
        String type = this.scanner.next();
        this.scanner.nextLine();
        System.out.println("price in $(number) - ");
        double price = this.scanner.nextDouble();
        this.scanner.nextLine();
        Workspace workspace = new Workspace(id, type, price);
        this.workspaceArray.add(workspace);
        System.out.println("New coworking space added successfully!");
    }

    public boolean browseCoworkingSpaces(){
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
        if (browseCoworkingSpaces()) {
            System.out.println("Type in the id of coworking space you want to remove: ");
            System.out.println("id(number) - ");
            int id = this.scanner.nextInt();
            for (Workspace item : this.workspaceArray) {
                if (item.id == id) {
                    this.workspaceArray.remove(item);
                }
            }
            System.out.printf("Coworking space %d removed successfully", id);
        }
    }

    public void viewAllReservations() {
        if (this.reservationsArray.isEmpty()) {
            System.out.println("There are no reservations yet");
        }
        else {
            System.out.println(this.reservationsArray);
        }
    }


}

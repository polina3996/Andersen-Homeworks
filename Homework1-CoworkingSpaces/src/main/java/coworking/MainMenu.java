package coworking;

import coworking.databases.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Shows main menu and processes User Input
 */
public class MainMenu {
    Scanner scanner;
    DB db;
    //FileSaverReader fileSaverReader;

    public MainMenu(Scanner scanner, DB db){
        this.scanner = scanner;
        this.db = db;
        //this.fileSaverReader = new FileSaverReader();
    }

    public void showMainMenu() {
        System.out.println("""
                
                -------------------------
                Welcome, my dear user!
                Main Menu
                OPTIONS:\s
                Input 1, if you want to log in as an ADMIN
                Input 2, if you want to log in as a USER
                Input 3, if you want to EXIT
                """);
    }

    public boolean processUserInput() {
        int mainOption = this.scanner.nextInt();

        // Escape option
        if (mainOption == 3) {
            System.out.println("Thank you. Goodbye!");
            return false;
        }

        // Admin option
        else if (mainOption == 1) {
            System.out.println("""
                Welcome, ADMIN!
                Admin Menu
                OPTIONS:\s
                Input 1, if you want to add a new coworking space
                Input 2, if you want to remove a coworking space
                Input 3, if you want view all reservations
                Input 4, if you want to update a coworking space
                Input 5, if you want to remove a reservation
                """);

            int adminOption = this.scanner.nextInt();

            Admin admin = new Admin(this.db, this.scanner);
            if (adminOption == 1) {
                admin.addCoworkingSpace();
            }
            else if (adminOption == 2) {
                admin.removeCoworkingSpace();
            }
            else if (adminOption == 3)  {
                admin.viewAllReservations();
            }
            else if (adminOption == 4)  {
                admin.updateCoworkingSpace();
            }
            else if (adminOption == 5)  {
                admin.removeReservation();
            }
        }

        // User option
        else if (mainOption == 2) {
            System.out.println("""
                Welcome, USER!
                Customer Menu
                OPTIONS:\s
                Input 1, if you want to browse available spaces
                Input 2, if you want to make a reservation
                Input 3, if you want view your reservations
                Input 4, if you want cancel your reservation
                """);

            int userOption = this.scanner.nextInt();
            Customer customer = new Customer(this.db, this.scanner);

            if (userOption ==1) {
                customer.browseAvailableSpaces();
            }
            else if (userOption ==2) {
                customer.makeAReservation();
            }
            else if (userOption ==3) {
                customer.viewMyReservations();
            }
            else {
                customer.cancelMyReservation();
            }
        }
        return true;
    }
}

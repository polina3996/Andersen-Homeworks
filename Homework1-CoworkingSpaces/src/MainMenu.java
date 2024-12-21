import java.util.ArrayList;
import java.util.Scanner;

/**
 * Shows main menu and processes User Input
 */
public class MainMenu {
    Scanner scanner;
    ArrayList<Workspace> workspaceArray;
    ArrayList<Reservation> reservationsArray;

    public MainMenu(Scanner scanner){
        this.scanner = scanner;
        this.workspaceArray = new ArrayList<Workspace>();
        this.reservationsArray = new ArrayList<Reservation>();
    }

    public void showMainMenu() {
        System.out.println();
        System.out.println("-------------------------");
        System.out.println("Welcome, my dear user!");
        System.out.println("Main Menu");
        System.out.println("OPTIONS: ");
        System.out.println("Input 1, if you want to log in as an ADMIN");
        System.out.println("Input 2, if you want to log in as a USER");
        System.out.println("Input 3, if you want to EXIT");
    }

    public boolean processUserInput() {
        int mainOption = this.scanner.nextInt();
        this.scanner.nextLine();

        // Escape option
        if (mainOption == 3) {
            System.out.println("Thank you. Goodbye!");
            return false;
        }

        // Admin option
        else if (mainOption == 1) {
            System.out.println("Welcome, ADMIN!"); //no login necessary?
            System.out.println("Admin Menu");
            System.out.println("OPTIONS: ");
            System.out.println("Input 1, if you want to add a new coworking space");
            System.out.println("Input 2, if you want to remove a coworking space");
            System.out.println("Input 3, if you want view all reservations");

            int adminOption = this.scanner.nextInt();
            this.scanner.nextLine();

            Admin admin = new Admin(this.scanner, this.workspaceArray, this.reservationsArray);
            if (adminOption == 1) {
                admin.addCoworkingSpace();
            }
            else if (adminOption == 2) {
                admin.removeCoworkingSpace();
            }
            else if (adminOption == 3)  {
                admin.viewAllReservations();
            }
        }

        // User option
        else if (mainOption == 2) {
            System.out.println("Welcome, USER!"); //no login necessary?
            System.out.println("Customer Menu");
            System.out.println("OPTIONS: ");
            System.out.println("Input 1, if you want to browse available spaces");
            System.out.println("Input 2, if you want to make a reservation");
            System.out.println("Input 3, if you want view your reservations");
            System.out.println("Input 4, if you want cancel your reservation");

            int userOption = this.scanner.nextInt();
            this.scanner.nextLine();
            Customer customer = new Customer(this.scanner, this.workspaceArray, this.reservationsArray);

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

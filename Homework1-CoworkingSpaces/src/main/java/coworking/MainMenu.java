package coworking;


import coworking.controller.AdminController;
import coworking.controller.CustomerController;
import coworking.repository.ReservationRepository;
import coworking.repository.UserRepository;
import coworking.repository.WorkspaceRepository;
import coworking.service.ReservationService;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Shows main menu and processes User Input
 */
@Component
public class MainMenu {
    Scanner scanner;
    private final WorkspaceRepository workspaceRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final UserRepository userRepository;
    private final AdminController adminController;
    private final CustomerController customerController;


    @Autowired
    public MainMenu(
            Scanner scanner,
            WorkspaceRepository workspaceRepository,
            ReservationRepository reservationRepository,
            ReservationService reservationService,
            UserRepository userRepository,
            AdminController adminController,
            CustomerController customerController
    ) {
        this.scanner = scanner;
        this.workspaceRepository = workspaceRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
        this.adminController = adminController;
        this.customerController = customerController;
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


            if (adminOption == 1) {
                adminController.addCoworkingSpace();
            }
            else if (adminOption == 2) {
                adminController.removeCoworkingSpace();
            }
            else if (adminOption == 3)  {
                adminController.viewAllReservations();
            }
            else if (adminOption == 4)  {
                adminController.updateCoworkingSpace();
            }
            else if (adminOption == 5)  {
                adminController.removeReservation();
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

            if (userOption ==1) {
                customerController.browseAvailableSpaces();
            }
            else if (userOption ==2) {
                customerController.makeAReservation();
            }
            else if (userOption ==3) {
                customerController.viewMyReservations();
            }
            else {
                customerController.cancelMyReservation();
            }
        }
        return true;
    }
}

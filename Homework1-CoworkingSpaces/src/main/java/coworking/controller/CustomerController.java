package coworking.controller;
import coworking.CheckEmptinessException;
import coworking.CheckMethods;
import coworking.repository.ReservationRepository;
import coworking.repository.WorkspaceRepository;
import coworking.repository.UserRepository;
import coworking.model.Reservation;
import coworking.model.Workspace;
import coworking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Browse available spaces, make reservations and cancel bookings
 * - select a workspace
 * - enter booking details(ex. name, date, time(start/end))
 * - store the reservation details in memory
 * - see their reservations and cancel them by selecting the reservation ID
 */



@Controller
public class CustomerController {
    Scanner scanner;
    private final WorkspaceRepository workspaceRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final UserRepository userRepository;

    @Autowired
    public CustomerController(Scanner scanner, WorkspaceRepository workspaceRepository, ReservationRepository reservationRepository, ReservationService reservationService, UserRepository userRepository) {
        this.scanner = scanner;
        this.workspaceRepository = workspaceRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    public List<Workspace> browseAvailableSpaces() {
        try{
            List<Workspace> availableWorkspaces = this.workspaceRepository.findAvailableWorkspaces();
            System.out.println("Here are available coworking spaces for you:");
            for (Workspace workspace:availableWorkspaces){
                System.out.println(workspace);
            }
            return availableWorkspaces;
        }
        catch (NullPointerException e){
            System.out.println("No available workspaces yet");
    }
        return new ArrayList<>();
    }

    public void makeAReservation() {
        List<Workspace> availableWorkspaces = browseAvailableSpaces();
        if (availableWorkspaces.isEmpty()){
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
        System.out.println("date of start in dd-MM-yyyy format - ");
        String start;
        while (true) {
            start = this.scanner.next();
            if (CheckMethods.checkDate(start, "dd-MM-yyyy")){
                break;
            }
            this.scanner.nextLine();
        }

        System.out.println("date of end in dd-MM-yyyy format - ");
        String end;
        while (true) {
            end = this.scanner.next();
            if (CheckMethods.checkDate(end, "dd-MM-yyyy")){
                break;
            }
            this.scanner.nextLine();
        }

        this.reservationService.makeReservation(this.userRepository, workspaceToBeReserved, name, start, end);
        System.out.println("New reservation was made successfully!");
    }

    public List<Reservation> viewMyReservations() {
        System.out.println("Type in your name: ");
        System.out.println("name - ");
        String name = this.scanner.next();

        List<Reservation> myReservations = null;
        try {
            System.out.println("Here are your reservations: ");
            myReservations = this.reservationRepository.findMyReservations(name);
            if (myReservations == null || myReservations.isEmpty()) {
                System.out.println("You have no reservations yet");
                return new ArrayList<>();
            }
            for (Reservation item : myReservations) {
                System.out.println(item);
            }
        } catch (CheckEmptinessException e) {
            System.out.println(e.getMessage());
        }
        return myReservations;
    }

    public void cancelMyReservation() {
        List<Reservation> myReservations = viewMyReservations();
        if (myReservations.isEmpty()) {
            return;
        }

        System.out.println("Choose the reservation you want to cancel by id: ");
        System.out.println("id - ");
        int id;
        Reservation reservationToBeCancelled = null;

        while (true) {
            try {
                id = this.scanner.nextInt();
                for (Reservation reservation : myReservations){
                    if (reservation.getId() == id){
                        reservationToBeCancelled = reservation;
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
        this.reservationService.removeReservation(reservationToBeCancelled);
        System.out.println("Your reservation was cancelled successfully!");

    }
}




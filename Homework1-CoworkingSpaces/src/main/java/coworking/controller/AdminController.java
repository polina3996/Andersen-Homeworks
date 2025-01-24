package coworking.controller;

import coworking.CheckEmptinessException;
import coworking.CheckMethods;
import coworking.repository.UserRepository;
import coworking.service.ReservationService;
import coworking.repository.ReservationRepository;
import coworking.repository.WorkspaceRepository;
import coworking.model.Reservation;
import coworking.model.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Manages spaces and view all bookings
 * Add, remove or update coworking spaces
 */

@Controller
public class AdminController {
    Scanner scanner;
    private final WorkspaceRepository workspaceRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(Scanner scanner, WorkspaceRepository workspaceRepository, ReservationRepository reservationRepository, ReservationService reservationService, UserRepository userRepository) {
         this.scanner = scanner;
         this.workspaceRepository = workspaceRepository;
         this.reservationRepository = reservationRepository;
         this.reservationService = reservationService;
         this.userRepository = userRepository;
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

        Workspace newWorkspace = new Workspace(type, price);
        this.workspaceRepository.save(newWorkspace);
        System.out.println("New coworking space added successfully!");
    }

    public List<Workspace> browseCoworkingSpaces(){
        try {
            List<Workspace> workspaces = this.workspaceRepository.findAll();
            CheckMethods.checkEmptiness(workspaces, "coworking spaces");
            System.out.println("Here are all coworking spaces :");
            for (Workspace item : workspaces) {
                System.out.println(item);
            }
            return workspaces;
        }
        catch (CheckEmptinessException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

    }

    public void removeCoworkingSpace() {
        List<Workspace> workspaces = browseCoworkingSpaces();
        if (workspaces.isEmpty()){
            return;
        }

        System.out.println("Type in the id of coworking space you want to remove: ");
        System.out.println("id(number) - ");
        Workspace workspacesRemoved = null;
        int id;

        while (true) {
            try {
                id = this.scanner.nextInt();
                for (Workspace workspace: workspaces){
                    if (workspace.getId() == id){
                        workspacesRemoved = workspace;
                        break;
                    }
                }
                if (workspacesRemoved == null){
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

        this.workspaceRepository.delete(workspacesRemoved);
        System.out.printf("Coworking space and associated reservation %d removed successfully", id);
    }


    public List<Reservation> viewAllReservations() {
        try {
            List<Reservation> reservations = this.reservationRepository.findReservations();
            CheckMethods.checkEmptiness(reservations, "reservations");
            System.out.println("Here are all reservations :");
            for (Reservation item : reservations) {
                System.out.println(item);
            }
            return reservations;
        }
        catch (CheckEmptinessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }


    public void updateCoworkingSpace() {
        List<Workspace> workspaces = browseCoworkingSpaces();
        if (workspaces.isEmpty()){
            return;
        }

        System.out.println("Type in the id of coworking space you want to update: ");
        System.out.println("id(number) - ");
        Workspace workspaceToBeUpdated = null;
        int id;
        while (true) {
            try {
                id = this.scanner.nextInt();
                for (Workspace workspace : workspaces){
                    if (workspace.getId() == id){
                        workspaceToBeUpdated = workspace;
                        break;
                    }
                }
                if (workspaceToBeUpdated == null) {
                    System.out.println("No such workspaces. Please enter another one: ");
                    continue;
            }
            }catch (NullPointerException e) {
                System.out.println("No such workspace. Please enter another one: ");
                continue;
            }
            catch (InputMismatchException e) {
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

        workspaceToBeUpdated.setType(newType);
        workspaceToBeUpdated.setPrice(newPrice);
        this.workspaceRepository.update(workspaceToBeUpdated);
        System.out.println("Coworking space changed successfully");
        }

    public void removeReservation(){
        List<Reservation> reservations = viewAllReservations();
        if (reservations.isEmpty()) {
            return;
        }

        System.out.println("Choose the reservation you want to remove by id: ");
        System.out.println("id - ");
        int id;
        Reservation reservationToBeRemoved = null;
        while (true) {
            try {
                id = this.scanner.nextInt();
                for (Reservation reservation : reservations) {
                    if (reservation.getId() == id) {
                        reservationToBeRemoved = reservation;
                        break;
                    }
                }
                if (reservationToBeRemoved == null) {
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
        this.reservationService.removeReservation(reservationToBeRemoved);
        System.out.println("Reservation was cancelled successfully!");
    }
}




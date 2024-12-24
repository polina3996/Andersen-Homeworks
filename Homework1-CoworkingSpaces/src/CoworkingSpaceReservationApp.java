import java.util.Scanner;

public class CoworkingSpaceReservationApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MainMenu mainMenu = new MainMenu(scanner);
        do {
            mainMenu.showMainMenu();
        } while (mainMenu.processUserInput());
        scanner.close();
    }
}
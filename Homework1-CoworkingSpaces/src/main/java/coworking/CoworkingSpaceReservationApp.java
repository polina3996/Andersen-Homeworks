package coworking;

import coworking.databases.DAO.ReservationDAO;
import coworking.databases.DAO.WorkspaceDAO;
import coworking.databases.HibernateSessionUtil;
import coworking.databases.service.ReservationService;
import org.hibernate.Session;

import java.lang.reflect.Method;
import java.util.Scanner;

public class CoworkingSpaceReservationApp {
    public static void main(String[] args)  {
        String path = "C:\\Users\\polin\\IdeaProjects\\Andersen-Homeworks\\Homework1-CoworkingSpaces\\src\\main\\java\\coworking\\Greeting.java";
        String className = "coworking.Greeting";

        try {
            GreetingClassLoader classLoader = new GreetingClassLoader(path);
            Class<?> loadedClass = classLoader.loadClass(className);
            Object instance = loadedClass.getDeclaredConstructor().newInstance();
            Method method = loadedClass.getMethod("printGreeting");
            method.invoke(instance);
        }
        catch (Exception e) {
            System.out.println("Class loading has gone wrong" + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        Session session = HibernateSessionUtil.getSessionFactory().openSession();
        WorkspaceDAO workspaceDAO = new WorkspaceDAO(session);
        ReservationDAO reservationDAO = new ReservationDAO(session);
        ReservationService reservationService = new ReservationService(session);

        MainMenu mainMenu = new MainMenu(scanner, workspaceDAO, reservationDAO, reservationService);
        do {
            mainMenu.showMainMenu();
        } while (mainMenu.processUserInput());

        scanner.close();
        session.close();
    }
}
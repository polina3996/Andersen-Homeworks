package coworking;

import coworking.databases.ConnectionException;
import coworking.databases.DB;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
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
        DB db = new DB();

        MainMenu mainMenu = new MainMenu(scanner, db);
        do {
            mainMenu.showMainMenu();
        } while (mainMenu.processUserInput());

        scanner.close();
    }
}
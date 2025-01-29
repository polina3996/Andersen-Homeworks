package coworking;

import coworking.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;


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

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


        MainMenu mainMenu = context.getBean(MainMenu.class);
        do {
            mainMenu.showMainMenu();
        } while (mainMenu.processUserInput());

        ((AnnotationConfigApplicationContext) context).close();
    }
}
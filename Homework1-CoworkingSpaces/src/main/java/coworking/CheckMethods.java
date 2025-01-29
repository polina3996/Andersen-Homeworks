package coworking;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CheckMethods {
    /**
     * Checks whether the given List is empty or not or throws custom exception
     * @param lst: List(Workspaces or Reservations)
     * @param name: name of given parameter("coworking spaces" or "reservations")
     * @return boolean
     */
    public static <T> boolean checkEmptiness(List<T> lst, String name) {
        String message = String.format("There are no %s yet", name);
        if (lst == null || lst.isEmpty()) {
            throw new CheckEmptinessException(message);
        }
       return false;
    }


    /**
     * Checks whether the given date corresponds to required format or throws exception
     * @param date: String from input
     * @return boolean
     */
    public static boolean checkDate(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Wrong date format. Please enter another one: ");
            return false;
        }
    }
}

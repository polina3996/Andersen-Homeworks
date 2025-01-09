import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class CheckMethods {
    /**
     * Checks the uniqueness of workspace in a workspace array or throws custom exception
     * @param id: id of workspace
     * @param workspaceArray: list of all workspaces
     * @return boolean
     */
    public static boolean checkNonUniquiness(int id, ArrayList<Workspace> workspaceArray) {
        for (Workspace workspace: workspaceArray) {
            if (workspace.getId() == id) {
                throw new NonUniqueException("This ID is already occupied. Please enter another one: ");
            };
        }
        return false;
    }

    /**
     * Checks whether the given ArrayList is empty or not or throws custom exception
     * @param arr: ArrayList(Coworking Spaces or Reservations)
     * @param name: name of given parameter("coworking spaces" or "reservations")
     * @return boolean
     */
    public static <T> boolean checkEmptiness(ArrayList<T> arr, String name) {
        String message = String.format("There are no %s yet", name);
        if (arr == null || arr.isEmpty()) {
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

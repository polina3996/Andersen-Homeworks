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
     * Check the presence of coworking space or throws custom exception
     * @param id: id of workspace
     * @param workspaceArray: list of all workspaces
     * @return boolean
     */
    public static boolean checkCoworkPresence(int id, ArrayList<Workspace> workspaceArray) {
        for (Workspace workspace: workspaceArray) {
            if (workspace.getId() == id) {
                return true;
            };
            throw new NonPresentException("No such coworking space. Please enter another one: ");
        }
        return false;
    }

    /**
     * Check the presence of reservation or throws custom exception
     * @param id: id of reservation
     * @param reservationsArray: list of actual reservations
     * @return boolean
     */
    public static boolean checkReservPresence(String id, ArrayList<Reservation> reservationsArray) {
        for (Reservation reservation: reservationsArray) {
            if (reservation.getId().toString().equals(id)) {
                return true;
            };
            throw new NonPresentException("No such reservation. Please enter another one: ");
        }
        return false;
    }
}

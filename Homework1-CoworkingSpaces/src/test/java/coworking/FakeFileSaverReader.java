package coworking;
import java.util.ArrayList;


public class FakeFileSaverReader extends FileSaverReader {
    private ArrayList<Workspace> workspacesFile = new ArrayList<>();
    private ArrayList<Reservation> reservationsFile = new ArrayList<>();

    public <T> void saveToFile(ArrayList<T> arr, String filename) {
        if (filename.equals("workspaces.ser")) {
            workspacesFile = (ArrayList<Workspace>) arr;
        } else if (filename.equals("reservations.ser")) {
            reservationsFile = (ArrayList<Reservation>) arr;
        }
    }

    public ArrayList<Workspace> readFromWorkspacesFile() {
            return workspacesFile;
        }

    public ArrayList<Reservation> readFromReservationsFile(){
            return  reservationsFile;
        }

    }


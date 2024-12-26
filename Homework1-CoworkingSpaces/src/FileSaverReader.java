import java.io.*;
import java.util.ArrayList;

public class FileSaverReader {
    public FileSaverReader(){

    }
    public void saveWorkspacesToFile(ArrayList<Workspace> workspaceArray){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("workspaces.ser"))) {
            oos.writeObject(workspaceArray);
            //System.out.println("Array serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveReservationsToFile(ArrayList<Reservation> reservationArray){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("reservations.ser"))) {
            oos.writeObject(reservationArray);
            //System.out.println("Array serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Workspace> readWorkspacesFromFile(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("workspaces.ser"))) {
            return (ArrayList<Workspace>) ois.readObject();
        } catch (FileNotFoundException e){
            return new ArrayList<Workspace>();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Reservation> readReservationsFromFile(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("reservations.ser"))) {
            return (ArrayList<Reservation>) ois.readObject();
        } catch (FileNotFoundException e){
            return new ArrayList<Reservation>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }
}

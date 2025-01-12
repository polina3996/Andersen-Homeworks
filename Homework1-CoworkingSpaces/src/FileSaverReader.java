import java.io.*;
import java.util.ArrayList;

public class FileSaverReader {
    public FileSaverReader(){

    }
    public <T> void saveToFile(ArrayList<T> arr, String fileName){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(arr);
            System.out.println("Array serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public <T> ArrayList<T> readFromFile(String fileName){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ArrayList<T>) ois.readObject();
        } catch (FileNotFoundException e){
            return new ArrayList<T>();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

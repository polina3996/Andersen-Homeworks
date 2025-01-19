package coworking;

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



}

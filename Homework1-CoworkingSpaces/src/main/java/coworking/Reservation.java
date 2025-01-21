package coworking;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation implements Serializable {
    /**
     * display all reservations
     */
    private int id;
    private int workspaceId;
    private String type;
    private String name;
    private String start;
    private String end;
    private double price;
    private String date;

    public Reservation(int id, int workspaceId, String type, String name, String start, String end, double price, String date) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.type =  type;
        this.name = name;
        this.start = start;
        this.end = end;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public int getWorkspaceId() {
        return this.workspaceId;
    }

    public void setWorkspaceId(int newId) {
        this.workspaceId = newId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String newType) {
        this.type = newType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getStart() {
        return this.start;
    }


    public String getEnd() {
        return this.end;
    }


    public double getPrice() {
        return this.price;
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    public String getDate() {
        return this.date;
    }


    @Override
    public String toString() {
        return String.format("id: %s, type: %s, name: %s, price: %.2f, start: %s, end: %s, date: %s", getId(), getType(), getName(), getPrice(), getStart(), getEnd(), getDate());
    }
}

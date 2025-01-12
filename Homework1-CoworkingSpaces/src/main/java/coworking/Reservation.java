package coworking;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Reservation implements Serializable {
    /**
     * display all reservations
     */
    private UUID id;
    private int workspaceId;
    private String type;
    private String name;
    private LocalDate start;
    private LocalDate end;
    private double price;
    private final LocalDate DATE;

    public Reservation(UUID id, int workspaceId, String type, String name, LocalDate start, LocalDate end, double price) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.type =  type;
        this.name = name;
        this.start = start;
        this.end = end;
        this.price = price;
        this.DATE = LocalDate.now(); //by default - current date
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID newId) {
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

    public LocalDate getStart() {
        return this.start;
    }


    public LocalDate getEnd() {
        return this.end;
    }


    public double getPrice() {
        return this.price;
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    public LocalDate getDate() {
        return this.DATE;
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String start = getStart().format(formatter);
        String end = getEnd().format(formatter);
        String date = getDate().format(formatter);
        return String.format("id: %s, type: %s, name: %s, price: %.2f, start: %s, end: %s, date: %s", getId(), getType(), getName(), getPrice(), start, end, date);
    }
}

package coworking.model;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name="workspaces")
public class Workspace implements Serializable {
    /**
     * Entity, that represents a table "Workspaces" in database.
     * Has reference to "Reservations" table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double price;

    @Column(name="availability_status", nullable = false)
    private boolean availabilityStatus = true;

    public Workspace(){}

    public Workspace(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public int getId(){
        return this.id;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String newType) {
        this.type = newType;
    }

    public double getPrice(){
        return this.price;
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    public boolean getAvailabilityStatus(){
        return this.availabilityStatus;
    }

    public void setAvailabilityStatus(boolean newAvailabilityStatus) {
        this.availabilityStatus = newAvailabilityStatus;
    }
    @Override
    public String toString() {
        return String.format("A workspace with id: %d, type: %s, price: %.2f, availability: %b", getId(), getType(), getPrice(), getAvailabilityStatus());
    }
}
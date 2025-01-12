package coworking;

import java.io.Serializable;

public class Workspace implements Serializable {
    /**
     * Display a list of coworking spaces with details
     * (ex. id, type (open space, private room, etc.),
     * price and availability status)
     */
    private int id;
    private String type;
    private double price;
    private boolean availabilityStatus;

    public Workspace(int id, String type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.availabilityStatus = true;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
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
        return String.format("id: %d, type: %s, price: %.2f, availability: %b", getId(), getType(), getPrice(), getAvailabilityStatus());
    }
}

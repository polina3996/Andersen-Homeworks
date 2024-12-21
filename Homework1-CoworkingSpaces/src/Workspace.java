public class Workspace {
    /**
     * Display a list of coworking spaces with details
     * (ex. id, type (open space, private room, etc.),
     * price and availability status)
     */
    int id;
    String type;
    double price;
    boolean availabilityStatus;

    public Workspace(int id, String type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.availabilityStatus = true;
    }

    @Override
    public String toString() {
        return String.format("id: %d, type: %s, price: %.2f, availability: %b", this.id, this.type, this.price, this.availabilityStatus);
    }
}

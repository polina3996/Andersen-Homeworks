import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Reservation {
    /**
     * display all reservations
     */
    UUID id;
    int workspaceId;
    String type;
    String name;
    LocalDate start;
    LocalDate end;
    double price;
    LocalDate date;

    public Reservation(UUID id, int workspaceId, String type, String name, LocalDate start, LocalDate end, double price) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.type =  type;
        this.name = name;
        this.start = start;
        this.end = end;
        this.price = price;
        this.date = LocalDate.now(); //automatically - current time
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String start = this.start.format(formatter);
        String end = this.end.format(formatter);
        return String.format("id: %s, type: %s, name: %s, price: %.2f, start: %s, end: %s", this.id, this.type, this.name, this.price, start, end);
    }
}

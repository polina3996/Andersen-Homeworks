package coworking.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="reservations")
public class Reservation implements Serializable {
    /**
     * Entity, that represents a table "Reservations" in database.
     * Has reference to "Users" table and "Workspaces" table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @ManyToOne
    @JoinColumn(name="workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="start_date", nullable = false)
    private LocalDate start;

    @Column(name="end_date", nullable = false)
    private LocalDate end;

    @Column(name="date_of_creation", nullable = false)
    private LocalDate date = LocalDate.now();

    public Reservation(){}

    public Reservation(Workspace workspace, User user, LocalDate start, LocalDate end) {
        this.workspace = workspace;
        this.user = user;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return this.id;
    }

    public Workspace getWorkspace() {
        return this.workspace;
    }

    public void setWorkspace(Workspace newWorkspace) {
        this.workspace = newWorkspace;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User newUser) {
        this.user = newUser;
    }

    public LocalDate getStart() {
        return this.start;
    }

    public void setStart(LocalDate newStart){ this.start = newStart;}

    public LocalDate getEnd() {
        return this.end;
    }

    public void setEnd(LocalDate newEnd){ this.end = newEnd;}

    public LocalDate getDate() {
        return this.date;
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return String.format("Reservation with ID: %s, workspace: %s, for a user: %s, start: %s, end: %s, created on: %s", getId(), getWorkspace(), getUser(), getStart().format(formatter), getEnd().format(formatter), getDate().format(formatter));
    }
}

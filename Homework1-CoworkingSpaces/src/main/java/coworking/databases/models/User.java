package coworking.databases.models;

import jakarta.persistence.*;

@Entity
@Table(name="Users")
public class User {
    /**
     * Entity, that represents a table "Users" in database.
     * Has reference to "Reservations" table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @Column(nullable = false)
    private String name;

    public User() {}

    public User(String name) {
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    @Override
    public String toString() {
        return String.format("A user with id: %d, name: %s", getId(), getName());
    }
}

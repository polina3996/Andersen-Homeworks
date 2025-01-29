package coworking.databases.service;

import coworking.databases.DAO.UserDAO;
import coworking.databases.models.Reservation;
import coworking.databases.models.User;
import coworking.databases.models.Workspace;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ReservationService {
    protected final Session session;

    public ReservationService(Session session) {
        this.session = session;
    }

    public void makeReservation(UserDAO userDAO, Workspace workspaceToBeReserved, String name, String start, String end) {
        Transaction transaction = session.beginTransaction();

        User user = userDAO.findUser(name);
        if (user == null){
            user = new User(name);
            session.save(user); //userFromTable is transient(save it first, because we can't save reservation without userFromTable)
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Reservation reservation = new Reservation(workspaceToBeReserved, user, LocalDate.parse(start, formatter), LocalDate.parse(end, formatter));
        workspaceToBeReserved.setAvailabilityStatus(false);

        session.save(reservation);
        session.update(workspaceToBeReserved);
        transaction.commit();
    }

    public void cancelMyReservation(Reservation reservationToBeCancelled) {
        Workspace workspace = reservationToBeCancelled.getWorkspace();
        workspace.setAvailabilityStatus(true);

        Transaction transaction = session.beginTransaction();
        session.delete(reservationToBeCancelled);
        session.update(workspace);
        transaction.commit();
    }
}

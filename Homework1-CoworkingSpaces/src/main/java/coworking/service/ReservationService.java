package coworking.service;

import coworking.model.Reservation;
import coworking.model.User;
import coworking.model.Workspace;
import coworking.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ReservationService {
    protected final SessionFactory sessionFactory;

    public ReservationService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void makeReservation(UserRepository userRepository, Workspace workspaceToBeReserved, String name, String start, String end) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = userRepository.findUser(name);
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
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save entity", e);
        }
    }

    public void removeReservation(Reservation reservationToBeRemoved) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Workspace workspace = reservationToBeRemoved.getWorkspace();
            workspace.setAvailabilityStatus(true);

            session.delete(reservationToBeRemoved);
            session.update(workspace);
            transaction.commit();
    }catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        throw new RuntimeException("Failed to save entity", e);
    }
}
}

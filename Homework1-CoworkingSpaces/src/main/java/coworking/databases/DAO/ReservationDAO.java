package coworking.databases.DAO;

import coworking.databases.HQLQueries;
import coworking.databases.models.Reservation;
import coworking.databases.models.Workspace;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;


public class ReservationDAO extends DAO<Workspace> {
    public ReservationDAO(Session session) {
        super(session);
    }

    public List<Reservation> findReservations() {
        List<Reservation> reservations = session.createQuery(HQLQueries.selectFromReservTableSQL, Reservation.class).list();
        return reservations;
    }

    public List<Reservation> findMyReservations(String name) {
        Query<Reservation> query = session.createQuery(HQLQueries.selectFromMyReservTableSQL, Reservation.class);
        query.setParameter("name", name);
        List<Reservation> myReservations = query.list();
        return myReservations;
    }
}

package coworking.repository;

import coworking.databases.HQLQueries;
import coworking.model.Reservation;
import coworking.model.Workspace;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

@org.springframework.stereotype.Repository
public class ReservationRepository extends Repository<Workspace> {
    private final SessionFactory sessionFactory;

    public ReservationRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }
    public List<Reservation> findReservations() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(HQLQueries.selectFromReservTableSQL, Reservation.class).list();
    }
    }

    public List<Reservation> findMyReservations(String name) {
        try (Session session = sessionFactory.openSession()){
            Query<Reservation> query = session.createQuery(HQLQueries.selectFromMyReservTableSQL, Reservation.class);
            query.setParameter("name", name);
            List<Reservation> myReservations = query.list();
            return myReservations;
    }
}
}

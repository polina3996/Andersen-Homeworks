package coworking.repository;

import coworking.databases.HQLQueries;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import coworking.model.User;


@org.springframework.stereotype.Repository
public class UserRepository extends Repository<User> {
    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public User findUser(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(HQLQueries.selectFromUsersTableSQL, User.class);
            query.setParameter("name", name);
            User user = query.uniqueResult();
            return user;
    }
}
}

package coworking.databases.DAO;

import coworking.databases.HQLQueries;
import coworking.databases.models.User;
import org.hibernate.Session;
import org.hibernate.query.Query;



public class UserDAO extends DAO<User> {
    public UserDAO(Session session) {
        super(session);
    }

    public User findUser(String name) {
        Query<User> query = session.createQuery(HQLQueries.selectFromUsersTableSQL, User.class);
        query.setParameter("name", name);
        User user = query.uniqueResult();
        return user;
    }
}

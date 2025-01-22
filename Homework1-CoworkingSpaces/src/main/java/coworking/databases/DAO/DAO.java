package coworking.databases.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;


public abstract class DAO<T> {
    protected final Session session;

    protected DAO(Session session) {
        this.session = session;
    }
    public void save(T entity) {
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
    }

    public void update(T entity) {
        Transaction transaction = session.beginTransaction();
        session.update(entity);
        transaction.commit();
    }

    public void delete(T entity) {
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
    }
}


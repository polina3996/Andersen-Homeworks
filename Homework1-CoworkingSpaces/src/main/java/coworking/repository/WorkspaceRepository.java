package coworking.repository;

import coworking.databases.HQLQueries;
import coworking.model.Workspace;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import java.util.List;

@org.springframework.stereotype.Repository
public class WorkspaceRepository extends Repository<Workspace> {
    private final SessionFactory sessionFactory;

    public WorkspaceRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public List<Workspace> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(HQLQueries.selectFromWorkspTableSQL, Workspace.class).list();
        }
    }

    public List<Workspace> findAvailableWorkspaces() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(HQLQueries.selectAvailableWorkspTableSQL, Workspace.class).list();
    }
}
}


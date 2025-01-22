package coworking.databases.DAO;

import coworking.databases.HQLQueries;
import coworking.databases.models.Workspace;
import org.hibernate.Session;


import java.util.List;

public class WorkspaceDAO extends DAO<Workspace> {
    public WorkspaceDAO(Session session) {
        super(session);
    }

    public List<Workspace> findAll() {
        List<Workspace> allWorkspaces = session.createQuery(HQLQueries.selectFromWorkspTableSQL, Workspace.class).list();
        return allWorkspaces;
    }
    public List<Workspace> findAvailableWorkspaces() {
        List<Workspace> availableWorkspaces = session.createQuery(HQLQueries.selectAvailableWorkspTableSQL, Workspace.class).list();
        return availableWorkspaces;
    }
}


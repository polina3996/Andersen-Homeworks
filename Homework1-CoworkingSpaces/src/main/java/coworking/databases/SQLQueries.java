package coworking.databases;

public class SQLQueries {
    public static String createTablesSQL = """
            CREATE TABLE IF NOT EXISTS workspaces(
                         	id serial PRIMARY KEY,
                         	type char(20) NOT NULL,
                         	price numeric NOT NULL,
                         	availability_status boolean DEFAULT true
             );
             CREATE TABLE IF NOT EXISTS reservations(
                         	id serial PRIMARY KEY,
             	            workspace_id int NOT NULL,
                         	name char(20) NOT NULL,
                         	start_date date,
                         	end_date date,
                         	date timestamp DEFAULT CURRENT_TIMESTAMP,
             	            FOREIGN KEY (workspace_id) REFERENCES workspaces (id) ON DELETE CASCADE
             )
            """;

    public static final String insertIntoWorkspTableSQL = """
            INSERT INTO workspaces (type, price) VALUES (?, ?)""";
    public static final String insertIntoReservTableSQL = """
            INSERT INTO reservations (workspace_id, name, start_date, end_date) VALUES (?, ?, ?, ?)""";


    public static final String removeFromWorkspTableSQL = """
            DELETE FROM workspaces WHERE id=?""";
    public static final String removeFromReservTableSQL = """
            DELETE FROM reservations WHERE id=?""";


    public static final String selectAvailableWorkspTableSQL = """
            SELECT * FROM workspaces WHERE availability_status=true""";
    public static final String selectFromReservTableSQL = """
            SELECT reservations.id, reservations.workspace_id, reservations.name, reservations.start_date,\s
            reservations.end_date, reservations.date, workspaces.type, workspaces.price, workspaces,availability_status
            FROM reservations JOIN workspaces ON reservations.workspace_id = workspaces.id""";
    public static final String selectFromMyReservTableSQL = """
            SELECT reservations.id, reservations.workspace_id, reservations.start_date,
            reservations.end_date, reservations.date, workspaces.type, workspaces.price
            FROM reservations JOIN workspaces ON reservations.workspace_id = workspaces.id
            WHERE name=?""";
    public static final String selectFromWorkspTableByIdSQL = """
            SELECT * FROM workspaces WHERE id=?""";
    public static final String selectFromWorkspTableSQL = """
            SELECT * FROM workspaces""";
    public static final String selectFromReservByIdSQL = """
            SELECT * FROM reservations JOIN workspaces\s
            ON reservations.workspace_id=workspaces.id
            WHERE id=?""";

    public static final String updateWorkspTableSQL = """
            UPDATE workspaces SET type=?, price=? WHERE id=?""";
    public static final String updateAvailabilityStatusSQL = """
            UPDATE workspaces SET availability_status=? WHERE id=?""";






}

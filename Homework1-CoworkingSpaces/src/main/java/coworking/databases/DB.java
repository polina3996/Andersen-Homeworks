package coworking.databases;
import coworking.Reservation;
import coworking.Workspace;

import java.sql.*;
import java.util.ArrayList;

/**
 * Connects to database ;
 * creates tables;
 */
public class DB {
    public DB(){
    }

    public Connection connect() {
        try {
            Class.forName(Configuration.getDB_Driver());
            try {
                Connection connection = DriverManager.getConnection(Configuration.getDbUrl(), Configuration.getLogin(), Configuration.getPassword());
                System.out.println("Connected to the database successfully!");
                try (Statement statement = connection.createStatement()) {
                    statement.execute(SQLQueries.createTablesSQL);
                    System.out.println("Tables Workspaces and Reservations created or already exist");
                }
                return connection;
            } catch(SQLException e){
                e.printStackTrace();
                System.out.println("SQL Error");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC driver for DBMS not found!");
        }
        return null; //if no Connection returned
    }

    public int insertIntoWorkspaces(String type, double price) {
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.insertIntoWorkspTableSQL)) {
                preparedStatement.setString(1, type);
                preparedStatement.setDouble(2, price);
                connect().close();
                return preparedStatement.executeUpdate();// N of rows affected
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public int insertIntoReservations(int workspaceId, String name, String start, String end) {
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.insertIntoReservTableSQL)) {
                preparedStatement.setInt(1, workspaceId);
                preparedStatement.setString(2, name);
                preparedStatement.setDate(3, Date.valueOf(start));
                preparedStatement.setDate(4, Date.valueOf(end));
                //by default  - current timestamp is also added
                connect().close();
                return preparedStatement.executeUpdate();// N of rows affected
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public int removeFromWorkspaces(int id) {
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.removeFromWorkspTableSQL)) {
                preparedStatement.setInt(1, id);
                connect().close();
                return preparedStatement.executeUpdate();// N of rows affected(0 of not such id)
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public int removeFromMyReservations(int id) {
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.removeFromReservTableSQL)) {
                preparedStatement.setInt(1, id);
                connect().close();
                return preparedStatement.executeUpdate();// N of rows affected
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public ArrayList<Workspace> selectFromWorkspaces(){
        if (connect() != null){
            try (Statement statement = connect().createStatement()) {
                ResultSet rs = statement.executeQuery(SQLQueries.selectFromWorkspTableSQL);
                ArrayList<Workspace> workspaces = new ArrayList<Workspace>();
                while (rs.next()){
                    int id = rs.getInt("id");
                    String type = rs.getString("type");
                    double price = rs.getDouble("price");
                    boolean availabilityStatus = rs.getBoolean("availability_status");
                    Workspace workspace = new Workspace(id, type, price, availabilityStatus);
                    workspaces.add(workspace);
                }
                connect().close();
                return workspaces;
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");
        }
    }

    public Workspace selectFromWorkspacesById(int id) throws SQLException {
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.selectFromWorkspTableByIdSQL)) {
                preparedStatement.setInt(1, id);

                try(ResultSet rs = preparedStatement.executeQuery()) {
                    Workspace workspace = null;
                    while (rs.next()) {
                        String type = rs.getString("type");
                        double price = rs.getDouble("price");
                        boolean availabilityStatus = rs.getBoolean("availability_status");
                        workspace = new Workspace(id, type, price, availabilityStatus);
                    }
                    connect().close();
                    return workspace;
                } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public  ArrayList<Workspace> selectAvailableWorkspaces() {
        if (connect() != null){
            try (Statement statement = connect().createStatement()) {
                ResultSet rs = statement.executeQuery(SQLQueries.selectAvailableWorkspTableSQL);
                ArrayList<Workspace> availableWorkspaces = new ArrayList<Workspace>();
                while (rs.next()){
                    int id = rs.getInt("id");
                    String type = rs.getString("type");
                    double price = rs.getDouble("price");
                    boolean availabilityStatus = rs.getBoolean("availability_status");
                    Workspace workspace = new Workspace(id, type, price, availabilityStatus);
                    availableWorkspaces.add(workspace);
                }
                connect().close();
                return availableWorkspaces;
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public  ArrayList<Reservation> selectFromReservations() {
        if (connect() != null){
            try (Statement statement = connect().createStatement()) {
                ResultSet rs = statement.executeQuery(SQLQueries.selectFromReservTableSQL);
                ArrayList<Reservation> reservations = new ArrayList<Reservation>();
                while (rs.next()){
                    int id = rs.getInt("id");
                    int workspaceId = rs.getInt("workspace_id");
                    String name = rs.getString("name");
                    String start = rs.getDate("start_date").toString();
                    String end = rs.getDate("end_date").toString();
                    String date = rs.getTimestamp("date").toString();
                    String type = rs.getString("type");
                    double price = rs.getDouble("price");
                    Reservation reservation = new Reservation(id, workspaceId, type, name, start, end, price, date);
                    reservations.add(reservation);
                }
                connect().close();
                return reservations;
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public Reservation selectFromReservationsById(int id) {
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.selectFromReservByIdSQL)) {
                preparedStatement.setInt(1, id);

                try(ResultSet rs = preparedStatement.executeQuery()) {
                    Reservation reservation = null;
                    while (rs.next()) {
                        int workspaceId = rs.getInt("workspace_id");
                        String name = rs.getString("name");
                        String start = rs.getDate("start_date").toString();
                        String end = rs.getDate("end_date").toString();
                        String date = rs.getTimestamp("date").toString();
                        String type = rs.getString("type");
                        double price = rs.getDouble("price");
                        reservation = new Reservation(id, workspaceId, type, name, start, end, price, date);
                    }
                    connect().close();
                    return reservation;
                }
            } catch (SQLException | NullPointerException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public ArrayList<Reservation> selectFromMyReservations(String name) {
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.selectFromMyReservTableSQL)) {
                preparedStatement.setString(1, name);
                ArrayList<Reservation> reservations = new ArrayList<Reservation>();
                try(ResultSet rs = preparedStatement.executeQuery()){
                    while (rs.next()){
                        int id = rs.getInt("id");
                        int workspaceId = rs.getInt("workspace_id");
                        String start = rs.getDate("start_date").toString();
                        String end = rs.getDate("end_date").toString();
                        String date = rs.getTimestamp("date").toString();
                        String type = rs.getString("type");
                        double price = rs.getDouble("price");
                        Reservation reservation = new Reservation(id, workspaceId, type, name, start, end, price, date);
                        reservations.add(reservation);
                    }
                    connect().close();
                    return reservations;
                }
            }catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public int updateWorkspace(String type, double price, int id){
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.updateWorkspTableSQL)) {
                preparedStatement.setString(1, type);
                preparedStatement.setDouble(2, price);
                preparedStatement.setInt(3, id);
                connect().close();
                return preparedStatement.executeUpdate();// N of rows affected
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }

    public int updateAvailabilityStatus(boolean availabilityStatus, int id){
        if (connect() != null){
            try (PreparedStatement preparedStatement = connect().prepareStatement(SQLQueries.updateAvailabilityStatusSQL)) {
                preparedStatement.setBoolean(1, availabilityStatus);
                preparedStatement.setInt(2, id);
                connect().close();
                return preparedStatement.executeUpdate();// N of rows affected
            } catch (SQLException | NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new ConnectionException("No connection to DB. Check username, password or DB-driver");}
    }
}

package coworking.databases;

public class HQLQueries {
    public static final String selectAvailableWorkspTableSQL = """
            FROM Workspace WHERE availabilityStatus=true""";
    public static final String selectFromReservTableSQL = """
            FROM Reservation r\s
            JOIN FETCH r.workspace\s
            JOIN FETCH r.user""";
    public static final String selectFromMyReservTableSQL = """
            FROM Reservation r
            JOIN FETCH r.workspace
            JOIN FETCH r.user
            WHERE r.user.name = :name""";
    public static final String selectFromWorkspTableSQL = """
            FROM Workspace""";
    public static final String selectFromUsersTableSQL = """
            FROM User u
            WHERE u.name = :name""";

}

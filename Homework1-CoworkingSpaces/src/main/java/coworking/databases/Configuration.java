package coworking.databases;

/**
 * Configuration variables
 */
public class Configuration {
    private static String DB_URL = "jdbc:postgresql://localhost:5432/CoworkingSpaceApp";
    private static String login = "postgres";
    private static String password = "postgres3996";
    private static String DB_Driver = "org.postgresql.Driver";

    public static String getDbUrl() {
        return DB_URL;
    }

    public static void setDbUrl(String dbUrl) {
        DB_URL = dbUrl;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Configuration.login = login;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Configuration.password = password;
    }

    public static String getDB_Driver() {
        return DB_Driver;
    }

    public static void setDB_Driver(String DB_Driver) {
        Configuration.DB_Driver = DB_Driver;
    }
}

package carsharing;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DBManager {
    public final static String H2_DRIVER = "org.h2.Driver";
    public final static String defaultDbName = "carsharing";
    public final static String dbPath = "./src/carsharing/db";
    public final static String jdbcURL = String.format("jdbc:h2:%s/%s", dbPath, defaultDbName);

    static {
        File dbDir = new File(dbPath);

        if (!dbDir.exists())
            dbDir.mkdirs();

        try {
            Class.forName(H2_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException {
        Connection connection = null;

        try {

            connection = DriverManager.getConnection(jdbcURL);
            connection.setAutoCommit(true);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return connection;
    }

    public static void createTableCompany() {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT AUTO_INCREMENT NOT NULL," +
                    "NAME VARCHAR(50) NOT NULL," +
                    "PRIMARY KEY (ID)," +
                    "UNIQUE KEY company_name_unique (NAME)" +
                    ");");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void createTableCar() {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT AUTO_INCREMENT NOT NULL," +
                    "NAME VARCHAR(50) NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "PRIMARY KEY (ID)," +
                    " UNIQUE KEY car_name_unique (NAME)," +
                    " CONSTRAINT fk_company_id FOREIGN KEY (COMPANY_ID)" +
                    " REFERENCES COMPANY(ID)" +
                    " ON DELETE CASCADE" +
                    " ON UPDATE CASCADE" +
                    ");");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void createTableCustomer() {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT AUTO_INCREMENT NOT NULL," +
                    "NAME VARCHAR(50) NOT NULL," +
                    "RENTED_CAR_ID INT," +
                    "PRIMARY KEY (ID)," +
                    " UNIQUE KEY customer_name_unique (NAME)," +
                    " CONSTRAINT fk_car_id FOREIGN KEY (RENTED_CAR_ID)" +
                    " REFERENCES CAR(ID)" +
                    " ON DELETE SET NULL" +
                    " ON UPDATE CASCADE" +
                    ");");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void dropTables() {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE IF EXISTS CAR;" +
                    "DROP TABLE IF EXISTS COMPANY;" +
                    "DROP TABLE IF EXISTS CUSTOMER;");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}

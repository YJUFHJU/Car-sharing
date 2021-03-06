package carsharing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Class implementing EntityDao interface for CAR table
 */
public class CarDaoImpl implements EntityDao<Car> {
    private final static String tableName = "CAR";

    private final static String INSERT_CAR = String.format("insert into %s(NAME, COMPANY_ID) values(?, ?);", tableName);
    private final static String RENAME_CAR_BY_ID = String.format("update %s set NAME=? where ID=?", tableName);
    private final static String DELETE_CAR_BY_ID = String.format("delete from %s where ID=?;", tableName);
    private final static String SELECT_CARS_BY_CO_ID = String.format("select * from %s where COMPANY_ID=?;", tableName);
    private final static String SELECT_CAR_BY_ID = String.format("select * from %s where ID=?;", tableName);
    private final static String SELECT_AVL_CARS_BY_CO_ID = String.format("select * from %s where COMPANY_ID=? " +
                "and ID not in (select RENTED_CAR_ID from CUSTOMER where RENTED_CAR_ID is not null);", tableName);


    @Override
    public List<Car> selectAll() {
        return null;
    }

    @Override
    public Car select(int ID) {
        Car car = null;

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(SELECT_CAR_BY_ID)) {

            prepStat.setInt(1, ID);

            ResultSet resultSet = prepStat.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("NAME");
                int companyId = resultSet.getInt("COMPANY_ID");

                car = new Car(ID, name, companyId);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return car;
    }

    @Override
    public void insert(Car car) throws SQLException {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(INSERT_CAR)) {

            prepStat.setString(1, car.getName());
            prepStat.setInt(2, car.getCompanyId());
            prepStat.execute();
        }
    }

    @Override
    public void delete(int ID) throws SQLException {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(DELETE_CAR_BY_ID)) {

            prepStat.setInt(1, ID);
            prepStat.execute();
        }
    }

    @Override
    public void update(int ID, String newName) throws SQLException {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(RENAME_CAR_BY_ID)) {

            prepStat.setString(1, newName);
            prepStat.setInt(2, ID);
            prepStat.execute();
        }
    }

    /*
     * Select all available cars with the given Company ID
     * (cars that are not taken by other customers at the moment)
     * and return them as a List of Car objects
     */
    public List<Car> selectAvlCarsByCoId(int companyID) {
        List<Car> cars = new ArrayList<>();

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(SELECT_AVL_CARS_BY_CO_ID)) {

            prepStat.setInt(1, companyID);

            ResultSet resultSet = prepStat.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");

                cars.add(new Car(id, name, companyID));
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return cars;
    }

    /*
     * Select all cars with the given Company ID
     * and return them as a List of Car objects
     */
    public List<Car> selectCarsByCoId(int companyID) {
        List<Car> cars = new ArrayList<>();

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(SELECT_CARS_BY_CO_ID)) {

            prepStat.setInt(1, companyID);

            ResultSet resultSet = prepStat.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");

                cars.add(new Car(id, name, companyID));
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return cars;
    }
}

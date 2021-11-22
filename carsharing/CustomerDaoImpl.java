package carsharing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    private final static String tableName = "CUSTOMER";

    private final static String INSERT_CUSTOMER = String.format("insert into %s(NAME, RENTED_CAR_ID) values(?, ?);", tableName);
    private final static String SELECT_ALL_CUSTOMERS = String.format("select * from %s;", tableName);
    private final static String SELECT_CUSTOMER_BY_ID = String.format("select * from %s where ID=?;", tableName);
    private final static String DELETE_CUSTOMER_BY_ID = String.format("delete from %s where ID=?;", tableName);
    private final static String RENAME_CUSTOMER_BY_ID = String.format("update %s set NAME=? where ID=?;", tableName);
    private final static String UPDATE_RENTED_CAR_ID = String.format("update %s set RENTED_CAR_ID=? where ID=?;", tableName);

    @Override
    public List<Customer> selectAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(SELECT_ALL_CUSTOMERS)) {

            ResultSet resultSet = prepStat.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                Integer rentedCarId = resultSet.getInt("RENTED_CAR_ID");

                customers.add(new Customer(id, name, rentedCarId));
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return customers;
    }

    @Override
    public Customer selectCustomer(Integer ID) {
        Customer customer = null;

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(SELECT_CUSTOMER_BY_ID)) {

            prepStat.setInt(1, ID);

            ResultSet resultSet = prepStat.executeQuery();
            if (resultSet.next()) {
                Integer id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                Integer rentedCarId = resultSet.getInt("RENTED_CAR_ID");

                customer = new Customer(id, name, rentedCarId);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return customer;
    }

    @Override
    public void insertCustomer(Customer customer) throws SQLException {

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(INSERT_CUSTOMER)) {

            prepStat.setString(1, customer.getName());
            if (customer.getRentedCarId() == null)
                prepStat.setNull(2, Types.INTEGER);
            else
                prepStat.setInt(2, customer.getRentedCarId());

            prepStat.execute();
        }
    }

    @Override
    public void deleteCustomer(int ID) throws SQLException {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(DELETE_CUSTOMER_BY_ID)) {

            prepStat.setInt(1, ID);
            prepStat.execute();
        }
    }

    @Override
    public void updateCustomer(int ID, String newName) throws SQLException {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(RENAME_CUSTOMER_BY_ID)) {

            prepStat.setString(1, newName);
            prepStat.setInt(2, ID);
            prepStat.execute();
        }
    }

    @Override
    public boolean updateRentedCarId(Customer customer, Integer newId) throws SQLException {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(UPDATE_RENTED_CAR_ID)) {

            if (newId == null)
                prepStat.setNull(1, Types.INTEGER);
            else
                prepStat.setInt(1, newId);
            prepStat.setInt(2, customer.getId());

            return prepStat.executeUpdate() > 0;
        }
    }
}

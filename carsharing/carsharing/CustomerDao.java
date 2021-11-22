package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {

    List<Customer> selectAllCustomers();

    Customer selectCustomer(Integer id);

    void insertCustomer(Customer customer) throws SQLException;

    void deleteCustomer(int ID) throws SQLException;

    void updateCustomer(int ID, String newName) throws SQLException;

    boolean updateRentedCarId(Customer customer, Integer newId) throws SQLException;
}

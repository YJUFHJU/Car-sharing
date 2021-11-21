package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {

    List<Customer> selectAllCustomers();

    Customer selectCustomerById(Integer id);

    void insertCustomer(Customer customer) throws SQLException;

    boolean updateRentedCarId(Customer customer, Integer newId) throws SQLException;
}

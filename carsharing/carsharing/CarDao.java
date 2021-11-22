package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CarDao {

    List<Car> selectCarsByCoId(int companyID);

    Car selectCar(int ID);

    void insertCar(Car car) throws SQLException;

    void deleteCar(int ID) throws SQLException;

    void updateCar(int ID, String newName) throws SQLException;
}

package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CarDao {

    List<Car> selectCarsByCoId(int companyId);

    Car selectCarById(int ID);

    void insertCar(Car car) throws SQLException;

    //void removeCarById(int id);

    //void updateCarById(int id, String... newProps);
}

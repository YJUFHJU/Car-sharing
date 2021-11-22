package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface EntityDao <T extends Entity> {

    List<T> selectAll();

    T select(int ID);

    void insert(T entity) throws SQLException;

    void delete(int ID) throws SQLException;

    void update(int ID, String newName) throws SQLException;
}

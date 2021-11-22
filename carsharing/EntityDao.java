package carsharing;

import java.sql.SQLException;
import java.util.List;

/*
 * Interface declaring basic methods for accessing
 * data in tables and transferring it into objects
 */
public interface EntityDao <T extends Entity> {

	//select all rows from table and return it as a list of corresponding objects
    List<T> selectAll();

    //select row with given ID and return it as a corresponding object
    T select(int ID);

    //insert new row into the table based on the given object
    void insert(T object) throws SQLException;

    //delete row from table with given ID
    void delete(int ID) throws SQLException;

    //update 'NAME' value with the given 'newName' where ID='ID'
    void update(int ID, String newName) throws SQLException;
}

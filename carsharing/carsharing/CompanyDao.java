package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDao {

    List<Company> selectAllCompanies();

    Company selectCompany(int ID);

    void insertCompany(Company company) throws SQLException;

    void deleteCompany(int ID) throws SQLException;

    void updateCompany(int ID, String newName) throws SQLException;
}

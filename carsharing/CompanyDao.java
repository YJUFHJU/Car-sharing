package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDao {

    List<Company> selectAllCompanies();

    Company selectCompanyById(int id);

    void insertCompany(Company company) throws SQLException;

    //void removeCompanyById(int id);

    //void updateCompanyById(int id, String... newProps);
}

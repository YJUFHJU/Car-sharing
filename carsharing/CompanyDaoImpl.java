package carsharing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private final static String tableName = "COMPANY";

    private final static String INSERT_COMPANY = String.format("insert into %s(NAME) values(?);", tableName);
    private final static String SELECT_ALL_COMPANIES = String.format("select * from %s;", tableName);
    private final static String SELECT_COMPANY_BY_ID = String.format("select * from %s where ID=?;", tableName);


    @Override
    public List<Company> selectAllCompanies() {
        List<Company> companies = new ArrayList<>();

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(SELECT_ALL_COMPANIES)) {

            ResultSet resultSet = prepStat.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");

                companies.add(new Company(id, name));
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return companies;
    }

    @Override
    public Company selectCompanyById(int ID) {
        Company company = null;

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(SELECT_COMPANY_BY_ID)) {

            prepStat.setInt(1, ID);

            ResultSet resultSet = prepStat.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");

                company = new Company(id, name);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return company;
    }

    @Override
    public void insertCompany(Company company) throws SQLException {

        try (Connection connection = DBManager.getConnection();
             PreparedStatement prepStat = connection.prepareStatement(INSERT_COMPANY)) {

            prepStat.setString(1, company.getName());
            prepStat.execute();
        }
    }
}

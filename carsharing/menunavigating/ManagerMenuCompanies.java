package carsharing.menunavigating;

import carsharing.Company;
import carsharing.CompanyDao;
import carsharing.CompanyDaoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ManagerMenuCompanies implements Menu {

    @Override
    public void printOptions() {
        System.out.println("\n1. Company list\n2. Create a company\n0. Back");
    }

    @Override
    public void navigate(BufferedReader userInput) {
        String option = "0";
        CompanyDao companyDao = new CompanyDaoImpl();
        Menu carsMenu = new ManagerMenuCars();

        do {
            printOptions();

            try {
                option = userInput.readLine().strip();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            switch (option) {
                case "1":
                    carsMenu.navigate(userInput);
                    break;
                case "2":
                    createCompany(userInput, companyDao);
                    break;
                case "0":
                    //do nothing
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        } while (!"0".equals(option));
    }

    private void createCompany(BufferedReader userInput, CompanyDao companyDao) {
        System.out.println("\nEnter the company name:");

        try {
            String name = userInput.readLine().strip();
            companyDao.insertCompany(new Company(1, name));
            System.out.println("The company was created!");
        } catch (SQLIntegrityConstraintViolationException sqlicve) {

            switch (sqlicve.getErrorCode()) {
                case ERROR_CODES.NOT_UNIQUE: //name is not unique
                    System.out.println("Company with such name already exists.");
                    break;
                case ERROR_CODES.IS_NULL: //name is null
                    System.out.println("Company name can not be empty.");
                    break;
                default:
                    sqlicve.printStackTrace();
            }
        } catch (SQLException | IOException exc) {
            exc.printStackTrace();
        }
    }
}

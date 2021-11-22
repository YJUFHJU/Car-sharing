package carsharing.menunavigating;

import carsharing.Company;
import carsharing.CompanyDaoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ManagerMenuCompanies implements Menu {

    @Override
    public void printOptions() {
        System.out.println("\n1. Company list" +
                "\n2. Create a company" +
                "\n3. Delete a company" +
                "\n4. Rename a company" +
                "\n0. Back");
    }

    @Override
    public void navigate(BufferedReader userInput) {
        String option = "0";
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
                    createCompany(userInput);
                    break;
                case "3":
                    deleteCompany(userInput);
                    break;
                case "4":
                    renameCompany(userInput);
                    break;
                case "0":
                    //do nothing
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        } while (!"0".equals(option));
    }

    private void renameCompany(BufferedReader userInput) {
        CompanyDaoImpl companyDao = new CompanyDaoImpl();
        Company chosenCompany = chooseCompany(userInput);

        if (chosenCompany == null)
            return;

        System.out.println("\nEnter the company new name:");

        try {
            companyDao.update(chosenCompany.getId(), userInput.readLine().strip());
            System.out.println("The company was renamed!");
        } catch (SQLIntegrityConstraintViolationException sqlicve) {

            switch (sqlicve.getErrorCode()) {
                case ERROR_CODES.NOT_UNIQUE: //name is not unique
                    System.out.println("Different company with such name already exists.");
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

    private void deleteCompany(BufferedReader userInput) {
        CompanyDaoImpl companyDao = new CompanyDaoImpl();
        Company chosenCompany = chooseCompany(userInput);

        if (chosenCompany == null)
            return;

        try {
            companyDao.delete(chosenCompany.getId());
            System.out.println("\nCompany was successfully deleted!");
        } catch (SQLException sqle) {
        	if (sqle.getErrorCode() == ERROR_CODES.REFER_INTEGRITY)
        		System.out.println("\nCannot delete not empty company!");
        	else
        		sqle.printStackTrace();
        }
    }

    private void createCompany(BufferedReader userInput) {
        CompanyDaoImpl companyDao = new CompanyDaoImpl();
        System.out.println("\nEnter the company name:");

        try {
            String name = userInput.readLine().strip();
            companyDao.insert(new Company(1, name));
            System.out.println("\nThe company was created!");
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

    Company chooseCompany(BufferedReader userInput) {
        Company company = null;
        CompanyDaoImpl companyDao = new CompanyDaoImpl();
        List<Company> companies = companyDao.selectAll();

        if (!printCompaniesList(companies))
            return null;

        while (company == null) {
            int option;

            try {
                option = Integer.parseInt(userInput.readLine());
            } catch (NumberFormatException ime) {
                System.out.println("Option must be a number.");
                continue;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                break;
            }
            if (option == 0)
                return null;

            if (option > companies.size())
                System.out.println("Company with such number does not exist.");
            else
                company = companyDao.select(companies.get(option - 1).getId());

        }

        return company;
    }

    boolean printCompaniesList(List<Company> companies) {
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
            return false;
        } else
            System.out.println("\nChoose the company:");

        for (int i = 1; i <= companies.size(); i++) {
            System.out.printf("%d. %s\n", i, companies.get(i - 1).getName());
        }
        System.out.println("0. Back");
        return true;
    }
}

package carsharing.menunavigating;

import carsharing.Car;
import carsharing.CarDao;
import carsharing.CarDaoImpl;
import carsharing.Company;
import carsharing.CompanyDao;
import carsharing.CompanyDaoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ManagerMenuCars implements Menu {

    @Override
    public void printOptions() {
        System.out.println("\n1. Car list\n2. Create a car\n0. Back");
    }

    @Override
    public void navigate(BufferedReader userInput) {
        String option = "0";
        CarDao carDao = new CarDaoImpl();
        Company chosenCompany = chooseCompany(userInput);

        if (chosenCompany == null)
            return; //No companies or 'Back' option

        System.out.printf("\n'%s' company", chosenCompany.getName());
        do {
            printOptions();

            try {
                option = userInput.readLine().strip();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            switch (option) {
                case "1":
                    printCarsList(carDao.selectCarsByCoId(chosenCompany.getId()));
                    break;
                case "2":
                    createCarByCompanyId(userInput, carDao, chosenCompany);
                    break;
                case "0":
                    //do nothing
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (!"0".equals(option));
    }

    private void createCarByCompanyId(BufferedReader userInput, CarDao carDao, Company company) {
        System.out.println("\nEnter the car name:");

        try {
            carDao.insertCar(new Car(1, userInput.readLine().strip(), company.getId()));
            System.out.println("The car was created!");
        } catch (SQLIntegrityConstraintViolationException sqlicve) {

            switch (sqlicve.getErrorCode()) {
                case ERROR_CODES.NOT_UNIQUE: //name is not unique
                    System.out.println("Car with such name already exists.");
                    break;
                case ERROR_CODES.IS_NULL: //name is null
                    System.out.println("Car name can not be empty.");
                    break;
                default:
                    sqlicve.printStackTrace();
            }
        } catch (SQLException | IOException exc) {
            exc.printStackTrace();
        }
    }

    boolean printCarsList(List<Car> cars) {
        if (cars.isEmpty()) {
            System.out.println("\nThe car list is empty!");
            return false;
        } else
            System.out.println("\nCar list:");

        for (int i = 1; i <= cars.size(); i++) {
            System.out.printf("%d. %s\n", i, cars.get(i - 1).getName());
        }
        return true;
    }

    Company chooseCompany(BufferedReader userInput) {
        Company company = null;
        CompanyDao companyDao = new CompanyDaoImpl();
        List<Company> companies = companyDao.selectAllCompanies();

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
                company = companyDao.selectCompanyById(companies.get(option - 1).getId());

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
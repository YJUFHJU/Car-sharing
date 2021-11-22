package carsharing.menunavigating;

import carsharing.Car;
import carsharing.CarDao;
import carsharing.CarDaoImpl;
import carsharing.Company;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ManagerMenuCars implements Menu {

    @Override
    public void printOptions() {
        System.out.println("\n1. Car list" +
                "\n2. Create a car" +
                "\n3. Delete a car" +
                "\n4. Rename a car" +
                "\n0. Back");
    }

    @Override
    public void navigate(BufferedReader userInput) {
        String option = "0";
        CarDao carDao = new CarDaoImpl();
        Company chosenCompany = new ManagerMenuCompanies().chooseCompany(userInput);

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
                    createCarByCompany(userInput, chosenCompany);
                    break;
                case "3":
                    deleteCarByCompany(userInput, chosenCompany);
                    break;
                case"4":
                    renameCarByCompany(userInput, chosenCompany);
                    break;
                case "0":
                    //do nothing
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (!"0".equals(option));
    }

    private void renameCarByCompany(BufferedReader userInput, Company company) {
        CarDao carDao = new CarDaoImpl();
        Car chosenCar = chooseCarByCompany(userInput, company);

        if (chosenCar == null)
            return;

        System.out.println("\nEnter the car new name:");

        try {
            carDao.updateCar(chosenCar.getId(), userInput.readLine().strip());
            System.out.println("The car was renamed!");
        } catch (SQLIntegrityConstraintViolationException sqlicve) {

            switch (sqlicve.getErrorCode()) {
                case ERROR_CODES.NOT_UNIQUE: //name is not unique
                    System.out.println("Different car with such name already exists.");
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

    private void deleteCarByCompany(BufferedReader userInput, Company company) {
        CarDao carDao = new CarDaoImpl();
        Car chosenCar = chooseCarByCompany(userInput, company);

        if (chosenCar == null)
            return;

        try {
            carDao.deleteCar(chosenCar.getId());
            System.out.println("Car was deleted successfully!");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void createCarByCompany(BufferedReader userInput, Company company) {
        CarDao carDao = new CarDaoImpl();
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

    Car chooseCarByCompany(BufferedReader userInput, Company chosenCompany) {
        Car car = null;
        CarDao carDao = new CarDaoImpl();
        ManagerMenuCars menuCars = new ManagerMenuCars();

        List<Car> companyCars = carDao.selectCarsByCoId(chosenCompany.getId());
        if (!menuCars.printCarsList(companyCars))
            return null;
        System.out.println("0. Back");

        while (car == null) {
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

            if (option > companyCars.size())
                System.out.println("Car with such number does not exist.");
            else
                car = carDao.selectCar(companyCars.get(option - 1).getId());
        }
        return car;
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
}

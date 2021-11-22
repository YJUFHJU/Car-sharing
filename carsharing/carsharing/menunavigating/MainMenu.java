package carsharing.menunavigating;

import carsharing.Customer;
import carsharing.CustomerDao;
import carsharing.CustomerDaoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class MainMenu implements Menu {

    @Override
    public void printOptions() {
        System.out.println("\n1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
    }

    @Override
    public void navigate(BufferedReader userInput) {
        String option = "0";
        Menu managerMenuCompanies = new ManagerMenuCompanies();
        Menu customerMenu = new CustomerMenu();

        do {
            printOptions();

            try {
                option = userInput.readLine().strip();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            switch (option) {
                case "1":
                    managerMenuCompanies.navigate(userInput);
                    break;
                case "2":
                    customerMenu.navigate(userInput);
                    break;
                case "3":
                    createCustomer(userInput);
                    break;
                case "0":
                    //do nothing
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        } while (!"0".equals(option));
    }

    public void createCustomer(BufferedReader userInput) {
        System.out.println("\nEnter the customer name:");

        CustomerDao customerDao = new CustomerDaoImpl();
        try {
            String name = userInput.readLine().strip();
            customerDao.insertCustomer(new Customer(1, name, null));
            System.out.println("\nThe customer was created!");
        } catch (SQLIntegrityConstraintViolationException sqlicve) {

            switch (sqlicve.getErrorCode()) {
                case ERROR_CODES.NOT_UNIQUE: //name is not unique
                    System.out.println("\nCustomer with such name already exists.");
                    break;
                case ERROR_CODES.IS_NULL: //name is null
                    System.out.println("\nCustomer name can not be empty.");
                    break;
                default:
                    sqlicve.printStackTrace();
            }
        } catch (SQLException | IOException exc) {
            exc.printStackTrace();
        }
    }
}

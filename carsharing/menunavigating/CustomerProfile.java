package carsharing.menunavigating;

import carsharing.Customer;
import carsharing.CustomerDaoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CustomerProfile {

    private static void printOptions() {
        System.out.println("\n1. Change nickname" +
                "\n2. Delete profile" +
                "\n0. Back");
    }

    static void page(BufferedReader userInput, Customer customer) {
        String option = "0";

        System.out.printf("\n'%s' profile", customer.getName());
        do {
            printOptions();

            try {
                option = userInput.readLine().strip();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            switch (option) {
                case "1":
                    renameCustomer(customer, userInput);
                    break;
                case "2":
                    deleteCustomer(customer);
                    return;
                case "0":
                    //do nothing
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        } while (!"0".equals(option));
    }

    static void deleteCustomer(Customer customer) {
        CustomerDaoImpl customerDao = new CustomerDaoImpl();

        try {
            customerDao.delete(customer.getId());
            customer.setId(null);
            System.out.println("\nProfile was deleted successfully!");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    static void renameCustomer(Customer customer, BufferedReader userInput) {
        CustomerDaoImpl customerDao = new CustomerDaoImpl();

        System.out.println("\nEnter new nickname:");

        try {
            String newName = userInput.readLine().strip();
            customerDao.update(customer.getId(), newName);
            customer.setName(newName);
            System.out.printf("\nNickname was changed to '%s'.\n", newName);
        } catch (SQLIntegrityConstraintViolationException sqlicve) {

            switch (sqlicve.getErrorCode()) {
                case Menu.ERROR_CODES.NOT_UNIQUE: //name is not unique
                    System.out.println("Different customer with such nickname already exists.");
                    break;
                case Menu.ERROR_CODES.IS_NULL: //name is null
                    System.out.println("Nickname can not be empty.");
                    break;
                default:
                    sqlicve.printStackTrace();
            }
        } catch (SQLException | IOException exc) {
            exc.printStackTrace();
        }
    }
}

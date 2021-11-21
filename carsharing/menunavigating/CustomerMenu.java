package carsharing.menunavigating;

import carsharing.Car;
import carsharing.CarDao;
import carsharing.CarDaoImpl;
import carsharing.Company;
import carsharing.CompanyDao;
import carsharing.CompanyDaoImpl;
import carsharing.Customer;
import carsharing.CustomerDao;
import carsharing.CustomerDaoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CustomerMenu implements Menu {

    @Override
    public void printOptions() {
        System.out.println("\n1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
    }

    @Override
    public void navigate(BufferedReader userInput) {
        String option = "0";
        Customer chosenCustomer = chooseCustomer(userInput);

        if (chosenCustomer == null)
            return; //No customers or 'Back' option

        do {
            printOptions();

            try {
                option = userInput.readLine().strip();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            switch (option) {
                case "1":
                    rentCar(userInput, chosenCustomer);
                    break;
                case "2":
                    returnCar(chosenCustomer);
                    break;
                case "3":
                    showRentedCar(chosenCustomer);
                    break;
                case "0":
                    //do nothing
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        } while (!"0".equals(option));
    }

    private void showRentedCar(Customer customer) {
        Car car;
        Company company;
        CarDao carDao = new CarDaoImpl();
        CompanyDao companyDao = new CompanyDaoImpl();

        if (customer.getRentedCarId() == null) {
            System.out.println("\nYou didn't rent a car!");
            return;
        }

        car = carDao.selectCarById(customer.getRentedCarId());
        company = companyDao.selectCompanyById(car.getCompanyId());

        System.out.printf("\nYour rented car:\n%s\nCompany:\n%s\n", car.getName(), company.getName());
    }

    private boolean returnCar(Customer customer) {
        CustomerDao customerDao = new CustomerDaoImpl();

        if (customer.getRentedCarId() == null) {
            System.out.println("\nYou didn't rent a car!");
            return false;
        }

        try {
            customerDao.updateRentedCarId(customer, null);
            customer.setRentedCarId(null);
            System.out.println("\nYou've returned a rented car!");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean rentCar(BufferedReader userInput, Customer customer) {
        Car chosenCar;
        CustomerDao customerDao = new CustomerDaoImpl();
        ManagerMenuCars menuCars = new ManagerMenuCars();
        Company chosenCompany;

        //customer already has a car
        if (customer.getRentedCarId() != null) {
            System.out.println("\nYou've already rented a car!");
            return false;
        }

        chosenCompany = menuCars.chooseCompany(userInput);
        if(chosenCompany == null)
            return false;

        chosenCar = chooseCarByCompanyId(userInput, chosenCompany);
        if (chosenCar == null)
            return false;

        try {
            customerDao.updateRentedCarId(customer, chosenCar.getId());
            customer.setRentedCarId(chosenCar.getId());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }

        System.out.printf("\nYou rented '%s'\n", chosenCar.getName());
        return true;
    }

    Car chooseCarByCompanyId(BufferedReader userInput, Company chosenCompany) {
        Car car = null;
        CarDaoImpl carDao = new CarDaoImpl();
        ManagerMenuCars menuCars = new ManagerMenuCars();

        /*
        Checking if any cars belong to picked company
        and then checking if any cars are not taken by other customers.
         */
        List<Car> companyCars = carDao.selectCarsByCoId(chosenCompany.getId());
        if (!companyCars.isEmpty()) {
            companyCars = carDao.selectAvlCarsByCoId(chosenCompany.getId());

            if (companyCars.isEmpty()) {
                System.out.printf("\nNo available cars in the '%s' company\n", chosenCompany.getName());
                return null;
            }
        } else {
            System.out.println("\nThe car list is empty!");
            return null;
        }

        menuCars.printCarsList(companyCars);
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
                car = carDao.selectCarById(companyCars.get(option - 1).getId());
        }
        return car;
    }

    private Customer chooseCustomer(BufferedReader userInput) {
        Customer customer = null;
        CustomerDao customerDao = new CustomerDaoImpl();
        List<Customer> customers = customerDao.selectAllCustomers();

        if (!printCustomersList(customers))
            return null;

        while (customer == null) {
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

            if (option > customers.size())
                System.out.println("Customer with such number does not exist.");
            else
                customer = customerDao.selectCustomerById(customers.get(option - 1).getId());
        }

        return customer;
    }

    boolean printCustomersList(List<Customer> customers) {
        if (customers.isEmpty()) {
            System.out.println("\nThe customer list is empty!");
            return false;
        } else
            System.out.println("\nCustomer list:");

        for (int i = 1; i <= customers.size(); i++) {
            System.out.printf("%d. %s\n", i, customers.get(i - 1).getName());
        }
        System.out.println("0. Back");
        return true;
    }
}

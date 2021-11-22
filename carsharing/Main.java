package carsharing;

import carsharing.menunavigating.MainMenu;
import carsharing.menunavigating.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            DBManager.createTableCompany();
            DBManager.createTableCar();
            DBManager.createTableCustomer();

            Menu start = new MainMenu();
            start.navigate(userInput);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
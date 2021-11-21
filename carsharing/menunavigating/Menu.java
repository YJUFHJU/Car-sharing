package carsharing.menunavigating;


import java.io.BufferedReader;

public interface Menu {

    void printOptions();

    void navigate(BufferedReader userInput);

    class ERROR_CODES {
        public final static int IS_NULL = 23502;
        public final static int NOT_UNIQUE = 23505;
    }
}

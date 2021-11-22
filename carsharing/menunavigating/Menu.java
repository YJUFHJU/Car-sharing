package carsharing.menunavigating;


import java.io.BufferedReader;

/*
 * Template for any Menu class
 */
public interface Menu {

	/*
	 * Method that prints options in the standart output
	 */
    void printOptions();

    /*
     * Main method for shifting between other Menu classes,
     * proccessing user input and calling corresponding methods
     */
    void navigate(BufferedReader userInput);

    class ERROR_CODES {
        public final static int IS_NULL = 23502;
        public final static int REFER_INTEGRITY = 23503;
        public final static int NOT_UNIQUE = 23505;
    }
}

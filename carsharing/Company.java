package carsharing;

/*
 * Class for representing element of the COMPANY table
 */
public class Company extends Entity {

    public Company(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return String.format("Company: [ID: %d, NAME: %s]", getId(), getName());
    }
}

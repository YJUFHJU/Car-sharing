package carsharing;

public class Company extends TableEntity {

    public Company(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return String.format("Company: [ID: %d, NAME: %s]", getId(), getName());
    }
}

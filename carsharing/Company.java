package carsharing;

public class Company extends TableEntity {

    public Company(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return String.format("%d. %s", getId(), getName());
    }
}

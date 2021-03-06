package carsharing;

/*
 * Class for representing element of the CAR table
 */
public class Car extends Entity {
    private Integer companyId;

    public Car(Integer id, String name, Integer companyId) {
        super(id, name);

        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer newCompanyId) {
        companyId = newCompanyId;
    }

    @Override
    public String toString() {
        return String.format("Car: [ID: %d, NAME: %s, COMPANY_ID: %d]", getId(), getName(), companyId);
    }

}

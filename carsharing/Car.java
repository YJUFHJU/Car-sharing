package carsharing;

public class Car extends TableEntity {
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
        return String.format("%d. %s", getId(), getName());
    }

}

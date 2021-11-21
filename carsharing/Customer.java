package carsharing;

public class Customer extends TableEntity {
    private Integer rentedCarId;

    public Customer(Integer id, String name, Integer rentedCarId) {
        super(id, name);

        if (rentedCarId != null && rentedCarId <= 0)
            rentedCarId = null;

        this.rentedCarId = rentedCarId;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer newRentedCarId) {
        rentedCarId = newRentedCarId;
    }

    @Override
    public String toString() {
        return String.format("Customer:[id: %d; name: %s; rentedCarId: %d]", getId(), getName(), getRentedCarId());
    }
}

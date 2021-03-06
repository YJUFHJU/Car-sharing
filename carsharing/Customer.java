package carsharing;

/*
 * Class for representing element of the CUSTOMER table
 */
public class Customer extends Entity {
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
        return String.format("Customer:[ID: %d; NAME: %s; RENTED_CAR_ID: %d]", getId(), getName(), rentedCarId);
    }
}

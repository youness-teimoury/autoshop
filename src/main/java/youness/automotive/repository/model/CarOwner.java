package youness.automotive.repository.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The entity mapping that represents the owner of the car
 */
@Entity
@Table(name = "car_owner")
public class CarOwner extends BaseEntity {
    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "second_name")
    private String secondName;

    @Pattern(regexp = "\\d{6,15}", message = "Phone number should be between 6 to 15 digits only")
    @Column(name = "phone", unique = true)
    private String phone;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")// ovoid double mapping by mapper
    private Set<Car> cars = new HashSet<>();// TODO add to tests

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public void addCar(Car car) {// TODO add test
        this.cars.add(car);
        car.setOwner(this);
    }

    @Override
    public String toString() {
        return firstName + " " + secondName;
    }
}

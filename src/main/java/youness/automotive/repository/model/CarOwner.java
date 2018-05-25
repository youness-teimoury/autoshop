package youness.automotive.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
}

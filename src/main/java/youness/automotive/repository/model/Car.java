package youness.automotive.repository.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The JPA entity to map car
 */
@Entity
@Table(name = "car")
public class Car extends BaseEntity {

    @NotNull(message = "Car should have an owner")
    @ManyToOne
    private CarOwner owner;

    @NotNull(message = "Car should have a model")
    @ManyToOne
    private CarModel model;

    // TODO create a custom validator to check the max with current year
    @Range(min = 1900, max = 2018, message = "Year should be above 1900 and below 2018!")
    private int year;

    @Range(min = 0, max = 1000000, message = "Odometer can be between 0 to 1 million kilometers!")
    private int odometer;

    @OneToMany
    private Set<MaintenanceJob> maintenanceJobs;

    public CarOwner getOwner() {
        return owner;
    }

    public void setOwner(CarOwner owner) {
        this.owner = owner;
    }

    public CarModel getModel() {
        return model;
    }

    public void setModel(CarModel model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public Set<MaintenanceJob> getMaintenanceJobs() {
        return maintenanceJobs;
    }

    public void setMaintenanceJobs(Set<MaintenanceJob> maintenanceJobs) {
        this.maintenanceJobs = maintenanceJobs;
    }
}

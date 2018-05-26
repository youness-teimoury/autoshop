package youness.automotive.repository.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
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

    /**
     * The current owner of the car
     * // TODO can be a ManyToMany link (car might be owned by many people during time)
     */
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

    @NotBlank
    @Column(name = "engine_no")
    private String engineNo;

    @OneToMany(mappedBy = "car")
    private Set<MaintenanceJob> maintenanceJobs = new HashSet<>();

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

    public void addMaintenanceJob(MaintenanceJob maintenanceJob) {
        this.maintenanceJobs.add(maintenanceJob);
        maintenanceJob.setCar(this);
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    @Override
    public String toString() {
        return this.getModel().getMaker().getName() + " " + this.getModel().getName() + " " + this.getYear();
    }
}

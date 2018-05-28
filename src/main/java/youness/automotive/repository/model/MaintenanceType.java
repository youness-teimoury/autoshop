package youness.automotive.repository.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The entity mapping to keep maintenance task types
 * The examples could be oil change, tire rotation
 * Some maintenance task are not applicable to specific car types; oil change to the electric cars
 */
@Entity
@Table(name = "maintenance_task_type")
public class MaintenanceType extends BaseEntity {
    @NotBlank(message = "The name should be specified.")
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;

    /**
     * The car types that this maintenance type is applicable to
     * Cascades are not allowed here for simplicity and to make sure transient objects are not allowed
     * Initialization is to avoid boilerplate code to create(check, sync, create) the set in addCarType method
     */
    @ManyToMany
    @JoinTable(name = "car_type_and_maintenance_type",
            joinColumns = @JoinColumn(name = "maintenance_type_id"),
            inverseJoinColumns = @JoinColumn(name = "car_type_id"))
    private Set<CarType> applicableCarTypes = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CarType> getApplicableCarTypes() {
        return applicableCarTypes;
    }

    public void setApplicableCarTypes(Set<CarType> applicableCarTypes) {
        this.applicableCarTypes = applicableCarTypes;
    }

    public void addCarType(CarType carType) {
        /*
         * if condition is to avoid loop in being called as the result of calling carType.addMaintenanceType(this)
         */
        if (!this.applicableCarTypes.contains(carType)) {
            this.applicableCarTypes.add(carType);
            carType.addMaintenanceType(this);
        }
    }
}

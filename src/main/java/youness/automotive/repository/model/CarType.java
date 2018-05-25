package youness.automotive.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * CarType entity maintains the mapping to different car types for each individual car
 * Electric, Gas, Diesel can be an example of different car types
 */
@Entity
@Table(name = "car_type")
public class CarType extends BaseEntity {

    /**
     * The public name of the user
     */
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name", unique = true)
    private String name;

    /**
     * The maintenance types applicable to this car type
     * Cascades are not allowed here for simplicity and to make sure transient objects are not allowed
     * Initialization is to avoid boilerplate code to create(check, sync, create) the set in addMaintenanceType method
     */
    @ManyToMany(mappedBy = "applicableCarTypes")
    private Set<MaintenanceType> applicableMaintenanceTypes = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MaintenanceType> getApplicableMaintenanceTypes() {
        return applicableMaintenanceTypes;
    }

    public void setApplicableMaintenanceTypes(Set<MaintenanceType> applicableMaintenanceTypes) {
        this.applicableMaintenanceTypes = applicableMaintenanceTypes;
    }

    public void addMaintenanceType(MaintenanceType maintenanceType) {
        /*
         * if condition is to avoid loop in being called as the result of calling maintenanceType.addCarType(this);
         */
        if (!this.applicableMaintenanceTypes.contains(maintenanceType)) {
            this.applicableMaintenanceTypes.add(maintenanceType);
            maintenanceType.addCarType(this);
        }
    }
}

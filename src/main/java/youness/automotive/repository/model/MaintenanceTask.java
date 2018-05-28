package youness.automotive.repository.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The entity mapping to track the maintenance record provided to a specific car
 */
@Entity
@Table(name = "maintenance_task")
public class MaintenanceTask extends BaseEntity {
    @NotNull(message = "The maintenance job should be specified.")
    @ManyToOne(fetch = FetchType.LAZY)
    private MaintenanceJob maintenanceJob;

    @NotNull(message = "The maintenance type should be specified.")
    @ManyToOne(fetch = FetchType.LAZY)
    private MaintenanceType type;

    @Column(name = "description")
    private String description;

    @Column(name = "charge")
    private double charge;

    public MaintenanceJob getMaintenanceJob() {
        return maintenanceJob;
    }

    public void setMaintenanceJob(MaintenanceJob maintenanceJob) {
        this.maintenanceJob = maintenanceJob;
    }

    public MaintenanceType getType() {
        return type;
    }

    public void setType(MaintenanceType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

}

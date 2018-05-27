package youness.automotive.repository.model;

import org.springframework.format.annotation.DateTimeFormat;
import youness.automotive.utils.DateUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The entity mapping to track the maintenance jobs provided to a specific car
 * Each instance represents the series of maintenance tasks that are provided to a car during a maintenance period
 * (e.g. each visit/drop in)
 */
@Entity
@Table(name = "maintenance_job")
public class MaintenanceJob extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate = new Date();

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * The detailed tasks under this job
     * Initialization is to avoid boilerplate code to create(check, sync, create) the set in addTask method
     */
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "maintenanceJob")
    private Set<MaintenanceTask> maintenanceTasks = new HashSet<>();

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
        if (!this.car.getMaintenanceJobs().contains(this)) {
            this.car.addMaintenanceJob(this);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<MaintenanceTask> getMaintenanceTasks() {
        return maintenanceTasks;
    }

    public void setMaintenanceTasks(Set<MaintenanceTask> maintenanceTasks) {
        this.maintenanceTasks = maintenanceTasks;
    }

    public void addTask(MaintenanceTask maintenanceTask) {
        this.maintenanceTasks.add(maintenanceTask);
        maintenanceTask.setMaintenanceJob(this);
    }

    @Override
    public String toString() {
        return DateUtils.format(startDate) + (endDate == null ? " [ongoing]" : "to " + DateUtils.format(endDate));
    }
}

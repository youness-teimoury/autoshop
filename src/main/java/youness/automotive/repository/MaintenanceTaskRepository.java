package youness.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youness.automotive.repository.model.MaintenanceJob;
import youness.automotive.repository.model.MaintenanceTask;

import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The spring data repository for MaintenanceTask
 */
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long> {
    List<MaintenanceTask> findByMaintenanceJob(MaintenanceJob maintenanceJob);
}

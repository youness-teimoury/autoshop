package youness.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youness.automotive.repository.model.Car;
import youness.automotive.repository.model.MaintenanceJob;

import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The spring data repository for MaintenanceJob
 */
public interface MaintenanceJobRepository extends JpaRepository<MaintenanceJob, Long> {
    List<MaintenanceJob> findByCar(Car car);
}

package youness.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youness.automotive.repository.model.MaintenanceType;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The spring data repository for MaintenanceType
 */
public interface MaintenanceTypeRepository extends JpaRepository<MaintenanceType, Long> {
    MaintenanceType findByName(String maintenanceTypeName);
}

//    https://github.com/jaxio/celerio-angular-quickstart

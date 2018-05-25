package youness.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youness.automotive.repository.model.CarType;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The spring data repository for CarType
 */
public interface CarTypeRepository extends JpaRepository<CarType, Long> {
    CarType findByName(String carTypeName);
}

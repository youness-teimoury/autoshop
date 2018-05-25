package youness.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youness.automotive.repository.model.CarMaker;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The spring data repository for CarMaker
 */
public interface CarMakerRepository extends JpaRepository<CarMaker, Long> {
    CarMaker findByName(String carMakerName);
}

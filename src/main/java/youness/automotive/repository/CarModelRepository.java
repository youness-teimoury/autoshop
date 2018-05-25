package youness.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youness.automotive.repository.model.CarModel;

import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The spring data repository for CarModel
 */
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    List<CarModel> findByName(String modelName);
}

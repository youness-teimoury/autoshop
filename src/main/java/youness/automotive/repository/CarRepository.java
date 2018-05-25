package youness.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youness.automotive.repository.model.Car;
import youness.automotive.repository.model.CarOwner;

import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The data repository interface for car entities
 */
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwner(CarOwner owner);
//    @Query("SELECT g FROM Car g WHERE g.owner = ?1 AND g.name = ?2")
//    Car findGByName(CarType carType, String name);
//
//    List<Car> findByDeleted(boolean deleted);
//
//    Car findByNameAndOwnerPhone(String name, String ownerPhone);
}

package youness.automotive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youness.automotive.repository.model.CarOwner;

import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The spring data repository for CarOwner
 */
public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {
    List<CarOwner> findByFirstName(String firstName);

    List<CarOwner> findBySecondName(String secondName);

    List<CarOwner> findByPhone(String phone);
}

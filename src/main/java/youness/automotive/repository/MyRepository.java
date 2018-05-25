package youness.automotive.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import youness.automotive.repository.model.Car;
import youness.automotive.repository.model.CarType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The main repository that handles DAO calls
 */
@Repository
public class MyRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Maintains the group owner ship
     * Car names should be unique in the scope of a user
     * key: owner user
     * value: group names
     */
    private Map<String, Set<String>> topicOwnerShip = new HashMap<>();

    /**
     * Maintains subscriptions to a group
     * key: groupName
     * value: users
     */
    private Map<String, Set<String>> groupSubscriptions = new HashMap<>();

    @Autowired
    private CarRepository carRepository;

    /**
     * Add group to the list of user's groups
     * Returns successfully if group is already added
     *
     * @param groupName
     * @param owner
     */
    @Transactional
    public void addNewCar(String groupName, CarType owner) {
        Car car = new Car();
        //car.setMake(groupName);
        carRepository.save(car);
    }


}

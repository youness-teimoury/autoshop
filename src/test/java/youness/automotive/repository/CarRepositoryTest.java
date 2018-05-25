package youness.automotive.repository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import youness.automotive.repository.model.*;
import youness.automotive.utils.TestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarMakerRepository carMakerRepository;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private CarOwnerRepository carOwnerRepository;

    private TestUtils testUtils;

    @Before
    public void init() {
        testUtils = new TestUtils();
        testUtils.setCarMakerRepository(carMakerRepository);
        testUtils.setCarTypeRepository(carTypeRepository);
        testUtils.setCarModelRepository(carModelRepository);
        testUtils.setCarOwnerRepository(carOwnerRepository);
    }

    @Test
    public void shouldSaveNewCarAndFindThatByOwner() {
        // Arrange
        String carMakerName = "TOYOTA";
        String carModelName = "RAV4";
        String carTypeName = "Gas";
        String carOwnerPhoneNo = "4038055680";

        CarOwner carOwner = testUtils.createAndSaveCarOwner(carOwnerPhoneNo);
        CarMaker carMaker = testUtils.createAndSaveCarMaker(carMakerName);
        CarType carType = testUtils.createAndSaveCarType(carTypeName);
        CarModel carModel = testUtils.createAndSaveCarModel(carModelName, carMaker, carType);

        Car car = new Car();
        car.setModel(carModel);
        car.setOwner(carOwner);
        car.setYear(2016);

        // Act
        carRepository.save(car);

        // Assert
        List<Car> fetchedCars = carRepository.findByOwner(carOwner);
        assertThat(fetchedCars, CoreMatchers.notNullValue());
        assertThat(fetchedCars, IsCollectionWithSize.hasSize(1));
        assertThat(fetchedCars.get(0), new Matcher<Car>() {
            @Override
            public boolean matches(Object o) {
                Car car = (Car) o;
                return car.getModel().equals(carModel)
                        && car.getOwner().equals(carOwner)
                        && car.getYear() == 2016;
            }

            @Override
            public void describeMismatch(Object o, Description description) {
            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
            }

            @Override
            public void describeTo(Description description) {
            }
        });
    }

    @Test
    public void testCarValidations() {
        // Arrange
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Car car = new Car();

        // Act
        Set<ConstraintViolation<Car>> violations = validator.validate(car);

        /// Assert
        assertThat(violations, IsCollectionWithSize.hasSize(3));
    }

}

package youness.automotive.repository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import youness.automotive.repository.model.CarModel;
import youness.automotive.utils.TestUtils;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarModelRepositoryTest {

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarMakerRepository carMakerRepository;

    @Autowired
    private CarTypeRepository carTypeRepository;

    private TestUtils testUtils;

    @Before
    public void init() {
        testUtils = new TestUtils();
        testUtils.setCarMakerRepository(carMakerRepository);
        testUtils.setCarTypeRepository(carTypeRepository);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldNotSaveCarModelWithoutCarMaker() {
        // Arrange
        String testCarModel = "RAV4";
        CarModel carModel = new CarModel();
        carModel.setName(testCarModel);

        // Act
        carModelRepository.save(carModel);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldNotSaveCarModelWithoutCarType() {
        // Arrange
        String testCarModel = "RAV4";
        CarModel carModel = new CarModel();
        carModel.setName(testCarModel);
        String testCarMakerName = "TOYOTA";
        carModel.setMaker(testUtils.createAndSaveCarMaker(testCarMakerName));

        // Act
        carModelRepository.save(carModel);
    }

    @Test
    public void shouldSaveCarModelAndFindByName() {
        // Arrange
        String testCarModel = "RAV4";
        CarModel carModel = new CarModel();
        carModel.setName(testCarModel);

        String testCarMakerName = "TOYOTA";
        carModel.setMaker(testUtils.createAndSaveCarMaker(testCarMakerName));
        String testCarTypeName = "Gas";
        carModel.setCarType(testUtils.createAndSaveCarType(testCarTypeName));

        // Act
        carModelRepository.save(carModel);

        // Assert
        List<CarModel> fetchedCarModels = carModelRepository.findByName(testCarModel);
        assertThat(fetchedCarModels, CoreMatchers.notNullValue());
        assertThat(fetchedCarModels, IsCollectionWithSize.hasSize(1));
        CarModel fetchedCarModel = fetchedCarModels.get(0);
        assertThat(fetchedCarModel.getId(), CoreMatchers.notNullValue());
        assertThat(fetchedCarModel.getMaker(), CoreMatchers.notNullValue());
        assertThat(fetchedCarModel.getMaker().getName(), CoreMatchers.is(testCarMakerName));
    }

}

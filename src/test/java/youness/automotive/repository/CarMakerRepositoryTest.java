package youness.automotive.repository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import youness.automotive.repository.model.CarMaker;
import youness.automotive.repository.model.CarModel;
import youness.automotive.repository.model.CarType;
import youness.automotive.utils.TestUtils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarMakerRepositoryTest {

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

    @Test
    public void shouldSaveNewCarMakerAndFindThatByName() {
        // Arrange
        String carMakerName = "testCarMakerName";

        CarMaker carMaker = new CarMaker();
        carMaker.setName(carMakerName);

        // Act
        carMakerRepository.save(carMaker);

        // Assert
        CarMaker fetchedCarMaker = carMakerRepository.findByName(carMakerName);
        assertThat(fetchedCarMaker, CoreMatchers.notNullValue());
        assertThat(fetchedCarMaker.getId(), CoreMatchers.notNullValue());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotLetSavingTwoCarMakersWithTheSameName() {
        // Arrange
        String carMakerName = "testCarMakerName";
        CarMaker carMaker1 = new CarMaker();
        carMaker1.setName(carMakerName);
        carMakerRepository.save(carMaker1);

        CarMaker carMaker2 = new CarMaker();
        carMaker2.setName(carMakerName);

        // Act
        carMakerRepository.save(carMaker2);
    }

    @Test
    public void shouldSaveTransientCarModelWhenCarMakerIsSaved() {
        // Arrange
        CarType carType = testUtils.createAndSaveCarType("testCarType");
        String carModelName = "testCarModelName";
        CarModel carModel = new CarModel();
        carModel.setName(carModelName);
        carModel.setCarType(carType);

        String carMakerName = "testCarMakerName";
        CarMaker carMaker = new CarMaker();
        carMaker.setName(carMakerName);
        carMaker.addCarModel(carModel);

        // Act
        carMakerRepository.save(carMaker);

        // Assert
        CarMaker fetchedCarMaker = carMakerRepository.findByName(carMakerName);
        assertThat(fetchedCarMaker, CoreMatchers.notNullValue());
        assertThat(fetchedCarMaker.getId(), CoreMatchers.notNullValue());
        assertThat(fetchedCarMaker.getCarModels(), CoreMatchers.notNullValue());
        assertThat(fetchedCarMaker.getCarModels(), IsCollectionWithSize.hasSize(1));
        CarModel fetchedCarModel = fetchedCarMaker.getCarModels().iterator().next();
        assertThat(fetchedCarModel, CoreMatchers.notNullValue());
        assertThat(fetchedCarModel.getId(), CoreMatchers.notNullValue());
        assertThat(fetchedCarModel.getName(), is(carModelName));
    }
}

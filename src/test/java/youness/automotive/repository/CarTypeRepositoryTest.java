package youness.automotive.repository;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import youness.automotive.repository.model.CarType;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarTypeRepositoryTest {

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Test
    public void shouldSaveNewCarTypeAndFindThatByName() {
        // Arrange
        String carTypeName = "testCarTypeName";

        CarType carType = new CarType();
        carType.setName(carTypeName);

        // Act
        carTypeRepository.save(carType);

        // Assert
        CarType fetchedCarType = carTypeRepository.findByName(carTypeName);
        assertThat(fetchedCarType, CoreMatchers.notNullValue());
        assertThat(fetchedCarType.getId(), CoreMatchers.notNullValue());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotLetSavingTwoCarTypesWithTheSameName() {
        // Arrange
        String carTypeName = "testCarTypeName";
        CarType carType1 = new CarType();
        carType1.setName(carTypeName);
        carTypeRepository.save(carType1);

        CarType carType2 = new CarType();
        carType2.setName(carTypeName);

        // Act
        carTypeRepository.save(carType2);
    }

}

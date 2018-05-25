package youness.automotive.repository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import youness.automotive.repository.model.CarOwner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarOwnerRepositoryTest {

    @Autowired
    private CarOwnerRepository carOwnerRepository;

    @Test
    public void shouldSaveNewCarOwnerAndFindThatByPhone() {
        // Arrange
        String carOwnerFirstName = "testCarOwnerFirstName";
        String carOwnerSecondName = "testCarOwnerSecondName";
        String carOwnerPhone = "4038055680";

        CarOwner carOwner = new CarOwner();
        carOwner.setFirstName(carOwnerFirstName);
        carOwner.setSecondName(carOwnerSecondName);
        carOwner.setPhone(carOwnerPhone);

        // Act
        carOwnerRepository.save(carOwner);

        // Assert
        List<CarOwner> fetchedCarOwners = carOwnerRepository.findByPhone(carOwnerPhone);
        assertThat(fetchedCarOwners, CoreMatchers.notNullValue());
        assertThat(fetchedCarOwners, IsCollectionWithSize.hasSize(1));
        CarOwner fetchedCarOwner = fetchedCarOwners.get(0);
        assertThat(fetchedCarOwner.getFirstName(), CoreMatchers.is(carOwnerFirstName));
        assertThat(fetchedCarOwner.getSecondName(), CoreMatchers.is(carOwnerSecondName));
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldNotSaveNewCarOwnerWithInvalidPhoneNumber() {
        // Arrange
        String carOwnerFirstName = "testCarOwnerFirstName";
        String carOwnerSecondName = "testCarOwnerSecondName";
        String carOwnerPhone = "invalidPhoneNumber";

        CarOwner carOwner = new CarOwner();
        carOwner.setFirstName(carOwnerFirstName);
        carOwner.setSecondName(carOwnerSecondName);
        carOwner.setPhone(carOwnerPhone);

        // Act
        carOwnerRepository.save(carOwner);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotLetSavingTwoCarOwnersWithTheSamePhoneNumber() {
        // Arrange
        String carOwner1FirstName = "testCarOwner1FirstName";
        String carOwner1SecondName = "testCarOwner1SecondName";
        String carOwner2FirstName = "testCarOwner2FirstName";
        String carOwner2SecondName = "testCarOwner2SecondName";
        String duplicatePhoneNo = "4038055680";

        CarOwner carOwner1 = new CarOwner();
        carOwner1.setFirstName(carOwner1FirstName);
        carOwner1.setSecondName(carOwner1SecondName);
        carOwner1.setPhone(duplicatePhoneNo);
        carOwnerRepository.save(carOwner1);

        CarOwner carOwner2 = new CarOwner();
        carOwner2.setFirstName(carOwner2FirstName);
        carOwner2.setSecondName(carOwner2SecondName);
        carOwner2.setPhone(duplicatePhoneNo);

        // Act
        carOwnerRepository.save(carOwner2);
    }


}

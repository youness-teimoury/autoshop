package youness.automotive.repository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import youness.automotive.repository.model.CarType;
import youness.automotive.repository.model.MaintenanceType;
import youness.automotive.utils.TestUtils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MaintenanceTypeRepositoryTest {

    @Autowired
    private MaintenanceTypeRepository maintenanceTypeRepository;

    @Autowired
    private CarTypeRepository carTypeRepository;

    private TestUtils testUtils;

    @Before
    public void init() {
        testUtils = new TestUtils();
        testUtils.setCarTypeRepository(carTypeRepository);
    }

    @Test
    public void shouldSaveNewMaintenanceTypeAndFindThatByName() {
        // Arrange
        String maintenanceTypeName = "testMaintenanceTypeName";
        MaintenanceType maintenanceType = new MaintenanceType();
        maintenanceType.setName(maintenanceTypeName);

        // Act
        maintenanceTypeRepository.save(maintenanceType);

        // Assert
        MaintenanceType fetchedMaintenanceType = maintenanceTypeRepository.findByName(maintenanceTypeName);
        assertThat(fetchedMaintenanceType, CoreMatchers.notNullValue());
        assertThat(fetchedMaintenanceType.getId(), CoreMatchers.notNullValue());
    }

    @Test
    public void shouldSaveNewMaintenanceTypeWithCarTypesAndFindThatByName() {
        // Arrange
        String maintenanceTypeName = "testMaintenanceTypeName";
        MaintenanceType maintenanceType = new MaintenanceType();
        maintenanceType.setName(maintenanceTypeName);

        String carTypeName1 = "carTypeName1";
        String carTypeName2 = "carTypeName2";
        CarType carType1 = testUtils.createAndSaveCarType(carTypeName1);
        CarType carType2 = testUtils.createAndSaveCarType(carTypeName2);

        maintenanceType.addCarType(carType1);
        maintenanceType.addCarType(carType2);

        // Act
        maintenanceTypeRepository.save(maintenanceType);

        // Assert
        MaintenanceType fetchedMaintenanceType = maintenanceTypeRepository.findByName(maintenanceTypeName);
        assertThat(fetchedMaintenanceType, CoreMatchers.notNullValue());
        assertThat(fetchedMaintenanceType.getId(), CoreMatchers.notNullValue());
        assertThat(fetchedMaintenanceType.getApplicableCarTypes(), IsCollectionWithSize.hasSize(2));
        // check if car types added above do exist in the collection;
        assertThat(fetchedMaintenanceType.getApplicableCarTypes().stream()
                .anyMatch(type -> type.getName().equals(carTypeName1)), is(true));
        assertThat(fetchedMaintenanceType.getApplicableCarTypes().stream()
                .anyMatch(type -> type.getName().equals(carTypeName2)), is(true));

    }

    // TODO add it to the service tests
    public void shouldNotLetSavingTwoMaintenanceTypesWithTheSameNameForTheSameCarType() {
    }

}

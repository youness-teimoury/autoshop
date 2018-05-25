package youness.automotive.repository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import youness.automotive.repository.model.MaintenanceJob;
import youness.automotive.repository.model.MaintenanceTask;
import youness.automotive.repository.model.MaintenanceType;
import youness.automotive.utils.TestUtils;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MaintenanceTaskRepositoryTest {

    @Autowired
    private MaintenanceTaskRepository maintenanceTaskRepository;

    @Autowired
    private MaintenanceJobRepository maintenanceJobRepository;

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

    @Autowired
    private MaintenanceTypeRepository maintenanceTypeRepository;

    private TestUtils testUtils;

    @Before
    public void init() {
        testUtils = new TestUtils();
        testUtils.setCarMakerRepository(carMakerRepository);
        testUtils.setCarTypeRepository(carTypeRepository);
        testUtils.setCarModelRepository(carModelRepository);
        testUtils.setCarOwnerRepository(carOwnerRepository);
        testUtils.setCarRepository(carRepository);
        testUtils.setMaintenanceJobRepository(maintenanceJobRepository);
        testUtils.setMaintenanceTypeRepository(maintenanceTypeRepository);
    }

    @Test
    public void shouldSaveNewMaintenanceTaskAndFindThatByCar() {
        // Arrange
        MaintenanceJob maintenanceJob = testUtils.createAndSaveMaintenanceJob();
        MaintenanceType maintenanceType = testUtils.createAndSaveMaintenanceType();
        String testDescription = "testDescription";

        MaintenanceTask maintenanceTask = new MaintenanceTask();
        maintenanceTask.setMaintenanceJob(maintenanceJob);
        maintenanceTask.setType(maintenanceType);
        maintenanceTask.setDescription(testDescription);

        // Act
        maintenanceTaskRepository.save(maintenanceTask);

        // Assert
        List<MaintenanceTask> fetchedMaintenanceTasks = maintenanceTaskRepository.findByMaintenanceJob(maintenanceJob);
        assertThat(fetchedMaintenanceTasks, CoreMatchers.notNullValue());
        assertThat(fetchedMaintenanceTasks, IsCollectionWithSize.hasSize(1));
        assertThat(fetchedMaintenanceTasks.get(0).getDescription(), is(testDescription));
    }
}

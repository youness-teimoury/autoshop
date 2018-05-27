package youness.automotive.repository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import youness.automotive.repository.model.Car;
import youness.automotive.repository.model.MaintenanceJob;
import youness.automotive.repository.model.MaintenanceTask;
import youness.automotive.repository.model.MaintenanceType;
import youness.automotive.utils.TestUtils;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MaintenanceJobRepositoryTest {

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
        testUtils.setMaintenanceTypeRepository(maintenanceTypeRepository);
    }

    @Test
    public void shouldSaveNewMaintenanceJobAndFindThatByCar() {
        // Arrange
        MaintenanceJob maintenanceJob = new MaintenanceJob();
        Date maintenanceStartDate = new Date();
        Car car = testUtils.createAndSaveCar();
        maintenanceJob.setCar(car);
        maintenanceJob.setStartDate(maintenanceStartDate);
        maintenanceJob.setEndDate(null);

        // Act
        maintenanceJobRepository.save(maintenanceJob);

        // Assert
        List<MaintenanceJob> fetchedMaintenanceJobs = maintenanceJobRepository.findByCar(car);
        assertThat(fetchedMaintenanceJobs, CoreMatchers.notNullValue());
        assertThat(fetchedMaintenanceJobs, IsCollectionWithSize.hasSize(1));
        assertThat(fetchedMaintenanceJobs.get(0).getStartDate(), is(maintenanceStartDate));
        assertThat(fetchedMaintenanceJobs.get(0).getEndDate(), CoreMatchers.nullValue());
    }

    @Test
    public void shouldSaveTransientMaintenanceTaskWhenMaintenanceJobIsSaved() {
        // Arrange
        MaintenanceJob maintenanceJob = new MaintenanceJob();
        Car car = testUtils.createAndSaveCar();
        maintenanceJob.setCar(car);

        MaintenanceType maintenanceType = testUtils.createAndSaveMaintenanceType();
        MaintenanceTask maintenanceTask = new MaintenanceTask();
        maintenanceTask.setType(maintenanceType);

        maintenanceJob.addTask(maintenanceTask);

        // Act
        maintenanceJobRepository.save(maintenanceJob);

        // Assert
        List<MaintenanceJob> fetchedMaintenanceJobs = maintenanceJobRepository.findByCar(car);
        assertThat(fetchedMaintenanceJobs, CoreMatchers.notNullValue());
        assertThat(fetchedMaintenanceJobs, IsCollectionWithSize.hasSize(1));
        assertThat(fetchedMaintenanceJobs.get(0).getMaintenanceTasks(), IsCollectionWithSize.hasSize(1));
    }
}

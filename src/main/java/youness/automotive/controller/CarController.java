package youness.automotive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import youness.automotive.controller.bean.*;
import youness.automotive.repository.CarModelRepository;
import youness.automotive.repository.CarOwnerRepository;
import youness.automotive.repository.CarRepository;
import youness.automotive.repository.MaintenanceJobRepository;
import youness.automotive.repository.model.*;
import youness.automotive.utils.BeanContainerUtils;
import youness.automotive.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 */
@Controller
@RequestMapping(CarController.CONTROLLER_PATH)
public class CarController implements GenericViewController<Car> {
    private static final String CONTROLLER_NAME = "car";
    static final String CONTROLLER_PATH = "/" + CONTROLLER_NAME;

    private static final String MAINTENANCE_JOBS_LINK_UNIQUE_NAME = "maintenanceJobs";

    @Autowired
    private CarRepository repository;

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarOwnerRepository carOwnerRepository;

    @Autowired
    private MaintenanceJobRepository maintenanceJobRepository;

    @Override
    public String getRootViewName() {
        return CONTROLLER_NAME;
    }

    @Override
    public JpaRepository<Car, Long> getRepository() {
        return repository;
    }

    @Override
    public Class<Car> getParentClass() {
        return Car.class;
    }

    @Override
    public String getViewTitle() {
        return "car";
    }

    @Override
    public List<PropertyMetadata<Car>> getPropertyMetadata() {
        List<PropertyMetadata<Car>> list = new ArrayList<>();
        list.add(new PropertyMetadata<>("owner", "Owner",
                car -> car.getOwner().toString()));
        list.add(new PropertyMetadata<>("model", "Model",
                car -> car.getModel().toString()));
        list.add(new PropertyMetadata<>("year", "Year",
                car -> String.valueOf(car.getYear())));
        list.add(new PropertyMetadata<>("odometer", "Odometer",
                car -> String.valueOf(car.getOdometer())));
        list.add(new PropertyMetadata<>("engineNo", "EngineNo.",
                Car::getEngineNo));
        return list;
    }

    @Override
    public List<GenericPropertyContainer> getPropertyContainers(Car entity) {
        List<GenericPropertyContainer> propertyContainers = new ArrayList<>();

        ComboPropertyContainer carOwnerContainer = new ComboPropertyContainer("owner", "Owner");
        carOwnerContainer.setPropertyValue(entity.getOwner() != null ? entity.getOwner().getId() : null);
        carOwnerContainer.setBeanContainers(
                BeanContainerUtils.createBeanContainers(
                        carOwnerRepository.findAll(), new PropertyMetadata<>("name", "Name", CarOwner::toString)));
        propertyContainers.add(carOwnerContainer);

        ComboPropertyContainer carModelContainer = new ComboPropertyContainer("model", "Model");
        carModelContainer.setPropertyValue(entity.getModel() != null ? entity.getModel().getId() : null);
        carModelContainer.setBeanContainers(
                BeanContainerUtils.createBeanContainers(
                        carModelRepository.findAll(), new PropertyMetadata<>("name", "Name", CarModel::toString)));
        propertyContainers.add(carModelContainer);

        // Name property container
        PropertyContainer yearPropertyContainer = new PropertyContainer("year", "Year");
        yearPropertyContainer.setPropertyValue(BeanContainerUtils.getPropertyValue(entity, "year"));
        propertyContainers.add(yearPropertyContainer);

        PropertyContainer odometerPropertyContainer = new PropertyContainer("odometer", "Odometer");
        odometerPropertyContainer.setPropertyValue(BeanContainerUtils.getPropertyValue(entity, "odometer"));
        propertyContainers.add(odometerPropertyContainer);

        PropertyContainer enginePropertyContainer = new PropertyContainer("engineNo", "Engine No");
        enginePropertyContainer.setPropertyValue(BeanContainerUtils.getPropertyValue(entity, "engineNo"));
        propertyContainers.add(enginePropertyContainer);

        return propertyContainers;
    }

    @Override
    public List<LinkedPropertyContainer> getLinkedPropertyContainers(Long beanId) {
        LinkedPropertyContainer linkedPropertyContainer =
                new LinkedPropertyContainer(MAINTENANCE_JOBS_LINK_UNIQUE_NAME, "Maintenance Jobs");
        linkedPropertyContainer.setChildType(MaintenanceType.class.getSimpleName());

        // Populate existing values
        populateAlreadySetValues(linkedPropertyContainer, beanId);
        populateSelectableValues(linkedPropertyContainer);
        return Collections.singletonList(linkedPropertyContainer);
    }

    private void populateAlreadySetValues(LinkedPropertyContainer linkedPropertyContainer, Long beanId) {
        // TODO make LinkedPropertyContainer tabular friendly
        Car car = repository.getOne(beanId);

        List<PropertyMetadata<MaintenanceTask>> maintenanceTaskProperties = new ArrayList<>();
        maintenanceTaskProperties.add(new PropertyMetadata<>("type", "Type",
                maintenanceTask -> maintenanceTask.getType().getName()));
        maintenanceTaskProperties.add(new PropertyMetadata<>("charge", "Charge",
                maintenanceTask -> String.valueOf(maintenanceTask.getCharge())));
        maintenanceTaskProperties.add(new PropertyMetadata<>("description", "Desc",
                MaintenanceTask::getDescription));

        car.getMaintenanceJobs().forEach(job -> {
            PropertyContainer jobStart = new PropertyContainer("jobStart", "Start");
            jobStart.setPropertyValue("Started at " + DateUtils.format(job.getStartDate()));
            linkedPropertyContainer.addPropertyContainer(jobStart);

            PropertyContainer jobEnd = new PropertyContainer("jobEnd", "End");
            jobEnd.setPropertyValue(job.getEndDate() == null ? "ongoing" : "Finished at  " + DateUtils.format(job
                    .getEndDate()));
            linkedPropertyContainer.addPropertyContainer(jobEnd);

            // Iterate through maintenance task properties and data to add some maintenance task data under each job
            maintenanceTaskProperties.forEach(p -> {
                job.getMaintenanceTasks().forEach(maintenanceTask -> {
                    PropertyContainer propertyContainer = new PropertyContainer(p.getName(), p.getTitle());
                    propertyContainer.setPropertyValue(p.getCaptionProvider().apply(maintenanceTask));
                    linkedPropertyContainer.addPropertyContainer(propertyContainer);
                });
            });
        });
        // TODO implement grouping on view's table to group by jobs
    }

    private void populateSelectableValues(LinkedPropertyContainer linkedPropertyContainer) {
        linkedPropertyContainer.setSelectEnabled(false);
//        linkedPropertyContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(maintenanceJobRepository.findAll(),
//                new PropertyMetadata<>("name", "Name", MaintenanceJob::toString)));
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException {
        // According to our conventional workflow, Maintenance Jobs can only be added on the Car list view
        // (MaintenanceJob requires a car as a mandatory attrib and it is non sense to assign an already created
        // Maintenance Job to a car fom here)
        throw new IllegalArgumentException("Link name is not allowed here!");
    }
}

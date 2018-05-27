package youness.automotive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import youness.automotive.controller.bean.ComboPropertyContainer;
import youness.automotive.controller.bean.DataLinkRequestBean;
import youness.automotive.controller.bean.DateTimePropertyContainer;
import youness.automotive.controller.bean.GenericPropertyContainer;
import youness.automotive.controller.bean.LinkedPropertyContainer;
import youness.automotive.controller.bean.PropertyContainer;
import youness.automotive.controller.bean.PropertyMetadata;
import youness.automotive.repository.CarRepository;
import youness.automotive.repository.MaintenanceJobRepository;
import youness.automotive.repository.MaintenanceTaskRepository;
import youness.automotive.repository.model.MaintenanceJob;
import youness.automotive.repository.model.MaintenanceTask;
import youness.automotive.repository.model.MaintenanceType;
import youness.automotive.utils.BeanContainerUtils;
import youness.automotive.utils.DateUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 */
@Controller
@RequestMapping(MaintenanceJobController.CONTROLLER_PATH)
public class MaintenanceJobController implements GenericViewController<MaintenanceJob> {
    private static final String CONTROLLER_NAME = "maintenanceJob";
    static final String CONTROLLER_PATH = "/" + CONTROLLER_NAME;

    private static final String MAINTENANCE_TASKS_LINK_UNIQUE_NAME = "maintenanceTasks";

    @Autowired
    private MaintenanceJobRepository repository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private MaintenanceTaskRepository maintenanceTaskRepository;

    @Override
    public String getRootViewName() {
        return CONTROLLER_NAME;
    }

    @Override
    public JpaRepository<MaintenanceJob, Long> getRepository() {
        return repository;
    }

    @Override
    public Class<MaintenanceJob> getParentClass() {
        return MaintenanceJob.class;
    }

    @Override
    public String getViewTitle() {
        return "maintenance job";
    }

    @Override
    public List<PropertyMetadata<MaintenanceJob>> getPropertyMetadata() {
        List<PropertyMetadata<MaintenanceJob>> list = new ArrayList<>();
        list.add(new PropertyMetadata<>("startDate", "Start Date", mj -> DateUtils.format(mj.getStartDate())));
        list.add(new PropertyMetadata<>("endDate",
                "End Date",
                mj -> mj.getEndDate() == null ? "ongoing" : DateUtils.format(mj.getEndDate())));
        list.add(new PropertyMetadata<>("owner", "Owner", mj -> mj.getCar().getOwner().toString()));
        list.add(new PropertyMetadata<>("car", "Car", mj -> mj.getCar().toString()));
        list.add(new PropertyMetadata<>("charges",
                "Total",
                mj -> String.valueOf(mj.getMaintenanceTasks().stream().mapToDouble(MaintenanceTask::getCharge).sum())));
        return list;
    }

    @Override
    public List<GenericPropertyContainer> getPropertyContainers(MaintenanceJob entity) {
        List<GenericPropertyContainer> propertyContainers = new ArrayList<>();

        // Name property container
        DateTimePropertyContainer startDatePropertyContainer = new DateTimePropertyContainer("startDate", "Start Date");
        startDatePropertyContainer.setPropertyValue(entity.getStartDate());
        propertyContainers.add(startDatePropertyContainer);

        DateTimePropertyContainer endDatePropertyContainer = new DateTimePropertyContainer("endDate", "End Date");
        endDatePropertyContainer.setPropertyValue(entity.getEndDate());
        propertyContainers.add(endDatePropertyContainer);

        // MaintenanceJob has a property named car and we set the Id of that car (if available) to the container so
        // that thymleaf can automatically handle the mapping to bean/entity when save button is hit
        ComboPropertyContainer carContainer = new ComboPropertyContainer("car", "Car");
        carContainer.setPropertyValue(entity.getCar() != null ? entity.getCar().getId() : null);
        carContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(carRepository.findAll(),
                new PropertyMetadata<>("name", "Name", car -> car.toString() + " - " + car.getOwner().toString())));
        propertyContainers.add(carContainer);

        return propertyContainers;
    }

    @Override
    public List<LinkedPropertyContainer> getLinkedPropertyContainers(Long beanId) {
        LinkedPropertyContainer linkedPropertyContainer =
                new LinkedPropertyContainer(MAINTENANCE_TASKS_LINK_UNIQUE_NAME, "Maintenance Tasks");
        linkedPropertyContainer.setChildType(MaintenanceTask.class.getSimpleName());
        linkedPropertyContainer.setSelectEnabled(false);// because it does not make sense to add tasks this way

        // Populate existing values
        populateAlreadySetValues(linkedPropertyContainer, beanId);
        populateSelectableValues(linkedPropertyContainer);
        return Collections.singletonList(linkedPropertyContainer);
    }

    private void populateAlreadySetValues(LinkedPropertyContainer linkedPropertyContainer, Long beanId) {
        MaintenanceJob job = repository.getOne(beanId);
        job.getMaintenanceTasks().forEach(t -> {
                    PropertyContainer propertyContainer = new PropertyContainer("detail", "Detail");
                    propertyContainer.setPropertyValue(
                            t.getType().getName() + " - " + t.getDescription() + "" + "" + " - charge: " + t
                                    .getCharge());
                    linkedPropertyContainer.addPropertyContainer(propertyContainer);
                }

        );
    }

    private void populateSelectableValues(LinkedPropertyContainer linkedPropertyContainer) {
        linkedPropertyContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(maintenanceTaskRepository
                        .findAll(),
                new PropertyMetadata<>("name", "Name", MaintenanceTask::toString)));
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException, InvalidParameterException {
        if (!MAINTENANCE_TASKS_LINK_UNIQUE_NAME.matches(linkUniqueName)) {
            throw new IllegalArgumentException("Link name is not recognized");
        }

        MaintenanceJob ownerEntity = repository.getOne(bean.getParentEntityId());
        MaintenanceTask childEntity = maintenanceTaskRepository.getOne(bean.getChildEntityId());

        // Check if the car (linked to the Job) is eligible to get the maintenance
        Set<MaintenanceType> applicableMaintenanceTypes =
                ownerEntity.getCar().getModel().getCarType().getApplicableMaintenanceTypes();
        MaintenanceType newlyAddedMaintenanceType = childEntity.getType();
        if (!applicableMaintenanceTypes.contains(newlyAddedMaintenanceType)) {
            throw new InvalidParameterException("The maintenance is not applicable to the car!");
        }

        if (!ownerEntity.getMaintenanceTasks().contains(childEntity)) {
            ownerEntity.addTask(childEntity);
            repository.save(ownerEntity);
            return childEntity.toString();
        } else {
            return null;
        }
    }

}

package youness.automotive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import youness.automotive.controller.bean.*;
import youness.automotive.repository.MaintenanceJobRepository;
import youness.automotive.repository.MaintenanceTaskRepository;
import youness.automotive.repository.MaintenanceTypeRepository;
import youness.automotive.repository.model.CarModel;
import youness.automotive.repository.model.MaintenanceJob;
import youness.automotive.repository.model.MaintenanceTask;
import youness.automotive.repository.model.MaintenanceType;
import youness.automotive.utils.BeanContainerUtils;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 */
@Controller
@RequestMapping(MaintenanceTaskController.CONTROLLER_PATH)
public class MaintenanceTaskController implements GenericViewController<MaintenanceTask> {
    public static final String ADD_TASK_FOR_JOB_RELATIVE_PATH = "addToJob";
    public static final String ADD_TASK_FOR_JOB_PARAM_NAME = "jobId";
    private static final String CONTROLLER_NAME = "maintenanceTask";
    static final String CONTROLLER_PATH = "/" + CONTROLLER_NAME;
    @Autowired
    private MaintenanceTaskRepository repository;

    @Autowired
    private MaintenanceJobRepository maintenanceJobRepository;

    @Autowired
    private MaintenanceTypeRepository maintenanceTypeRepository;

    @Override
    public String getRootViewName() {
        return CONTROLLER_NAME;
    }

    @Override
    public JpaRepository<MaintenanceTask, Long> getRepository() {
        return repository;
    }

    @Override
    public Class<MaintenanceTask> getParentClass() {
        return MaintenanceTask.class;
    }

    @Override
    public String getViewTitle() {
        return "maintenance task";
    }

    @Override
    public List<PropertyMetadata<MaintenanceTask>> getPropertyMetadata() {
        List<PropertyMetadata<MaintenanceTask>> list = new ArrayList<>();
        list.add(new PropertyMetadata<MaintenanceTask>("job",
                "Job",
                maintenanceTask -> maintenanceTask.getMaintenanceJob().toString()).withTransient(true));
        list.add(new PropertyMetadata<>("type", "Type", maintenanceTask -> maintenanceTask.getType().getName()));
        list.add(new PropertyMetadata<MaintenanceTask>("customer",
                "Customer",
                maintenanceTask -> maintenanceTask.getMaintenanceJob().getCar().getOwner().toString()).withTransient
                (true));
        list.add(new PropertyMetadata<>("car",
                "Car",
                maintenanceTask -> maintenanceTask.getMaintenanceJob().getCar().toString()));
        list.add(new PropertyMetadata<>("description", "Description", MaintenanceTask::getDescription));
        list.add(new PropertyMetadata<>("charge",
                "Charge",
                maintenanceTask -> String.valueOf(maintenanceTask.getCharge())));
        return list;
    }

    @Override
    public List<GenericPropertyContainer> getPropertyContainers(MaintenanceTask entity) {
        List<GenericPropertyContainer> propertyContainers = new ArrayList<>();

        ComboPropertyContainer carTypeContainer = new ComboPropertyContainer("type", "Type");
        carTypeContainer.setPropertyValue(entity.getType() != null ? entity.getType().getId() : null);
        carTypeContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(maintenanceTypeRepository.findAll(),
                new PropertyMetadata<>("name", "Name", MaintenanceType::getName)));
        propertyContainers.add(carTypeContainer);

        ComboPropertyContainer jobContainer = new ComboPropertyContainer("maintenanceJob", "MaintenanceJob");
        jobContainer.setPropertyValue(entity.getMaintenanceJob() != null ? entity.getMaintenanceJob().getId() : null);
        jobContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(maintenanceJobRepository.findAll(),
                new PropertyMetadata<>("name",
                        "Name",
                        maintenanceJob -> maintenanceJob.toString() + " - " + maintenanceJob.getCar()
                                .getOwner()
                                .getShortName() + " - " + maintenanceJob.getCar().getModel().getName())));
        propertyContainers.add(jobContainer);

        PropertyContainer chargePropertyContainer = new PropertyContainer("charge", "Charge");
        chargePropertyContainer.setPropertyValue(String.valueOf(entity.getCharge()));
        propertyContainers.add(chargePropertyContainer);

        PropertyContainer descriptionPropertyContainer = new PropertyContainer("description", "Description");
        descriptionPropertyContainer.setPropertyValue(entity.getDescription());
        propertyContainers.add(descriptionPropertyContainer);

        return propertyContainers;
    }

    @Override
    public List<LinkedPropertyContainer> getLinkedPropertyContainers(Long beanId) {
        return null;
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException {
        throw new IllegalArgumentException("Link name is not recognized");
    }

    @Override
    public void validateBeforeSave(MaintenanceTask maintenanceTask) throws ValidationException {
        GenericViewController.super.validateBeforeSave(maintenanceTask);

        // get the new or updated task type
        MaintenanceType newTaskType = maintenanceTask.getType();

        CarModel carModel = maintenanceTask.getMaintenanceJob().getCar().getModel();
        Set<MaintenanceType> applicableMaintenanceTypes =
                carModel.getCarType().getApplicableMaintenanceTypes();
        if (!applicableMaintenanceTypes.contains(newTaskType)) {
            throw new ValidationException(String.format("The maintenance %s, is not applicable to %s.",
                    newTaskType.getName(), carModel.toString()));
        }
    }

    @RequestMapping(value = "/" + ADD_TASK_FOR_JOB_RELATIVE_PATH)
    public String addTask(@RequestParam(ADD_TASK_FOR_JOB_PARAM_NAME) Long jobId, Model model) {
        MaintenanceJob job = maintenanceJobRepository.getOne(jobId);
        MaintenanceTask entity = new MaintenanceTask();
        entity.setMaintenanceJob(job);

        return setupAddModelAndReturnAlterView(entity, model);
    }
}
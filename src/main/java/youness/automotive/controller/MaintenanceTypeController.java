package youness.automotive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import youness.automotive.controller.bean.DataLinkRequestBean;
import youness.automotive.controller.bean.LinkedPropertyContainer;
import youness.automotive.controller.bean.PropertyContainer;
import youness.automotive.repository.CarTypeRepository;
import youness.automotive.repository.MaintenanceTypeRepository;
import youness.automotive.repository.model.CarType;
import youness.automotive.repository.model.MaintenanceType;
import youness.automotive.utils.BeanContainerUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping(MaintenanceTypeController.CONTROLLER_PATH)
public class MaintenanceTypeController implements GenericViewController<MaintenanceType> {
    private static final String CONTROLLER_NAME = "maintenanceType";
    static final String CONTROLLER_PATH = "/" + CONTROLLER_NAME;

    private static final String CAR_TYPES_LINK_UNIQUE_NAME = "carTypes";

    @Autowired
    private MaintenanceTypeRepository repository;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Override
    public String getRootViewName() {
        return CONTROLLER_NAME;
    }

    @Override
    public JpaRepository<MaintenanceType, Long> getRepository() {
        return repository;
    }

    @Override
    public Class<MaintenanceType> getParentClass() {
        return MaintenanceType.class;
    }

    @Override
    public String getEntityName() {
        return "maintenance type";
    }

    @Override
    public LinkedHashMap<String, String> getPropertyMetadata() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("name", "Name");
        return map;
    }

    @Override
    public List<LinkedPropertyContainer> getLinkedPropertyContainers(Long beanId) {
        LinkedPropertyContainer linkedPropertyContainer =
                new LinkedPropertyContainer(CAR_TYPES_LINK_UNIQUE_NAME, "Car types");
        linkedPropertyContainer.setChildType(CarType.class.getSimpleName());

        // Populate existing values
        populateAlreadySetValues(linkedPropertyContainer, beanId);
        populateSelectableValues(linkedPropertyContainer);
        return Arrays.asList(linkedPropertyContainer);
    }

    private void populateAlreadySetValues(LinkedPropertyContainer linkedPropertyContainer, Long beanId) {
        MaintenanceType maintenanceType = repository.getOne(beanId);
        maintenanceType.getApplicableCarTypes().forEach(e -> {
            PropertyContainer propertyContainer = new PropertyContainer("name", "Name");
            propertyContainer.setPropertyValue(e.getName());
            linkedPropertyContainer.addPropertyContainer(propertyContainer);
        });
    }

    private void populateSelectableValues(LinkedPropertyContainer linkedPropertyContainer) {
        linkedPropertyContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(carTypeRepository.findAll(),
                Arrays.asList("name"),
                "name"));
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException {
        if (!CAR_TYPES_LINK_UNIQUE_NAME.matches(linkUniqueName)) {
            throw new IllegalArgumentException("Link name is not recognized");
        }

        MaintenanceType ownerEntity = repository.getOne(bean.getParentEntityId());
        CarType childEntity = carTypeRepository.getOne(bean.getChildEntityId());

        if (!ownerEntity.getApplicableCarTypes().contains(childEntity)) {
            ownerEntity.addCarType(childEntity);
            repository.save(ownerEntity);
            return childEntity.getName();
        } else {
            return null;
        }
    }
}

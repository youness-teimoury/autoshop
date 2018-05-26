package youness.automotive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import youness.automotive.controller.bean.DataLinkRequestBean;
import youness.automotive.controller.bean.LinkedPropertyContainer;
import youness.automotive.controller.bean.PropertyContainer;
import youness.automotive.controller.bean.PropertyMetadata;
import youness.automotive.repository.CarTypeRepository;
import youness.automotive.repository.MaintenanceTypeRepository;
import youness.automotive.repository.model.CarType;
import youness.automotive.repository.model.MaintenanceType;
import youness.automotive.utils.BeanContainerUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(CarTypeController.CONTROLLER_PATH)
public class CarTypeController implements GenericViewController<CarType> {
    private static final String CONTROLLER_NAME = "carType";
    static final String CONTROLLER_PATH = "/" + CONTROLLER_NAME;

    private static final String MAINTENANCE_TYPES_LINK_UNIQUE_NAME = "maintenanceTypes";

    @Autowired
    private CarTypeRepository repository;

    @Autowired
    private MaintenanceTypeRepository maintenanceTypeRepository;

    @Override
    public String getRootViewName() {
        return CONTROLLER_NAME;
    }

    @Override
    public JpaRepository<CarType, Long> getRepository() {
        return repository;
    }

    @Override
    public Class<CarType> getParentClass() {
        return CarType.class;
    }

    @Override
    public String getViewTitle() {
        return "car type";
    }

    @Override
    public List<PropertyMetadata<CarType>> getPropertyMetadata() {
        List<PropertyMetadata<CarType>> list = new ArrayList<>();
        list.add(new PropertyMetadata<>("name", "Name", CarType::getName));
        return list;
    }

    @Override
    public List<LinkedPropertyContainer> getLinkedPropertyContainers(Long beanId) {
        LinkedPropertyContainer linkedPropertyContainer =
                new LinkedPropertyContainer(MAINTENANCE_TYPES_LINK_UNIQUE_NAME, "Maintenance types");
        linkedPropertyContainer.setParentType(CarType.class.getSimpleName());
        linkedPropertyContainer.setChildType(MaintenanceType.class.getSimpleName());

        // Populate existing values
        populateAlreadySetValues(linkedPropertyContainer, beanId);
        populateSelectableValues(linkedPropertyContainer);
        return Collections.singletonList(linkedPropertyContainer);
    }

    private void populateAlreadySetValues(LinkedPropertyContainer linkedPropertyContainer, Long beanId) {
        CarType carType = repository.getOne(beanId);
        carType.getApplicableMaintenanceTypes().forEach(e -> {
            PropertyContainer propertyContainer = new PropertyContainer("name", "Name");
            propertyContainer.setPropertyValue(e.getName());
            linkedPropertyContainer.addPropertyContainer(propertyContainer);
        });
    }

    private void populateSelectableValues(LinkedPropertyContainer linkedPropertyContainer) {
        linkedPropertyContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(maintenanceTypeRepository.findAll(),
                new PropertyMetadata<MaintenanceType>("name", "Name", MaintenanceType::getName)));
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException {
        if (!MAINTENANCE_TYPES_LINK_UNIQUE_NAME.matches(linkUniqueName)) {
            throw new IllegalArgumentException("Link name is not recognized");
        }

        CarType ownerEntity = repository.getOne(bean.getParentEntityId());
        MaintenanceType childEntity = maintenanceTypeRepository.getOne(bean.getChildEntityId());

        if (!ownerEntity.getApplicableMaintenanceTypes().contains(childEntity)) {
            ownerEntity.addMaintenanceType(childEntity);
            repository.save(ownerEntity);
            return childEntity.getName();
        } else {
            return null;
        }
    }

    // TODO remove
    @PostConstruct
    public void populateSomeTypesForTest() {
        CarType f = new CarType();
        f.setName("Gas");
        repository.save(f);
        CarType f2 = new CarType();
        f2.setName("Diesel");
        repository.save(f2);
        CarType f3 = new CarType();
        f3.setName("Electric");
        repository.save(f3);
    }

}

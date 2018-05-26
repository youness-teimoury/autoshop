package youness.automotive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import youness.automotive.controller.bean.DataLinkRequestBean;
import youness.automotive.controller.bean.LinkedPropertyContainer;
import youness.automotive.controller.bean.PropertyContainer;
import youness.automotive.controller.bean.PropertyMetadata;
import youness.automotive.repository.CarOwnerRepository;
import youness.automotive.repository.CarRepository;
import youness.automotive.repository.model.Car;
import youness.automotive.repository.model.CarOwner;
import youness.automotive.utils.BeanContainerUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 */
@Controller
@RequestMapping(CarOwnerController.CONTROLLER_PATH)
public class CarOwnerController implements GenericViewController<CarOwner> {
    private static final String CONTROLLER_NAME = "carOwner";
    static final String CONTROLLER_PATH = "/" + CONTROLLER_NAME;

    private static final String CARS_LINK_UNIQUE_NAME = "cars";

    @Autowired
    private CarOwnerRepository repository;

    @Autowired
    private CarRepository carRepository;

    @Override
    public String getRootViewName() {
        return CONTROLLER_NAME;
    }

    @Override
    public JpaRepository<CarOwner, Long> getRepository() {
        return repository;
    }

    @Override
    public Class<CarOwner> getParentClass() {
        return CarOwner.class;
    }

    @Override
    public String getViewTitle() {
        return "car owner";
    }

    @Override
    public List<PropertyMetadata<CarOwner>> getPropertyMetadata() {
        List<PropertyMetadata<CarOwner>> list = new ArrayList<>();
        list.add(new PropertyMetadata<>("firstName", "First Name", CarOwner::getFirstName));
        list.add(new PropertyMetadata<>("secondName", "Second Name", CarOwner::getSecondName));
        list.add(new PropertyMetadata<>("phone", "Phone", CarOwner::getPhone));
        return list;
    }

    @Override
    public List<LinkedPropertyContainer> getLinkedPropertyContainers(Long beanId) {
        LinkedPropertyContainer linkedPropertyContainer =
                new LinkedPropertyContainer(CARS_LINK_UNIQUE_NAME, "Owning Cars");
        linkedPropertyContainer.setChildType(Car.class.getSimpleName());

        // Populate existing values
        populateAlreadySetValues(linkedPropertyContainer, beanId);
        populateSelectableValues(linkedPropertyContainer);
        return Collections.singletonList(linkedPropertyContainer);
    }

    private void populateAlreadySetValues(LinkedPropertyContainer linkedPropertyContainer, Long beanId) {
        CarOwner carOwner = repository.getOne(beanId);
        carOwner.getCars().forEach(e -> {
            PropertyContainer propertyContainer = new PropertyContainer("name", "Name");
            propertyContainer.setPropertyValue(e.toString());
            linkedPropertyContainer.addPropertyContainer(propertyContainer);
        });
    }

    private void populateSelectableValues(LinkedPropertyContainer linkedPropertyContainer) {
        linkedPropertyContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(carRepository.findAll(),
                new PropertyMetadata<>("name", "Name", Car::toString)));
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException {
        if (!CARS_LINK_UNIQUE_NAME.matches(linkUniqueName)) {
            throw new IllegalArgumentException("Link name is not recognized");
        }

        CarOwner ownerEntity = repository.getOne(bean.getParentEntityId());
        Car childEntity = carRepository.getOne(bean.getChildEntityId());

        if (!ownerEntity.getCars().contains(childEntity)) {
            ownerEntity.addCar(childEntity);
            repository.save(ownerEntity);
            return childEntity.toString();
        } else {
            return null;
        }
    }

    // TODO remove
    @PostConstruct
    public void populateSomeTypesForTest() {
        CarOwner f = new CarOwner();
        f.setFirstName("Youness");
        f.setSecondName("Teimouri");
        f.setPhone("111111111");
        repository.save(f);
    }

}

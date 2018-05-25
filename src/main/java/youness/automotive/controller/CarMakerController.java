package youness.automotive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import youness.automotive.controller.bean.DataLinkRequestBean;
import youness.automotive.controller.bean.LinkedPropertyContainer;
import youness.automotive.controller.bean.PropertyContainer;
import youness.automotive.repository.CarMakerRepository;
import youness.automotive.repository.CarModelRepository;
import youness.automotive.repository.model.CarMaker;
import youness.automotive.repository.model.CarModel;
import youness.automotive.utils.BeanContainerUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 */
@Controller
@RequestMapping(CarMakerController.CONTROLLER_PATH)
public class CarMakerController implements GenericViewController<CarMaker> {
    private static final String CONTROLLER_NAME = "carMaker";
    static final String CONTROLLER_PATH = "/" + CONTROLLER_NAME;

    private static final String CAR_MODELS_LINK_UNIQUE_NAME = "carModels";

    @Autowired
    private CarMakerRepository repository;

    @Autowired
    private CarModelRepository carModelRepository;

    @Override
    public String getRootViewName() {
        return CONTROLLER_NAME;
    }

    @Override
    public JpaRepository<CarMaker, Long> getRepository() {
        return repository;
    }

    @Override
    public Class<CarMaker> getParentClass() {
        return CarMaker.class;
    }

    @Override
    public String getEntityName() {
        return "car maker";
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
                new LinkedPropertyContainer(CAR_MODELS_LINK_UNIQUE_NAME, "Models");
        linkedPropertyContainer.setChildType(CarModel.class.getSimpleName());

        // Populate existing values
        populateAlreadySetValues(linkedPropertyContainer, beanId);
        populateSelectableValues(linkedPropertyContainer);
        return Collections.singletonList(linkedPropertyContainer);
    }

    private void populateAlreadySetValues(LinkedPropertyContainer linkedPropertyContainer, Long beanId) {
        CarMaker carMaker = repository.getOne(beanId);
        carMaker.getCarModels().forEach(e -> {
            PropertyContainer propertyContainer = new PropertyContainer("name", "Name");
            propertyContainer.setPropertyValue(e.getName());
            linkedPropertyContainer.addPropertyContainer(propertyContainer);
        });
    }

    private void populateSelectableValues(LinkedPropertyContainer linkedPropertyContainer) {
        linkedPropertyContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(carModelRepository.findAll(),
                Collections.singletonList("name"),
                "name"));
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException {
        if (!CAR_MODELS_LINK_UNIQUE_NAME.matches(linkUniqueName)) {
            throw new IllegalArgumentException("Link name is not recognized");
        }

        CarMaker ownerEntity = repository.getOne(bean.getParentEntityId());
        CarModel childEntity = carModelRepository.getOne(bean.getChildEntityId());

        if (!ownerEntity.getCarModels().contains(childEntity)) {
            ownerEntity.addCarModel(childEntity);
            repository.save(ownerEntity);
            return childEntity.getName();
        } else {
            return null;
        }
    }

    // TODO remove
    @PostConstruct
    public void populateSomeTypesForTest() {
        CarMaker f = new CarMaker();
        f.setName("Toyota");
        repository.save(f);
        CarMaker f2 = new CarMaker();
        f2.setName("BMW");
        repository.save(f2);
        CarMaker f3 = new CarMaker();
        f3.setName("Tesla");
        repository.save(f3);
    }


}

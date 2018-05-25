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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 */
@Controller
@RequestMapping(CarModelController.CONTROLLER_PATH)
public class CarModelController implements GenericViewController<CarModel> {
    private static final String CONTROLLER_NAME = "carModel";
    static final String CONTROLLER_PATH = "/" + CONTROLLER_NAME;

    private static final String CAR_MAKERS_LINK_UNIQUE_NAME = "carMakers";

    @Autowired
    private CarModelRepository repository;

    @Autowired
    private CarMakerRepository carMakerRepository;

    @Override
    public String getRootViewName() {
        return CONTROLLER_NAME;
    }

    @Override
    public JpaRepository<CarModel, Long> getRepository() {
        return repository;
    }

    @Override
    public Class<CarModel> getParentClass() {
        return CarModel.class;
    }

    @Override
    public String getEntityName() {
        return "car model";
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
                new LinkedPropertyContainer(CAR_MAKERS_LINK_UNIQUE_NAME, "Makers");
        linkedPropertyContainer.setChildType(CarMaker.class.getSimpleName());

        // Populate existing values
        populateAlreadySetValues(linkedPropertyContainer, beanId);
        populateSelectableValues(linkedPropertyContainer);
        return Collections.singletonList(linkedPropertyContainer);
    }

    private void populateAlreadySetValues(LinkedPropertyContainer linkedPropertyContainer, Long beanId) {
        CarModel carModel = repository.getOne(beanId);
        if (carModel.getMaker() != null) {
            PropertyContainer propertyContainer = new PropertyContainer("carMaker", "Maker");
            propertyContainer.setPropertyValue(carModel.getMaker().getName());
            linkedPropertyContainer.addPropertyContainer(propertyContainer);
        }
    }

    private void populateSelectableValues(LinkedPropertyContainer linkedPropertyContainer) {
        linkedPropertyContainer.setBeanContainers(BeanContainerUtils.createBeanContainers(carMakerRepository.findAll(),
                Collections.singletonList("name"),
                "name"));
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException {
        if (!CAR_MAKERS_LINK_UNIQUE_NAME.matches(linkUniqueName)) {
            throw new IllegalArgumentException("Link name is not recognized");
        }

        CarModel ownerEntity = repository.getOne(bean.getParentEntityId());
        CarMaker childEntity = carMakerRepository.getOne(bean.getChildEntityId());

        ownerEntity.setMaker(childEntity);
        repository.save(ownerEntity);
        return childEntity.getName();
    }

}

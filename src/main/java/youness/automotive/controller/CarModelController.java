package youness.automotive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import youness.automotive.controller.bean.*;
import youness.automotive.repository.CarMakerRepository;
import youness.automotive.repository.CarModelRepository;
import youness.automotive.repository.CarTypeRepository;
import youness.automotive.repository.model.CarMaker;
import youness.automotive.repository.model.CarModel;
import youness.automotive.repository.model.CarType;
import youness.automotive.utils.BeanContainerUtils;

import java.util.ArrayList;
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

    @Autowired
    private CarTypeRepository carTypeRepository;

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
    public String getViewTitle() {
        return "car model";
    }

    @Override
    public List<PropertyMetadata<CarModel>> getPropertyMetadata() {
        List<PropertyMetadata<CarModel>> list = new ArrayList<>();
        list.add(new PropertyMetadata<>("name", "Name", CarModel::getName));
        list.add(new PropertyMetadata<>("maker", "Maker",
                carModel -> carModel.getMaker().getName()));
        list.add(new PropertyMetadata<>("carType", "Type",
                carModel -> carModel.getCarType().getName()));
        return list;
    }

    @Override
    public List<GenericPropertyContainer> getPropertyContainers(CarModel entity) {
        List<GenericPropertyContainer> propertyContainers = new ArrayList<>();
        // Name property container
        PropertyContainer propertyContainer = new PropertyContainer("name", "Name");
        propertyContainer.setPropertyValue(BeanContainerUtils.getPropertyValue(entity, "name"));
        propertyContainers.add(propertyContainer);

        ComboPropertyContainer carMakerContainer = new ComboPropertyContainer("maker", "Maker");
        carMakerContainer.setPropertyValue(entity.getMaker() != null ? entity.getMaker().getId() : null);
        carMakerContainer.setBeanContainers(
                BeanContainerUtils.createBeanContainers(
                        carMakerRepository.findAll(), new PropertyMetadata<>("name", "Name", CarMaker::getName)));
        propertyContainers.add(carMakerContainer);

        ComboPropertyContainer carTypeContainer = new ComboPropertyContainer("carType", "Type");
        carTypeContainer.setPropertyValue(entity.getCarType() != null ? entity.getCarType().getId() : null);
        carTypeContainer.setBeanContainers(
                BeanContainerUtils.createBeanContainers(
                        carTypeRepository.findAll(), new PropertyMetadata<>
                                ("name", "Name", CarType::getName)));
        propertyContainers.add(carTypeContainer);
        return propertyContainers;
    }

    @Override
    public List<LinkedPropertyContainer> getLinkedPropertyContainers(Long beanId) {
        return null;
    }

    @Override
    public String handleSaveDataLinkRequest(DataLinkRequestBean bean, String linkUniqueName)
            throws IllegalArgumentException {
//        if (!CAR_MAKERS_LINK_UNIQUE_NAME.matches(linkUniqueName)) {
//            throw new IllegalArgumentException("Link name is not recognized");
//        }
//
//        CarModel ownerEntity = repository.getOne(bean.getParentEntityId());
//        CarMaker childEntity = carMakerRepository.getOne(bean.getChildEntityId());
//
//        ownerEntity.setMaker(childEntity);
//        repository.save(ownerEntity);
//        return childEntity.getName();
        throw new IllegalArgumentException("Link name is not recognized");
    }
}

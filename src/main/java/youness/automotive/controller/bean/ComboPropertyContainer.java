package youness.automotive.controller.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Used as a container to maintain the information for entity's property which is a combo type (manyToOne)
 */
public class ComboPropertyContainer extends GenericPropertyContainer implements SelectableContainerHolder {
    /**
     * The id of the selected value
     * This value should be compatible (be one of) the values returned through beanContainers (BeanContainer.id)
     */
    private Long propertyValue;

    /**
     * Contains all the available bean containers who can be selected by user to become a property container
     */
    private List<BeanContainer> beanContainers = new ArrayList<>();


    public ComboPropertyContainer(String propertyName, String propertyCaption) {
        super(propertyName, propertyCaption);
    }

    @Override
    public String getType() {
        return "combo";
    }

    public Long getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Long propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public List<BeanContainer> getBeanContainers() {
        return beanContainers;
    }

    @Override
    public void setBeanContainers(List<BeanContainer> beanContainers) {
        this.beanContainers = beanContainers;
    }
}

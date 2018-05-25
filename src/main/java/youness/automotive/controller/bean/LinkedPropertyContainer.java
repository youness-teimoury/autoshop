package youness.automotive.controller.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Used as a container to maintain each entity's linked property information
 * Imagine if an owner has some cars. Then we can use this container to maintain the cars for the owner.
 */
public class LinkedPropertyContainer extends GenericPropertyContainer {
    /**
     * Maintains the initial/existing values (link values) form the target entity in owning entity
     */
    private List<PropertyContainer> propertyContainers = new ArrayList<>();

    /**
     * Contains all the available bean containers who can be selected by user to become a property container
     */
    private List<BeanContainer> beanContainers = new ArrayList<>();

    /**
     * The owner id
     */
    private Long parentId;

    /**
     * The entity class name of the owner of the entity
     */
    private String parentType;

    /**
     * The selected property value
     */
    private String childId;

    /**
     * The entity class name of the child entities
     */
    private String childType;

    /**
     * @param propertyName    used as the id of the table in view
     *                        this property should be unique in the scope of each controller
     * @param propertyCaption used as the label on top of the table in view
     */
    public LinkedPropertyContainer(String propertyName, String propertyCaption) {
        super(propertyName, propertyCaption);
    }

    public List<PropertyContainer> getPropertyContainers() {
        return propertyContainers;
    }

    public void setPropertyContainers(List<PropertyContainer> propertyContainers) {
        this.propertyContainers = propertyContainers;
    }

    public void addPropertyContainer(PropertyContainer propertyContainer) {
        this.propertyContainers.add(propertyContainer);
    }

    public List<BeanContainer> getBeanContainers() {
        return beanContainers;
    }

    public void setBeanContainers(List<BeanContainer> beanContainers) {
        this.beanContainers = beanContainers;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getChildType() {
        return childType;
    }

    public void setChildType(String childType) {
        this.childType = childType;
    }
}

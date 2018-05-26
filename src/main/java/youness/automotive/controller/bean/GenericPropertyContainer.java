package youness.automotive.controller.bean;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The generic bean that acts as a container to hold property names and their captions to be sent to view
 */
public abstract class GenericPropertyContainer {

    /**
     * The mapping entity's attribute name
     * It should be exactly same name as the entity's attribute name
     */
    private String propertyName;

    /**
     * The caption for the property
     */
    private String propertyCaption;

    public GenericPropertyContainer(String propertyName, String propertyCaption) {
        this.propertyName = propertyName;
        this.propertyCaption = propertyCaption;
    }

    public String getPropertyCaption() {
        return propertyCaption;
    }

    public void setPropertyCaption(String propertyCaption) {
        this.propertyCaption = propertyCaption;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Represents the type of this container.
     * It is used on the view to decide the proper UI component (and therefore to read specific properties related to each type)
     *
     * @return
     */
    public abstract String getType();

}

package youness.automotive.controller.bean;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Used as a container to maintain each entity's property information
 */
public class PropertyContainer extends GenericPropertyContainer {
    private String propertyValue;

    public PropertyContainer(String propertyName, String propertyCaption) {
        super(propertyName, propertyCaption);
    }

    @Override
    public String getType() {
        return "text";
    }

    /**
     * Used by template engine to read the value
     *
     * @return
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}

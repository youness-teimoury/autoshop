package youness.automotive.controller.bean;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The generic bean that acts as a container to hold property names and their captions to be sent to view
 */
public class GenericPropertyContainer {
    private String propertyCaption;
    private String propertyName;

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

}

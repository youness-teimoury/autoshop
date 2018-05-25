package youness.automotive.controller.bean;

import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The bean used to talk to list views (tables)
 * The bean contains the ordered list of property values and the unique identifier to identify the bean (entity's ID)
 */
public class BeanContainer {
    private Long id;
    private List<String> propertyValues;
    private String caption = "Test Caption";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<String> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}

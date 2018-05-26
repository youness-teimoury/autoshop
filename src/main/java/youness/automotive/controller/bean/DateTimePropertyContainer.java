package youness.automotive.controller.bean;

import java.util.Date;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Used as a container to maintain each entity's property of type date/time
 */
public class DateTimePropertyContainer extends GenericPropertyContainer {

    private Date propertyValue;

    public DateTimePropertyContainer(String propertyName, String propertyCaption) {
        super(propertyName, propertyCaption);
    }

    @Override
    public String getType() {
        return "datetime";
    }

    public Date getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Date propertyValue) {
        this.propertyValue = propertyValue;
    }
}

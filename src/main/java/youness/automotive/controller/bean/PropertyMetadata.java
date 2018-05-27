package youness.automotive.controller.bean;

import youness.automotive.repository.model.BaseEntity;

import java.util.function.Function;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Represents the an individual entity's property
 */
public class PropertyMetadata<T extends BaseEntity> {
    /**
     * The property name
     */
    private String name;
    /**
     * The title (e.g. column name) to be used as the label for this property
     */
    private String title;
    /**
     * Generates the caption as the previewing value for the property
     */
    private Function<T, String> captionProvider;

    /**
     * If transient, it means this property does not really exist as an underlying entity's property.
     * The use-case might be when you want to generate a new column based on some calculation of other attributes/column
     */
    private boolean isTransient;

    public PropertyMetadata(String name, String title, Function<T, String> captionProvider) {
        this.name = name;
        this.title = title;
        this.captionProvider = captionProvider;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Function<T, String> getCaptionProvider() {
        return captionProvider;
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    public PropertyMetadata<T> withTransient(boolean enabled) {
        setTransient(enabled);
        return this;
    }
}

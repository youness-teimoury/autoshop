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
    private String name;
    private String title;
    private Function<T, String> captionProvider;

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
}

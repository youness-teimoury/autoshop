package youness.automotive.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import youness.automotive.controller.bean.BeanContainer;
import youness.automotive.controller.bean.PropertyMetadata;
import youness.automotive.repository.model.BaseEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The container utils that uses reflection to populate data containers with properties from entities
 */
public class BeanContainerUtils {
    private final static Logger logger = LoggerFactory.getLogger(BeanContainerUtils.class);

    /**
     * Generates the list of bean containers from the list of Base Entities
     * Each bean container contains the Id of the Base Entity and an ordered list of entities' property values
     * Only the property values that are passed through propertyNames parameter will be considered
     *
     * @param entities
     * @param properties the names should exactly match the attribute name defined in the entity class
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> List<BeanContainer> createBeanContainers(List<T> entities,
                                                                                  List<PropertyMetadata<T>> properties) {
        return createBeanContainers(entities, properties, null);
    }

    /**
     * {@link BeanContainerUtils#createBeanContainers(List, List)}
     *
     * @param entities
     * @param property
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> List<BeanContainer> createBeanContainers(List<T> entities,
                                                                                  PropertyMetadata<T> property) {
        return createBeanContainers(entities, Collections.singletonList(property), property.getCaptionProvider());
    }

    /**
     * {@link BeanContainerUtils#createBeanContainers(List, List)}
     *
     * @param entities
     * @param properties
     * @param captionProvider a unique user facing name generator for each entity
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> List<BeanContainer> createBeanContainers(List<T> entities,
                                                                                  List<PropertyMetadata<T>> properties,
                                                                                  Function<T, String> captionProvider) {
        List<BeanContainer> beanContainers = new ArrayList<>();
        for (T entity : entities) {
            List<String> values = new ArrayList<>();
            for (PropertyMetadata<T> propertyMetadata : properties) {
                // Get the relative entity property value with property name
                values.add(propertyMetadata.getCaptionProvider().apply(entity));
            }

            BeanContainer beanContainer = new BeanContainer();
            beanContainer.setId(entity.getId());
            beanContainer.setPropertyValues(values);
            if (captionProvider != null) { // The only case captionProvider is null is for the tables
                beanContainer.setCaption(captionProvider.apply(entity));
            }
            beanContainers.add(beanContainer);
        }

        return beanContainers;
    }

    /**
     * Returns the property value out of the parameter entity through reflection
     *
     * @param entity
     * @param propertyName
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> String getPropertyValue(T entity, String propertyName) {
        try {
            Field field = entity.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);

            Object value = field.get(entity);
            return value == null ? "" : value.toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(String.format("Error in reading the property name %s from entity %s",
                    propertyName, entity.getClass().getSimpleName()), e);
        }
        return "";
    }

    public static <T extends BaseEntity> String getPropertyValue(BiFunction<T, String, String> captionValueExtractor, T entity, String propertyName) {
        return captionValueExtractor.apply(entity, propertyName);
    }

    /**
     * This is the inclusive version of Spring's BeanUtils.copyProperties
     * https://stackoverflow.com/questions/5079458/copy-specific-fields-by-using-beanutils-copyproperties
     *
     * @param src
     * @param trg
     * @param props
     */
    public static void copyProperties(Object src, Object trg, Iterable<String> props) {
        BeanWrapper srcWrap = PropertyAccessorFactory.forBeanPropertyAccess(src);
        BeanWrapper trgWrap = PropertyAccessorFactory.forBeanPropertyAccess(trg);
        props.forEach(p -> trgWrap.setPropertyValue(p, srcWrap.getPropertyValue(p)));
    }
}

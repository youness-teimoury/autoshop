package youness.automotive.utils;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import youness.automotive.controller.bean.BeanContainer;
import youness.automotive.controller.bean.PropertyMetadata;
import youness.automotive.repository.model.MaintenanceType;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BeanContainerUtilsTest {

    @Test
    public void shouldReturnThePropertyValueOfAnObjectByName() {
        // Arrange
        String testEntityName = "testEntityName";
        Long testId = 126L;
        MaintenanceType maintenanceType = new MaintenanceType();
        maintenanceType.setId(testId);
        maintenanceType.setName(testEntityName);

        // Act
        List<BeanContainer> list = BeanContainerUtils.createBeanContainers(Collections.singletonList(maintenanceType),
                new PropertyMetadata<>("name", "Name", MaintenanceType::getName));

        // Assert
        assertThat(list, CoreMatchers.notNullValue());
        assertThat(list, IsCollectionWithSize.hasSize(1));
        assertThat(list.get(0).getId(), is(testId));
        assertThat(list.get(0).getPropertyValues(), IsCollectionWithSize.hasSize(1));
        assertThat(list.get(0).getPropertyValues().get(0), is(testEntityName));
        assertThat(list.get(0).getCaption(), is(testEntityName));
    }

    @Test
    public void shouldReturnEmptyStringWhenPropertyValueIsNull() {
        // Arrange
        MaintenanceType maintenanceType = new MaintenanceType();
        maintenanceType.setName(null);

        // Act
        String value = BeanContainerUtils.getPropertyValue(maintenanceType, "name");

        // Assert
        assertThat(value, CoreMatchers.notNullValue());
        assertThat(value, is(""));
    }
}

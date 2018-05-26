package youness.automotive.controller.bean;

import java.util.List;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Represents a container that has represents a list of selectable values
 */
public interface SelectableContainerHolder {

    List<BeanContainer> getBeanContainers();

    void setBeanContainers(List<BeanContainer> beanContainers);

}

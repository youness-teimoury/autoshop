package youness.automotive.controller.bean;

import javax.validation.constraints.NotNull;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The bean used in controller to link two entities together (e.g. CarType to MaintenanceType)
 */
public class DataLinkRequestBean {

    /**
     * The parent entity's ID. This should be set by the controller at model initialization time.
     */
    @NotNull
    private Long parentEntityId;

    /**
     * The child entity's ID which is set by the view
     */
    private Long childEntityId;

    public DataLinkRequestBean() {
    }

    public DataLinkRequestBean(Long parentEntityId, Long childEntityId) {
        this.parentEntityId = parentEntityId;
        this.childEntityId = childEntityId;
    }

    public Long getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(Long parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public Long getChildEntityId() {
        return childEntityId;
    }

    public void setChildEntityId(Long childEntityId) {
        this.childEntityId = childEntityId;
    }
}

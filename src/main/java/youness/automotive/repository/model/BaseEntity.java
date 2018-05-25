package youness.automotive.repository.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * This is the base entity which all other entities should inherit from
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Boolean deleted = false;

    @Temporal(TemporalType.DATE)
    private Date creationDate = new Date();

    @Temporal(TemporalType.DATE)
    private Date lastModificationDate = new Date();

    // TODO add creator and modifier user

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || (obj.getClass() != this.getClass())) {
            return false;
        }

        BaseEntity entity = (BaseEntity) obj;
        return id != null && Objects.equals(id, entity.getId());
    }
}
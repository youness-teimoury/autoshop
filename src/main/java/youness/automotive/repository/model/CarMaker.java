package youness.automotive.repository.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The entity class to map the car maker
 */
@Entity
@Table(name = "car_maker")
public class CarMaker extends BaseEntity {
    @NotNull(message = "The name should be specified.")
    @Column(unique = true)
    private String name;

    /**
     * The models that this car maker makes.
     * Cascades are to let addCarModel method add models directly even if the maker is not created yet
     * Initialization is to avoid boilerplate code to create(check, sync, create) the set in addCarModel method
     */
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "maker")
    private Set<CarModel> carModels = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCarModel(CarModel carModel) {
        carModels.add(carModel);
        carModel.setMaker(this);
    }

    public Set<CarModel> getCarModels() {
        return carModels;
    }

    public void setCarModels(Set<CarModel> carModels) {
        this.carModels = carModels;
    }
}

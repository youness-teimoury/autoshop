package youness.automotive.repository.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The entity class to map the car model
 */
@Entity
@Table(name = "car_model")
public class CarModel extends BaseEntity {

    /**
     * The model name
     */
    @NotBlank
    private String name;

    /**
     * The builder of this model
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private CarMaker maker;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private CarType carType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarMaker getMaker() {
        return maker;
    }

    public void setMaker(CarMaker maker) {
        this.maker = maker;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }
}

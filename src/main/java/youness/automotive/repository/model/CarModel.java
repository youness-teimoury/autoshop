package youness.automotive.repository.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * The entity class to map the car model
 */
@Entity
@Table(name = "car_model",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "car_maker_id", "car_type_id"})
)
public class CarModel extends BaseEntity {

    /**
     * The model name
     */
    @NotBlank
    @Column(name = "name")
    private String name;

    /**
     * The builder of this model
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_maker_id")
    private CarMaker maker;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_type_id")
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

    @Override
    public String toString() {
        return getMaker().getName() + " " + getName() + " " + getCarType().getName();
    }
}

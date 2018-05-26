package youness.automotive.utils;

import youness.automotive.repository.*;
import youness.automotive.repository.model.*;

import static java.util.UUID.randomUUID;

public class TestUtils {
    private CarMakerRepository carMakerRepository;
    private CarTypeRepository carTypeRepository;
    private CarOwnerRepository carOwnerRepository;
    private CarModelRepository carModelRepository;
    private CarRepository carRepository;
    private MaintenanceJobRepository maintenanceJobRepository;
    private MaintenanceTypeRepository maintenanceTypeRepository;

    public void setCarMakerRepository(CarMakerRepository carMakerRepository) {
        this.carMakerRepository = carMakerRepository;
    }

    public void setCarTypeRepository(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    public void setCarOwnerRepository(CarOwnerRepository carOwnerRepository) {
        this.carOwnerRepository = carOwnerRepository;
    }

    public void setCarModelRepository(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void setMaintenanceJobRepository(MaintenanceJobRepository maintenanceJobRepository) {
        this.maintenanceJobRepository = maintenanceJobRepository;
    }

    public void setMaintenanceTypeRepository(MaintenanceTypeRepository maintenanceTypeRepository) {
        this.maintenanceTypeRepository = maintenanceTypeRepository;
    }

    public CarMaker createCarMaker(String name) {
        CarMaker carMaker = new CarMaker();
        carMaker.setName(name);
        return carMaker;
    }

    public CarMaker createAndSaveCarMaker(String name) {
        return carMakerRepository.save(createCarMaker(name));
    }

    public CarType createCarType(String typeName) {
        CarType carType = new CarType();
        carType.setName(typeName);
        return carType;
    }

    public CarType createAndSaveCarType(String typeName) {
        return carTypeRepository.save(createCarType(typeName));
    }

    public CarOwner createCarOwner(String phoneNo) {
        CarOwner carOwner = new CarOwner();
        carOwner.setFirstName("firstName" + randomUUID());
        carOwner.setSecondName("secondName" + randomUUID());
        carOwner.setPhone(phoneNo);
        return carOwner;
    }

    public CarOwner createAndSaveCarOwner(String phoneNo) {
        return carOwnerRepository.save(createCarOwner(phoneNo));
    }

    public CarModel createCarModel(String modelName, CarMaker carMaker, CarType carType) {
        CarModel carModel = new CarModel();
        carModel.setName(modelName);
        carModel.setMaker(carMaker);
        carModel.setCarType(carType);
        return carModel;
    }

    public CarModel createAndSaveCarModel(String modelName, CarMaker carMaker, CarType carType) {
        return carModelRepository.save(createCarModel(modelName, carMaker, carType));
    }

    public Car createAndSaveCar() {
        String carMakerName = "TOYOTA";
        String carModelName = "RAV4";
        String carTypeName = "Gas";
        String carOwnerPhoneNo = "4038055680";

        CarOwner carOwner = createAndSaveCarOwner(carOwnerPhoneNo);
        CarMaker carMaker = createAndSaveCarMaker(carMakerName);
        CarType carType = createAndSaveCarType(carTypeName);
        CarModel carModel = createAndSaveCarModel(carModelName, carMaker, carType);

        Car car = new Car();
        car.setModel(carModel);
        car.setOwner(carOwner);
        car.setYear(2016);
        car.setEngineNo("testEngineNo");
        return carRepository.save(car);
    }

    public MaintenanceJob createAndSaveMaintenanceJob() {
        MaintenanceJob maintenanceJob = new MaintenanceJob();
        Car car = createAndSaveCar();
        maintenanceJob.setCar(car);
        return maintenanceJobRepository.save(maintenanceJob);
    }

    public MaintenanceType createAndSaveMaintenanceType() {
        String maintenanceTypeName = "testMaintenanceTypeName";
        MaintenanceType maintenanceType = new MaintenanceType();
        maintenanceType.setName(maintenanceTypeName);
        return maintenanceTypeRepository.save(maintenanceType);
    }
}

public class Car extends AbstractVehicles {
    private double fuel;
    private double distance;
    private double speed;

    public Car(Color color, double fuel, double distance, double speed){
        super(color);
        this.fuel=fuel;
        this.distance=distance;
        this.speed=speed;
    }

    public String getName(){
        return "Автомобиль";
    }

    @Override
    public double calculateConsumptionFuel(){
        return fuel/distance;
    }

    @Override
    public double calculateTravelTime(){
        return distance/speed;
    }
}

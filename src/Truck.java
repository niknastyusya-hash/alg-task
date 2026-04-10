public class Truck extends AbstractVehicles{
    private double fuel;
    private double distance;
    private double speed;

    public Truck(Color color, double fuel, double distance, double speed){
        super(color);
        this.fuel=fuel;
        this.distance=distance;
        this.speed=speed;
    }

    public String getName(){
        return "Грузовик";
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

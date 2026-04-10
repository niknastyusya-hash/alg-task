public abstract class AbstractVehicles implements Vehicles{
    private Color color;

    public AbstractVehicles(Color color){
        this.color=color;
    }

    public Color getColor(){
        return color;
    }

    public abstract String getName();

    public abstract double calculateConsumptionFuel();

    public abstract double calculateTravelTime();

}

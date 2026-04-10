public class Main{
    public static void main (String[] args){
        Car car = new Car(Color.RED, 400, 1000, 100);
        Truck truck = new Truck(Color.BLACK, 600, 1200, 120);

        System.out.println("Транспортное средство: " + car.getName());
        System.out.println("Цвет: " + car.getColor());
        System.out.println("Время пути: " + car.calculateTravelTime());
        System.out.println("Расход топлива: " + car.calculateConsumptionFuel());

        System.out.println("-------------------------------------");

        System.out.println("Транспортное средство: " + truck.getName());
        System.out.println("Цвет: " + truck.getColor());
        System.out.println("Время пути: " + truck.calculateTravelTime());
        System.out.println("Расход топлива: " + truck.calculateConsumptionFuel());
    }
}
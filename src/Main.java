public class Main {
    public static void main(String[] args) {

        Triangle triangle = new Triangle(Color.RED, 3, 4, 5);
        Rectangle rectangle = new Rectangle(Color.BLUE, 5, 8);
        Circle circle = new Circle(Color.GREEN, 6);

        triangle.displayInfo();
        rectangle.displayInfo();
        circle.displayInfo();
    }
}
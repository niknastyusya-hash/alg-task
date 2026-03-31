public class Circle extends AbstractFigure {
    private double radius;  // радиус

    public Circle(Color color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public String getFigureName() {
        return "Круг";
    }
}

public class Rectangle extends AbstractFigure {
    private double width;   // ширина
    private double height;  // высота

    public Rectangle(Color color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public String getFigureName() {
        return "Прямоугольник";
    }
}

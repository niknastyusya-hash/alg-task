public class Triangle extends AbstractFigure {
    private double A;
    private double B;
    private double C;

    public Triangle(Color color, double A, double B, double C) {
        super(color);
        this.A = A;
        this.B = B;
        this.C = C;
    }

    @Override
    public double calculatePerimeter() {
        return A + B + C;
    }

    @Override
    public double calculateArea() {
        double p = calculatePerimeter() / 2;
        return Math.sqrt(p * (p - A) * (p - B) * (p - C));
    }

    @Override
    public String getFigureName() {
        return "Треугольник";
    }
}

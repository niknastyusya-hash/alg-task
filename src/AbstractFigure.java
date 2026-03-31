public abstract class AbstractFigure implements Figure {
    private Color color;

    public AbstractFigure(Color color) {
        this.color = color;
    }

    public abstract String getFigureName();

    public void displayInfo() {
        System.out.println(getFigureName());
        System.out.println("Цвет: " + color);
        System.out.printf("Площадь: %.2f\n", calculateArea());
        System.out.printf("Периметр: %.2f\n", calculatePerimeter());
        System.out.println();
    }

    public Color getColor() {
        return color;
    }
}

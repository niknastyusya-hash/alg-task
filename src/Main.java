import java.util.Collection;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        Collection tree = new BinaryTreeCollection(50);

        tree.add(30);
        tree.add(70);
        tree.add(20);
        tree.add(40);
        tree.add(60);
        tree.add(80);

        System.out.println("Дерево:");
        System.out.println(tree);

        Integer elementToRemove = 30;
        System.out.println("\nУдаляем элемент: " + elementToRemove);
        tree.remove(elementToRemove);

        System.out.println("\nДерево после удаления:");
        System.out.println(tree);
    }
}
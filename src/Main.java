import java.util.Collection;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        BinaryTreeGenerics<Integer> tree = new BinaryTreeGenerics<>(50);

        tree.add(30);
        tree.add(70);
        tree.add(20);
        tree.add(40);
        tree.add(60);
        tree.add(80);

        System.out.println("Дерево (целочисленный тип данных):");
        System.out.println(tree);

        Integer elementToRemove = 30;
        System.out.println("\nУдаляем элемент: " + elementToRemove);
        tree.remove(elementToRemove);

        System.out.println("\nДерево (целочисленный тип данных) после удаления:");
        System.out.println(tree);

        BinaryTreeGenerics<String> stringTree = new BinaryTreeGenerics<>("яблоко");
        stringTree.add("банан");
        stringTree.add("апельсин");

        System.out.println("\nДерево (строковый тип данных):");
        System.out.println(stringTree);
    }
}
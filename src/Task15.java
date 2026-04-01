//задача 15 6.2
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
public class Task15 {
    public static void main(String[] args) {
        Queue<Double> queue = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите количество элементов: ");
        int count = scanner.nextInt();
        System.out.println("Введите " + count + " чисел: ");
        for (int i = 0; i < count; i++) {
            double number = scanner.nextDouble();
            queue.add(number);
        }
        double min = 10000000;
        for (double num : queue) {
            if (num < min) {
                min = num;
            }
        }
        System.out.println(queue);
        System.out.println("Минимальный элемент очереди: " + min);
    }
}
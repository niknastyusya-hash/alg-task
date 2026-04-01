//задача №6 (раздел 6.2)
import java.util.Stack;
import java.util.Scanner;
public class Main {
    public static void main(String [] args){
        Scanner scanner = new Scanner(System.in);
        Stack<Double> stack = new Stack<>();
        System.out.print("Введите количество элементов: ");
        int count = scanner.nextInt();
        System.out.println("Введите " + count + " чисел: ");
        for (int i = 0; i < count; i++) {
            double number = scanner.nextInt();
            stack.push(number);
        }
        double max = 0;
        for (int i = 0; i < count; i++) {
            if (stack.get(i)>max){
                max = stack.get(i);
            }
        }
        System.out.println(stack);
        System.out.println("Максимальный элемент стека: " + max);
    }
}
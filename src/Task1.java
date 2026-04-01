//задача 1 6.2
import java.util.Stack;
import java.util.Scanner;
public class Task1 {
    public static void main(String [] args){
        Scanner scanner = new Scanner(System.in);
        Stack<Integer> stack = new Stack<>();
        System.out.print("Введите количество элементов: ");
        int count = scanner.nextInt();
        System.out.println("Введите " + count + " чисел: ");
        for (int i = 0; i < count; i++) {
            int number = scanner.nextInt();
            stack.push(number);
        }
        int a = 1;
        for (int i = 0; i < count; i++) {
            if (stack.get(i)%2!=0){
                a*=stack.get(i);
            }
        }
        System.out.println(stack);
        System.out.println("Произведение нечетных элементов стека: " + a);
    }
}

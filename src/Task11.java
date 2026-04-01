//задача 11 6.2
import java.util.Scanner;
public class Task11 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int SIZE = 10; // максимальный размер очереди
        String[][][] queue = new String[SIZE][1][2];
        int front = 0; // начало очереди
        int rear = 0;  // конец очереди
        while (true) {
            System.out.println("\n1 - Добавить студента");
            System.out.println("2 - Просмотреть очередь");
            System.out.println("3 - Выход");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера
            switch (choice) {
                case 1:
                    if (rear < SIZE) {
                        System.out.print("Введите фамилию: ");
                        String surname = scanner.nextLine();
                        System.out.print("Введите средний балл: ");
                        String average = scanner.nextLine();
                        queue[rear][0][0] = surname;
                        queue[rear][0][1] = average;
                        rear++;
                        System.out.println("Студент добавлен в очередь.");
                    } else {
                        System.out.println("Очередь переполнена!");
                    }
                    break;
                case 2:
                    if (front == rear) {
                        System.out.println("Очередь пуста.");
                    } else {
                        System.out.println("\nСодержимое очереди:");
                        for (int i = front; i < rear; i++) {
                            System.out.println(
                                    "Фамилия: " + queue[i][0][0] +
                                            ", Средний балл: " + queue[i][0][1]
                            );
                        }
                    }
                    break;
                case 3:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }
}


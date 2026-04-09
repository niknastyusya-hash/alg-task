import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Task11Collection {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Queue<String[]> queue = new LinkedList<>();

        while (true) {
            System.out.println("\n1 - Добавить студента");
            System.out.println("2 - Просмотреть очередь");
            System.out.println("3 - Выход");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите фамилию: ");
                    String surname = scanner.nextLine();
                    System.out.print("Введите средний балл: ");
                    String average = scanner.nextLine();

                    String[] student = {surname, average};

                    queue.offer(student);
                    System.out.println("Студент добавлен в очередь.");
                    System.out.println("Текущий размер очереди: " + queue.size());
                    break;

                case 2:
                    if (queue.isEmpty()) {
                        System.out.println("Очередь пуста.");
                    } else {
                        System.out.println("\nСодержимое очереди:");
                        int position = 1;
                        for (String[] studentItem : queue) {
                            System.out.println(position + ". Фамилия: " + studentItem[0] +
                                    ", Средний балл: " + studentItem[1]);
                            position++;
                        }
                        System.out.println("Всего студентов в очереди: " + queue.size());
                    }
                    break;

                case 3:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Bridge bridge = new Bridge();

        for (int i = 1; i <= 3; i++) {
            final int num = i;
            new Thread(() -> car(bridge, "NORTH", num)).start();
            new Thread(() -> car(bridge, "SOUTH", num)).start();
        }
    }

    static void car(Bridge bridge, String dir, int num) {
        try {
            Thread.sleep((long)(Math.random() * 100));  // Подъезд к мосту
            bridge.enter(dir);
            Thread.sleep((long)(Math.random() * 300 + 200));  // Проезд по мосту
            bridge.leave(dir);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
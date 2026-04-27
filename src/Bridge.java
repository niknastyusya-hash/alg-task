public class Bridge {
    private int carsOnBridge = 0;
    private String direction = null;  // "NORTH" или "SOUTH"

    public synchronized void enter(String dir) throws InterruptedException {
        // Ждем, пока мост занят встречным направлением
        while (direction != null && !direction.equals(dir)) {
            wait();
        }

        direction = dir;
        carsOnBridge++;
        System.out.printf("%s въехал. На мосту: %d (%s)%n", dir, carsOnBridge, direction);
    }

    public synchronized void leave(String dir) {
        carsOnBridge--;
        System.out.printf("%s уехал. Осталось: %d%n", dir, carsOnBridge);

        if (carsOnBridge == 0) {
            direction = null;
            notifyAll();  // Будим всех ждущих
        }
    }
}

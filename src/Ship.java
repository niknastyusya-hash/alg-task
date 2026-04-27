// Класс судна
class Ship implements Runnable {
    Port port;
    String name;
    Thread t;

    Ship(Port port, String name) {
        this.port = port;
        this.name = name;
        t = new Thread(this, name);
        t.start();
    }

    public void run() {
        System.out.println(name + " подходит к порту");

        port.dock(name);

        System.out.println(name + " начало работ");
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            System.out.println("Исключение типа InterruptedException перехвачено");
        }
        System.out.println(name + " завершило работы");

        port.undock(name);
        System.out.println(name + " покидает порт");
    }
}
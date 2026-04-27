class Main {
    public static void main(String args[]) {
        System.out.println("ГРУЗОВОЙ ПОРТ");

        Port port = new Port(3);

        Ship s1 = new Ship(port, "Танкер 'Арктика'");
        Ship s2 = new Ship(port, "Контейнеровоз 'Северное сияние'");
        Ship s3 = new Ship(port, "Сухогруз 'Айсберг'");

        try {
            s1.t.join();
            s2.t.join();
            s3.t.join();
        } catch(InterruptedException e) {
            System.out.println("Исключение типа InterruptedException перехвачено");
        }
    }
}
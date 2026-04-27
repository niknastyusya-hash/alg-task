class Port {
    int n;                    // количество свободных причалов
    boolean valueSet = false;

    Port(int n) {
        this.n = n;
    }

    synchronized void dock(String shipName) {
        while(n == 0) {
            try {
                wait();
            } catch(InterruptedException e) {
                System.out.println("Исключение типа InterruptedException перехвачено");
            }
        }
        n--;
        System.out.println(shipName + " пришвартовалось. Свободных мест: " + n);
        if(n == 0)
            valueSet = true;
        notify();
    }

    synchronized void undock(String shipName) {
        n++;
        System.out.println(shipName + " отшвартовалось. Свободных мест: " + n);
        if(n > 0)
            valueSet = false;
        notify();
    }
}
class A extends Thread {// Класс потокового объекта
    public void run() {
        for(int i=0; i < 5; i++) {
            System.out.print ("A");
            try {Thread.sleep (100);}
            catch (InterruptedException e){}
        }
    }
}
class B implements Runnable {// Класс с потоковой функцией
    public void run() {
        for (int i=0; i < 5; i++) {
            System.out.print ("B");
            try {Thread.sleep (100);}
            catch (InterruptedException e){}
        }
    }
}
class TestThread {
    public static void main (String[] args) {
        A a = new A();//Первый способ создания потока
        B b = new B();//Второй способ создания потока (Шаг.1)
        Thread t= new Thread (b,"thread");//Второй способ создания потока(Шаг.2)
        a.start();//Запуск потоков А
        t.start();//Запуск потоков B
    }
}
/*Result: ABABABABAB*/
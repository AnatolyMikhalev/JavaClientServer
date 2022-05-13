public class Test {
    public static void main(String[] args) {

        Thread t = Thread.currentThread(); // получаем главный поток
        System.out.println(t.getName()); // main
    }
}

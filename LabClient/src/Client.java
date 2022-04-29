//10. 0 3 2 3 1 2
//
// TCP протокол
// Реализовать в клиенте указание адреса и порта сервера из файла настроек
// Указание порта для сервера из командной строки
// Сообщения, получаемые клиентом с сервера должны записываться в файл
//        «Журнала клиента» путь к которому определяется из файла настроек
// Сообщения, получаемые сервером от клиента должны записываться в файл
//        «Журнала сервера» путь к которому определяется с консоли ввода приложения
//
//        Сервер возвращает клиенту результат выражения (допустимые операции «+», «-»).
//        Операнды и операции передаются за раз по одному (например, выражение «3.4+1.6-
//        5=» нужно передавать с помощью трёх сообщений: «3.4+», «1.6-» и «5=», где «=» -
//        признак конца выражения). В случае не возможности разобрать сервером полученную
//        строку или при переполнении, возникшем при вычислении полученного выражения,
//        сервер присылает клиенту соответствующее уведомление.
//
//Если требуется использовать файл настроек, то его (файл настроек) нужно разместить в корневом каталоге
//        проекта либо жёстко указать к нему путь в коде разрабатываемого приложения.

import java.io.*;
import java.net.*;

public class Client implements Runnable {
    public static final int PORT = 2500;
    public static final String HOST = "localhost";
    private String name = null;

    public Client(String s) {
        name = s;
    }

    public void run() {
        try {
            Socket socket = new Socket(HOST, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // для считывания информации с консоли

            while (true) {
                System.out.println("Введите операнду и операцию:");
                String word = reader.readLine();

                out.write(word + "\n"); // отправляем сообщение на сервер
                out.flush();

                String serverWord = in.readLine(); // ждём, что скажет сервер
                System.out.println(serverWord); // получив - выводим на экран

                if(word.charAt(word.length() - 1) == '='){
                    break;
                }
                //if (word.charAt(word.length() - 1) == '=') break;
                Thread.yield();
            }
        } catch (IOException e) {
            System.err.println("Исключение: " + e);
        }
    }

    public static void main(String[] args) {
        String name = "имя";
        //for (int i = 1; i <= CLIENT_COUNT; i++) {
        Client ja = new Client(name + 1);
        Thread th = new Thread(ja);
        th.start();
        //}
    }
}
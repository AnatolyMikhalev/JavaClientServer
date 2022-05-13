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

public class Server {
    private static final int TIME_SEND_SLEEP = 100;
    private ServerSocket servSocket;
    public static int PORT;
    BufferedWriter writer;

    public static void main(String[] args) {
        Server server = new Server();
        server.go();
    }

    public Server() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Введите ПОРТ: ");
            servSocket = new ServerSocket(Integer.parseInt(in.readLine()));

            System.out.print("Введите путь к журналу сервера: ");       //  D://Java//lab4//ClientSeverSocket//LabServer//src//logfile.txt
            String logfileName = in.readLine();
            File logfile = new File(logfileName);
            if(!logfile.exists())
                logfile.createNewFile();
            writer = new BufferedWriter(new FileWriter(logfile));

        } catch (IOException e) {
            System.err.println("Не удаётся открыть сокет для сервера: " + e.toString());
        }
    }

    public static boolean isNumeric(String string) {
        int intValue;

        if (string == null || string.equals("")) {
            return false;
        }
        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    public void go() {
        class Listener implements Runnable {
            Socket socket;
            BufferedReader in;
            BufferedWriter out;
            int sum = 0;

            public Listener(Socket aSocket) {
                socket = aSocket;
            }

            public void run() {
                try {
                    try {
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        System.out.println("Слушатель запущен");

                        char operation = '+';

                        while (true) {
                            String clientWord = in.readLine();
                            writer.write(clientWord + '\n');
                            writer.flush();

                            if (isNumeric(clientWord.substring(0, clientWord.length() - 1)) && !(clientWord.charAt(clientWord.length() - 1) != '=' && clientWord.charAt(clientWord.length() - 1) != '-' && clientWord.charAt(clientWord.length() - 1) != '+')) {
                                if (operation == '+') {
                                    sum += Integer.parseInt(clientWord.substring(0, clientWord.length() - 1));
                                    out.write("Выражение принято\n");
                                    out.flush();
                                } else if (operation == '-') {
                                    sum -= Integer.parseInt(clientWord.substring(0, clientWord.length() - 1));
                                    out.write("Выражение принято\n");
                                    out.flush();
                                }

                                operation = clientWord.charAt(clientWord.length() - 1);
                                if (operation == '=') {
                                    out.write("Результат: " + Integer.toString(sum) + '\n');
                                    out.flush();
                                    break;
                                }
                            } else {
                                out.write("Неверное выражение\n");
                                out.flush();
                            }
                        }

                    } finally {
                        in.close();
                        out.close();
                    }
                } catch (IOException e) {
                    System.err.println("Исключение: " + e.toString());
                }
            }
        }

        System.out.println("Сервер запущен...");
        while (true) {
            try {
                Socket socket = servSocket.accept();
                Listener listener = new Listener(socket);
                Thread thread = new Thread(listener);
                thread.start();
            } catch (IOException e) {
                System.err.println("Исключение: " + e.toString());
            }
        }
    }
}
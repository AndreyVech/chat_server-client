import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class Client implements Runnable {
    Socket socket;
    Scanner in;
    PrintStream out;
    ChatServer server;
//    создадим массив имен
    ArrayList<String> clientsNames = new ArrayList<>();

    public Client(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;
        new Thread(this).start();
    }

    void receive(String message){
        out.println(message);
    }



    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Put your name");
            String clientName = in.nextLine();
            clientsNames.add(clientName);
            out.println("Welcome to chat - " + clientName);
            String input = in.nextLine();

            Date date = new Date();
            SimpleDateFormat nowTime = new SimpleDateFormat("hh:mm:ss");

            while (!input.equals("bye")) {
//              сделаем красивый вид сообщения в чате
                String inputMessage = clientName + " [" + nowTime.format(date) + "]: "+ input;
//                отправим всем
                sendAll(inputMessage);
//                и сохраним на сервере
                ChatServer.saveToServer(inputMessage);
                input = in.nextLine();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendAll(String message){
        for (Client client : server.clients){
//            отправим всем, кроме себя
            if (client != this) {
                client.receive(message);
            }
        }
    }
}


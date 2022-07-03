import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
//    массив клиентов
    ArrayList<Client> clients = new ArrayList<>();
//    массив истории чата
    static ArrayList<String> chatHistory = new ArrayList<>();

    ServerSocket serverSocket;

    ChatServer() throws IOException {
        serverSocket = new ServerSocket(1234);
    }

    public static void main(String[] args) throws IOException {
        new ChatServer().run();
    }

    public void run() {
        while (true) {
            System.out.println("Waiting...");
            // ждем клиента из сети
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                // создаем клиента на своей стороне
                clients.add(new Client(socket, this));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//будем всё сохранять и выводить в консоль сервака
    public static void saveToServer(String inputMessage){
        chatHistory.add(inputMessage);
        System.out.println(inputMessage);
    }
}

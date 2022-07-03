import java.awt.Color;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;

public class NetClient extends JFrame implements KeyListener {

	final String serverIP = "127.0.0.1";
	final int serverPort = 1234;
	JTextPane textPane;
	JTextArea textArea;
	JScrollPane scrollPane;
	InputStreamReader in;
	PrintWriter out;

	NetClient() {
		// Создаем окно
		super("Simple Chat client");
		setSize(400, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Добавляем на окно текстовое поле
		textArea = new JTextArea();
		textPane = new JTextPane();
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setMargin(new Insets(10, 10, 10, 10));
		scrollPane = new JScrollPane(textArea);
		this.add(scrollPane);

		// Подсоединяемся к серверу
		connect();

	}

	void connect() {
		try {
			Socket socket = new Socket(serverIP, serverPort);
			in = new InputStreamReader(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream());
			textArea.addKeyListener(this);
		} catch (IOException e) {
			textArea.setForeground(Color.RED);
			textArea.append("Server " + serverIP + " port " + serverPort + " " + "" + "NOT AVAILABLE");
			e.printStackTrace();
		}
		new Thread() {
			// в отдельном потоке
			// принимаем символы от сервера
			public void run() {
				while (true) {
					try {
						addCharToTextArea((char) (in.read()));
					} catch (IOException e) {
						textArea.setForeground(Color.RED);
						textArea.append("\nCONNECTION ERROR");
						e.printStackTrace();
						return;
					}
				}
			};
		}.start();

	}

	public static void main(String[] args) {
		new NetClient().setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}
	@Override 
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// отправляем напечатанный символ в сеть и на экран
		out.print(arg0.getKeyChar());
		out.flush();
		
		System.out.print((int)(arg0.getKeyChar()));
		addCharToTextArea(arg0.getKeyChar());
	}

	void addCharToTextArea(char c) {
		textArea.append(c + "");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}

package client;

import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		Socket clientSocket = null;
		try {
			clientSocket = new Socket("localhost", 11111);
			Scanner scanner = new Scanner(System.in);
			MainMenuHandler handler = new MainMenuHandler(clientSocket, scanner, false);
			handler.start();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				clientSocket.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println("the game Server has crashed or bad internet connection");
		}
	}
}

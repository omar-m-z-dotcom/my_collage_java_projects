package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import interfaces.ConstructErrorInterface;
import interfaces.RunTimeErrorInterface;
import validator.InputValidator;

/**
 * 
 * <b>handles playing a single player game</b>
 *
 */
public class SingleGameHandler extends Thread implements ConstructErrorInterface, RunTimeErrorInterface{
	private Socket clientSocket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Scanner scanner;

	/**
	 * @param clientSocket
	 * @param scanner
	 */
	public SingleGameHandler(Socket clientSocket, Scanner scanner) {
		this.clientSocket = clientSocket;
		this.scanner = scanner;
		try {
			inputStream = new DataInputStream(this.clientSocket.getInputStream());
			outputStream = new DataOutputStream(this.clientSocket.getOutputStream());
		} catch (Exception e) {
			handleConstructError(e);
		}
	}

	/**
	 * <b>handles server disconnection at construction time</b>
	 */
	@Override
	public void handleConstructError(Exception e) {
		e.printStackTrace();
		try {
			clientSocket.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("the game Server has crashed or bad internet connection");
		scanner.close();
		return;
	}

	/**
	 * <b>handles server disconnection at run time</b>
	 */
	@Override
	public void handleRunTimeError(Exception e) {
		e.printStackTrace();
		try {
			clientSocket.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("the game Server has crashed or bad internet connection");
		scanner.close();
		return;
	}

	public void startGame() throws Exception {
		String response = InputReader.readInput(inputStream, true);
		String answer;
		//if the lookup server has failed
		if (response.equalsIgnoreCase("failure in lookup server (game canceled)")) {
			new MainMenuHandler(clientSocket, scanner, true).start();
			return;
		}
		while (true) {
			answer = scanner.nextLine();
			//if the user wants to exit the single player game
			if (answer.equalsIgnoreCase("exit game")) {
				clientSocket.close();
				System.out.println("exiting game");
				Thread.sleep(3000);
				MainMenuHandler thread = new MainMenuHandler(new Socket("localhost", 11111), scanner, false);
				thread.start();
				return;
			}
			String regex = "[a-zA-Z]{1}";
			//while the user hasn't entered one alphabetic character
			while (!InputValidator.isValid(answer, regex)) {
				System.out.println("you can only enter one alphabetic character at a time");
				answer = scanner.nextLine();
			}
			Character answerChar = answer.charAt(0);
			OutputSender.sendOutput(outputStream, answerChar);
			response = InputReader.readInput(inputStream, true);
			//if the server said that won or lost the game
			if (response.equalsIgnoreCase("you won congrates")
					|| response.equalsIgnoreCase("you lost better luck next time")) {
				break;
			} else
				continue;
		}
		new MainMenuHandler(clientSocket, scanner, true).start();
	}

	@Override
	public void run() {
		try {
			startGame();
		} catch (Exception e) {
			handleRunTimeError(e);
		}
	}

}

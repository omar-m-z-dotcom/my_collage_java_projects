package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import interfaces.ConstructErrorInterface;
import interfaces.LoginInterface;
import interfaces.RunTimeErrorInterface;
import interfaces.SignupInterface;
import model.GameType;
import validator.InputValidator;

/**
 * <b>handles the main menu from the client side</b>
 *
 */
public class MainMenuHandler extends Thread
		implements SignupInterface, LoginInterface, ConstructErrorInterface, RunTimeErrorInterface {
	private Socket clientSocket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Scanner scanner;
	private boolean isLogedin;
	/**
	 * a flag that specifies whether the user has exited a team based game while
	 * it's not finished
	 */
	private boolean exitedTeamGame = false;

	/**
	 * @param clientSocket
	 * @param scanner
	 * @param isLogedin
	 */
	public MainMenuHandler(Socket clientSocket, Scanner scanner, boolean isLogedin) {
		this.isLogedin = isLogedin;
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
	 * <b>handels the construction error of this class if server has
	 * disconnected</b>
	 * 
	 * @param e the exception that is thrown
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
	 * <b>handels the signup from the client side</b>
	 */
	@Override
	public void signup() throws IOException {
		String response = "";
		// while the client signup not successful
		while (!response.equalsIgnoreCase("signup successful")) {
			response = InputReader.readInput(inputStream, true);
			String answer = scanner.nextLine();
			OutputSender.sendOutput(outputStream, answer);
			response = InputReader.readInput(inputStream, true);
			// if the name of the user is not valid
			if (response.equalsIgnoreCase("invalid name try again")) {
				continue;
			}
			answer = scanner.nextLine();
			OutputSender.sendOutput(outputStream, answer);
			response = InputReader.readInput(inputStream, true);
			// if the username of the user is not valid
			if (response.equalsIgnoreCase("invalid username try again")) {
				continue;
			}
			answer = scanner.nextLine();
			OutputSender.sendOutput(outputStream, answer);
			response = InputReader.readInput(inputStream, true);
			// if the password of the user is not valid or another user has already taken
			// the same username
			if (response.equalsIgnoreCase("invalid password try again")
					|| response.equalsIgnoreCase("users with duplicate usernames are not allowed try again")) {
				continue;
			}
			// if the client signup is successful
			if (response.equalsIgnoreCase("signup successful")) {
				break;
			}
		}
	}

	/**
	 * <b>handels the login from the client side</b>
	 */
	@Override
	public void login() throws IOException {
		String response = "";
		// while the client login not successful
		while (!response.equalsIgnoreCase("login successful")) {
			response = InputReader.readInput(inputStream, true);
			String answer = scanner.nextLine();
			OutputSender.sendOutput(outputStream, answer);
			response = InputReader.readInput(inputStream, true);
			if (response.equalsIgnoreCase("invalid username try again")) {
				continue;
			}
			answer = scanner.nextLine();
			OutputSender.sendOutput(outputStream, answer);
			response = InputReader.readInput(inputStream, true);
			// if the password of the user is not valid or there are no users with the
			// entered username and password
			if (response.equalsIgnoreCase("invalid password try again")
					|| response.equalsIgnoreCase("no user with such username and password try again")) {
				continue;
			}
			// if this client is the first user of this app
			if (response.equalsIgnoreCase("your are the first user of the app so you have to register first")) {
				signup();
				break;
			}
			// if the client login is successful
			if (response.equalsIgnoreCase("login successful")) {
				break;
			}
		}

	}

	/**
	 * <b>handels starting a team game in the server before joining in the game</b>
	 * 
	 * @throws Exception
	 */
	public void startTeamGame() throws Exception {
		String regex = "^.+try again$";
		while (true) {
			String response = InputReader.readInput(inputStream, true);
			Integer intNumber = scanner.nextInt();
			// the number of teams
			int teamsNum = intNumber;
			scanner.nextLine();
			OutputSender.sendOutput(outputStream, intNumber);
			response = InputReader.readInput(inputStream, true);
			// if the server has responded with something that ends with try again
			if (InputValidator.isValid(response, regex)) {
				continue;
			}
			intNumber = scanner.nextInt();
			// the number of players
			int playersPerTeamNum = intNumber;
			scanner.nextLine();
			OutputSender.sendOutput(outputStream, intNumber);
			// falg that spicifices if a loop has failed
			boolean loopFailed = false;
			// for each team
			for (int i = 0; i < teamsNum; i++) {
				response = InputReader.readInput(inputStream, true);
				if (InputValidator.isValid(response, regex)) {
					loopFailed = true;
					break;
				}
				String stringAnswer = scanner.nextLine();
				OutputSender.sendOutput(outputStream, stringAnswer);
			}
			if (loopFailed) {
				continue;
			}
			loopFailed = false;
			// for each player in the game
			for (int i = 0; i < (teamsNum * playersPerTeamNum); i++) {
				response = InputReader.readInput(inputStream, true);
				if (InputValidator.isValid(response, regex)) {
					loopFailed = true;
					break;
				}
				String stringAnswer = scanner.nextLine();
				OutputSender.sendOutput(outputStream, stringAnswer);
			}
			if (loopFailed) {
				continue;
			}
			InputReader.readInput(inputStream, true);
			break;
		}
	}

	/**
	 * <b>handles joining a team game and playing the team game</b>
	 * 
	 * @throws Exception
	 */
	public void handleTeamGame() throws Exception {
		InputReader.readInput(inputStream, true);
		Long gameId = scanner.nextLong();
		scanner.nextLine();
		OutputSender.sendOutput(outputStream, gameId);
		String response = InputReader.readInput(inputStream, true);
		if (response.equalsIgnoreCase("wait while other players join")) {
			while (true) {
				response = InputReader.readInput(inputStream, true);
				String isUserTurnRegix = "your next guess:";
				String waitRegix = "^.+wait.+";
				String winString = "your team has won congrats";
				ArrayList<String> lossStrings = new ArrayList<String>();
				lossStrings.add("your team has lost better luck next time");
				lossStrings.add("you have used up all your lives and your team has lost better luck next time");
				lossStrings.add("you have used up all your lives but your team stil has a chance to win");
				// if the server said that it's your turn
				if (InputValidator.isValid(response, isUserTurnRegix)) {
					String answer = scanner.nextLine();
					// if the user wants to exit the team based game
					if (answer.equalsIgnoreCase("exit game")) {
						clientSocket.close();
						System.out.println("exiting game");
						System.out.println();
						exitedTeamGame = true;
						Thread.sleep(3000);
						return;
					}
					String regex = "[a-zA-Z]{1}";
					// while the user havn't responded with one alphabetic character
					while (!InputValidator.isValid(answer, regex)) {
						System.out.println("you can only enter one alphabetic character at a time");
						answer = scanner.nextLine();
					}
					Character answerChar = answer.charAt(0);
					OutputSender.sendOutput(outputStream, answerChar);
					continue;
				}
				// if server told you to wait your turn
				if (InputValidator.isValid(response, waitRegix)) {
					continue;
				}
				// if the server told you that your team has won
				if (response.equalsIgnoreCase(winString)) {
					break;
				}
				boolean hasLost = false;
				// if the server said that you have lost the game or your team has lost
				for (int i = 0; i < lossStrings.size(); i++) {
					if (response.equalsIgnoreCase(lossStrings.get(i))) {
						hasLost = true;
						break;
					}
				}
				if (hasLost) {
					break;
				}
				continue;
			}
		} else {
			return;
		}
	}

	/**
	 * <b>handels server disconnection from the client side at run time</b>
	 */
	@Override
	public void handleRunTimeError(Exception e) {
		try {
			clientSocket.close();
		} catch (Exception e1) {
		}
		System.out.println("the game Server has crashed or bad internet connection");
		scanner.close();
		return;
	}

	@Override
	public void run() {
		try {
			if (clientSocket.isClosed()) {
				scanner.close();
				return;
			}
			if (!isLogedin) {
				InputReader.readInput(inputStream, true);
				while (true) {
					String answer = scanner.nextLine();
					OutputSender.sendOutput(outputStream, answer);
					if (answer.equalsIgnoreCase("login")) {
						login();
						isLogedin = true;
					} else if (answer.equalsIgnoreCase("signup")) {
						signup();
						isLogedin = true;
					} else if (answer.equalsIgnoreCase("exit")) {
						clientSocket.close();
						scanner.close();
						return;
					} else {
						InputReader.readInput(inputStream, true);
						continue;
					}
					break;
				}
			}
			InputReader.readInput(inputStream, true);
			while (true) {
				String answer = scanner.nextLine();
				OutputSender.sendOutput(outputStream, answer);
				if (answer.equalsIgnoreCase("start a " + GameType.SINGLE_PLAYER_GAME.toString())) {
					new SingleGameHandler(clientSocket, scanner).start();
				} else if (answer.equalsIgnoreCase("start a " + GameType.TEAM_BASED_GAME.toString())) {
					startTeamGame();
					InputReader.readInput(inputStream, true);
					continue;
				} else if (answer.equalsIgnoreCase("see massages")) {
					while (true) {
						InputReader.readInput(inputStream, true);
						answer = scanner.nextLine();
						OutputSender.sendOutput(outputStream, answer);
						if (answer.equalsIgnoreCase("see all massages")
								|| answer.equalsIgnoreCase("see only new massages")) {
							InputReader.readInput(inputStream, true);
							break;
						} else {
							InputReader.readInput(inputStream, true);
							continue;
						}
					}
					InputReader.readInput(inputStream, true);
					continue;
				} else if (answer.equalsIgnoreCase("exit")) {
					clientSocket.close();
					scanner.close();
					return;
				} else if (answer.equalsIgnoreCase("join a game")) {
					handleTeamGame();
					// if the user wants to exit the team based game
					if (exitedTeamGame) {
						MainMenuHandler thread = new MainMenuHandler(new Socket("localhost", 11111), scanner, false);
						thread.start();
						return;
					}
					InputReader.readInput(inputStream, true);
					continue;
				} else {
					InputReader.readInput(inputStream, true);
					continue;
				}
				break;
			}
		} catch (Exception e) {
			handleRunTimeError(e);
		}
	}

}

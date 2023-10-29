package gameServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
import interfaces.ConstructErrorInterface;
import interfaces.LoginInterface;
import interfaces.RunTimeErrorInterface;
import interfaces.SignupInterface;
import model.GameType;
import model.Massage;
import model.MassageType;
import model.User;
import validator.InputValidator;

/**
 * 
 * handels the backend logic of the user main menu
 *
 */
public class MainMenuThread extends Thread
		implements SignupInterface, LoginInterface, ConstructErrorInterface, RunTimeErrorInterface {
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private User currentUser;
	private boolean isLogedin;

	public MainMenuThread(Socket socket, String username, boolean isLogedin) {
		this.isLogedin = isLogedin;
		this.socket = socket;
		try {
			inputStream = new DataInputStream(this.socket.getInputStream());
			outputStream = new DataOutputStream(this.socket.getOutputStream());
		} catch (Exception e) {
			handleConstructError(e);
		}
		if (isLogedin) {
			currentUser = new User(null, username, null, true);
			List<User> users = GameFilesManager.getData(GameFilesManager.getUsersFilePath(),
					GameFilesManager.getUsersFileLock(), User.class);
			for (int i = 0; i < users.size(); i++) {
				if (currentUser.getUsername().equals(users.get(i).getUsername())) {
					currentUser = users.get(i);
					break;
				}
			}
		}
	}

	@Override
	public void handleConstructError(Exception e) {
		e.printStackTrace();
		try {
			socket.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (isLogedin) {
			MainMenuThread.disconnectionLogout(currentUser.getUsername());
		}
	}

	@Override
	public void signup() throws Exception {
		while (true) {
			String validRegex = "^[a-zA-Z ]+$";
			outputStream.writeUTF("enter your name: ");
			String name = inputStream.readUTF();
			if (!InputValidator.isValid(name, validRegex)) {
				outputStream.writeUTF("invalid name try again");
				continue;
			}
			validRegex = "^\\w+$";
			outputStream.writeUTF("enter your username: ");
			String username = inputStream.readUTF();
			if (!InputValidator.isValid(username, validRegex)) {
				outputStream.writeUTF("invalid username try again");
				continue;
			}
			outputStream.writeUTF("enter your password: ");
			String password = inputStream.readUTF();
			if (!InputValidator.isValid(password, validRegex)) {
				outputStream.writeUTF("invalid password try again");
				continue;
			}
			User newUser = new User(name, username, password, true);
			boolean isRegistered = GameFilesManager.addData(newUser, GameFilesManager.getUsersFilePath(),
					GameFilesManager.getUsersFileLock(), User.class);
			if (isRegistered) {
				outputStream.writeUTF("signup successful");
				currentUser = newUser;
				break;
			} else {
				outputStream.writeUTF("users with duplicate usernames are not allowed try again");
				continue;
			}
		}
	}

	@Override
	public void handleRunTimeError(Exception e) {
		e.printStackTrace();
		try {
			socket.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (currentUser != null) {
			currentUser.setIsActive(false);
			GameFilesManager.modifyData(currentUser, GameFilesManager.getUsersFilePath(),
					GameFilesManager.getUsersFileLock(), User.class);
		}
	}

	@Override
	public void login() throws Exception {
		while (true) {
			String validRegex = "^\\w+$";
			outputStream.writeUTF("enter your username: ");
			String username = inputStream.readUTF();
			if (!InputValidator.isValid(username, validRegex)) {
				outputStream.writeUTF("invalid username try again");
				continue;
			}
			outputStream.writeUTF("enter your password: ");
			String password = inputStream.readUTF();
			if (!InputValidator.isValid(password, validRegex)) {
				outputStream.writeUTF("invalid password try again");
				continue;
			}
			List<User> users = GameFilesManager.getData(GameFilesManager.getUsersFilePath(),
					GameFilesManager.getUsersFileLock(), User.class);
			if (users.size() == 0) {
				outputStream.writeUTF("your are the first user of the app so you have to register first");
				signup();
				break;
			}
			for (int i = 0; i < users.size(); i++) {
				// the username and password matches those that were given by the user and this
				// user is not active so that no two or more users with the same username and password
				// are loged in at the same time
				if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password)
						&& !users.get(i).getIsActive()) {
					currentUser = users.get(i);
					currentUser.setIsActive(true);
					GameFilesManager.modifyData(currentUser, GameFilesManager.getUsersFilePath(),
							GameFilesManager.getUsersFileLock(), User.class);
					break;
				}
			}
			if (currentUser == null) {
				outputStream.writeUTF("no user with such username and password try again");
				continue;
			} else {
				outputStream.writeUTF("login successful");
				break;
			}
		}
	}

	/**
	 * handels getting the user massages
	 * 
	 * @throws Exception
	 */
	public void seeMassages() throws Exception {
		while (true) {
			outputStream.writeUTF("do you want to see all massages or see only new massages");
			String answer = inputStream.readUTF();
			if (answer.equalsIgnoreCase("see all massages")) {
				String allMassages = "";
				List<Massage> userMassages = GameFilesManager.getData(GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
				for (int i = 0; i < userMassages.size(); i++) {
					if (userMassages.get(i).getReciver().equals(currentUser.getUsername())
							&& userMassages.get(i).getType().equals(MassageType.JOIN_MASSAGE)
							&& userMassages.get(i).getContent().contains("join code:")) {
						if (!userMassages.get(i).getIsRead()) {
							userMassages.get(i).setIsRead(true);
							GameFilesManager.modifyData(userMassages.get(i), GameFilesManager.getMassagesFilePath(),
									GameFilesManager.getMassagesFileLock(), Massage.class);
						}
						allMassages = allMassages.concat(userMassages.get(i).getContent() + "\n");
					}
				}
				if (allMassages.equals("")) {
					outputStream.writeUTF("you don't have any massages");
				} else {
					outputStream.writeUTF(allMassages);
				}
				break;
			} else if (answer.equalsIgnoreCase("see only new massages")) {
				String allMassages = "";
				List<Massage> userMassages = GameFilesManager.getData(GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
				for (int i = 0; i < userMassages.size(); i++) {
					if (userMassages.get(i).getReciver().equals(currentUser.getUsername())
							&& userMassages.get(i).getType().equals(MassageType.JOIN_MASSAGE)
							&& userMassages.get(i).getContent().contains("join code:")
							&& !userMassages.get(i).getIsRead()) {
						allMassages = allMassages.concat(userMassages.get(i).getContent() + "\n");
						userMassages.get(i).setIsRead(true);
						GameFilesManager.modifyData(userMassages.get(i), GameFilesManager.getMassagesFilePath(),
								GameFilesManager.getMassagesFileLock(), Massage.class);
					}
				}
				if (allMassages.equals("")) {
					outputStream.writeUTF("you don't have any new massages");
				} else {
					outputStream.writeUTF(allMassages);
				}
				break;
			} else {
				outputStream.writeUTF("invalid input try again");
				continue;
			}
		}
	}

	/**
	 * <b>logs out the user if he suddenly disconnected from the game server</b>
	 * 
	 * @param username the username of the user who will be loged out if he suddenly
	 *                 disconnected from the game server
	 */
	public static void disconnectionLogout(String username) {
		User userToLogout = new User(null, username, null, true);
		List<User> users = GameFilesManager.getData(GameFilesManager.getUsersFilePath(),
				GameFilesManager.getUsersFileLock(), User.class);
		for (int i = 0; i < users.size(); i++) {
			if (userToLogout.getUsername().equals(users.get(i).getUsername())) {
				userToLogout = users.get(i);
				break;
			}
		}
		userToLogout.setIsActive(false);
		GameFilesManager.modifyData(userToLogout, GameFilesManager.getUsersFilePath(),
				GameFilesManager.getUsersFileLock(), User.class);
	}

	@Override
	public void run() {
		try {
			if (socket.isClosed()) {
				return;
			}
			if (!isLogedin) {
				outputStream.writeUTF("welcome to the hangman game\n" + "do you want to signup, login or exit?: ");
				while (true) {
					String answer = inputStream.readUTF();
					if (answer.equalsIgnoreCase("login")) {
						login();
						isLogedin = true;
					} else if (answer.equalsIgnoreCase("signup")) {
						signup();
						isLogedin = true;
					} else if (answer.equalsIgnoreCase("exit")) {
						socket.close();
						return;
					} else {
						outputStream.writeUTF("invalid input try again");
						continue;
					}
					break;
				}
			}
			outputStream.writeUTF("do you want to see massages, join a game, start a " + GameType.SINGLE_PLAYER_GAME
					+ ", " + GameType.TEAM_BASED_GAME + " or exit?: ");
			while (true) {
				String answer = inputStream.readUTF();
				if (answer.equalsIgnoreCase("start a " + GameType.SINGLE_PLAYER_GAME.toString())) {
					SingleGameThread thread = new SingleGameThread(socket, currentUser.getUsername());
					thread.start();
				} else if (answer.equalsIgnoreCase("start a " + GameType.TEAM_BASED_GAME.toString())) {
					TeamGameThread thread = new TeamGameThread(socket, currentUser.getUsername());
					thread.start();
				} else if (answer.equalsIgnoreCase("see massages")) {
					seeMassages();
					outputStream.writeUTF("do you want to see massages, join a game, start a "
							+ GameType.SINGLE_PLAYER_GAME + ", " + GameType.TEAM_BASED_GAME + " or exit?: ");
					continue;
				} else if (answer.equalsIgnoreCase("exit")) {
					currentUser.setIsActive(false);
					GameFilesManager.modifyData(currentUser, GameFilesManager.getUsersFilePath(),
							GameFilesManager.getUsersFileLock(), User.class);
					socket.close();
					return;
				} else if (answer.equalsIgnoreCase("join a game")) {
					outputStream.writeUTF("enter the join code: ");
					try {
						long answer1 = inputStream.readLong();
						if (answer1 < 1) {
							throw new Exception();
						}
						TeamGameThread thread = (TeamGameThread) GameServer.searchTeamGameThread(answer1);
						String response = thread.addPlayer(currentUser.getUsername(), socket);
						if (response != null) {
							outputStream.writeUTF(response);
							outputStream.writeUTF("do you want to see massages, join a game, start a "
									+ GameType.SINGLE_PLAYER_GAME + ", " + GameType.TEAM_BASED_GAME + " or exit?: ");
							continue;
						}
					} catch (Exception e) {
						if (e instanceof NullPointerException) {
							outputStream.writeUTF("no game with such join code");
						} else {
							outputStream.writeUTF("invalid input try again");
						}
						outputStream.writeUTF("do you want to see massages, join a game, start a "
								+ GameType.SINGLE_PLAYER_GAME + ", " + GameType.TEAM_BASED_GAME + " or exit?: ");
						continue;
					}
				} else {
					outputStream.writeUTF("invalid input try again");
					continue;
				}
				break;
			}
		} catch (Exception e) {
			handleRunTimeError(e);
		}
	}

}

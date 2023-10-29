package gameServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import interfaces.ConstructErrorInterface;
import interfaces.RunTimeErrorInterface;
import model.Game;
import model.GameType;
import model.Massage;
import model.MassageType;
import model.Player;
import model.StatusInGame;
import model.Team;
import model.User;
import validator.InputValidator;

/**
 * 
 * handels the start up of the team based game
 *
 */
public class TeamGameThread extends Thread implements ConstructErrorInterface, RunTimeErrorInterface {
	private Team[] teams;
	private Game teamBasedGame;
	private String hangmanWord;
	private Socket clientSocket;
	private String clientUserName;
	private boolean InitFailed = false;
	private ArrayList<TeamPlayerCommunicator> teamPlayerCommunicators = new ArrayList<TeamPlayerCommunicator>();

	public TeamGameThread(Socket socket, String username) {
		this.clientUserName = username;
		this.clientSocket = socket;
		try {
			while (true) {
				DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
				outputStream.writeUTF("how many teams do want in this game?: ");//
				int teamsNum = inputStream.readInt();
				if (teamsNum > GameServer.configs.get("maxTeamsNum") || teamsNum <= 1) {
					outputStream.writeUTF("the max number of teams is " + GameServer.configs.get("maxTeamsNum")
							+ " and the min number of teams is 2 try again");//
					continue;
				}
				outputStream.writeUTF("how many players do want per team?: ");
				int teamPlayersNum = inputStream.readInt();
				if (teamPlayersNum > GameServer.configs.get("maxTeamMembersNum") || teamPlayersNum < 1) {
					outputStream.writeUTF(
							"the max number of players per team is " + GameServer.configs.get("maxTeamMembersNum")
									+ " and the min number of players per team is 1 try again");//
					continue;
				}
				teams = new Team[teamsNum];
				for (int i = 0; i < teams.length; i++) {
					teams[i] = new Team(null, teamPlayersNum, new ArrayList<String>(), 0, StatusInGame.UNDECIDED);
				}
				boolean invalidTeamNames = false;
				for (int i = 0; i < teams.length; i++) {
					String regex = "^\\w+$";
					outputStream.writeUTF("enter the name of team " + (i + 1) + " ?: ");
					String answer = inputStream.readUTF();
					if (!InputValidator.isValid(answer, regex)) {
						outputStream.writeUTF("the name of the team is invalid try again");//
						invalidTeamNames = true;
						break;
					}
					teams[i].setName(answer);
					if (i != 0) {
						for (int j = 0; j < i; j++) {
							if (teams[i].getName().equalsIgnoreCase(teams[j].getName())) {
								invalidTeamNames = true;
								outputStream.writeUTF("dublicate team names is not allowed try again");//
								break;
							}
						}
					}
					if (invalidTeamNames) {
						break;
					}
				}
				if (invalidTeamNames) {
					continue;
				}
				boolean teamsInitFailed = false;
				String[] playerNames = new String[teams[0].getSize() * teams.length];
				int k = 0;
				for (int i = 0; i < teams.length; i++) {
					for (int j = 0; j < teams[i].getSize(); j++, k++) {
						String regex = "^\\w+$";
						outputStream.writeUTF(
								"enter the username of player " + (j + 1) + " in team " + teams[i].getName() + ": ");
						String answer = inputStream.readUTF();
						playerNames[k] = answer;
						if (k != 0) {
							for (int l = 0; l < k; l++) {
								if (playerNames[l].equals(playerNames[k])) {
									outputStream.writeUTF("dublicate player usernames is not allowed try again");//
									teamsInitFailed = true;
									break;
								}
							}
						}
						if (teamsInitFailed) {
							break;
						}
						if (!InputValidator.isValid(answer, regex)) {
							outputStream.writeUTF("the username of the player is invalid try again");//
							teamsInitFailed = true;
							break;
						}
						List<User> users = GameFilesManager.getData(GameFilesManager.getUsersFilePath(),
								GameFilesManager.getUsersFileLock(), User.class);
						Boolean userIsFound = false;
						for (User user : users) {
							if (user.getUsername().equals(answer)) {
								userIsFound = true;
								teams[i].getMembers().add(user.getUsername());
								break;
							}
						}
						if (!userIsFound) {
							outputStream.writeUTF("no player with such username try again");//
							teamsInitFailed = true;
							break;
						}
					}
					if (teamsInitFailed) {
						break;
					}
				}
				if (teamsInitFailed) {
					continue;
				}
				hangmanWord = HangmanWordGetter.getHangmanWord();
				if (hangmanWord == null) {
					throw new Exception("can't connect to lookup server");
				}
				long gameId = GameServer.getGameId();
				teamBasedGame = new Game(gameId, GameType.TEAM_BASED_GAME, (teams.length * teams[0].getSize()),
						teams.length, false, false);
				for (int i = 0; i < teams.length; i++) {
					teams[i].setGameId(gameId);
				}
				outputStream.writeUTF(
						"the game is set\n" + "a join code will be sent to you and the other players you invited");//
				break;
			}
			GameFilesManager.addData(teamBasedGame, GameFilesManager.getGamesFilePath(),
					GameFilesManager.getGamesFileLock(), Game.class);
			for (int i = 0; i < teams.length; i++) {
				GameFilesManager.addData(teams[i], GameFilesManager.getTeamsFilePath(),
						GameFilesManager.getTeamsFileLock(), Team.class);
			}
			GameServer.addThread(this);
			MainMenuThread thread = new MainMenuThread(clientSocket, username, true);
			thread.start();
			for (int i = 0; i < teams.length; i++) {
				for (int j = 0; j < teams[i].getSize(); j++) {
					Massage joinMassage = new Massage(clientUserName, teams[i].getMembers().get(j),
							MassageType.JOIN_MASSAGE, "join code:" + teams[i].getGameId(), false);
					GameFilesManager.addData(joinMassage, GameFilesManager.getMassagesFilePath(),
							GameFilesManager.getMassagesFileLock(), Massage.class);
				}
			}
			return;
		} catch (Exception e) {
			handleConstructError(e);
		}
	}

	@Override
	public void handleConstructError(Exception e) {
		InitFailed = true;
		e.printStackTrace();
		if (e.getMessage().equalsIgnoreCase("can't connect to lookup server")) {
			try {
				DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
				outputStream.writeUTF("lookup server failed can't start the game");//
			} catch (IOException e1) {
				e1.printStackTrace();
				try {
					clientSocket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				MainMenuThread.disconnectionLogout(clientUserName);
				return;
			}
			MainMenuThread thread = new MainMenuThread(clientSocket, clientUserName, true);
			thread.start();
			return;
		} else {
			try {
				clientSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			MainMenuThread.disconnectionLogout(clientUserName);
			return;
		}
	}

	public long getGameId() {
		return teamBasedGame.getId();
	}

	/**
	 * waits while all players that are invited enter the game
	 */
	private synchronized void waitForPlayers() {
		try {
			wait();
			teamBasedGame.setHasBegun(true);
			GameFilesManager.modifyData(teamBasedGame, GameFilesManager.getGamesFilePath(),
					GameFilesManager.getGamesFileLock(), Game.class);
		} catch (InterruptedException e) {
			e.printStackTrace();
			handleRunTimeError(e);
		}
	}

	/**
	 * 
	 * @param username     the username of the invited player
	 * @param playerSocket
	 * @return a string if the user wasn't able to enter the game else null
	 */
	public synchronized String addPlayer(String username, Socket playerSocket) {
		if (teamBasedGame.getHasBegun()) {
			return "your not allowed to join this game";
		}
		boolean hasJoined = false;
		if (teamPlayerCommunicators.size() != (teams.length * teams[0].getSize())) {
			for (int i = 0; i < teams.length; i++) {
				for (int j = 0; j < teams[i].getMembers().size(); j++) {
					if (username.equals(teams[i].getMembers().get(j))) {
						new TeamPlayerCommunicator(playerSocket, teams, hangmanWord, j, i, false,
								teamPlayerCommunicators);
						hasJoined = true;
					}
				}
			}
			if (teamPlayerCommunicators.size() == (teams.length * teams[0].getSize()) && hasJoined) {
				notify();
				return null;
			} else if (hasJoined) {
				return null;
			}
		}
		return "your not invited to join this game";

	}

	@Override
	public void run() {
		if (InitFailed) {
			return;
		}
		waitForPlayers();
		for (int i = 0; !allFinished(); i++) {
			teamPlayerCommunicators.get(i).startPlayerTurn();
			if (i == (teamPlayerCommunicators.size() - 1)) {
				i = -1;
			}
		}
		teamBasedGame.setIsFinished(true);
		GameFilesManager.modifyData(teamBasedGame, GameFilesManager.getGamesFilePath(),
				GameFilesManager.getGamesFileLock(), Game.class);
		GameServer.removeThread(this);
	}

	/**
	 * 
	 * @return true if all users are done playing the game else false
	 */
	private boolean allFinished() {
		for (TeamPlayerCommunicator teamPlayerCommunicator : teamPlayerCommunicators) {
			if (!teamPlayerCommunicator.geIsFinished()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void handleRunTimeError(Exception e) {
		GameFilesManager.removeData(teamBasedGame, GameFilesManager.getGamesFilePath(),
				GameFilesManager.getGamesFileLock(), Game.class);
		for (int i = 0; i < teams.length; i++) {
			GameFilesManager.removeData(teams[i], GameFilesManager.getTeamsFilePath(),
					GameFilesManager.getTeamsFileLock(), Team.class);
		}
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i).getPlayer() != null) {
				GameFilesManager.removeData(teamPlayerCommunicators.get(i).getPlayer(),
						GameFilesManager.getPlayersFilePath(), GameFilesManager.getPlayersFileLock(), Player.class);
			}
		}
		GameServer.removeThread(this);
	}

}

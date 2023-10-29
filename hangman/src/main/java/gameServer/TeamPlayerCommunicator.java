package gameServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.Massage;
import model.MassageType;
import model.Player;
import model.StatusInGame;
import model.Team;

public class TeamPlayerCommunicator {
	private Socket playerSocket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Player player;
	private long lives = GameServer.configs.get("maxLives");
	private Team[] teams;
	private String hangmanWord;
	/**
	 * the index of the player inside the list of names of the team members
	 */
	private int playerIndex;
	/**
	 * the index of the team in the teams array to which the player belonges to
	 */
	private int teamIndex;
	private boolean isFinished;
	private ArrayList<TeamPlayerCommunicator> teamPlayerCommunicators;

	/**
	 * @param playerSocket
	 * @param teams
	 * @param hangmanWord
	 * @param playerIndex
	 * @param teamIndex
	 * @param isFinished
	 * @param teamPlayerCommunicators
	 */
	public TeamPlayerCommunicator(Socket playerSocket, Team[] teams, String hangmanWord, int playerIndex, int teamIndex,
			boolean isFinished, ArrayList<TeamPlayerCommunicator> teamPlayerCommunicators) {
		this.playerSocket = playerSocket;
		this.teams = teams;
		this.hangmanWord = hangmanWord;
		this.playerIndex = playerIndex;
		this.teamIndex = teamIndex;
		this.isFinished = isFinished;
		this.teamPlayerCommunicators = teamPlayerCommunicators;
		try {
			inputStream = new DataInputStream(playerSocket.getInputStream());
			outputStream = new DataOutputStream(playerSocket.getOutputStream());
			outputStream.writeUTF("wait while other players join");
			String playerUsername = teams[teamIndex].getMembers().get(playerIndex);
			player = new Player(playerUsername, teams[teamIndex].getGameId(), 0, hangmanWord.length(),
					StatusInGame.UNDECIDED);
			GameFilesManager.addData(player, GameFilesManager.getPlayersFilePath(),
					GameFilesManager.getPlayersFileLock(), Player.class);
			teamPlayerCommunicators.add(this);
		} catch (Exception e) {
			try {
				playerSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			teamPlayerCommunicators.remove(this);
			MainMenuThread.disconnectionLogout(player.getName());
		}
	}

	/**
	 * @return the isFinished
	 */
	public boolean geIsFinished() {
		return isFinished;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the teamIndex
	 */
	public int getTeamIndex() {
		return teamIndex;
	}

	/**
	 * 
	 * @param sender
	 * @param reciver
	 * @param gameId
	 * @param hasLost          a falg the indicates if the player that sent that
	 *                         massage has lost
	 * @param previousPosition the position of the previous letter that was guessed
	 *                         by the previous player that played his turn
	 * @param currentPosition  the position of the current letter that is being
	 *                         currently guessed by the current player that plays
	 *                         his turn
	 * @param wrongChars       the list of wrong guesses made by the previous
	 *                         players
	 * @param rightChar        the correct character that was guessed by previous
	 *                         player
	 * @param currentWord      the hangman word that is being guessed by the players
	 *                         with all or some of it's letters hidden
	 * @param winnerTeam
	 * @param defeatedTeam
	 * @param massageType      the type of the massage being set
	 * @return the massage the will be passed to other players
	 */
	private Massage setMessage(String sender, String reciver, long gameId, Boolean hasLost, Integer previousPosition,
			Integer currentPosition, ArrayList<Character> wrongChars, Character rightChar, String currentWord,
			Team winnerTeam, Team defeatedTeam, MassageType massageType) {
		Massage massage = new Massage();
		massage.setSender(sender);
		massage.setReciver(reciver);
		massage.setIsRead(false);
		massage.setType(massageType);
		String content = "";
		content = content.concat("gameId:" + gameId + ",");
		if (hasLost != null) {
			content = content.concat("hasLost:" + hasLost + ",");
		}
		if (previousPosition != null) {
			content = content.concat("previousPosition:" + previousPosition + ",");
		}
		if (currentPosition != null) {
			content = content.concat("currentPosition:" + currentPosition + ",");
		}
		if (wrongChars != null) {
			if (wrongChars.isEmpty()) {
				content = content.concat("wrongChars:,");
			} else {
				String wrongCharList = "";
				for (Character character : wrongChars) {
					wrongCharList = wrongCharList.concat(character + "-");
				}
				char[] Chars = wrongCharList.toCharArray();
				Chars[(wrongCharList.length() - 1)] = ',';
				wrongCharList = String.valueOf(Chars);
				content = content.concat("wrongChars:" + wrongCharList);
			}
		}
		if (rightChar != null) {
			content = content.concat("rightChar:" + rightChar + ",");
		}
		if (currentWord != null) {
			content = content.concat("currentWord:" + currentWord + ",");
		}
		if (winnerTeam != null) {
			content = content.concat("winnerTeam:" + winnerTeam.getName() + ",");
		}
		if (defeatedTeam != null) {
			content = content.concat("defeatedTeam:" + defeatedTeam.getName() + ",");
		}
		massage.setContent(content);
		return massage;
	}

	private Integer getPreviousPosition(Massage massage) {
		Integer previousPosition = null;
		String[] content = massage.getContent().split(",");
		for (int i = 0; i < content.length; i++) {
			if (content[i].contains("previousPosition:")) {
				previousPosition = Integer.parseInt(content[i].split(":")[1]);
				break;
			}
		}
		return previousPosition;
	}

	private Integer getCurrentPosition(Massage massage) {
		Integer currentPosition = null;
		String[] content = massage.getContent().split(",");
		for (int i = 0; i < content.length; i++) {
			if (content[i].contains("currentPosition:")) {
				currentPosition = Integer.parseInt(content[i].split(":")[1]);
				break;
			}
		}
		return currentPosition;
	}

	private ArrayList<Character> getWrongChars(Massage massage) {
		ArrayList<Character> wrongChars = new ArrayList<Character>();
		String[] content = massage.getContent().split(",");
		for (int i = 0; i < content.length; i++) {
			if (content[i].contains("wrongChars:")) {
				if (content[i].split(":").length != 1) {
					String[] wrongCharList = content[i].split(":")[1].split("-");
					for (int j = 0; j < wrongCharList.length; j++) {
						wrongChars.add(wrongCharList[j].charAt(0));
					}
					break;
				}
			}
		}
		return wrongChars;
	}

	private Character getRightChar(Massage massage) {
		Character rightChar = null;
		String[] content = massage.getContent().split(",");
		for (int i = 0; i < content.length; i++) {
			if (content[i].contains("rightChar:")) {
				rightChar = content[i].split(":")[1].charAt(0);
				break;
			}
		}
		return rightChar;
	}

	private String getCurrentWord(Massage massage) {
		String currentWord = null;
		String[] content = massage.getContent().split(",");
		for (int i = 0; i < content.length; i++) {
			if (content[i].contains("currentWord:")) {
				currentWord = content[i].split(":")[1];
				break;
			}
		}
		return currentWord;
	}

	private String getWinnerTeam(Massage massage) {
		String winnerTeam = null;
		String[] content = massage.getContent().split(",");
		for (int i = 0; i < content.length; i++) {
			if (content[i].contains("winnerTeam:")) {
				winnerTeam = content[i].split(":")[1];
				break;
			}
		}
		return winnerTeam;
	}

	private String getDefeatedTeam(Massage massage) {
		String defeatedTeam = null;
		String[] content = massage.getContent().split(",");
		for (int i = 0; i < content.length; i++) {
			if (content[i].contains("defeatedTeam:")) {
				defeatedTeam = content[i].split(":")[1];
				break;
			}
		}
		return defeatedTeam;
	}

	/**
	 * 
	 * @return true if all the other team members of the player have lost or if
	 *         number of team members is 1 else returns false
	 */
	private boolean otherTeamMembersHaveLost() {
		List<Massage> massages = GameFilesManager.getData(GameFilesManager.getMassagesFilePath(),
				GameFilesManager.getMassagesFileLock(), Massage.class);
		String[] teamMemberNames = new String[(teams[teamIndex].getSize() - 1)];
		boolean[] haveLost = new boolean[(teams[teamIndex].getSize() - 1)];
		if (teamMemberNames.length == 1) {
			return true;
		}
		/**
		 * get the names of the player's team members beside himself
		 */
		for (int i = 0, j = 0; i < teams[teamIndex].getSize(); i++) {
			if (i == playerIndex) {
				continue;
			}
			teamMemberNames[j] = teams[teamIndex].getMembers().get(i);
			haveLost[j] = false;
			j++;
		}
		// for each team member
		for (int i = 0; i < teamMemberNames.length; i++) {
			// cheak all the massages
			for (int j = 0; j < massages.size(); j++) {
				// if there is a massage that says that the team member has lost then mark him
				// as having lost in the haveLost array
				if (massages.get(j).getSender().equals(teamMemberNames[i])
						&& massages.get(j).getType().equals(MassageType.TEAM_STATUS_MASSAGE)
						&& massages.get(j).getContent().contains("gameId:" + player.getGameId())
						&& massages.get(j).getContent().contains("hasLost:" + true)) {
					if (!massages.get(j).getIsRead()) {
						massages.get(j).setIsRead(true);
						GameFilesManager.modifyData(massages.get(j), GameFilesManager.getMassagesFilePath(),
								GameFilesManager.getMassagesFileLock(), Massage.class);
					}
					haveLost[i] = true;
					break;
				}
			}
		}
		// all other team members have lost return true else return false
		for (boolean b : haveLost) {
			if (b == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param rightChar the right charchter guessed by the previous player
	 * @param position  the position of the right character
	 */
	private void informOfCorrectAnswer(char rightChar, int position) {
		// for each player in the game beside yourself
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i) == this) {
				continue;
			}
			// send a massage that you have picked the right answer then tell other players
			// to see it
			Massage massage = setMessage(player.getName(), teamPlayerCommunicators.get(i).getPlayer().getName(),
					player.getGameId(), null, position, null, null, rightChar, null, null, null,
					MassageType.CORRECT_GUESS_MASSAGE);
			GameFilesManager.addData(massage, GameFilesManager.getMassagesFilePath(),
					GameFilesManager.getMassagesFileLock(), Massage.class);
			teamPlayerCommunicators.get(i).getCorrectAnswer();
		}
	}

	/**
	 * gets the right character and it's position and the player the guessed it
	 */
	public void getCorrectAnswer() {
		List<Massage> massages = GameFilesManager.getData(GameFilesManager.getMassagesFilePath(),
				GameFilesManager.getMassagesFileLock(), Massage.class);
		// for each massage if this massage is the massage containing the correct guesss
		// character
		for (int i = 0; i < massages.size(); i++) {
			if (massages.get(i).getReciver().equals(player.getName())
					&& massages.get(i).getContent().contains("gameId:" + player.getGameId())
					&& massages.get(i).getType().equals(MassageType.CORRECT_GUESS_MASSAGE)
					&& massages.get(i).getContent().contains("rightChar:") && !massages.get(i).getIsRead()) {
				massages.get(i).setIsRead(true);
				GameFilesManager.modifyData(massages.get(i), GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
				// if the player reciving this massage is not finished playing
				if (!isFinished) {
					String playerName = massages.get(i).getSender();
					Integer position = getPreviousPosition(massages.get(i));
					Character rightChar = getRightChar(massages.get(i));
					try {
						outputStream.writeUTF(playerName + " picked the correct Character at postion " + (position + 1)
								+ ": " + rightChar);
					} catch (IOException e) {
						e.printStackTrace();
						try {
							playerSocket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						MainMenuThread.disconnectionLogout(player.getName());
					}
				}
				break;
			}
		}
	}

	/**
	 * sends a massage to all the other teams that the player's team has lost
	 */
	private void informOfDefeatedTeam() {
		// mark this team as defeted and send a massage about this to all other players
		// and tell them to read it
		teams[teamIndex].setStatus(StatusInGame.LOSS);
		GameFilesManager.modifyData(teams[teamIndex], GameFilesManager.getTeamsFilePath(),
				GameFilesManager.getTeamsFileLock(), Team.class);
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i).getTeamIndex() != teamIndex) {
				continue;
			}
			if (!teamPlayerCommunicators.get(i).getPlayer().getStatus().equals(StatusInGame.LOSS)) {
				teamPlayerCommunicators.get(i).getPlayer().setStatus(StatusInGame.LOSS);
				GameFilesManager.modifyData(teamPlayerCommunicators.get(i).getPlayer(),
						GameFilesManager.getPlayersFilePath(), GameFilesManager.getPlayersFileLock(), Player.class);
			}
		}
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i).getTeamIndex() == teamIndex) {
				continue;
			}
			Massage massage = setMessage(player.getName(), teamPlayerCommunicators.get(i).getPlayer().getName(),
					player.getGameId(), true, null, null, null, null, null, null, teams[teamIndex],
					MassageType.TEAM_STATUS_MASSAGE);
			GameFilesManager.addData(massage, GameFilesManager.getMassagesFilePath(),
					GameFilesManager.getMassagesFileLock(), Massage.class);
			teamPlayerCommunicators.get(i).getDefeatedTeam();
		}
	}

	/**
	 * gets the massage of the defeat of the other team
	 */
	public void getDefeatedTeam() {
		List<Massage> massages = GameFilesManager.getData(GameFilesManager.getMassagesFilePath(),
				GameFilesManager.getMassagesFileLock(), Massage.class);
		for (int i = 0; i < massages.size(); i++) {
			if (massages.get(i).getReciver().equals(player.getName())
					&& massages.get(i).getContent().contains("gameId:" + player.getGameId())
					&& massages.get(i).getType().equals(MassageType.TEAM_STATUS_MASSAGE)
					&& massages.get(i).getContent().contains("defeatedTeam:") && !massages.get(i).getIsRead()) {
				massages.get(i).setIsRead(true);
				GameFilesManager.modifyData(massages.get(i), GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
				if (!isFinished) {
					String defeatedTeam = getDefeatedTeam(massages.get(i));
					try {
						outputStream.writeUTF("team " + defeatedTeam + " has just lost");
					} catch (IOException e) {
						e.printStackTrace();
						try {
							playerSocket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						MainMenuThread.disconnectionLogout(player.getName());
					}
				}
				break;
			}
		}
	}

	/**
	 * sends a massage to all the other players the the player's team has won
	 */
	private void informOfWinnerTeam() {
		teams[teamIndex].setStatus(StatusInGame.WIN);
		GameFilesManager.modifyData(teams[teamIndex], GameFilesManager.getTeamsFilePath(),
				GameFilesManager.getTeamsFileLock(), Team.class);
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i).getTeamIndex() != teamIndex) {
				continue;
			}
			if (!teamPlayerCommunicators.get(i).getPlayer().getStatus().equals(StatusInGame.WIN)) {
				teamPlayerCommunicators.get(i).getPlayer().setStatus(StatusInGame.WIN);
				GameFilesManager.modifyData(teamPlayerCommunicators.get(i).getPlayer(),
						GameFilesManager.getPlayersFilePath(), GameFilesManager.getPlayersFileLock(), Player.class);
			}
		}
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i) == this) {
				continue;
			}
			Massage massage = setMessage(player.getName(), teamPlayerCommunicators.get(i).getPlayer().getName(),
					player.getGameId(), null, null, null, null, null, null, teams[teamIndex], null,
					MassageType.TEAM_STATUS_MASSAGE);
			GameFilesManager.addData(massage, GameFilesManager.getMassagesFilePath(),
					GameFilesManager.getMassagesFileLock(), Massage.class);
			teamPlayerCommunicators.get(i).getWinnerTeam();
		}
	}

	/**
	 * gets the massage of the wining of the other team
	 */
	public void getWinnerTeam() {
		List<Massage> massages = GameFilesManager.getData(GameFilesManager.getMassagesFilePath(),
				GameFilesManager.getMassagesFileLock(), Massage.class);
		for (int i = 0; i < massages.size(); i++) {
			if (massages.get(i).getReciver().equals(player.getName())
					&& massages.get(i).getContent().contains("gameId:" + player.getGameId())
					&& massages.get(i).getType().equals(MassageType.TEAM_STATUS_MASSAGE)
					&& massages.get(i).getContent().contains("winnerTeam:") && !massages.get(i).getIsRead()) {
				massages.get(i).setIsRead(true);
				GameFilesManager.modifyData(massages.get(i), GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
				String winnerTeam = getWinnerTeam(massages.get(i));
				if (!isFinished) {
					try {
						if (winnerTeam.equals(teams[teamIndex].getName())) {
							outputStream.writeUTF("your team has won congrats");
						} else {
							outputStream.writeUTF("your team has lost better luck next time");
						}
						MainMenuThread thread = new MainMenuThread(playerSocket, player.getName(), true);
						thread.start();
					} catch (IOException e) {
						e.printStackTrace();
						try {
							playerSocket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						MainMenuThread.disconnectionLogout(player.getName());
					}
				}
				if (!teams[teamIndex].getName().equals(winnerTeam)) {
					if (!teams[teamIndex].getStatus().equals(StatusInGame.LOSS)) {
						teams[teamIndex].setStatus(StatusInGame.LOSS);
						GameFilesManager.modifyData(teams[teamIndex], GameFilesManager.getTeamsFilePath(),
								GameFilesManager.getTeamsFileLock(), Team.class);
					}
					for (int j = 0; j < teamPlayerCommunicators.size(); j++) {
						if (teamPlayerCommunicators.get(j).getTeamIndex() == teamIndex) {
							if (!teamPlayerCommunicators.get(j).getPlayer().getStatus().equals(StatusInGame.LOSS)) {
								teamPlayerCommunicators.get(j).getPlayer().setStatus(StatusInGame.LOSS);
								GameFilesManager.modifyData(teamPlayerCommunicators.get(j).getPlayer(),
										GameFilesManager.getPlayersFilePath(), GameFilesManager.getPlayersFileLock(),
										Player.class);
							}
						}
					}

				}
				break;
			}
		}
		isFinished = true;
	}

	/**
	 * sends a massage to all the player's team members that he has lost
	 */
	private void informOfDefeatedPlayer() {
		player.setStatus(StatusInGame.LOSS);
		GameFilesManager.modifyData(player, GameFilesManager.getPlayersFilePath(),
				GameFilesManager.getPlayersFileLock(), Player.class);
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i).getTeamIndex() == teamIndex && teamPlayerCommunicators.get(i) != this) {
				Massage massage = setMessage(player.getName(), teamPlayerCommunicators.get(i).getPlayer().getName(),
						player.getGameId(), true, null, null, null, null, null, null, null,
						MassageType.TEAM_STATUS_MASSAGE);
				GameFilesManager.addData(massage, GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
			}
		}
	}

	/**
	 * 
	 * @param previousPosition
	 * @param currentPosition
	 * @param wrongChars
	 * @param currentWord
	 */
	private void passNextTurnInfo(Integer previousPosition, Integer currentPosition, ArrayList<Character> wrongChars,
			String currentWord) {
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i) == this) {
				if (i == (teamPlayerCommunicators.size() - 1)) {
					Massage massage = setMessage(player.getName(), teamPlayerCommunicators.get(0).getPlayer().getName(),
							player.getGameId(), null, previousPosition, currentPosition, wrongChars, null, currentWord,
							null, null, MassageType.NEXT_TURN_MASSAGE);
					GameFilesManager.addData(massage, GameFilesManager.getMassagesFilePath(),
							GameFilesManager.getMassagesFileLock(), Massage.class);
				} else {
					Massage massage = setMessage(player.getName(),
							teamPlayerCommunicators.get((i + 1)).getPlayer().getName(), player.getGameId(), null,
							previousPosition, currentPosition, wrongChars, null, currentWord, null, null,
							MassageType.NEXT_TURN_MASSAGE);
					GameFilesManager.addData(massage, GameFilesManager.getMassagesFilePath(),
							GameFilesManager.getMassagesFileLock(), Massage.class);
				}
				break;
			}
		}
	}

	/**
	 * @return the next player that will play his turn after the current player
	 */
	private Player getNextPlayer() {
		for (int i = 0; i < teamPlayerCommunicators.size(); i++) {
			if (teamPlayerCommunicators.get(i) == this) {
				if (i == (teamPlayerCommunicators.size() - 1)) {
					return teamPlayerCommunicators.get(0).getPlayer();
				} else {
					return teamPlayerCommunicators.get((i + 1)).getPlayer();
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @return the massage that was sent to the current player
	 */
	private Massage getNextTurnMassage() {
		List<Massage> massages = GameFilesManager.getData(GameFilesManager.getMassagesFilePath(),
				GameFilesManager.getMassagesFileLock(), Massage.class);
		for (int i = 0; i < massages.size(); i++) {
			if (massages.get(i).getReciver().equals(player.getName())
					&& massages.get(i).getContent().contains("gameId:" + player.getGameId())
					&& massages.get(i).getType().equals(MassageType.NEXT_TURN_MASSAGE)
					&& massages.get(i).getContent().contains("currentWord:")
					&& massages.get(i).getContent().contains("wrongChars:") && !massages.get(i).getIsRead()) {
				return massages.get(i);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param currentWord
	 * @param playerScore
	 * @param lives
	 * @param previousPosition
	 * @param wrongGuessChars
	 * @return a string that will be sent to the player at the client side
	 */
	private String preparePlayerMessage(String currentWord, int playerScore, long lives, int previousPosition,
			ArrayList<Character> wrongGuessChars) {
		String massage = "guess the the word: " + currentWord + "\n";
		massage = massage.concat("current score: " + playerScore + "\n");
		massage = massage.concat("remaining lives: " + lives + "\n");
		massage = massage.concat("wrong guesses at position " + (previousPosition + 1) + ": ");
		for (int i = 0; i < wrongGuessChars.size(); i++) {
			massage = massage.concat(wrongGuessChars.get(i).toString() + " ");
		}
		massage = massage.concat("\n");
		massage = massage.concat("your next guess: ");
		return massage;
	}

	public void startPlayerTurn() {
		int currentPosition = 0;
		int previousPosition = 0;
		ArrayList<Character> wrongChars = null;
		String currentWord = null;
		Massage massage = null;
		boolean alreadyPassedTurn = false;
		if (isFinished) {
			massage = getNextTurnMassage();
			massage.setReciver(getNextPlayer().getName());
			GameFilesManager.modifyData(massage, GameFilesManager.getMassagesFilePath(),
					GameFilesManager.getMassagesFileLock(), Massage.class);
			return;
		}
		try {
			if (playerSocket.isClosed()) {
				isFinished = true;
				if (otherTeamMembersHaveLost()) {
					informOfDefeatedTeam();
				} else {
					informOfDefeatedPlayer();
				}
				massage = getNextTurnMassage();
				massage.setReciver(getNextPlayer().getName());
				GameFilesManager.modifyData(massage, GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
				MainMenuThread.disconnectionLogout(player.getName());
				return;
			}
			massage = getNextTurnMassage();
			// if this the first turn in the game
			if (massage == null) {
				currentPosition = 0;
				previousPosition = 0;
				wrongChars = new ArrayList<Character>();
				currentWord = "";
				for (int i = 0; i < hangmanWord.length(); i++) {
					currentWord = currentWord.concat("_");
				}
			} else {
				currentPosition = getCurrentPosition(massage);
				previousPosition = getPreviousPosition(massage);
				wrongChars = getWrongChars(massage);
				currentWord = getCurrentWord(massage);
				massage.setIsRead(true);
				GameFilesManager.modifyData(massage, GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
			}
			String playerMessage = preparePlayerMessage(currentWord, player.getScore(), lives, previousPosition,
					wrongChars);
			outputStream.writeUTF(playerMessage);
			char nextChar = inputStream.readChar();
			// if the user hasn't lost yet
			if (lives != 0) {
				// if he picked the correct guess and he still has to guess the the rest of the
				// letters
				if (currentPosition != (hangmanWord.length() - 1) && nextChar == hangmanWord.charAt(currentPosition)) {
					player.setScore((player.getScore() + 1));
					Character righCharacter = nextChar;
					char[] Chars = currentWord.toCharArray();
					Chars[currentPosition] = righCharacter.charValue();
					currentWord = String.valueOf(Chars);
					informOfCorrectAnswer(righCharacter, currentPosition);
					GameFilesManager.modifyData(player, GameFilesManager.getPlayersFilePath(),
							GameFilesManager.getPlayersFileLock(), Player.class);
					currentPosition++;
					previousPosition++;
					wrongChars = new ArrayList<Character>();
					passNextTurnInfo(previousPosition, currentPosition, wrongChars, currentWord);
					alreadyPassedTurn = true;
					outputStream.writeUTF("correct guess wait while other players finish their turn");
					// if he picked the wrong guess and he still has to guess the the rest of the
					// letters
				} else if (currentPosition != (hangmanWord.length() - 1)
						&& nextChar != hangmanWord.charAt(currentPosition)
						|| (currentPosition == (hangmanWord.length() - 1)
								&& nextChar != hangmanWord.charAt(currentPosition))) {
					lives--;
					if (lives != 0) {
						wrongChars.add(nextChar);
						passNextTurnInfo(previousPosition, currentPosition, wrongChars, currentWord);
						alreadyPassedTurn = true;
						outputStream.writeUTF("wrong guess wait while other players finish their turn");
					}
					// if he correctly guessed all the letters of the of the hangman word
				} else if (currentPosition == (hangmanWord.length() - 1)
						&& nextChar == hangmanWord.charAt(currentPosition)) {
					player.setScore((player.getScore() + 1));
					informOfWinnerTeam();
					isFinished = true;
					outputStream.writeUTF("your team has won congrats");
					MainMenuThread thread = new MainMenuThread(playerSocket, player.getName(), true);
					thread.start();
				}
			}
			// if he used up all his lives
			if (lives == 0) {
				isFinished = true;
				wrongChars.add(nextChar);
				if (otherTeamMembersHaveLost()) {
					informOfDefeatedTeam();
					outputStream
							.writeUTF("you have used up all your lives and your team has lost better luck next time");
				} else {
					informOfDefeatedPlayer();
					outputStream.writeUTF("you have used up all your lives but your team stil has a chance to win");
				}
				passNextTurnInfo(previousPosition, currentPosition, wrongChars, currentWord);
				alreadyPassedTurn = true;
				MainMenuThread thread = new MainMenuThread(playerSocket, player.getName(), true);
				thread.start();
			}
		} catch (Exception e) {
			isFinished = true;
			if (player.getStatus().equals(StatusInGame.LOSS) || player.getStatus().equals(StatusInGame.WIN)) {
				MainMenuThread.disconnectionLogout(player.getName());
				return;
			}
			if (otherTeamMembersHaveLost()) {
				informOfDefeatedTeam();
			} else {
				informOfDefeatedPlayer();
			}
			if (massage == null && alreadyPassedTurn == false) {
				passNextTurnInfo(previousPosition, currentPosition, wrongChars, currentWord);
				MainMenuThread.disconnectionLogout(player.getName());
				return;
			} else if (massage != null && alreadyPassedTurn == false) {
				massage.setReciver(getNextPlayer().getName());
				massage.setIsRead(false);
				GameFilesManager.modifyData(massage, GameFilesManager.getMassagesFilePath(),
						GameFilesManager.getMassagesFileLock(), Massage.class);
				MainMenuThread.disconnectionLogout(player.getName());
				return;
			}

		}
	}
}

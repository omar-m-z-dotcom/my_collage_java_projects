package gameServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import interfaces.ConstructErrorInterface;
import interfaces.RunTimeErrorInterface;
import model.Game;
import model.GameType;
import model.Player;
import model.StatusInGame;

/**
 * 
 * handels the single game logic
 *
 */
public class SingleGameThread extends Thread implements ConstructErrorInterface, RunTimeErrorInterface {
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Player player;
	private Game singlePlayerGame;

	/**
	 * @param socket     the player socket
	 * @param playerName
	 */
	public SingleGameThread(Socket socket, String playerName) {
		this.socket = socket;
		player = new Player(playerName, 0, 0, 0, null);
		try {
			this.inputStream = new DataInputStream(socket.getInputStream());
			this.outputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			handleConstructError(e);
		}
		long gameId = GameServer.getGameId();
		player = new Player(playerName, gameId, 0, 0, StatusInGame.UNDECIDED);
		singlePlayerGame = new Game(gameId, GameType.SINGLE_PLAYER_GAME, 1, 0, false, true);	
		GameFilesManager.addData(player, GameFilesManager.getPlayersFilePath(), GameFilesManager.getPlayersFileLock(),
				Player.class);
		GameFilesManager.addData(singlePlayerGame, GameFilesManager.getGamesFilePath(),
				GameFilesManager.getGamesFileLock(), Game.class);
	}

	@Override
	public void handleConstructError(Exception e) {
		e.printStackTrace();
		try {
			socket.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MainMenuThread.disconnectionLogout(player.getName());

	}

	@Override
	public void handleRunTimeError(Exception e) {
		e.printStackTrace();
		if (!Objects.isNull(e.getMessage()) && e.getMessage().equalsIgnoreCase("can't connect to lookup server")) {
			GameFilesManager.removeData(player, GameFilesManager.getPlayersFilePath(),
					GameFilesManager.getPlayersFileLock(), Player.class);
			GameFilesManager.removeData(singlePlayerGame, GameFilesManager.getGamesFilePath(),
					GameFilesManager.getGamesFileLock(), Game.class);
			try {
				outputStream.writeUTF("failure in lookup server (game canceled)");
				MainMenuThread thread = new MainMenuThread(socket, player.getName(), true);
				thread.start();
				return;
			} catch (Exception e1) {
				e1.printStackTrace();
				try {
					socket.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				MainMenuThread.disconnectionLogout(player.getName());
				return;
			}
		}
		try {
			socket.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		player.setStatus(StatusInGame.LOSS);
		singlePlayerGame.setIsFinished(true);
		GameFilesManager.modifyData(player, GameFilesManager.getPlayersFilePath(),
				GameFilesManager.getPlayersFileLock(), Player.class);
		GameFilesManager.modifyData(singlePlayerGame, GameFilesManager.getGamesFilePath(),
				GameFilesManager.getGamesFileLock(), Game.class);
		MainMenuThread.disconnectionLogout(player.getName());
		return;
	}

	/**
	 * 
	 * @param currentWord
	 * @param playerScore
	 * @param lives
	 * @param position
	 * @param wrongGuessChars
	 * @param rightGuessChar
	 * @return the massage that will be passed to the player
	 */
	private String setMessage(String currentWord, int playerScore, long lives, int position,
			ArrayList<Character> wrongGuessChars, Character rightGuessChar) {
		String massage = "guess the the word: " + currentWord + "\n";
		massage = massage.concat("current score: " + playerScore + "\n");
		massage = massage.concat("remaining lives: " + lives + "\n");
		massage = massage.concat("wrong guesses at position " + (position + 1) + ": ");
		for (int i = 0; i < wrongGuessChars.size(); i++) {
			massage = massage.concat(wrongGuessChars.get(i).toString() + " ");
		}
		massage = massage.concat("\n");
		massage = massage.concat("right guess at position " + (position + 1) + ": ");
		if (rightGuessChar != null) {
			massage = massage.concat(rightGuessChar.toString());
		}
		massage = massage.concat("\n");
		massage = massage.concat("your next guess: ");
		return massage;
	}

	public void startGame() throws Exception {
		String hangmanWord = HangmanWordGetter.getHangmanWord();
		if (hangmanWord == null) {
			throw new Exception("can't connect to lookup server");
		}
		player.setHangmanWordLength(hangmanWord.length());
		GameFilesManager.modifyData(player, GameFilesManager.getPlayersFilePath(),
				GameFilesManager.getPlayersFileLock(), Player.class);
		int playerScore = 0;
		int hangmanWordLength = hangmanWord.length();
		long lives = GameServer.configs.get("maxLives").longValue();
		int position = 0;
		ArrayList<Character> wrongGuessChars = new ArrayList<Character>();
		Character rightGuessChar = null;
		String currentWord = "";
		for (int i = 0; i < hangmanWordLength; i++) {
			currentWord = currentWord.concat("_");
		}
		String massage = setMessage(currentWord, playerScore, lives, position, wrongGuessChars, rightGuessChar);
		outputStream.writeUTF(massage);
		while (true) {
			// if the user hasn't lost yet
			if (lives != 0) {
				char nextChar = inputStream.readChar();
				// if he picked the correct guess and he still has to guess the the rest of the
				// letters
				if (position != (hangmanWord.length() - 1) && nextChar == hangmanWord.charAt(position)) {
					playerScore++;
					rightGuessChar = nextChar;
					char[] Chars = currentWord.toCharArray();
					Chars[position] = rightGuessChar.charValue();
					currentWord = String.valueOf(Chars);
					massage = setMessage(currentWord, playerScore, lives, position, wrongGuessChars, rightGuessChar);
					outputStream.writeUTF(massage);
					player.setScore(playerScore);
					GameFilesManager.modifyData(player, GameFilesManager.getPlayersFilePath(),
							GameFilesManager.getPlayersFileLock(), Player.class);
					position++;
					wrongGuessChars = new ArrayList<Character>();
					continue;
					// if he picked the wrong guess and he still has to guess the the rest of the
					// letters
				} else if (position != (hangmanWord.length() - 1) && nextChar != hangmanWord.charAt(position)
						|| (position == (hangmanWord.length() - 1) && nextChar != hangmanWord.charAt(position))) {
					lives--;
					if (lives == 0) {
						continue;
					}
					wrongGuessChars.add(nextChar);
					massage = setMessage(currentWord, playerScore, lives, position, wrongGuessChars, rightGuessChar);
					outputStream.writeUTF(massage);
					continue;
					// if he correctly guessed all the letters of the of the hangman word
				} else if (position == (hangmanWord.length() - 1) && nextChar == hangmanWord.charAt(position)) {
					playerScore++;
					player.setScore(playerScore);
					player.setStatus(StatusInGame.WIN);
					singlePlayerGame.setIsFinished(true);
					GameFilesManager.modifyData(player, GameFilesManager.getPlayersFilePath(),
							GameFilesManager.getPlayersFileLock(), Player.class);
					GameFilesManager.modifyData(singlePlayerGame, GameFilesManager.getGamesFilePath(),
							GameFilesManager.getGamesFileLock(), Game.class);
					massage = "you won congrates";
					outputStream.writeUTF(massage);
					break;
				}
				// if he used up all his lives
			} else {
				player.setScore(playerScore);
				player.setStatus(StatusInGame.LOSS);
				singlePlayerGame.setIsFinished(true);
				GameFilesManager.modifyData(player, GameFilesManager.getPlayersFilePath(),
						GameFilesManager.getPlayersFileLock(), Player.class);
				GameFilesManager.modifyData(singlePlayerGame, GameFilesManager.getGamesFilePath(),
						GameFilesManager.getGamesFileLock(), Game.class);
				massage = "you lost better luck next time";
				outputStream.writeUTF(massage);
				break;
			}

		}
		MainMenuThread thread = new MainMenuThread(socket, player.getName(), true);
		thread.start();
	}

	@Override
	public void run() {
		try {
			if (socket.isClosed()) {
				return;
			}
			startGame();
		} catch (Exception e) {
			handleRunTimeError(e);
		}
	}

}

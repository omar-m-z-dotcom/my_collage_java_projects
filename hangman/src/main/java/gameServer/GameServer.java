package gameServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class GameServer {

	//the list of team game threads stored in game server
	private static ArrayList<Thread> threads = new ArrayList<Thread>();
	//the game server configrations
	public static HashMap<String, Long> configs = GameFilesManager.getConfigs();

	/**
	 * 
	 * @param thread the thread to add to the list of team game threads
	 */
	public static synchronized void addThread(Thread thread) {
		threads.add(thread);
	}

	/**
	 * 
	 * @param thread the thread to remove from the list of team game threads
	 */
	public static synchronized void removeThread(Thread thread) {
		threads.remove(thread);
	}

	/**
	 * 
	 * @return the id of the next game
	 */
	public static synchronized long getGameId() {
		long gameId = configs.get("nextGameId").longValue();
		configs.put("nextGameId", (gameId + 1));
		return gameId;
	}

	/**
	 * 
	 * @param gameId the game id of the TeamGameThread
	 * @return TeamGameThread with the given game id if not found returns null
	 */
	public static synchronized Thread searchTeamGameThread(long gameId) {
		for (int i = 0; i < threads.size(); i++) {
			if (threads.get(i) instanceof TeamGameThread) {
				if (((TeamGameThread) threads.get(i)).getGameId() == gameId) {
					return threads.get(i);
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(11111);
			while (true) {
				Socket socket = serverSocket.accept();
				MainMenuThread thread = new MainMenuThread(socket, null, false);
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				serverSocket.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println("failed to start");
		}
	}

}

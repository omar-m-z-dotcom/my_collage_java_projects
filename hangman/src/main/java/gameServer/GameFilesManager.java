package gameServer;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import model.Game;
import model.User;

/**
 * 
 * <b>manages access to the game server files</b>
 *
 */
public class GameFilesManager {
	// the list of game server file paths and their locks that are used for file
	// access concurrency
	private static String configFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\config.csv";
	private static String gamesFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\games.csv";
	private static String playersFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\players.csv";
	private static String usersFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\users.csv";
	private static String teamsFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\teams.csv";
	private static String massagesFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\massages.csv";
	private static ReadWriteLock gamesFileLock = new ReentrantReadWriteLock(true);
	private static ReadWriteLock playersFileLock = new ReentrantReadWriteLock(true);
	private static ReadWriteLock usersFileLock = new ReentrantReadWriteLock(true);
	private static ReadWriteLock teamsFileLock = new ReentrantReadWriteLock(true);
	private static ReadWriteLock massagesFileLock = new ReentrantReadWriteLock(true);

	/**
	 * @return the gamesFilePath
	 */
	public static String getGamesFilePath() {
		return gamesFilePath;
	}

	/**
	 * @return the playersFilePath
	 */
	public static String getPlayersFilePath() {
		return playersFilePath;
	}

	/**
	 * @return the usersFilePath
	 */
	public static String getUsersFilePath() {
		return usersFilePath;
	}

	/**
	 * @return the teamsFilePath
	 */
	public static String getTeamsFilePath() {
		return teamsFilePath;
	}

	/**
	 * @return the massagesFilePath
	 */
	public static String getMassagesFilePath() {
		return massagesFilePath;
	}

	/**
	 * @return the gamesFileLock
	 */
	public static ReadWriteLock getGamesFileLock() {
		return gamesFileLock;
	}

	/**
	 * @return the playersFileLock
	 */
	public static ReadWriteLock getPlayersFileLock() {
		return playersFileLock;
	}

	/**
	 * @return the usersFileLock
	 */
	public static ReadWriteLock getUsersFileLock() {
		return usersFileLock;
	}

	/**
	 * @return the teamsFileLock
	 */
	public static ReadWriteLock getTeamsFileLock() {
		return teamsFileLock;
	}

	/**
	 * @return the massagesFileLock
	 */
	public static ReadWriteLock getMassagesFileLock() {
		return massagesFileLock;
	}

	/**
	 * 
	 * @return the game server configurations in a hashmap
	 */
	public static HashMap<String, Long> getConfigs() {
		try {
			List<String[]> configEntries = new CSVReaderBuilder(new FileReader(configFilePath)).build().readAll();
			String[] configHeaders = configEntries.get(0);
			String[] configValues = configEntries.get(1);
			HashMap<String, Long> configs = new HashMap<String, Long>();
			for (int i = 0; i < configValues.length; i++) {
				configs.put(configHeaders[i], Long.parseLong(configValues[i]));
			}
			// reads the id of the last game from the games file
			List<Game> games = new CsvToBeanBuilder<Game>(new FileReader(gamesFilePath)).withType(Game.class).build()
					.parse();
			if (games.size() == 0) {
				configs.put("nextGameId", (long) 1);
			} else {
				long nextGameId = 0;
				for (Game game : games) {
					if (nextGameId < game.getId()) {
						nextGameId = game.getId();
					}
				}
				nextGameId++;
				configs.put("nextGameId", nextGameId);
			}
			return configs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param <T>         the type of data being retrieved
	 * @param filePath
	 * @param fileLock
	 * @param objectClass the class of the object being retrieved
	 * @return a list of the retrieved data
	 */
	public static <T> List<T> getData(String filePath, ReadWriteLock fileLock, Class<? extends T> objectClass) {
		try {
			// get the file read lock and lock it for reading
			Lock readLock = fileLock.readLock();
			readLock.lock();
			// get the mapping Strategy to map the retrieved data class fields to the
			// headers of the csv file and set the type of object it's dealing with
			HeaderColumnNameMappingStrategy<T> ms = new HeaderColumnNameMappingStrategy<T>();
			ms.setType(objectClass);
			// read the data
			List<T> data = new CsvToBeanBuilder<T>(new FileReader(filePath)).withType(objectClass)
					.withMappingStrategy(ms).build().parse();
			// unlock the read lock then return the data
			readLock.unlock();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param <T>         the type of data being added
	 * @param dataToAdd
	 * @param filePath
	 * @param fileLock
	 * @param objectClass the class of the object being added
	 * @return true if the data was added successfully else false
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean addData(T dataToAdd, String filePath, ReadWriteLock fileLock,
			Class<? extends T> objectClass) {
		try {
			// get the file write lock and lock it for write
			Lock writeLock = fileLock.writeLock();
			writeLock.lock();
			List<T> data = null;
			// flag that specifies whether the data has already been read
			boolean dataIsRead = false;
			// if the data being added is of type User
			if (dataToAdd instanceof User) {
				dataIsRead = true;
				User userToAdd = (User) dataToAdd;
				// read the User data and if another user has the same username return false
				List<User> users = new CsvToBeanBuilder<User>(new FileReader(filePath)).withType(User.class).build()
						.parse();
				for (User user1 : users) {
					if (user1.getUsername().equals(userToAdd.getUsername())) {
						return false;
					}
				}
				data = (List<T>) users;
			}
			HeaderColumnNameMappingStrategy<T> ms = new HeaderColumnNameMappingStrategy<T>();
			ms.setType(objectClass);
			if (!dataIsRead) {
				data = new CsvToBeanBuilder<T>(new FileReader(filePath)).withType(objectClass).withMappingStrategy(ms)
						.build().parse();
			}
			// add the dataToAdd to the end of the list existing then write out the list of
			// data
			data.add(dataToAdd);
			FileWriter writer = new FileWriter(filePath);
			new StatefulBeanToCsvBuilder<T>(writer).withMappingStrategy(ms).build().write(data);
			// unlock the write lock then return the true
			writer.close();
			writeLock.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 * @param <T>         the type of data being modified
	 * @param newData
	 * @param filePath
	 * @param fileLock
	 * @param objectClass the class of the object being modified
	 */
	public static <T extends Comparable<T>> void modifyData(T newData, String filePath, ReadWriteLock fileLock,
			Class<? extends T> objectClass) {
		try {
			Lock writeLock = fileLock.writeLock();
			writeLock.lock();
			HeaderColumnNameMappingStrategy<T> ms = new HeaderColumnNameMappingStrategy<T>();
			ms.setType(objectClass);
			List<T> data = new CsvToBeanBuilder<T>(new FileReader(filePath)).withType(objectClass)
					.withMappingStrategy(ms).build().parse();
			// for each data row if this row is the row we want to update
			for (int i = 0; i < data.size(); i++) {
				if (newData.compareTo(data.get(i)) == 1) {
					data.set(i, newData);
					break;
				}
			}
			FileWriter writer = new FileWriter(filePath);
			new StatefulBeanToCsvBuilder<T>(writer).withMappingStrategy(ms).build().write(data);
			writer.close();
			writeLock.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param <T>         the type of data being removed
	 * @param newData
	 * @param filePath
	 * @param fileLock
	 * @param objectClass the class of the object being removed
	 */
	public static <T extends Comparable<T>> void removeData(T newData, String filePath, ReadWriteLock fileLock,
			Class<? extends T> objectClass) {
		try {
			Lock writeLock = fileLock.writeLock();
			writeLock.lock();
			HeaderColumnNameMappingStrategy<T> ms = new HeaderColumnNameMappingStrategy<T>();
			ms.setType(objectClass);
			List<T> data = new CsvToBeanBuilder<T>(new FileReader(filePath)).withType(objectClass)
					.withMappingStrategy(ms).build().parse();
			// for each data row if this row is the row we want to remove
			for (int i = 0; i < data.size(); i++) {
				if (newData.compareTo(data.get(i)) == 1) {
					data.remove(i);
					break;
				}
			}
			FileWriter writer = new FileWriter(filePath);
			new StatefulBeanToCsvBuilder<T>(writer).withMappingStrategy(ms).build().write(data);
			writer.close();
			writeLock.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
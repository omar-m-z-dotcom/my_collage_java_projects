package gameServer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import model.Game;

public class GamesFileManager {
	private static String file = System.getProperty("user.dir") + "\\src\\main\\resources\\games.csv";
	private static ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

	public static List<Game> getGames() {
		Lock readLock = rwLock.readLock();
		readLock.lock();
		try {
			List<Game> games = new CsvToBeanBuilder<Game>(new FileReader(file)).withType(Game.class).build().parse();
			readLock.unlock();
			return games;
		} catch (IllegalStateException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void addGame(Game game) {
		Lock writeLock = rwLock.writeLock();
		writeLock.lock();
		try {
			List<Game> games = new CsvToBeanBuilder<Game>(new FileReader(file)).withType(Game.class).build().parse();
			games.add(game);
			FileWriter writer = new FileWriter(file);
			StatefulBeanToCsv<Game> beanToCsv = new StatefulBeanToCsvBuilder<Game>(writer).build();
			beanToCsv.write(games);
			writer.close();
			writeLock.unlock();
		} catch (IllegalStateException | IOException | CsvDataTypeMismatchException
				| CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
		}
	}

	public static void modifyGame(Game newGame) {
		Lock writeLock = rwLock.writeLock();
		writeLock.lock();
		try {
			List<Game> games = new CsvToBeanBuilder<Game>(new FileReader(file)).withType(Game.class).build().parse();
			for (int i = 0; i < games.size(); i++) {
				if (newGame.compareTo(games.get(i)) == 1) {
					games.set(i, newGame);
					break;
				}
			}
			FileWriter writer = new FileWriter(file);
			StatefulBeanToCsv<Game> beanToCsv = new StatefulBeanToCsvBuilder<Game>(writer).build();
			beanToCsv.write(games);
			writer.close();
			writeLock.unlock();
		} catch (IllegalStateException | IOException | CsvDataTypeMismatchException
				| CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
		}
	}
}

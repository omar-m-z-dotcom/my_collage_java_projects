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

import model.Player;

public class PlayersFileManager {
	private static String file = System.getProperty("user.dir") + "\\src\\main\\resources\\players.csv";
	private static ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

	public static List<Player> getPlayers() {
		Lock readLock = rwLock.readLock();
		readLock.lock();
		ClassLoader.getSystemResource(file);
		try {
			List<Player> players = new CsvToBeanBuilder<Player>(new FileReader(file)).withType(Player.class).build()
					.parse();
			readLock.unlock();
			return players;
		} catch (IllegalStateException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void addPlayer(Player player) {
		Lock writeLock = rwLock.writeLock();
		writeLock.lock();
		try {
			List<Player> players = new CsvToBeanBuilder<Player>(new FileReader(file)).withType(Player.class).build()
					.parse();
			players.add(player);
			FileWriter writer = new FileWriter(file);
			StatefulBeanToCsv<Player> beanToCsv = new StatefulBeanToCsvBuilder<Player>(writer).build();
			beanToCsv.write(players);
			writer.close();
			writeLock.unlock();
		} catch (IllegalStateException | IOException | CsvDataTypeMismatchException
				| CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
		}
	}

	public static void modifyPlayer(Player newPlayer) {
		Lock writeLock = rwLock.writeLock();
		writeLock.lock();
		try {
			List<Player> players = new CsvToBeanBuilder<Player>(new FileReader(file)).withType(Player.class).build()
					.parse();
			for (int i = 0; i < players.size(); i++) {
				if (newPlayer.compareTo(players.get(i)) == 1) {
					players.set(i, newPlayer);
					break;
				}
			}
			FileWriter writer = new FileWriter(file);
			StatefulBeanToCsv<Player> beanToCsv = new StatefulBeanToCsvBuilder<Player>(writer).build();
			beanToCsv.write(players);
			writer.close();
			writeLock.unlock();
		} catch (IllegalStateException | IOException | CsvDataTypeMismatchException
				| CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
		}
	}
}

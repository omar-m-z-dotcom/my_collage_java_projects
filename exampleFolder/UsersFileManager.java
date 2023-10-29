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

import model.User;

public class UsersFileManager {
	private static String file = System.getProperty("user.dir") + "\\src\\main\\resources\\users.csv";
	private static ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

	public static List<User> getUsers() {
		Lock readLock = rwLock.readLock();
		readLock.lock();
		try {
			List<User> users = new CsvToBeanBuilder<User>(new FileReader(file)).withType(User.class).build().parse();
			readLock.unlock();
			return users;
		} catch (IllegalStateException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean addUser(User user) {
		Lock writeLock = rwLock.writeLock();
		writeLock.lock();
		try {
			List<User> users = new CsvToBeanBuilder<User>(new FileReader(file)).withType(User.class).build().parse();
			for (User user1 : users) {
				if (user1.getUsername().equals(user.getUsername())) {
					return false;
				}
			}
			users.add(user);
			FileWriter writer = new FileWriter(file);
			StatefulBeanToCsv<User> beanToCsv = new StatefulBeanToCsvBuilder<User>(writer).build();
			beanToCsv.write(users);
			writer.close();
			writeLock.unlock();
		} catch (IllegalStateException | IOException | CsvDataTypeMismatchException
				| CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
		}
		return true;
	}
}

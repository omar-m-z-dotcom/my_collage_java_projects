package lookupServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import interfaces.ConstructErrorInterface;
import interfaces.RunTimeErrorInterface;

public class LookupThread extends Thread implements ConstructErrorInterface, RunTimeErrorInterface {
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	/**
	 * @param socket
	 */
	public LookupThread(Socket socket) {
		this.socket = socket;
		try {
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
		} catch (Exception e) {
			handleConstructError(e);
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
	}

	@Override
	public void handleRunTimeError(Exception e) {
		e.printStackTrace();
		try {
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		String lookupFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\lookup.csv";
		int minWordLength = Integer.MAX_VALUE;
		int maxWordLength = Integer.MIN_VALUE;
		try {
			if (socket.isClosed()) {
				return;
			}
			CSVReader reader = new CSVReaderBuilder(new FileReader(lookupFilePath)).build();
			List<String[]> hangmanWords = reader.readAll();
			for (String[] strings : hangmanWords) {
				if (minWordLength > strings[0].length()) {
					minWordLength = strings[0].length();
				}
				if (maxWordLength < strings[0].length()) {
					maxWordLength = strings[0].length();
				}
			}
			outputStream.writeInt(minWordLength);
			outputStream.writeInt(maxWordLength);
			int hangmanWordLength = inputStream.readInt();
			for (int i = 0; i < hangmanWords.size(); i++) {
				if (hangmanWords.get(i).length != hangmanWordLength) {
					hangmanWords.remove(i);
				}
			}
			int randomSelector = ThreadLocalRandom.current().nextInt(0, (hangmanWords.size() + 1));
			String hangmanWord = hangmanWords.get(randomSelector)[0];
			outputStream.writeUTF(hangmanWord);
			socket.close();
		} catch (Exception e) {
			handleRunTimeError(e);
		}
	}

}

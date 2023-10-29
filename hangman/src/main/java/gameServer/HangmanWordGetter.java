package gameServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * handles getting the hangman word from the lookup server
 *
 */
public class HangmanWordGetter {
	public static String getHangmanWord() {
		Socket lookuptSocket = null;
		try {
			lookuptSocket = new Socket("localhost", 21111);
			DataInputStream lookupInputStream = new DataInputStream(lookuptSocket.getInputStream());
			DataOutputStream lookupOutputStream = new DataOutputStream(lookuptSocket.getOutputStream());
			int minWordLength = lookupInputStream.readInt();
			int maxWordLength = lookupInputStream.readInt();
			int randomWordLength = ThreadLocalRandom.current().nextInt(minWordLength, (maxWordLength + 1));
			lookupOutputStream.writeInt(randomWordLength);
			String hangmanWord = lookupInputStream.readUTF();
			lookuptSocket.close();
			return hangmanWord;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				lookuptSocket.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
}

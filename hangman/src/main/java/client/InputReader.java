package client;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * <b>reads the input from the server and returns the response in a
 * string and may also display the response</b>
 *
 */
public class InputReader {
	/**
	 * 
	 * @param inputStream
	 * @param displayInput (flag to specify whether to display input or not)
	 * @return response
	 * @throws IOException
	 */
	public static String readInput(DataInputStream inputStream, boolean displayInput) throws IOException {
		String response = null;
		try {
			response = inputStream.readUTF();
		} catch (IOException e) {
			throw e;
		}
		if (displayInput) {
			System.out.println(response);
			System.out.println();
		}
		return response;
	}
}

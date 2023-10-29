package client;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * <b>sends the output from the client to the server</b>
 *
 */
public class OutputSender {
	/**
	 * 
	 * @param <T>          (the type of the output to send)
	 * @param outputStream
	 * @param output
	 * @throws IOException
	 */
	public static <T> void sendOutput(DataOutputStream outputStream, T output) throws IOException {
		try {
			if (output instanceof Character) {
				outputStream.writeChar((Character) output);
			} else if (output instanceof String) {
				outputStream.writeUTF((String) output);
			} else if (output instanceof Integer) {
				outputStream.writeInt((Integer) output);
			} else if (output instanceof Long) {
				outputStream.writeLong((Long) output);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}

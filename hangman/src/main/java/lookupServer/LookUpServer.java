package lookupServer;

import java.net.ServerSocket;
import java.net.Socket;

public class LookUpServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(21111);
			while (true) {
				Socket socket = serverSocket.accept();
				new LookupThread(socket).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				serverSocket.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

}

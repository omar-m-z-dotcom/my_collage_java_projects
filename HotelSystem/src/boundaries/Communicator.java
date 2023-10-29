package boundaries;

import java.util.Scanner;

/**
 * provides communication between the system and the user
 *
 */
public class Communicator {
	/**
	 * prompts user for information by sending a massage and waiting for an answer
	 * 
	 * @param massage to send to the user
	 * @return response of the user
	 */
	public static String promptUser(String massage) {
		System.out.print(massage);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String userResponse = scanner.nextLine();
		return userResponse;
	}

	/**
	 * sends a massage to the user
	 * 
	 * @param massage to send to the user
	 */
	public static void sendUserMassage(String massage) {
		System.out.print(massage);
	}
}

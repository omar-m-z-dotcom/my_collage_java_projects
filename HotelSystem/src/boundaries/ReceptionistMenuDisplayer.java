package boundaries;

import java.text.ParseException;
import java.util.Scanner;

import controllers.Receptionist;

/**
 * displays the menu that the receptionist will use to manage the hotel rooms
 */
public class ReceptionistMenuDisplayer {
	/**
	 * the method that displays the receptionist menu
	 * 
	 * @param receptionist that manages the hotel rooms
	 * @throws ParseException
	 */
	public void displayReceptionistMenu(Receptionist receptionist) throws ParseException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("choose one of the following:\n" + "1- occupy room(check-in guests)\n"
				+ "2- release room(check-out guests)\n" + "3- display available rooms\n" + "4- display room details\n"
				+ "5- print receipt\n" + "6- display recorded guests\n" + "7- record a guest\n" + "8- reserve a room\n"
				+ "9- exit program\n\n" + "answer: ");
		int input = 0;
		input = scanner.nextInt();
		System.out.println("\n");
		scanner.nextLine();
		switch (input) {
		case 1: {
			receptionist.checkInGuests();
			System.out.println("\n");
		}
			break;
		case 2: {
			receptionist.checkOutGuests();
			System.out.println("\n");
		}
			break;
		case 3: {
			receptionist.displayAvailableRooms();
			System.out.println("\n");
		}
			break;
		case 4: {
			receptionist.displayRoomDetails();
			System.out.println("\n");
		}
			break;
		case 5: {
			receptionist.displayReceipt(null);
			System.out.println("\n");
		}
			break;
		case 6: {
			receptionist.displayRecordedGuests();
			System.out.println("\n");
		}
			break;
		case 7: {
			receptionist.recordGuest();
			System.out.println("\n");
		}
			break;
		case 8: {
			receptionist.reservRoom();
			System.out.println("\n");
		}
			break;
		case 9: {
			scanner.close();
			System.exit(0);
		}
		default: {
			System.out.println("wrong choise, please choose again\n");
		}
		}
	}
}

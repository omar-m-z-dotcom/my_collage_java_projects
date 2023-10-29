package driver;

import java.text.ParseException;

import boundaries.AdminInterface;
import boundaries.ReceptionistMenuDisplayer;

public class Main {

	public static void main(String[] args) throws ParseException {
		AdminInterface adminInterface = new AdminInterface();
		adminInterface.setHotelRooms();
		adminInterface.setHotelRoomPrices();
		adminInterface.setReservationPackagePrices();
		ReceptionistMenuDisplayer receptionistMenuDisplayer = new ReceptionistMenuDisplayer();
		while (true) {
			receptionistMenuDisplayer.displayReceptionistMenu(adminInterface.getResult());
		}
	}

}

package boundaries;

import controllers.HotelCreator;
import controllers.Receptionist;

/**
 * provides an interface to the admin
 */
public class AdminInterface {
	private HotelCreator hotelCreator = new HotelCreator();

	/**
	 * creates the hotel and sets the number of hotel rooms and type and view of
	 * each room
	 */
	public void setHotelRooms() {
		System.out.println("starting initialization\n");
		hotelCreator.createHotel();
		hotelCreator.setHotelRoomsNum();
		hotelCreator.setRoomParams();
		System.out.println("\n");
	}

	/**
	 * sets the room prices
	 */
	public void setHotelRoomPrices() {
		hotelCreator.setRoomPrices();
		System.out.println("\n");
	}

	/**
	 * sets the package prices
	 */
	public void setReservationPackagePrices() {
		hotelCreator.setPackagePrices();
		System.out.println("intialization complete\n");
		System.out.println("\n");
	}

	/**
	 * @return the hotel receptionist
	 */
	public Receptionist getResult() {
		return hotelCreator.getReceptionist();
	}
}

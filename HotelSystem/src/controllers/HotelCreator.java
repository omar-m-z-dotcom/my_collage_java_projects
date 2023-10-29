package controllers;

import java.util.HashMap;

import boundaries.Communicator;
import entities.Hotel;
import entities.Room;
import types.PackageType;
import types.RoomType;
import types.RoomView;

/**
 * creates the hotel object
 */
public class HotelCreator {
	private Hotel hotel;

	/**
	 * creates the initial hotel object
	 */
	public void createHotel() {
		hotel = new Hotel();
		hotel.getReceptionist().setHotel(hotel);
	}

	/**
	 * sets the number of hotel rooms
	 */
	public void setHotelRoomsNum() {
		int hotelRoomsNum = Integer.parseInt(Communicator.promptUser("enter the number of hotel rooms: "));
		System.out.println("\n");
		hotel.setRooms(new Room[hotelRoomsNum]);
	}

	/**
	 * sets the room prices
	 */
	public void setRoomPrices() {

		HashMap<RoomType, Integer> roomPrices = new HashMap<RoomType, Integer>();
		RoomType[] roomTypes = RoomType.values();
		for (RoomType roomType : roomTypes) {
			int roomPrice = Integer
					.parseInt(Communicator.promptUser("enter the price for a " + roomType.toString() + ": "));
			roomPrices.put(roomType, roomPrice);
		}
		hotel.setRoomPrices(roomPrices);
	}

	/**
	 * sets the package prices
	 */
	public void setPackagePrices() {
		HashMap<PackageType, Integer> packagePrices = new HashMap<PackageType, Integer>();
		PackageType[] packageTypes = PackageType.values();
		for (PackageType packageType : packageTypes) {
			int packagePrice = Integer
					.parseInt(Communicator.promptUser("enter the price for " + packageType.toString() + " package: "));
			packagePrices.put(packageType, packagePrice);
		}
		hotel.setPackagePrices(packagePrices);
	}

	/**
	 * sets each hotel room number, type and view
	 */
	public void setRoomParams() {
		HashMap<RoomType, Integer> roomTypeNums = new HashMap<RoomType, Integer>();
		HashMap<RoomView, Integer> roomViewNums = new HashMap<RoomView, Integer>();
		RoomType[] roomTypes = RoomType.values();
		RoomView[] roomViews = RoomView.values();
		RoomFactoryGetter roomFactoryGetter = new RoomFactoryGetter();
		Room[] hotelRooms = hotel.getRooms();
		for (RoomType roomType : roomTypes) {
			int roomTypeNum = Integer
					.parseInt(Communicator.promptUser("enter the number of " + roomType.toString() + " rooms: "));
			roomTypeNums.put(roomType, roomTypeNum);
		}
		for (RoomView roomView : roomViews) {
			int roomViewNum = Integer.parseInt(
					Communicator.promptUser("enter the number of rooms with a " + roomView.toString() + ": "));
			roomViewNums.put(roomView, roomViewNum);
		}
		for (int i = 0; i < hotelRooms.length; i++) {
			hotelRooms[i] = roomFactoryGetter.getRoomFactory(roomTypeNums).createRoom(i + 1, roomTypeNums,
					roomViewNums);
		}
	}

	/**
	 * @return the hotel receptionist
	 */
	public Receptionist getReceptionist() {
		return hotel.getReceptionist();
	}
}

package entities;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.Receptionist;
import types.PackageType;
import types.RoomType;

/**
 * stores the hotel details
 */
public class Hotel {
	private Room[] rooms;
	private ArrayList<Guest> recordedGuests = new ArrayList<Guest>();
	private Receptionist receptionist = new Receptionist();
	private HashMap<RoomType, Integer> roomPrices = new HashMap<RoomType, Integer>();
	private HashMap<PackageType, Integer> packagePrices = new HashMap<PackageType, Integer>();

	/**
	 * @return the rooms
	 */
	public Room[] getRooms() {
		return rooms;
	}

	/**
	 * @return the recordedGuests
	 */
	public ArrayList<Guest> getRecordedGuests() {
		return recordedGuests;
	}

	/**
	 * @param recordedGuests the recordedGuests to set
	 */
	public void setRecordedGuests(ArrayList<Guest> recordedGuests) {
		this.recordedGuests = recordedGuests;
	}

	/**
	 * @param rooms the rooms to set
	 */
	public void setRooms(Room[] rooms) {
		this.rooms = rooms;
	}

	/**
	 * @return the receptionist
	 */
	public Receptionist getReceptionist() {
		return receptionist;
	}

	/**
	 * @param receptionist the receptionist to set
	 */
	public void setReceptionist(Receptionist receptionist) {
		this.receptionist = receptionist;
	}

	/**
	 * @return the roomPrices
	 */
	public HashMap<RoomType, Integer> getRoomPrices() {
		return roomPrices;
	}

	/**
	 * @param roomPrices the roomPrices to set
	 */
	public void setRoomPrices(HashMap<RoomType, Integer> roomPrices) {
		this.roomPrices = roomPrices;
	}

	/**
	 * @return the packagePrices
	 */
	public HashMap<PackageType, Integer> getPackagePrices() {
		return packagePrices;
	}

	/**
	 * @param packagePrices the packagePrices to set
	 */
	public void setPackagePrices(HashMap<PackageType, Integer> packagePrices) {
		this.packagePrices = packagePrices;
	}
}

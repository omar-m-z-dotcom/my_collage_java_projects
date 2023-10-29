package entities;

import java.util.ArrayList;
import java.util.Iterator;

import types.RoomType;
import types.RoomView;

/**
 * stores the room details
 */
public class Room {
	private int roomNum;
	private RoomView roomView;
	private RoomType roomType;
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

	/**
	 * @return the roomNum
	 */
	public int getRoomNum() {
		return roomNum;
	}

	/**
	 * @param roomNum the roomNum to set
	 */
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	/**
	 * @return the roomView
	 */
	public RoomView getRoomView() {
		return roomView;
	}

	/**
	 * @param roomView the roomView to set
	 */
	public void setRoomView(RoomView roomView) {
		this.roomView = roomView;
	}

	/**
	 * @return the roomType
	 */
	public RoomType getRoomType() {
		return roomType;
	}

	/**
	 * @param roomType the roomType to set
	 */
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	/**
	 * @return the reservations
	 */
	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	/**
	 * @param reservations the reservations to set
	 */
	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}

	/**
	 * returns a string representation of the room reservations
	 * 
	 * @return the room reservations in string
	 */
	private String toStringReservations() {
		String reservations = "";
		for (Iterator<Reservation> iterator = this.reservations.iterator(); iterator.hasNext();) {
			Reservation reservation = iterator.next();
			if (iterator.hasNext()) {
				reservations = reservations.concat(reservation.toString() + ", ");
			} else {
				reservations = reservations.concat(reservation.toString());
			}
		}
		return reservations;
	}

	/**
	 * returns a string representation of the room
	 * 
	 * @return room in string
	 */
	@Override
	public String toString() {
		return "Room= [ roomNum=" + roomNum + ", " + (roomView != null ? "roomView=" + roomView + ", " : "")
				+ (roomType != null ? "roomType=" + roomType + ", " : "")
				+ (reservations != null ? "reservations= [ " + toStringReservations() + " ]" : "") + " ]";
	}

}

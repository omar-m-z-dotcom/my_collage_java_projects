package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import types.PackageType;

/**
 * stores the reservation details
 */
public class Reservation {
	private Room reservedRoom;
	private Date reservationStart;
	private Date reservationEnd;
	private PackageType reservationPackage;
	private ArrayList<Guest> guests = new ArrayList<Guest>();

	/**
	 * @return the reservedRoom
	 */
	public Room getReservedRoom() {
		return reservedRoom;
	}

	/**
	 * @param reservedRoom the reservedRoom to set
	 */
	public void setReservedRoom(Room reservedRoom) {
		this.reservedRoom = reservedRoom;
	}

	/**
	 * @return the reservationStart
	 */
	public Date getReservationStart() {
		return reservationStart;
	}

	/**
	 * @param reservationStart the reservationStart to set
	 */
	public void setReservationStart(Date reservationStart) {
		this.reservationStart = reservationStart;
	}

	/**
	 * @return the reservationEnd
	 */
	public Date getReservationEnd() {
		return reservationEnd;
	}

	/**
	 * @param reservationEnd the reservationEnd to set
	 */
	public void setReservationEnd(Date reservationEnd) {
		this.reservationEnd = reservationEnd;
	}

	/**
	 * @return the reservationPackage
	 */
	public PackageType getReservationPackage() {
		return reservationPackage;
	}

	/**
	 * @param reservationPackage the reservationPackage to set
	 */
	public void setReservationPackage(PackageType reservationPackage) {
		this.reservationPackage = reservationPackage;
	}

	/**
	 * @return the guests
	 */
	public ArrayList<Guest> getGuests() {
		return guests;
	}

	/**
	 * @param guests the guests to set
	 */
	public void setGuests(ArrayList<Guest> guests) {
		this.guests = guests;
	}

	/**
	 * returns a string representation of the reservation guests
	 * 
	 * @return the reservation guests in string
	 */
	private String toStringGuests() {
		String guests = "";
		for (Iterator<Guest> iterator = this.guests.iterator(); iterator.hasNext();) {
			Guest guest = (Guest) iterator.next();
			if (iterator.hasNext()) {
				guests = guests.concat(guest.toString() + ", ");
			} else {
				guests = guests.concat(guest.toString());
			}
		}
		return guests;
	}

	/**
	 * returns a string representation of the reservation
	 * 
	 * @return reservation in string
	 */
	@Override
	public String toString() {
		return "Reservation= [ " + (reservationStart != null ? "reservationStart=" + reservationStart + ", " : "")
				+ (reservationEnd != null ? "reservationEnd=" + reservationEnd + ", " : "")
				+ (reservationPackage != null ? "reservationPackage=" + reservationPackage + ", " : "")
				+ (guests != null ? "guests= [ " + toStringGuests() + " ]" : "") + " ]";
	}

}

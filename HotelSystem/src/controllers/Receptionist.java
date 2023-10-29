package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import boundaries.Communicator;
import entities.Guest;
import entities.Hotel;
import entities.Reservation;
import entities.Room;
import types.PackageType;
import types.RoomType;
import types.RoomView;

/**
 * 
 * manages the hotel rooms and records hotel room reservations and guests data
 *
 */
public class Receptionist {
	private Hotel hotel;

	/**
	 * @return the hotel
	 */
	public Hotel getHotel() {
		return hotel;
	}

	/**
	 * @param hotel the hotel to set
	 */
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	/**
	 * the method that displays the recorded guests
	 */
	public void displayRecordedGuests() {
		ArrayList<Guest> recordedGuests = hotel.getRecordedGuests();
		for (Guest guest : recordedGuests) {
			Communicator.sendUserMassage(guest.toString() + "\n");
		}
		String answer = Communicator.promptUser("\ndo you want to add a new guest(yes/no):");
		if (answer.equalsIgnoreCase("yes")) {
			recordGuest();
		}
	}

	/**
	 * 
	 * @param requestedPeriodStart     the start of the requested period to check
	 *                                 for collision with the period of
	 *                                 reservationToComapreWith
	 * @param requestedPeriodEnd       the end of the requested period to check for
	 *                                 collision with the period of
	 *                                 reservationToComapreWith
	 * @param reservationToCompareWith the reservation whose period will be checked
	 *                                 if it collides with the requested period
	 * @return true in case a collision has been detected false otherwise
	 */
	private boolean isColliding(Date requestedPeriodStart, Date requestedPeriodEnd,
			Reservation reservationToCompareWith) {
		Date reservationStart = reservationToCompareWith.getReservationStart();
		Date reservationEnd = reservationToCompareWith.getReservationEnd();
		if ((requestedPeriodStart.compareTo(reservationEnd) < 0 && requestedPeriodEnd.compareTo(reservationEnd) > 0)
				|| (requestedPeriodStart.compareTo(reservationStart) == 0
						&& requestedPeriodEnd.compareTo(reservationEnd) == 0)
				|| (requestedPeriodEnd.compareTo(reservationStart) > 0
						&& requestedPeriodStart.compareTo(reservationStart) < 0)) {
			return true;
		}
		return false;

	}

	/**
	 * the method that reserves a hotel room
	 * 
	 * @return the reserved hotel room or null if reservation failed
	 * @throws ParseException
	 */
	public Room reservRoom() throws ParseException {
		Room[] hotelRooms = hotel.getRooms();
		Room roomToReserve = null;
		RoomType requestedType;
		RoomView requestedView;
		Date requestedReservationStart;
		Date requestedReservationEnd;
		PackageType reguestedPackage = null;
		ArrayList<Guest> reservationGuests;
		RoomType[] roomTypes = RoomType.values();
		RoomView[] roomViews = RoomView.values();
		PackageType[] packageTypes = PackageType.values();
		Communicator.sendUserMassage("choose which type of room to reserve:\n");
		for (int i = 0; i < roomTypes.length; i++) {
			Communicator.sendUserMassage(String.valueOf(i + 1) + "- " + roomTypes[i].toString() + "\n");
		}
		int answer = Integer.parseInt(Communicator.promptUser("answer: ")) - 1;
		requestedType = roomTypes[answer];
		Communicator.sendUserMassage("choose the view of the room to reserve:\n");
		for (int i = 0; i < roomViews.length; i++) {
			Communicator.sendUserMassage(String.valueOf(i + 1) + "- " + roomViews[i].toString() + "\n");
		}
		int answer1 = Integer.parseInt(Communicator.promptUser("answer: ")) - 1;
		requestedView = roomViews[answer1];
		boolean isTherecompliantToSpecsRooms = false;
		for (int i = 0; i < hotelRooms.length; i++) {
			if (hotelRooms[i].getRoomType().equals(requestedType)
					&& hotelRooms[i].getRoomView().equals(requestedView)) {
				isTherecompliantToSpecsRooms = true;
				break;
			}
		}
		if (!isTherecompliantToSpecsRooms) {
			Communicator.sendUserMassage("there are no rooms available with required specifications");
			return null;
		}
		Communicator.sendUserMassage("enter the number of the room to reserve:\n");
		for (int i = 0; i < hotelRooms.length; i++) {
			if (hotelRooms[i].getRoomType().equals(requestedType)
					&& hotelRooms[i].getRoomView().equals(requestedView)) {
				Communicator.sendUserMassage("room " + hotelRooms[i].getRoomNum() + "\n");
			}
		}
		int roomNum = Integer.parseInt(Communicator.promptUser("answer: "));
		for (int i = 0; i < hotelRooms.length; i++) {
			if (hotelRooms[i].getRoomNum() == roomNum && hotelRooms[i].getRoomView().equals(requestedView)
					&& hotelRooms[i].getRoomType().equals(requestedType)) {
				roomToReserve = hotelRooms[i];
				break;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		requestedReservationStart = sdf
				.parse(Communicator.promptUser("enter the reservation start date in the format(dd/MM/yyyy): "));
		requestedReservationEnd = sdf
				.parse(Communicator.promptUser("enter the reservation end date in the format(dd/MM/yyyy): "));
		ArrayList<Reservation> roomReservations = roomToReserve.getReservations();
		for (Reservation reservation : roomReservations) {
			if (isColliding(requestedReservationStart, requestedReservationEnd, reservation)) {
				Communicator.sendUserMassage("the room is unavailable for the requested period");
				return null;
			}
		}
		Communicator.sendUserMassage("choose which package to include with the reservation:\n");
		for (int i = 0; i < packageTypes.length; i++) {
			Communicator.sendUserMassage(String.valueOf(i + 1) + "- " + packageTypes[i].toString() + "\n");
		}
		int answer2 = Integer.parseInt(Communicator.promptUser("answer: ")) - 1;
		reguestedPackage = packageTypes[answer2];
		int newGuestsNum = 0;
		newGuestsNum = Integer.parseInt(Communicator.promptUser("how many guests are in the reservation: "));
		reservationGuests = new ArrayList<Guest>();
		ArrayList<Guest> recordedGuests = hotel.getRecordedGuests();
		for (int i = 0; i < newGuestsNum; i++) {
			long newGuestNationalId = Long
					.parseLong(Communicator.promptUser("enter the national id of guest" + (i + 1) + ": "));
			for (int j = 0; j < recordedGuests.size(); j++) {
				if (recordedGuests.get(j).getNationalId() == newGuestNationalId) {
					reservationGuests.add(recordedGuests.get(j));
					break;
				}
			}
		}
		Reservation requestedReservation = new Reservation();
		requestedReservation.setReservationStart(requestedReservationStart);
		requestedReservation.setReservationEnd(requestedReservationEnd);
		requestedReservation.setReservationPackage(reguestedPackage);
		requestedReservation.setGuests(reservationGuests);
		requestedReservation.setReservedRoom(roomToReserve);
		roomToReserve.getReservations().add(requestedReservation);
		return roomToReserve;
	}

	/**
	 * the method that is used to check-in guests by incrementing their number of
	 * visits by 1
	 * 
	 * @throws ParseException
	 */
	public void checkInGuests() {
		Room[] hotelRooms = hotel.getRooms();
		ArrayList<Reservation> roomReservations = null;
		int roomNum = Integer.parseInt(Communicator.promptUser("what is the room number: "));
		for (int i = 0; i < hotelRooms.length; i++) {
			if (hotelRooms[i].getRoomNum() == roomNum) {
				roomReservations = hotelRooms[i].getReservations();
				break;
			}
		}
		if (roomReservations.isEmpty()) {
			Communicator.sendUserMassage("the room dosen't have any reservations\n\n");
			return;
		}
		Communicator.sendUserMassage("choose which room reservation to check-in:\n");
		for (int i = 0; i < roomReservations.size(); i++) {
			Communicator.sendUserMassage(String.valueOf(i + 1) + "- " + roomReservations.get(i).toString() + "\n");
		}
		int input = Integer.parseInt(Communicator.promptUser("answer: ")) - 1;
		ArrayList<Guest> guests = roomReservations.get(input).getGuests();
		displayReceipt(roomReservations.get(input));
		for (Iterator<Guest> iterator = guests.iterator(); iterator.hasNext();) {
			Guest guest = iterator.next();
			guest.setVistsNum((guest.getVistsNum()) + 1);
		}
	}

	/**
	 * the method that is used to check-out guests by removing the reservation from
	 * the list of reservations
	 */
	public void checkOutGuests() {
		Room[] hotelRooms = hotel.getRooms();
		ArrayList<Reservation> roomReservations = null;
		int roomNum = Integer.parseInt(Communicator.promptUser("what is the room number: "));
		for (int i = 0; i < hotelRooms.length; i++) {
			if (hotelRooms[i].getRoomNum() == roomNum) {
				roomReservations = hotelRooms[i].getReservations();
				break;
			}
		}
		if (roomReservations.isEmpty()) {
			Communicator.sendUserMassage("the room dosen't have any reservations\n");
		}
		Communicator.sendUserMassage("choose which room reservation to check-out:\n");
		for (int i = 0; i < roomReservations.size(); i++) {
			Communicator.sendUserMassage(String.valueOf(i + 1) + "- " + roomReservations.get(i).toString() + "\n");
		}
		int input = Integer.parseInt(Communicator.promptUser("answer: ")) - 1;
		roomReservations.remove(input);
	}

	/**
	 * the method used to recored new guests data
	 * 
	 * @return the recorded guest
	 */
	public Guest recordGuest() {
		ArrayList<Guest> recordedGuests = hotel.getRecordedGuests();
		String newGuestName;
		long newGuestNationalId;
		int newGuestvistsNum;
		newGuestName = Communicator.promptUser("what is the name of the guest: ");
		newGuestNationalId = Long.parseLong(Communicator.promptUser("what is the national id of the guest: "));
		newGuestvistsNum = Integer.parseInt(Communicator.promptUser("how many times did the guest visit the hotel: "));
		for (Iterator<Guest> iterator = recordedGuests.iterator(); iterator.hasNext();) {
			Guest guest = iterator.next();
			if (guest.getNationalId() == newGuestNationalId) {
				Communicator.sendUserMassage(
						"can't record the new guest because there is a guest already recorded with the same national id\n");
				return null;
			}
		}
		Guest newGuest = new Guest();
		newGuest.setName(newGuestName);
		newGuest.setNationalId(newGuestNationalId);
		newGuest.setVistsNum(newGuestvistsNum);
		recordedGuests.add(newGuest);
		return newGuest;
	}

	/**
	 * displays the the available rooms based on the period the user enters
	 * 
	 * @throws ParseException
	 */
	public void displayAvailableRooms() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date filterStart = sdf
				.parse(Communicator.promptUser("enter the filter start date in the the format(dd/MM/yyyy): "));
		Date filterEnd = sdf
				.parse(Communicator.promptUser("enter the filter end date in the the format(dd/MM/yyyy): "));
		Room[] hotelRooms = hotel.getRooms();
		for (int i = 0; i < hotelRooms.length; i++) {
			boolean isAvailable = true;
			ArrayList<Reservation> roomReservations = hotelRooms[i].getReservations();
			for (Iterator<Reservation> iterator = roomReservations.iterator(); iterator.hasNext();) {
				Reservation reservation = iterator.next();
				if (isColliding(filterStart, filterEnd, reservation)) {
					isAvailable = false;
					break;
				}
			}
			if (isAvailable) {
				Communicator.sendUserMassage("room " + hotelRooms[i].getRoomNum() + "\n");
			}
		}
	}

	/**
	 * displays the all the details for a certain room
	 */
	public void displayRoomDetails() {
		Room[] hotelRooms = hotel.getRooms();
		int roomNum = Integer.parseInt(Communicator.promptUser("what is the room number: "));
		for (int i = 0; i < hotelRooms.length; i++) {
			if (hotelRooms[i].getRoomNum() == roomNum) {
				Communicator.sendUserMassage(hotelRooms[i].toString());
				return;
			}
		}
	}

	/**
	 * displays the receipt for a room reservation
	 * 
	 * @param reservation to display the receipt for
	 */
	public void displayReceipt(Reservation reservation) {
		if (reservation == null) {
			Room[] hotelRooms = hotel.getRooms();
			ArrayList<Reservation> roomReservations = null;
			int roomNum = Integer.parseInt(Communicator.promptUser("what is the room number: "));
			for (int i = 0; i < hotelRooms.length; i++) {
				if (hotelRooms[i].getRoomNum() == roomNum) {
					roomReservations = hotelRooms[i].getReservations();
					break;
				}
			}
			if (roomReservations.isEmpty()) {
				Communicator.sendUserMassage("the room dosen't have any reservations\n");
				return;
			}
			Communicator.sendUserMassage("choose a reservation to display it's receipt:\n");
			for (int i = 0; i < roomReservations.size(); i++) {
				Communicator.sendUserMassage(String.valueOf(i + 1) + "- " + roomReservations.get(i).toString() + "\n");
			}
			int input = Integer.parseInt(Communicator.promptUser("answer: ")) - 1;
			Date reservationStart = roomReservations.get(input).getReservationStart();
			Date reservationEnd = roomReservations.get(input).getReservationEnd();
			long diffInMillies = reservationEnd.getTime() - reservationStart.getTime();
			int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			int totalRoomPrice = diff
					* (hotel.getRoomPrices().get(roomReservations.get(input).getReservedRoom().getRoomType()));
			int totalPackagePrice = diff
					* (hotel.getPackagePrices().get(roomReservations.get(input).getReservationPackage()));
			Communicator
					.sendUserMassage("the total price of reservation: " + (totalRoomPrice + totalPackagePrice) + "\n");
		} else {
			Date reservationStart = reservation.getReservationStart();
			Date reservationEnd = reservation.getReservationEnd();
			long diffInMillies = reservationEnd.getTime() - reservationStart.getTime();
			int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			int roomPrice = diff * (hotel.getRoomPrices().get(reservation.getReservedRoom().getRoomType()));
			int packagePrice = diff * (hotel.getPackagePrices().get(reservation.getReservationPackage()));
			Communicator.sendUserMassage("the total price of reservation: " + (roomPrice + packagePrice) + "\n");
		}
	}
}

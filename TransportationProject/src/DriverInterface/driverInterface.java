package DriverInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import DataPackage.RideRequest;
import DataPackage.driver;
import DataPackage.offer;
import DriverMediator.driverMediator;

public class driverInterface {
	public driver Driver = new driver();
	public ArrayList<RideRequest> RequestedRidesList = new ArrayList<RideRequest>();
	public registration_sender reg_sender;
	public ride_request_reciver ride_req_rec;
	public offer_sender offer_sender;

	public driverInterface() {
		reg_sender = new registration_sender(this);
		ride_req_rec = new ride_request_reciver(this);
		offer_sender = new offer_sender(this);
	}

	public static class registration_sender {
		driverInterface DI;// this a refrance to the driver interface

		public registration_sender(driverInterface d) {
			DI = d;// we are refrancing the driver who this class is inside of
			// by constructing the class using a driverInterface instance we can access the
			// driverInterface instance varibles
		}

		public void get_driver_info() {
			System.out.println("what is your name");
			Scanner sc = new Scanner(System.in);
			this.DI.Driver.name = sc.nextLine();
			System.out.println("what is your password");
			this.DI.Driver.password = sc.nextLine();
			System.out.println("what is your email");
			this.DI.Driver.email = sc.nextLine();
			System.out.println("what is your phone number");
			this.DI.Driver.phone_number = sc.nextLong();
			System.out.println("what is your National id");
			this.DI.Driver.National_id = sc.nextLong();
			System.out.println("what is the full path to your driver license picture file");
			try {
				this.DI.Driver.driver_license_image = ImageIO.read(new File(sc.nextLine()));// reads the full path to
																							// the driver_license_image
				// then stores it in the driver_license_image varible
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(true) {
				System.out.println("what are your favourite areas to work in.\n type a name of a place and it will added to your info"
						+ ".\n when your done typeing the areas you work in type exit.");
				String place=sc.nextLine();
				if(place.equalsIgnoreCase("exit"))
					break;
				else {
					DI.Driver.favourite_areas.add(place);
					continue;
				}
			}
			send_regestration(this.DI.Driver);
			System.out.println("your registration request has been sent");
		}

		public void send_regestration(driver d) {
			driverMediator.rs.send_registration(d);
		}

	}

	public static class ride_request_reciver {
		driverInterface DI;// this a refrance to the driver interface

		public ride_request_reciver(driverInterface d) {
			DI = d;// we are refrancing the driver who this class is inside of
			// by constructing the class using a driverInterface instance we can access the
			// driverInterface instance varibles
		}

		public void get_requested_rides_list() {
			DI.RequestedRidesList = driverMediator.RLG.get_RequestedRideList(DI.Driver);
			if (DI.RequestedRidesList == null) {
				System.out.println(
						"ride request retrival didn't succeed, please register in the system first before requesting any rides");
			} else {
				System.out.println("ride request in your favourite areas are:");
				for (RideRequest r : DI.RequestedRidesList) {
					System.out.println(r.sourceLocation + " " + r.DestinationLocation + " " + r.requester_data.name
							+ " " + r.requester_data.phone_number);
				}
			}
		}

	}

	public static class offer_sender {
		driverInterface DI;// this a refrance to the driver interface

		public offer_sender(driverInterface d) {
			DI = d;// we are refrancing the driver who this class is inside of
			// by constructing the class using a driverInterface instance we can access the
			// driverInterface instance varibles
		}

		public void get_offer_info() {
			offer o = new offer();
			System.out.println("what is the source location");
			Scanner sc = new Scanner(System.in);
			o.sourceLocation = sc.nextLine();
			System.out.println("what is the destination lication");
			o.DestinationLocation = sc.nextLine();
			System.out.println("what is the price of the offer");
			o.price = sc.nextInt();
			o.offer_giver_info = this.DI.Driver;
			send_offer(o);
		}

		public void send_offer(offer o) {
			Boolean IsSuccesful = driverMediator.os.send_offer(o);
			if (IsSuccesful == true) {
				System.out.println("offer has been sent succesfuly");
			}
			else {
				System.out.println("offer sending didn't succeed, please register in the system first before sending any offers");
			}
		}
	}

}

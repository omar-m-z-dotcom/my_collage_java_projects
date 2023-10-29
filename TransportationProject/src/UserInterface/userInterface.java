package UserInterface;

import java.util.Scanner;

import DataPackage.RideRequest;
import DataPackage.user;
import UserMediator.userMediator;

public class userInterface {
	public user User = new user();
	public registration_sender reg_sender;
	public ride_request_sender ride_req_sender;

	public userInterface() {
		reg_sender = new registration_sender(this);
		ride_req_sender = new ride_request_sender(this);
	}

	public static class registration_sender {
		userInterface UI;// this a refrance to the user interface

		public registration_sender(userInterface u) {
			UI = u;// we are refrancing the user who this class is inside of
			// by constructing the class using a userInterface instance we can access the
			// userInterface instance varibles
		}

		public void get_user_info() {
			System.out.println("what is your name");
			Scanner sc = new Scanner(System.in);
			this.UI.User.name = sc.nextLine();
			System.out.println("what is your password");
			this.UI.User.password = sc.nextLine();
			System.out.println("what is your email");
			this.UI.User.email = sc.nextLine();
			System.out.println("what is your phone number");
			this.UI.User.phone_number = sc.nextLong();
			send_regestration(this.UI.User);
			System.out.println("your registered in the system now");

		}

		public void send_regestration(user u) {
			userMediator.rs.send_regestration(u);
		}
	}

	public static class ride_request_sender {
		userInterface UI;// this a refrance to the user interface

		public ride_request_sender(userInterface u) {
			UI = u;// we are refrancing the user who this class is inside of
			// by constructing the class using a userInterface instance we can access the
			// userInterface instance varibles
		}

		public void get_RideRequest_info() {
			RideRequest r = new RideRequest();
			System.out.println("what is the source location");
			Scanner sc = new Scanner(System.in);
			r.sourceLocation = sc.nextLine();
			System.out.println("what is the destination location");
			r.DestinationLocation = sc.nextLine();
			r.requester_data = this.UI.User;
			send_ride_request(r);

		}

		public void send_ride_request(RideRequest r) {
			Boolean IsSuccesful = userMediator.rrs.send_ride_request(r);
			if (IsSuccesful == true) {
				System.out.println("ride request sent succesfuly");
			}
			else {
				System.out.println("ride request send didn't succeed, please register in the system first before requesting any rides");
			}
		}
	}

}

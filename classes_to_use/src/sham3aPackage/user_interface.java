package sham3aPackage;

import java.util.Scanner;

//what shamma should have done

public class user_interface {
	public boolean allowed_in = false;// this tells the gui if the user is allowed to interact with the system
	public user u = new user();
	public registration_sender reg_sender;
	public login_sender ls;
	public ride_request_sender rrs;

	public user_interface() {
		reg_sender = new registration_sender(this);
		ls = new login_sender(this);
		rrs = new ride_request_sender(this);
	}

	public static class registration_sender {
		user_interface j;
		user u;

		registration_sender(user_interface k) {
			this.j = k;
		}

		public void get_user_info() {
			System.out.println("what is your name");
			Scanner sc = new Scanner(System.in);
			String name = sc.nextLine();
			this.u.name = name;

			System.out.println("what is your email");
			String email = sc.nextLine();
			this.u.email = email;

			System.out.println("what is your password");
			String password = sc.nextLine();
			this.u.password = password;

			System.out.println("what is your password");
			int phone = sc.nextInt();
			this.u.phone_number = phone;
			sc.close();

			send_regestration(u);
		}

		public void send_regestration(user u) {
			// note: the calling of itermidiate class is not your job
			// amr abrahimn will do it
		}

		public void view_registration_status(boolean status) {

			if (status == true) {
				System.out.println("you are registered now");
				j.u.name = this.u.name;
				j.u.email = this.u.email;
				j.u.password = this.u.password;
				j.u.phone_number = this.u.phone_number;
				j.allowed_in = true;
			} else {
				System.out.println("registration error, enter a different user name and password");

			}

		}

	}

	public static class login_sender {
		user_interface j;
		user u;

		login_sender(user_interface k) {

			this.j = k;
		}

		public void get_user_info() {

			System.out.println("what is your name");
			Scanner sc = new Scanner(System.in);
			String name = sc.nextLine();
			this.u.name = name;

			System.out.println("what is your email");
			String email = sc.nextLine();
			this.u.email = email;

			System.out.println("what is your password");
			String password = sc.nextLine();
			this.u.password = password;

			System.out.println("what is your password");
			int phone = sc.nextInt();
			this.u.phone_number = phone;

			sc.close();

			send_login(u);

		}

		public void send_login(user u) {
			// note: the calling of itermidiate class is not tour job
			// amr abrahimn will do it
		}

		public void login_status(boolean status) {

			if (status == true) {
				System.out.println("you are loged in now");
				j.u.name = this.u.name;
				j.u.email = this.u.email;
				j.u.password = this.u.password;
				j.u.phone_number = this.u.phone_number;
				j.allowed_in = true;
			} else {
				System.out.println("wrong username or password please try again");
			}
		}
	}

	public static class ride_request_sender {
		user_interface j;
		RideRequest r;

		ride_request_sender(user_interface k) {
			this.j = k;
		}

		public void get_ride_info() {
			if (j.allowed_in == true) {

				System.out.println("what is the source area");
				Scanner sc = new Scanner(System.in);
				String source = sc.nextLine();
				this.r.source_location = source;

				System.out.println("what is the destination area");
				String destination = sc.nextLine();
				this.r.destention_location = destination;

				sc.close();

				send_ride(r);
			}
			else {
				System.out.println("you can't interact with the system because your not registered in the system or loged in");
			}
		}

		public void send_ride(RideRequest r) {
			// note: the calling of itermidiate class is not tour job
			// amr abrahimn will do it
		}

	}
}

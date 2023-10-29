package sham3aPackage;

import java.util.ArrayList;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class driver_interface {
	public boolean allowed_in = false;//this tells the gui if the driver is allowed to ineract with the system
	//public driver_intermidiat d_int;
	public driver d = new driver();
	public registration_sender reg_sender;
	public login_sender login_sender;
	public ride_request_reciver ride_req_rec;
	public offer_sender off_sender;
	public Button send_reg = new Button("send registration");
	public Button send_login = new Button("send login");
	public Button view_ride_req = new Button("view ride request");
	public Button send_offer = new Button("send offer");

	ArrayList<RideRequest> ridesList = new ArrayList<RideRequest>();
	
	public driver_interface() {
		//d_int=new driver_intermidiat(this);
	}

	public static class registration_sender  {
		driver_interface j;
		driver u;

		registration_sender(driver_interface u) {
			this.j = u;
		}

		public void get_user_info() {

			send_regestration(u);
		}

		public void send_regestration(driver u) {
			// note: the calling of itermidiate class is not tour job
			// amr abrahimn will do it
		}

		/*/public void view_registration_status(boolean status) {
			// view_registration_status based on the value
		}*///wrong do not do it 
	}

	public static class login_sender  {
		driver_interface j;
		driver u;

		login_sender(driver_interface u) {
			this.j = u;
		}

		public void get_user_info() {

			send_login(u);
		}

		public void send_login(driver u) {
			// note: the calling of itermidiate class is not tour job
			// amr abrahimn will do it
		}

		public void view_login_status(boolean status) {
			// view_login_status based on the value
		}

		
	}

	public static class ride_request_reciver {
		driver_interface j;

		ride_request_reciver(driver_interface u) {
			this.j = u;
		}

		public void get_ride_info() {
			// note: the calling of itermidiate class is not tour job
			// amr abrahimn will do it
		}

		public void store_ride_requests(ArrayList<RideRequest> ridesList) {
			// after some logic

			view_ride_request();

		}

		public void view_ride_request() {

		}

		
	}

	public static class offer_sender {
		driver_interface j;

		offer_sender(driver_interface u) {
			this.j = u;
		}

		public void get_offer() {
			offer o;
			// after some logic
			send_offer(o);
		}

		public void send_offer(offer o) {
			// note: the calling of itermidiate class is not tour job
			// amr abrahimn will do it
		}
	}

}

package sham3aPackage;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class admin_interface {
	// public admin_intermidiate a_int;
	public pending_drivers_getter t;
	public driver_verifier dv;
	public Button get_pending_drivers = new Button("get pending drivers");
	public Button verify_driver = new Button("verify driver");
	public Button confirm_varification = new Button("confirm varification");
	public ArrayList<driver> pending_driversList = new ArrayList<driver>();

	admin_interface() {
		// a_int=new admin_intermidiate(this);
	}

	public static class pending_drivers_getter {
		admin_interface j;

		pending_drivers_getter(admin_interface u) {
			this.j = u;
		}

		public void send_view_pending_drivers_reqeust() {
			// note: the calling of itermidiate class is not tour job
			// amr abrahimn will do it
		}

		public void store_pending_drivers(ArrayList<driver> ridesList) {
			// after some logic
			view_pending_drivers();

		}

		public void view_pending_drivers() {
			// do not view the image of the driver
		}

	}

	public static class driver_verifier {
		admin_interface j;
		driver d;

		driver_verifier(admin_interface u) {
			this.j = u;
		}

		public void verify() {

			// do some logic

			// after doing the logic send to the intermediate

			// very important note you have to store the driver that the admin wants to send
			// varification for to
			// in the driver instanse (driver d that is in the driver_verifier class) so
			// that the driver info can be used
			// by the intemidiate class to send the verification to the right driver

			// note: the calling of itermidiate class is not your job
			// amr abrahimn will do it
		}
	}
}

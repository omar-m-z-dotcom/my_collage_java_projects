package sham3aPackage;

public class driver_intermidiat {
	driver_interface d;
	driver_intermidiat(driver_interface d){
		this.d=d;
	}
	public void send_reg(driver u) {
		// sends regestration using the driver input ----------->                                              class
		 //{// then waits for a boolean to be sent
		// sends this boolean back to the view_registration_status function} <---- wrong do not do it
	}

	public void send_login_req(driver u) {
		// sends regestration using the driver input ----------->                                              class
		// then waits for a boolean to be sent
		// sends this boolean back to the view_login_status function
	}
	
	public void request_ride_requests() {
		// sends a request to the system to recive the ride requests ----------->                                              class
		// recives the ride requests
		// sends the ride requests back to the gui using store_ride_requests function
	}
	
	public void send_offer(offer o) { 
		// sends the offer to the system ----------->                                              class
	}
}

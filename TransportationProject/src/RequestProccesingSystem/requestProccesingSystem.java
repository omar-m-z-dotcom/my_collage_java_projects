package RequestProccesingSystem;

import java.util.ArrayList;

import DataPackage.RideRequest;
import DataPackage.driver;
import DataPackage.offer;
import DataPackage.user;

public class requestProccesingSystem {
	static ArrayList<user> users = new ArrayList<user>();// this where we store our users
	static ArrayList<driver> drivers = new ArrayList<driver>();// this where we store our drivers
	static ArrayList<RideRequest> RideRequests = new ArrayList<RideRequest>();// this where we store our ride_request
																				// that are
	// coming from users
	static ArrayList<offer> offers = new ArrayList<offer>();// this where we store our offer that are coming from
															// drivers
	static ArrayList<driver> Pendingdrivers = new ArrayList<driver>();// this where we store our drivers pending for
	// registration
	public static request_processor user_register_proccesor = new Uregister_req_proccesor();
	public static request_processor ride_request_proccesor = new ride_req_proccesor();
	public static request_processor driver_register_proccesor = new Dregister_req_proccesor();
	public static request_processor Ride_retrival_proccesor = new ride_retrival_proccesor();
	public static request_processor Offer_sending_proccesor = new offer_send_proccesor();
	public static request_processor Pending_drivers_getter = new RPSpending_drivers_getter();
	public static request_processor DVerivacation_giver = new Dverivacation_giver();

	// process user registration requests
	public static class Uregister_req_proccesor implements request_processor {
		public user proc_user;// the user to be processed

		@Override
		public void process_request() {
			requestProccesingSystem.users.add(proc_user);
		}
	}

	// process user ride_requests
	public static class ride_req_proccesor implements request_processor {
		public RideRequest proc_ride_req;// the ride request to be processed
		public boolean IsSuccesful = false;// determines if the ride request was succesfuly stored

		@Override
		public void process_request() {
			IsSuccesful = false;
			for (user User : requestProccesingSystem.users) {// sees if the user is registared or not if he is the ride
																// request
				// will be stored else
				// it won't be stored
				if (User.name.equalsIgnoreCase(proc_ride_req.requester_data.name)
						& User.password.equalsIgnoreCase(proc_ride_req.requester_data.password)
						& User.email.equalsIgnoreCase(proc_ride_req.requester_data.email)
						& User.phone_number == proc_ride_req.requester_data.phone_number) {
					IsSuccesful = true;
					requestProccesingSystem.RideRequests.add(proc_ride_req);
					break;
				} else {
					IsSuccesful = false;
				}
			}
		}

	}

	// process driver registration
	public static class Dregister_req_proccesor implements request_processor {
		public driver proc_driver;// the user to be processed

		@Override
		public void process_request() {
			requestProccesingSystem.Pendingdrivers.add(proc_driver);
		}

	}

	// process driver's request to retrive ride requests
	public static class ride_retrival_proccesor implements request_processor {
		public driver proc_driver;// we will use driver information stored inside the variable to get the ride
									// requests
		// relevant to the driver based on the list of his favourite_areas to work in
		public ArrayList<RideRequest> relevant_ride_req = null;// where we store relevate ride
		// requests to the driver if it's null then the driver isn't registered else the
		// he is registered

		@Override
		public void process_request() {
			relevant_ride_req = null;
			for (driver d : requestProccesingSystem.drivers) {// if the driver is registered the request is acepted else
																// request fails
				if (d.name.equalsIgnoreCase(proc_driver.name) & d.password.equalsIgnoreCase(proc_driver.password)
						& d.email.equalsIgnoreCase(proc_driver.email) & d.phone_number == proc_driver.phone_number
						& d.National_id == proc_driver.National_id) {
					relevant_ride_req = new ArrayList<RideRequest>();
					;
					break;
				}
			}

			if (relevant_ride_req == null)
				return;
			else {
				for (RideRequest r : requestProccesingSystem.RideRequests) {// gets the ride requests relevante to the
																			// the driver
					for (String sourceLocation : proc_driver.favourite_areas) {
						if (r.sourceLocation.equalsIgnoreCase(sourceLocation))
							relevant_ride_req.add(r);
					}
				}
			}
		}
	}

	// process offers sent by drivers
	public static class offer_send_proccesor implements request_processor {
		public offer proc_offer;// the offer being sent by the driver
		public Boolean IsSuccesful = false;// determines if the offer sending was succesful

		@Override
		public void process_request() {
			IsSuccesful = false;
			for (driver d : requestProccesingSystem.drivers) {// the driver is registered the request is acepted else
																// request fails
				if (d.name.equalsIgnoreCase(proc_offer.offer_giver_info.name)
						& d.password.equalsIgnoreCase(proc_offer.offer_giver_info.password)
						& d.email.equalsIgnoreCase(proc_offer.offer_giver_info.email)
						& d.phone_number == proc_offer.offer_giver_info.phone_number
						& d.National_id == proc_offer.offer_giver_info.National_id) {
					IsSuccesful = true;
					break;
				}
			}
			if (IsSuccesful == false)
				return;
			else {
				requestProccesingSystem.offers.add(proc_offer);
			}
		}

	}

	// gets the list of pending drivers
	public static class RPSpending_drivers_getter implements request_processor {
		public ArrayList<driver> Pendingdrivers;

		@Override
		public void process_request() {
			Pendingdrivers = requestProccesingSystem.Pendingdrivers;
		}

	}

	// verifies a driver's registration
	public static class Dverivacation_giver implements request_processor {
		public driver verified_driver;
		public String VerificationMode;

		@Override
		public void process_request() {
			if (VerificationMode.equalsIgnoreCase("2")) {
				for (driver d : requestProccesingSystem.Pendingdrivers) {
					if (d.name.equalsIgnoreCase(verified_driver.name)
							& d.password.equalsIgnoreCase(verified_driver.password)
							& d.email.equalsIgnoreCase(verified_driver.email)
							& d.phone_number == verified_driver.phone_number
							& d.National_id == verified_driver.National_id) {
						requestProccesingSystem.Pendingdrivers.remove(d);
					}
				}
				requestProccesingSystem.drivers.add(verified_driver);
			} else {
				for (driver d : requestProccesingSystem.Pendingdrivers) {
					if (d.name.equalsIgnoreCase(verified_driver.name)
							& d.password.equalsIgnoreCase(verified_driver.password)
							& d.email.equalsIgnoreCase(verified_driver.email)
							& d.phone_number == verified_driver.phone_number
							& d.National_id == verified_driver.National_id) {
						requestProccesingSystem.Pendingdrivers.remove(d);
					}
				}
			}
		}

	}

}

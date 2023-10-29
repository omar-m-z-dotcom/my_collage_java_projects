package DriverMediator;

import java.util.ArrayList;

import DataPackage.RideRequest;
import DataPackage.driver;
import DataPackage.offer;
import RequestProccesingSystem.requestProccesingSystem;
import RequestProccesingSystem.requestProccesingSystem.Dregister_req_proccesor;
import RequestProccesingSystem.requestProccesingSystem.offer_send_proccesor;
import RequestProccesingSystem.requestProccesingSystem.ride_retrival_proccesor;


public class driverMediator {
	public static DMRegistration_sender rs = new DMRegistration_sender();
	public static RideRequestsL_Getter RLG = new RideRequestsL_Getter();
	public static DMoffer_sender os = new DMoffer_sender();

	public static class DMRegistration_sender {
		public void send_registration(driver d) {
			Dregister_req_proccesor d1 = (Dregister_req_proccesor) requestProccesingSystem.driver_register_proccesor;
			d1.proc_driver = d;
			d1.process_request();
		}
	}

	public static class RideRequestsL_Getter {
		public ArrayList<RideRequest> get_RequestedRideList(driver d) {
			ride_retrival_proccesor r1 = (ride_retrival_proccesor) requestProccesingSystem.Ride_retrival_proccesor;
			r1.proc_driver = d;
			r1.process_request();
			return r1.relevant_ride_req;
		}
	}

	public static class DMoffer_sender {
		public Boolean send_offer(offer o) {
			offer_send_proccesor o1 = (offer_send_proccesor) requestProccesingSystem.Offer_sending_proccesor;
			o1.proc_offer = o;
			o1.process_request();
			return o1.IsSuccesful;

		}
	}
}

package UserMediator;

import DataPackage.RideRequest;
import DataPackage.user;
import RequestProccesingSystem.requestProccesingSystem;
import RequestProccesingSystem.requestProccesingSystem.Uregister_req_proccesor;
import RequestProccesingSystem.requestProccesingSystem.ride_req_proccesor;

public class userMediator {
	public static UMregistration_sender rs = new UMregistration_sender();
	public static UMRide_Request_Sender rrs = new UMRide_Request_Sender();

	public static class UMregistration_sender {
		public void send_regestration(user u) {
			Uregister_req_proccesor u1 = (Uregister_req_proccesor) requestProccesingSystem.user_register_proccesor;
			u1.proc_user = u;
			u1.process_request();
		}
	}

	public static class UMRide_Request_Sender {
		public Boolean send_ride_request(RideRequest r) {
			ride_req_proccesor r1 = (ride_req_proccesor) requestProccesingSystem.ride_request_proccesor;
			r1.proc_ride_req = r;
			r1.process_request();
			return r1.IsSuccesful;

		}
	}

}

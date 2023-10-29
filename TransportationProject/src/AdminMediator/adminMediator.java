package AdminMediator;

import java.util.ArrayList;

import DataPackage.driver;
import RequestProccesingSystem.requestProccesingSystem;
import RequestProccesingSystem.requestProccesingSystem.Dverivacation_giver;
import RequestProccesingSystem.requestProccesingSystem.RPSpending_drivers_getter;

public class adminMediator {
	public static AMPendingDriversGetter PDG = new AMPendingDriversGetter();
	public static driver_varification_sender dvs = new driver_varification_sender();

	public static class AMPendingDriversGetter {
		public ArrayList<driver> get_pending_drivers() {
			RPSpending_drivers_getter g = (RPSpending_drivers_getter) requestProccesingSystem.Pending_drivers_getter;
			g.process_request();
			return g.Pendingdrivers;
		}
	}

	public static class driver_varification_sender {
		public void send_verification(String varificationMode, driver d) {
			Dverivacation_giver Dver_giver = (Dverivacation_giver) requestProccesingSystem.DVerivacation_giver;
			Dver_giver.VerificationMode = varificationMode;
			Dver_giver.verified_driver = d;
			Dver_giver.process_request();
		}
	}

}

package AdminInterface;

import java.util.ArrayList;
import java.util.Scanner;

import AdminMediator.adminMediator;
import DataPackage.driver;

public class adminInterface {
	public ArrayList<driver> pending_driversList = new ArrayList<driver>();
	public pending_drivers_getter pdg;
	public driver_verifier dv;

	public adminInterface() {
		pdg = new pending_drivers_getter(this);
		dv = new driver_verifier(this);
	}

	public static class pending_drivers_getter {
		adminInterface AI;

		public pending_drivers_getter(adminInterface a) {
			AI = a;
		}

		public void get_pending_drivers() {
			AI.pending_driversList=adminMediator.PDG.get_pending_drivers();
			
			System.out.println("the list of pending drivers are:");
			
			for (driver d : AI.pending_driversList) {
				System.out.println(d.name+" "+d.password+" "+d.email+" "+d.phone_number+" "+d.National_id);
			}
		}
	}

	public static class driver_verifier {
		adminInterface AI;

		public driver_verifier(adminInterface a) {
			AI = a;
		}

		public void verify() {
			driver d = new driver();
			System.out.println("what is the driver's name");
			Scanner sc = new Scanner(System.in);
			d.name = sc.nextLine();
			System.out.println("what is the driver's password");
			d.password = sc.nextLine();
			System.out.println("what is the driver's email");
			d.email = sc.nextLine();
			System.out.println("what is the driver's phone number");
			d.phone_number = sc.nextLong();
			System.out.println("what is the driver's National id");
			d.National_id = sc.nextLong();
			System.out.println("to reject driver enter 1 to accept driver enter 2");
			String registration_Action = sc.nextLine();
			adminMediator.dvs.send_verification(registration_Action, d);
			System.out.println("verification sent");
		}
	}
}

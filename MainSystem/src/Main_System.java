//made by omar mohamed 20196115
//using the strtegy pattern

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//this class is the starter for all classes in the main subsystem
public class Main_System {
	ArrayList<user> users = new ArrayList<user>();// this where we store our users
	ArrayList<driver> drivers = new ArrayList<driver>();// this where we store our drivers
	ArrayList<RideRequest> RideRequests = new ArrayList<RideRequest>();// this where we store our ride_request that are
																		// coming from users
	ArrayList<offer> offers = new ArrayList<offer>();// this where we store our offer that are coming from drivers
	ArrayList<driver> Pendingdrivers = new ArrayList<driver>();// this where we store our drivers pending for
																// registration

	// these are all synchronization objects that synchronize the access to our
	// main_system ArrayLists
	Object UserL_access_Sync = new Object();
	Object DriverL_access_Sync = new Object();
	Object RideRequestL_access_Sync = new Object();
	Object OfferL_access_Sync = new Object();
	Object PendingdriversL_access_Sync = new Object();

	// process user registration
	public static class Uregister_req_proccesor extends Thread implements request_processor {
		Main_System m;

		public Uregister_req_proccesor(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {

				try {
					ServerSocket serversocket = new ServerSocket(22000);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					user u = (user) ObjInStream.readObject();

					synchronized (m.UserL_access_Sync) {

						if (m.users.isEmpty()) {
							m.users.add(u);

							ObjOutStream.writeBoolean(true);
							continue;

						}

						else {
							Boolean issuccessful = null;
							for (user u1 : m.users) {
								if (u1.name.equalsIgnoreCase(u.name) & u1.password.equalsIgnoreCase(u.password)) {
									ObjOutStream.writeBoolean(false);
									issuccessful = false;
									break;
								}
								issuccessful = true;
							}
							if (issuccessful == true) {
								m.users.add(u);

								ObjOutStream.writeBoolean(true);
								continue;
							} else {
								continue;
							}
						}
					}
				} catch (Exception e) {
					continue;
				}
			}

		}

		@Override
		public void run() {
			process_request();
		}

	}

	// process user login
	public static class Ulogin_req_proccesor extends Thread implements request_processor {
		Main_System m;

		public Ulogin_req_proccesor(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {

				try {
					ServerSocket serversocket = new ServerSocket(22001);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					user u = (user) ObjInStream.readObject();

					synchronized (m.UserL_access_Sync) {

						if (m.users.isEmpty()) {
							ObjOutStream.writeBoolean(false);
							continue;

						}

						else {
							Boolean issuccessful = null;
							for (user u1 : m.users) {
								if (u1.name.equalsIgnoreCase(u.name) & u1.password.equalsIgnoreCase(u.password)) {
									ObjOutStream.writeBoolean(true);
									issuccessful = true;
									break;
								}
								issuccessful = false;
							}
							if (issuccessful == false) {
								ObjOutStream.writeBoolean(false);
								continue;
							} else {
								continue;
							}
						}
					}
				} catch (Exception e) {
					continue;
				}
			}

		}

		@Override
		public void run() {
			process_request();
		}

	}

	// process user ride_requests
	public static class ride_req_proccesor extends Thread implements request_processor {
		Main_System m;

		public ride_req_proccesor(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {
				try {
					ServerSocket serversocket = new ServerSocket(22002);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					RideRequest r = (RideRequest) ObjInStream.readObject();

					synchronized (m.RideRequestL_access_Sync) {
						m.RideRequests.add(r);
					}
					continue;
				} catch (Exception e) {
					continue;
				}
			}

		}

		@Override
		public void run() {
			process_request();
		}

	}

	// process driver registration
	public static class Dregister_req_proccesor extends Thread implements request_processor {
		Main_System m;

		public Dregister_req_proccesor(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {

				try {
					ServerSocket serversocket = new ServerSocket(22003);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					driver d = (driver) ObjInStream.readObject();

					synchronized (m.PendingdriversL_access_Sync) {
						this.m.Pendingdrivers.add(d);
					}
					continue;
				} catch (Exception e) {
					continue;
				}
			}

		}

		@Override
		public void run() {
			process_request();
		}

	}

	// process driver login
	public static class Dlogin_req_proccesor extends Thread implements request_processor {
		Main_System m;

		public Dlogin_req_proccesor(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {

				try {
					ServerSocket serversocket = new ServerSocket(22004);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					driver d = (driver) ObjInStream.readObject();

					synchronized (m.DriverL_access_Sync) {

						if (m.drivers.isEmpty()) {
							ObjOutStream.writeBoolean(false);
							continue;

						}

						else {
							Boolean issuccessful = null;
							for (driver d1 : m.drivers) {
								if (d1.name.equalsIgnoreCase(d.name) & d1.password.equalsIgnoreCase(d.password)) {
									ObjOutStream.writeBoolean(true);
									issuccessful = true;
									break;
								}
								issuccessful = false;
							}
							if (issuccessful == false) {
								ObjOutStream.writeBoolean(false);
								continue;
							} else {
								continue;
							}
						}
					}
				} catch (Exception e) {
					continue;
				}
			}

		}
	}

	// process driver's request to retrive ride requests
	public static class ride_retrival_proccesor extends Thread implements request_processor {
		Main_System m;

		public ride_retrival_proccesor(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {
				try {
					ServerSocket serversocket = new ServerSocket(22005);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					synchronized (m.RideRequestL_access_Sync) {
						ObjOutStream.writeObject(m.RideRequests);
					}
					continue;
				} catch (Exception e) {
					continue;
				}
			}

		}

		@Override
		public void run() {
			process_request();
		}

	}

	// process offers sent by drivers
	public static class offer_send_proccesor extends Thread implements request_processor {
		Main_System m;

		public offer_send_proccesor(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {
				try {
					ServerSocket serversocket = new ServerSocket(22006);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					offer o = (offer) ObjInStream.readObject();

					synchronized (m.OfferL_access_Sync) {
						m.offers.add(o);
					}
					continue;
				} catch (Exception e) {
					continue;
				}
			}

		}

		@Override
		public void run() {
			process_request();
		}
	}

	//gets the list of pending drivers
	public static class pending_drivers_getter extends Thread implements request_processor {
		Main_System m;

		public pending_drivers_getter(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {
				try {
					ServerSocket serversocket = new ServerSocket(22007);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					synchronized (m.PendingdriversL_access_Sync) {
						ObjOutStream.writeObject(m.Pendingdrivers);
					}

					continue;
				} catch (Exception e) {
					continue;
				}
			}

		}

		@Override
		public void run() {
			process_request();
		}

	}

	//verifies a driver's registration
	public static class Dverivacation_giver extends Thread implements request_processor {
		Main_System m;

		public Dverivacation_giver(Main_System m) {
			this.m = m;
		}

		@Override
		public void process_request() {
			while (true) {
				try {
					ServerSocket serversocket = new ServerSocket(22008);
					Socket socket = serversocket.accept();
					ObjectOutputStream ObjOutStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ObjInStream = new ObjectInputStream(socket.getInputStream());

					driver d = (driver) ObjInStream.readObject();

					synchronized (m.PendingdriversL_access_Sync) {
						for (driver d1 : m.Pendingdrivers) {
							if (d1.name.equalsIgnoreCase(d.name) & d1.password.equalsIgnoreCase(d.password)) {
								m.Pendingdrivers.remove(d1);
								break;
							}
						}
					}

					synchronized (m.DriverL_access_Sync) {
						m.drivers.add(d);
					}

					continue;
				} catch (Exception e) {
					continue;
				}
			}

		}

		@Override
		public void run() {
			process_request();
		}

	}

}

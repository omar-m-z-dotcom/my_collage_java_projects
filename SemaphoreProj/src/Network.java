import java.util.Random;
import java.util.Scanner;

//the standard counter semaphore with few mods
class semaphore {
	protected int value = 0;

	protected semaphore() {
		value = 0;
	}

	protected semaphore(int initial) {
		value = initial;
	}
	//we will take the device as input as we will use it for the output on screen
	public synchronized void P(Device d) {

		value--;
		if (value < 0) {
			try {
				System.out.println("(" + d.name + ")(" + d.type + ") arrived and waiting");
				wait();
			} catch (InterruptedException e) {
			}
			return;
		}
		System.out.println("(" + d.name + ")(" + d.type + ") arrived");
	}

	public synchronized void V(Device d) {
		value++;
		System.out.println("connection " + d.connection_num + ": " + d.name + " Logged out");
		if (value <= 0)
			notify();
	}
}

class Device extends Thread {
	String name;
	String type;
	Router target_router;//the router that the device will try to connect to
	int connection_num;//the connection slot the device is present in

	public void set_Device() {//sets the name and type of device
		String[] Device_info;
		Device_info = (Network.sc.nextLine()).split(" ");
		name = Device_info[0];
		type = Device_info[1];
	}

	public int getRandomNumber(int min, int max) {//get a random number between min and max
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}

	@Override
	public void run() {
		target_router.queue_s.P(this);
		//critical section
		for (int i = 0; i < target_router.queue_size; i++) {
			if (target_router.d_array[i] == null) {
				target_router.d_array[i] = this;
				connection_num = i + 1;
				break;
			}
		}
		System.out.println("connection " + connection_num + ": " + name + " Occupied");
		System.out.println("connection " + connection_num + ": " + name + " Login");
		System.out.println("connection " + connection_num + ": " + name + " preforms some online activity");
		try {
			Thread.sleep((long) getRandomNumber(5000, 10000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < target_router.queue_size; i++) {
			if (target_router.d_array[i] != null && target_router.d_array[i] == this) {
				target_router.d_array[i] = null;
				break;
			}
		}
		target_router.queue_s.V(this);
	}

}

class Router {
	//the router's devices queueSize
	int queue_size;
	semaphore queue_s;
	public Device[] d_array;
	//setting the devices queue and the semaphore
	Router(int size) {
		queue_size = size;
		d_array = new Device[queue_size];
		queue_s = new semaphore(size);
	}
}

public class Network {
	public static Router r;
	//the list of devices that we want to connect to the router
	public static Device[] d;
	//we will use only one scanner obj throughout the whole program because using multiple scanner objs in java can cause some bugs
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		System.out.println("What is the number of WI-FI Connections?");
		int wi_fi_Buf_size = sc.nextInt();
		r = new Router(wi_fi_Buf_size);
		System.out.println("What is the number of devices Clients want to connect?");
		int devices_num = sc.nextInt();
		d = new Device[devices_num];
		//skips the null at the end of user input that the nextInt methode dosen't skip
		sc.nextLine();
		//setting the devices to connect to the Network class router + setting the name and type of each device
		for (int i=0;i<d.length;i++) {
			d[i] = new Device();
			d[i].target_router = r;
			System.out.println("enter info for device " + (i+1) + ": ");
			d[i].set_Device();
		}
		//starting all devices(threads)
		for (int i=0;i<d.length;i++) {
			d[i].start();
		}
		//waiting for each thread to finish
		for (int i=0;i<d.length;i++) {
			try {
				d[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

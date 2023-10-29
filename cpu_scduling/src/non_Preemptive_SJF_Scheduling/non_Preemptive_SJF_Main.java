package non_Preemptive_SJF_Scheduling;

import java.util.Scanner;

class non_Preemptive_SJF_process {
	String process_name;
	int process_id;
	int arrival_time;
	int Burst_time;
	int execution_start_time;// the time at which the process started executing
	int execution_end_time;// the actual(real) time the process finished at
	int waiting_time;
	int turnaround_time;
	boolean isDone = false;
}

class non_Preemptive_SJF_scheduler {
	non_Preemptive_SJF_process[] ready_queue;
	int process_id = 1000;// you can pick any reasonable number you want
	int current_time = 0;
	String cpu_schdule = "";
	String Processes_stats = "";
	double AVG_waiting_time = 0;
	double AVG_turnaround_time = 0;

	// geting the number of processes to Schedule
	void get_process_data(Scanner input) {
		System.out.print("enter the number of Processes for Scheduling: ");
		ready_queue = new non_Preemptive_SJF_process[input.nextInt()];
		input.nextLine();// skips the null at the end of user input that the nextInt methode dosen't skip
		// initializing the processes in the ready queue
		for (int i = 0; i < ready_queue.length; i++) {
			ready_queue[i] = new non_Preemptive_SJF_process();
		}
		System.out.println(
				"to solve the starvation problem your not allowed to enter new processes in the ready queue until\n"
						+ "all the processes you just entered in the ready queue are all done. this way we will prevent the starvation problem\n"
						+ "because the highest burst time process will always be able to get it's share of the cpu time this way\n");
		// https://www.quora.com/What-is-the-solution-for-the-starvation-problem-in-the-shortest-job-first-scheduling-algorithm
		/*
		 * Prateek Dwivedi , Ph.D. Theoretical Computer Science, Indian Institute of
		 * Technology, Kanpur
		 * 
		 * read his blog
		 */
		try {
			Thread.sleep(10000);// the Main thread will wait for 15 seconds so that you have some time to read
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("let us assume that the size of the queue is the one you just entered\n");
		try {
			Thread.sleep(3000);// the Main thread will wait for 15 seconds so that you have some time to read
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("the scheduler will count the number of process that enered the ready queue"
				+ " without looking at the current number of processes in the ready queue\n" 
				+ "if the count reaches the size of the ready queue then the scheduler won't allow anymore" 
				+ "processes to enter the ready queue untill the ready queue is fully empty\n" 
				+ "once the ready queue is fully empty the count is reset to 0 the scheduler counts and so on\n");
		try {
			Thread.sleep(10000);// the Main thread will wait for 15 seconds so that you have some time to read
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < ready_queue.length; i++) {
			System.out.print("enter the name for process" + (i + 1) + ": ");
			ready_queue[i].process_name = input.nextLine();
			ready_queue[i].process_id = process_id;
			process_id++;
			System.out.print("enter the arrival time for process" + (i + 1) + ": ");
			ready_queue[i].arrival_time = input.nextInt();
			System.out.print("enter the burst time for process" + (i + 1) + ": ");
			ready_queue[i].Burst_time = input.nextInt();
			input.nextLine();// skips the null at the end of user input that the nextInt methode dosen't skip
		}
		// sorting the processes according to their arrival time in an ascending order
		// with bubble sort
		for (int i = 0; i < ready_queue.length - 1; i++) {
			for (int j = 0; j < ready_queue.length - i - 1; j++) {
				if (ready_queue[j].arrival_time > ready_queue[j + 1].arrival_time) {
					non_Preemptive_SJF_process temp = ready_queue[j];
					ready_queue[j] = ready_queue[j + 1];
					ready_queue[j + 1] = temp;
				}
			}
		}
		for (int i = 0; i < ready_queue.length; i++) {
			System.out.println("process " + ready_queue[i].process_name +" has just entered count is " + (i+1));
		}
		System.out.println("count reached " + ready_queue.length + " no more processes are allowed in\n");
		System.out.println("of course this dosen't represent the real arrival time of the processes this just to showcase the theory\n");
		try {
			Thread.sleep(5000);// the Main thread will wait for 15 seconds so that you have some time to read
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println();// prints a new line
	}

	void run_process(non_Preemptive_SJF_process p) {
		p.execution_start_time = current_time;
		p.execution_end_time = current_time + p.Burst_time;
		current_time += p.Burst_time;
		p.isDone = true;
	}

	non_Preemptive_SJF_process search_for_next_process_to_run() {
		int min_burst_time_process_index = 0;
		// find the first process that passes the following criterias:
		// it's arrival time is less than
		// or equal to the current time and is not done yet
		for (int i = 0; i < ready_queue.length && ready_queue[i].arrival_time <= current_time; i++) {
			if (ready_queue[i].isDone == false) {
				// we assume that the first process we found with these criterias is with
				// minimum busrt time
				min_burst_time_process_index = i;
				break;
			}
		}
		// if there isn't any process that is not done at the current time of
		// schduling then return the process at index 0 in the ready_queue(which would
		// be already done)
		// because there won't be any point
		// to continue to search for the minimum busrt time process
		if (ready_queue[min_burst_time_process_index].isDone == true) {
			return ready_queue[min_burst_time_process_index];
		}
		// find the process with minimum busrt time and it's arrival time is less than
		// or equal to the current time and is not done yet
		for (int i = 0; i < ready_queue.length && ready_queue[i].arrival_time <= current_time; i++) {
			if (ready_queue[i].isDone == false
					&& ready_queue[i].Burst_time < ready_queue[min_burst_time_process_index].Burst_time) {
				min_burst_time_process_index = i;
			}
		}
		return ready_queue[min_burst_time_process_index];
	}

	boolean allDone() {
		for (int i = 0; i < ready_queue.length; i++) {
			if (ready_queue[i].isDone == false) {
				return false;
			}
		}
		return true;
	}

	void calculate_turnaround_time() {
		for (int i = 0; i < ready_queue.length; i++) {
			ready_queue[i].turnaround_time = ready_queue[i].execution_end_time - ready_queue[i].arrival_time;
			AVG_turnaround_time += ready_queue[i].turnaround_time;
		}
		AVG_turnaround_time /= ready_queue.length;
	}

	void calculate_waiting_time() {
		for (int i = 0; i < ready_queue.length; i++) {
			ready_queue[i].waiting_time = ready_queue[i].turnaround_time - ready_queue[i].Burst_time;
			AVG_waiting_time += ready_queue[i].waiting_time;
		}
		AVG_waiting_time /= ready_queue.length;
	}

	void printCpuSchedule() {
		// sorting the processes according to their execution start time in an ascending
		// order
		// with bubble sort
		for (int i = 0; i < ready_queue.length - 1; i++) {
			for (int j = 0; j < ready_queue.length - i - 1; j++) {
				if (ready_queue[j].execution_start_time > ready_queue[j + 1].execution_start_time) {
					non_Preemptive_SJF_process temp = ready_queue[j];
					ready_queue[j] = ready_queue[j + 1];
					ready_queue[j + 1] = temp;
				}
			}
		}
		// printing the cpu schdule
		cpu_schdule += "Non-Preemptive SJF Schedule:\n";
		for (int i = 0; i < ready_queue.length; i++) {
			if (i == 0) {
				cpu_schdule += (ready_queue[i].execution_start_time + "|--" + ready_queue[i].process_name + "--|"
						+ ready_queue[i].execution_end_time);
			} else {
				cpu_schdule += ("|--" + ready_queue[i].process_name + "--|" + ready_queue[i].execution_end_time);
			}
		}
		cpu_schdule += "\n\n";
		System.out.println(cpu_schdule);
		System.out.println();// prints a new line
	}

	void print_processes_info() {
		Processes_stats += ("Non-Preemptive SJF Scheduling statistics :\n");
		Processes_stats += String.format("%20s%20s%20s%20s\n", "ProcessName", "ProcessId", "WaitingTime",
				"TurnAroundTime");
		for (int i = 0; i < ready_queue.length; i++) {
			Processes_stats += String.format("%20s%20s%20s%20s\n", ready_queue[i].process_name,
					ready_queue[i].process_id, ready_queue[i].waiting_time, ready_queue[i].turnaround_time);
		}
		Processes_stats += String.format("%40s%20f%20f\n", "Average", AVG_waiting_time, AVG_turnaround_time);
		System.out.println(Processes_stats);
		System.out.println();// prints a new line
	}

	void start_Scheduling() {
		// must be reset before starting the Schedule
		current_time = 0;
		cpu_schdule = "";
		Processes_stats = "";
		AVG_waiting_time = 0;
		AVG_turnaround_time = 0;
		while (!allDone()) {
			non_Preemptive_SJF_process process_to_run = search_for_next_process_to_run();
			if (process_to_run.isDone == false)
				run_process(process_to_run);
			else {// just in case the returned process is already done which can happen if the
					// next process arrival time is bigger than the current time
				current_time++;
				continue;
			}
		}
		calculate_turnaround_time();
		calculate_waiting_time();
		printCpuSchedule();
		print_processes_info();
	}
}

public class non_Preemptive_SJF_Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String answer = null;
		non_Preemptive_SJF_scheduler schduler = new non_Preemptive_SJF_scheduler();
		do {
			schduler.get_process_data(sc);
			schduler.start_Scheduling();
			System.out.print("do you want to continue: ");
			answer = sc.nextLine();
		} while (answer.equalsIgnoreCase("yes"));
	}

}

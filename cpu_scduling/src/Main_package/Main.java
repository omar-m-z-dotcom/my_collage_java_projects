package Main_package;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Scanner;

class non_preemptive_priority_process {
	public String process_name;
	public int process_id;
	public int arrival_time;
	public int Burst_time;
	public int priority;// the highest priority is 1
	public int execution_start_time;// the time at which the process started executing
	public int execution_end_time;// the actual(real) time the process finished at + the context switch time
	public int waiting_time;
	public int turnaround_time;
	public boolean isDone = false;
}

class non_preemptive_priority_Scheduler {
	non_preemptive_priority_process[] ready_queue;
	int context_switch;
	int process_id = 1000;// you can pick any reasonable number you want
	int current_time = 0;
	String cpu_schdule = "";
	String Processes_stats = "";
	double AVG_waiting_time = 0;
	double AVG_turnaround_time = 0;

	void get_process_data(Scanner input) {
		// geting some info about the Scheduler and the number of processes to Schedule
		System.out.print("enter the number of Processes for Scheduling: ");
		int inputNumberOfProcesses = input.nextInt();
		ready_queue = new non_preemptive_priority_process[inputNumberOfProcesses];
		System.out.print("enter the context switch time for the Scheduler: ");
		context_switch = input.nextInt();
		input.nextLine();// skips the null at the end of user input that the nextInt methode dosen't skip
		// initializing the processes in the ready queue
		for (int i = 0; i < inputNumberOfProcesses; i++) {
			ready_queue[i] = new non_preemptive_priority_process();
		}
		for (int i = 0; i < inputNumberOfProcesses; i++) {
			System.out.print("enter the name for process" + (i + 1) + ": ");
			ready_queue[i].process_name = input.nextLine();
			ready_queue[i].process_id = process_id;
			process_id++;
			System.out.print("enter the arrival time for process" + (i + 1) + ": ");
			ready_queue[i].arrival_time = input.nextInt();
			System.out.print("enter the burst time for process" + (i + 1) + ": ");
			ready_queue[i].Burst_time = input.nextInt();
			System.out.print("enter the priority for process" + (i + 1) + ": ");
			ready_queue[i].priority = input.nextInt();
			input.nextLine();// skips the null at the end of user input that the nextInt methode dosen't skip
		}
		// sorting the processes according to their arrival time in an ascending order
		// with bubble sort
		for (int i = 0; i < inputNumberOfProcesses - 1; i++) {
			for (int j = 0; j < inputNumberOfProcesses - i - 1; j++) {
				if (ready_queue[j].arrival_time > ready_queue[j + 1].arrival_time) {
					non_preemptive_priority_process temp = ready_queue[j];
					ready_queue[j] = ready_queue[j + 1];
					ready_queue[j + 1] = temp;
				}
			}
		}
		System.out.println();// prints a new line
	}

	void resolve_aging() {// resolves starvation by divding the priority of the process with lowest
							// priority by 2
		int min_priority_process_index = 0;
		// find the first process that passes the following criterias:
		// it's arrival time is less than
		// or equal to the current time and is not done yet
		for (int i = 0; i < ready_queue.length && ready_queue[i].arrival_time <= current_time; i++) {
			if (ready_queue[i].isDone == false) {
				// we assume that the first process we found with these criterias is the lowest
				// priority process
				min_priority_process_index = i;
				break;
			}
		}
		// if there isn't any processes that are not done at the current time of
		// schduling then abort because there won't be any point
		// to resolve the aging problem
		if (ready_queue[min_priority_process_index].isDone == true)
			return;
		// find the process with the lowest priority and it's arrival time is less than
		// or equal to the current time and is not done yet
		for (int i = 0; i < ready_queue.length && ready_queue[i].arrival_time <= current_time; i++) {
			if (ready_queue[i].isDone == false
					&& ready_queue[i].priority > ready_queue[min_priority_process_index].priority) {
				min_priority_process_index = i;
			}
		}
		// dosen't already has the highest priority
		if (ready_queue[min_priority_process_index].priority != 1) {
			ready_queue[min_priority_process_index].priority /= 2;
			System.out.println("lowest priority process found at time " + current_time + " is "
					+ ready_queue[min_priority_process_index].process_name + " and it's priority has increased to "
					+ ready_queue[min_priority_process_index].priority + "\n");
		}
	}

	void run_process(non_preemptive_priority_process p) {
		p.execution_start_time = current_time;
		p.execution_end_time = current_time + p.Burst_time + context_switch;
		current_time += p.Burst_time + context_switch;
		p.isDone = true;
	}

	non_preemptive_priority_process search_for_next_process_to_run() {
		int max_priority_process_index = 0;
		// find the first process that passes the following criterias
		for (int i = 0; i < ready_queue.length && ready_queue[i].arrival_time <= current_time; i++) {
			if (ready_queue[i].isDone == false) {
				// we assume that the first process we found with these criterias is the highest
				// priority process
				max_priority_process_index = i;
				break;
			}
		}
		// if there isn't any process that is not done at the current time of
		// schduling then return the process at index 0 in the ready_queue(which would
		// be already done)
		// because there won't be any point
		// to continue to search for the highest priority process
		if (ready_queue[max_priority_process_index].isDone == true) {
			return ready_queue[max_priority_process_index];
		}

		// find the process with the highest priority and it's arrival time is less than
		// or equal to the current time and is not done yet
		for (int i = 0; i < ready_queue.length && ready_queue[i].arrival_time <= current_time; i++) {
			if (ready_queue[i].isDone == false
					&& ready_queue[i].priority < ready_queue[max_priority_process_index].priority) {
				max_priority_process_index = i;
			}
		}
		return ready_queue[max_priority_process_index];
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
					non_preemptive_priority_process temp = ready_queue[j];
					ready_queue[j] = ready_queue[j + 1];
					ready_queue[j + 1] = temp;
				}
			}
		}
		// printing the cpu schdule
		cpu_schdule += "Non-Preemptive Priority Schedule:\n";
		for (int i = 0; i < ready_queue.length; i++) {
			if (i == 0) {
				cpu_schdule += (ready_queue[i].execution_start_time + "|--" + ready_queue[i].process_name + "--|"
						+ (ready_queue[i].execution_end_time - context_switch) + "|--context-switch--|"
						+ (ready_queue[i].execution_end_time));
			} else {
				cpu_schdule += ("|--" + ready_queue[i].process_name + "--|"
						+ (ready_queue[i].execution_end_time - context_switch) + "|--context-switch--|"
						+ (ready_queue[i].execution_end_time));
			}
		}
		cpu_schdule += "\n\n";
		System.out.println(cpu_schdule);
		System.out.println();// prints a new line
	}

	void print_processes_info() {
		Processes_stats += ("Non-Preemptive Priority Scheduling statistics :\n");
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
			non_preemptive_priority_process process_to_run = search_for_next_process_to_run();
			if (process_to_run.isDone == false)
				run_process(process_to_run);
			else {// just in case the returned process is already done which can happen if the
					// next process arrival time is bigger than the current time
				current_time++;
				continue;
			}
			resolve_aging();
		}
		calculate_turnaround_time();
		calculate_waiting_time();
		printCpuSchedule();
		print_processes_info();
	}
}

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
			System.out.println("process " + ready_queue[i].process_name + " has just entered count is " + (i + 1));
		}
		System.out.println("count reached " + ready_queue.length + " no more processes are allowed in\n");
		System.out.println(
				"of course this dosen't represent the real arrival time of the processes this just to showcase the theory\n");
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

class Preemptive_SJF_process {
	String process_name;
	int process_id;
	int arrival_time;
	int Burst_time;
	int remaining_Burst_time;
	int execution_start_time;// the time at which the process started executing
	int execution_end_time;// the actual(real) time the process finished at + the context switch
	int waiting_time;
	int turnaround_time;
	boolean isDone = false;
}

class Preemptive_SJF_scheduler {
	Preemptive_SJF_process[] ready_queue;
	int process_id = 1000;// you can pick any reasonable number you want
	int current_time = 0;
	int context_switch;
	String cpu_schedule = "";
	String Processes_stats = "";
	double AVG_waiting_time = 0;
	double AVG_turnaround_time = 0;

	// geting the number of processes to Schedule and some info about the schduler
	void get_process_data(Scanner input) {
		System.out.print("enter the number of Processes for Scheduling: ");
		ready_queue = new Preemptive_SJF_process[input.nextInt()];
		System.out.print("enter the context switch time for this schduler: ");
		context_switch = input.nextInt();
		input.nextLine();// skips the null at the end of user input that the nextInt methode dosen't skip
		// initializing the processes in the ready queue
		for (int i = 0; i < ready_queue.length; i++) {
			ready_queue[i] = new Preemptive_SJF_process();
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
			ready_queue[i].remaining_Burst_time = ready_queue[i].Burst_time;
			input.nextLine();// skips the null at the end of user input that the nextInt methode dosen't skip
		}
		// sorting the processes according to their arrival time in an ascending order
		// with bubble sort
		for (int i = 0; i < ready_queue.length - 1; i++) {
			for (int j = 0; j < ready_queue.length - i - 1; j++) {
				if (ready_queue[j].arrival_time > ready_queue[j + 1].arrival_time) {
					Preemptive_SJF_process temp = ready_queue[j];
					ready_queue[j] = ready_queue[j + 1];
					ready_queue[j + 1] = temp;
				}
			}
		}
		for (int i = 0; i < ready_queue.length; i++) {
			System.out.println("process " + ready_queue[i].process_name + " has just entered count is " + (i + 1));
		}
		System.out.println("count reached " + ready_queue.length + " no more processes are allowed in\n");
		System.out.println(
				"of course this dosen't represent the real arrival time of the processes this just to showcase the theory\n");
		try {
			Thread.sleep(5000);// the Main thread will wait for 15 seconds so that you have some time to read
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println();// prints a new line
	}

	class dispatcher {
		static Preemptive_SJF_process prev_process = null;
		static Preemptive_SJF_scheduler scheduler = null;

		// we run the process for one second then we search for the next process to run.
		// we reapet this second by second.
		static void run_process(Preemptive_SJF_process choosen_process) {
			// there was no previous process
			if (prev_process == null) {
				// prev_process now references the choosen_process
				prev_process = choosen_process;
				// the current time would be zero in this if statment
				choosen_process.execution_start_time = scheduler.current_time;
				scheduler.current_time++;
				choosen_process.execution_end_time = scheduler.current_time;
				choosen_process.remaining_Burst_time--;
				if (choosen_process.remaining_Burst_time == 0) {
					choosen_process.isDone = true;
				}
			}
			// the prev_process is the same process that is being currently
			// run(choosen_process)
			else if (prev_process.process_id == choosen_process.process_id) {
				scheduler.current_time++;
				choosen_process.execution_end_time = scheduler.current_time;
				choosen_process.remaining_Burst_time--;
				if (choosen_process.remaining_Burst_time == 0) {
					choosen_process.isDone = true;
				}
			}
			// the prev_process isn't the same process that is being currently
			// run(choosen_process)
			else if (prev_process.process_id != choosen_process.process_id) {
				prev_process.execution_end_time = (scheduler.current_time + scheduler.context_switch);
				cpu_schedule_printer.update_cpu_schdule(prev_process);
				scheduler.current_time += scheduler.context_switch;
				// the choosen_process is still the process with the minimum burst time
				// currently(after the context switch)
				if (choosen_process.process_id == scheduler.search_for_next_process_to_run().process_id) {
					prev_process = choosen_process;
					choosen_process.execution_start_time = scheduler.current_time;
					scheduler.current_time++;
					choosen_process.execution_end_time = scheduler.current_time;
					choosen_process.remaining_Burst_time--;
					if (choosen_process.remaining_Burst_time == 0) {
						choosen_process.isDone = true;
					}
				}
				// the choosen_process isn't the process with the minimum burst time
				// currently(after the context switch)
				else {
					choosen_process = scheduler.search_for_next_process_to_run();
					prev_process = choosen_process;
					choosen_process.execution_start_time = scheduler.current_time;
					scheduler.current_time++;
					choosen_process.execution_end_time = scheduler.current_time;
					choosen_process.remaining_Burst_time--;
					if (choosen_process.remaining_Burst_time == 0) {
						choosen_process.isDone = true;
					}
				}
			}
		}
	}

	Preemptive_SJF_process search_for_next_process_to_run() {
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
					&& ready_queue[i].remaining_Burst_time < ready_queue[min_burst_time_process_index].remaining_Burst_time) {
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

	class cpu_schedule_printer {
		static boolean was_called_before = false;// indicates if the function update_cpu_schdule has been called before
		static Preemptive_SJF_scheduler scheduler = null;

		static void update_cpu_schdule(Preemptive_SJF_process p) {
			if (was_called_before) {
				scheduler.cpu_schedule += ("|--" + p.process_name + "--|"
						+ (p.execution_end_time - scheduler.context_switch) + "|--context-switch--|"
						+ p.execution_end_time);
			} else {
				scheduler.cpu_schedule += (p.execution_start_time + "|--" + p.process_name + "--|"
						+ (p.execution_end_time - scheduler.context_switch) + "|--context-switch--|"
						+ p.execution_end_time);
				was_called_before = true;
			}
		}

		static void print_cpu_schdule() {
			scheduler.cpu_schedule += "\n\n";
			System.out.println(scheduler.cpu_schedule);
			System.out.println();// prints a new line
		}
	}

	void print_processes_info() {
		Processes_stats += ("Preemptive SJF Scheduling statistics :\n");
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
		cpu_schedule = "";
		Processes_stats = "";
		AVG_waiting_time = 0;
		AVG_turnaround_time = 0;
		Preemptive_SJF_scheduler.cpu_schedule_printer.was_called_before = false;
		Preemptive_SJF_scheduler.cpu_schedule_printer.scheduler = this;
		Preemptive_SJF_scheduler.dispatcher.prev_process = null;
		Preemptive_SJF_scheduler.dispatcher.scheduler = this;
		while (!allDone()) {
			Preemptive_SJF_process process = search_for_next_process_to_run();
			if (process.isDone == false)
				Preemptive_SJF_scheduler.dispatcher.run_process(process);
			else {// just in case the returned process is already done which can happen if the
					// next process arrival time is bigger than the current time
				current_time++;
				continue;
			}
		}
		// to add the last process that ran to the cpu schdule
		Preemptive_SJF_scheduler.cpu_schedule_printer
				.update_cpu_schdule(Preemptive_SJF_scheduler.dispatcher.prev_process);
		calculate_turnaround_time();
		calculate_waiting_time();
		Preemptive_SJF_scheduler.cpu_schedule_printer.print_cpu_schdule();
		print_processes_info();
	}
}

class AGAT_process {
	String process_name;
	int process_id;
	int arrival_time;
	int Burst_time;
	int remaining_Burst_time;
	int priority;
	int AGAT;
	int quantum;
	int remaining_quantum;
	int execution_start_time;// the time at which the process started executing
	int execution_end_time;// the actual(real) time the process finished at
	int waiting_time;
	int turnaround_time;
	boolean isDone = false;
	boolean inReadyQueue = false;

	int get_40percent_quantum() {
		return (int) (Math.round(quantum * 0.4));
	}
}

class AGAT_scheduler {
	AGAT_process[] list_of_processes;
	int process_id = 1000;// you can pick any reasonable number you want
	int current_time = 0;
	String cpu_schedule = "";
	String Processes_stats = "";
	double AVG_waiting_time = 0;
	double AVG_turnaround_time = 0;

	// geting the number of processes to Schedule
	void get_process_data(Scanner input) {
		System.out.print("enter the number of Processes for Scheduling: ");
		list_of_processes = new AGAT_process[input.nextInt()];
		input.nextLine();// skips the null at the end of user input that the nextInt methode dosen't skip
		// initializing the processes in the ready queue
		for (int i = 0; i < list_of_processes.length; i++) {
			list_of_processes[i] = new AGAT_process();
		}
		for (int i = 0; i < list_of_processes.length; i++) {
			System.out.print("enter the name for process" + (i + 1) + ": ");
			list_of_processes[i].process_name = input.nextLine();
			list_of_processes[i].process_id = process_id;
			process_id++;
			System.out.print("enter the arrival time for process" + (i + 1) + ": ");
			list_of_processes[i].arrival_time = input.nextInt();
			System.out.print("enter the burst time for process" + (i + 1) + ": ");
			list_of_processes[i].Burst_time = input.nextInt();
			list_of_processes[i].remaining_Burst_time = list_of_processes[i].Burst_time;
			System.out.print("enter the priority for process" + (i + 1) + ": ");
			list_of_processes[i].priority = input.nextInt();
			System.out.print("enter the quantum time for process" + (i + 1) + ": ");
			list_of_processes[i].quantum = input.nextInt();
			list_of_processes[i].remaining_quantum = list_of_processes[i].quantum;
			input.nextLine();// skips the null at the end of user input that the nextInt methode dosen't skip
		}
		// sorting the processes according to their arrival time in an ascending order
		// with bubble sort
		for (int i = 0; i < list_of_processes.length - 1; i++) {
			for (int j = 0; j < list_of_processes.length - i - 1; j++) {
				if (list_of_processes[j].arrival_time > list_of_processes[j + 1].arrival_time) {
					AGAT_process temp = list_of_processes[j];
					list_of_processes[j] = list_of_processes[j + 1];
					list_of_processes[j + 1] = temp;
				}
			}
		}
		System.out.println();// prints a new line
	}

	void print_quantum_history(AGAT_process p) {
		System.out.println("the quantum for process " + p.process_name + " at " + current_time + " was: " + p.quantum);
		System.out.println();// prints a new line
	}

	void print_AGAT_history() {
		System.out.println("AGAT history at time " + current_time + ":");
		for (int i = 0; i < list_of_processes.length; i++) {
			System.out.printf("%5s", list_of_processes[i].process_name);
		}
		System.out.println();// prints a new line
		for (int i = 0; i < list_of_processes.length; i++) {
			System.out.printf("%5s", ((list_of_processes[i].quantum > 0) ? list_of_processes[i].AGAT : "-"));
		}
		System.out.println("\n");// prints two new lines
	}

	class AGAT_calculator {
		static AGAT_scheduler scheduler = null;

		static void calculate_AGAT() {
			double v1, v2;
			if (scheduler.list_of_processes[scheduler.list_of_processes.length - 1].arrival_time > 10) {
				v1 = scheduler.list_of_processes[scheduler.list_of_processes.length - 1].arrival_time / 10.0;
			} else
				v1 = 1;

			int max_remaining_burst_time = get_max_remaining_burst_time();

			if (max_remaining_burst_time > 10) {
				v2 = max_remaining_burst_time / 10.0;
			} else
				v2 = 1;

			for (int i = 0; i < scheduler.list_of_processes.length; i++) {
				scheduler.list_of_processes[i].AGAT = ((10 - scheduler.list_of_processes[i].priority)
						+ (int) (Math.ceil(scheduler.list_of_processes[i].arrival_time / v1))
						+ (int) (Math.ceil(scheduler.list_of_processes[i].remaining_Burst_time / v2)));
			}
		}

		private static int get_max_remaining_burst_time() {
			int max_remaining_burst_time_process_index = 0;
			// find the first process whose remaining burst time is not zero
			for (int i = 0; i < scheduler.list_of_processes.length; i++) {
				if (scheduler.list_of_processes[i].remaining_Burst_time != 0) {
					// we assume that the first process we found is with
					// max remaining burst time
					max_remaining_burst_time_process_index = i;
					break;
				}
			}
			// find the process with max remaining burst time
			for (int i = 0; i < scheduler.list_of_processes.length; i++) {
				if (scheduler.list_of_processes[i].remaining_Burst_time > scheduler.list_of_processes[max_remaining_burst_time_process_index].remaining_Burst_time) {
					max_remaining_burst_time_process_index = i;
				}
			}
			return scheduler.list_of_processes[max_remaining_burst_time_process_index].remaining_Burst_time;
		}
	}

	class dispatcher {
		static AGAT_scheduler scheduler = null;
		static Deque<AGAT_process> ready_queue = new ArrayDeque<AGAT_process>();

		static void run_processes() {
			if (ready_queue.isEmpty()) {
				scheduler.current_time++;
			} else {
				if (ready_queue.peekFirst().remaining_quantum == ready_queue.peekFirst().quantum) {
					AGAT_scheduler.AGAT_calculator.calculate_AGAT();
					scheduler.print_AGAT_history();
					ready_queue.peekFirst().execution_start_time = scheduler.current_time;

					if (ready_queue.peekFirst().remaining_Burst_time < ready_queue.peekFirst()
							.get_40percent_quantum()) {
						scheduler.current_time += ready_queue.peekFirst().remaining_Burst_time;
						ready_queue.peekFirst().execution_end_time = scheduler.current_time;
						ready_queue.peekFirst().remaining_quantum -= ready_queue.peekFirst().remaining_Burst_time;
						ready_queue.peekFirst().remaining_Burst_time = 0;
					} else {
						scheduler.current_time += ready_queue.peekFirst().get_40percent_quantum();
						ready_queue.peekFirst().execution_end_time = scheduler.current_time;
						ready_queue.peekFirst().remaining_Burst_time -= ready_queue.peekFirst().get_40percent_quantum();
						ready_queue.peekFirst().remaining_quantum -= ready_queue.peekFirst().get_40percent_quantum();
					}

					if (ready_queue.peekFirst().remaining_Burst_time <= 0) {
						ready_queue.peekFirst().isDone = true;
						ready_queue.peekFirst().quantum = 0;
						scheduler.print_quantum_history(ready_queue.peekFirst());
						AGAT_scheduler.cpu_schedule_printer.update_cpu_schdule(ready_queue.peekFirst());
						ready_queue.remove();
						return;
					}

					if (ready_queue.peekFirst().remaining_quantum == 0) {
						ready_queue.peekFirst().quantum += 2;
						ready_queue.peekFirst().remaining_quantum = ready_queue.peekFirst().quantum;
						scheduler.print_quantum_history(ready_queue.peekFirst());
						AGAT_scheduler.cpu_schedule_printer.update_cpu_schdule(ready_queue.peekFirst());
						AGAT_process process = ready_queue.remove();
						ready_queue.addLast(process);
						return;
					}
					return;
				}

				else if (get_min_AGAT_process().process_id == ready_queue.peekFirst().process_id) {
					scheduler.current_time++;
					ready_queue.peekFirst().execution_end_time = scheduler.current_time;
					ready_queue.peekFirst().remaining_Burst_time--;
					ready_queue.peekFirst().remaining_quantum--;

					if (ready_queue.peekFirst().remaining_Burst_time <= 0) {
						ready_queue.peekFirst().isDone = true;
						ready_queue.peekFirst().quantum = 0;
						scheduler.print_quantum_history(ready_queue.peekFirst());
						AGAT_scheduler.cpu_schedule_printer.update_cpu_schdule(ready_queue.peekFirst());
						ready_queue.remove();
						return;
					}

					if (ready_queue.peekFirst().remaining_quantum == 0) {
						ready_queue.peekFirst().quantum += 2;
						ready_queue.peekFirst().remaining_quantum = ready_queue.peekFirst().quantum;
						scheduler.print_quantum_history(ready_queue.peekFirst());
						AGAT_scheduler.cpu_schedule_printer.update_cpu_schdule(ready_queue.peekFirst());
						AGAT_process process = ready_queue.remove();
						ready_queue.addLast(process);
						return;
					}
					return;
				}

				else {
					AGAT_process min_AGAT_process = get_min_AGAT_process();
					ready_queue.peekFirst().execution_end_time = scheduler.current_time;
					ready_queue.peekFirst().quantum += ready_queue.peekFirst().remaining_quantum;
					ready_queue.peekFirst().remaining_quantum = ready_queue.peekFirst().quantum;
					AGAT_scheduler.cpu_schedule_printer.update_cpu_schdule(ready_queue.peekFirst());
					AGAT_process process = ready_queue.remove();
					ready_queue.addLast(process);
					ready_queue.remove(min_AGAT_process);
					ready_queue.addFirst(min_AGAT_process);

					if (ready_queue.peekFirst().remaining_Burst_time <= 0) {
						ready_queue.peekFirst().isDone = true;
						ready_queue.peekFirst().quantum = 0;
						scheduler.print_quantum_history(ready_queue.peekFirst());
						AGAT_scheduler.cpu_schedule_printer.update_cpu_schdule(ready_queue.peekFirst());
						ready_queue.remove();
						return;
					}

					if (ready_queue.peekFirst().remaining_quantum == 0) {
						ready_queue.peekFirst().quantum += 2;
						ready_queue.peekFirst().remaining_quantum = ready_queue.peekFirst().quantum;
						scheduler.print_quantum_history(ready_queue.peekFirst());
						AGAT_scheduler.cpu_schedule_printer.update_cpu_schdule(ready_queue.peekFirst());
						AGAT_process process1 = ready_queue.remove();
						ready_queue.addLast(process1);
						return;
					}
					return;
				}
			}
		}

		static private AGAT_process get_min_AGAT_process() {
			AGAT_process min_AGAT_process = ready_queue.peekFirst();
			AGAT_process compared_process;
			for (Iterator<AGAT_process> itr = ready_queue.iterator(); itr.hasNext();) {
				compared_process = itr.next();
				if (compared_process.AGAT < min_AGAT_process.AGAT) {
					min_AGAT_process = compared_process;
				}
			}
			return min_AGAT_process;
		}
	}

	boolean allDone() {
		for (int i = 0; i < list_of_processes.length; i++) {
			if (list_of_processes[i].isDone == false) {
				return false;
			}
		}
		return true;
	}

	void calculate_turnaround_time() {
		for (int i = 0; i < list_of_processes.length; i++) {
			list_of_processes[i].turnaround_time = list_of_processes[i].execution_end_time
					- list_of_processes[i].arrival_time;
			AVG_turnaround_time += list_of_processes[i].turnaround_time;
		}
		AVG_turnaround_time /= list_of_processes.length;
	}

	void calculate_waiting_time() {
		for (int i = 0; i < list_of_processes.length; i++) {
			list_of_processes[i].waiting_time = list_of_processes[i].turnaround_time - list_of_processes[i].Burst_time;
			AVG_waiting_time += list_of_processes[i].waiting_time;
		}
		AVG_waiting_time /= list_of_processes.length;
	}

	class cpu_schedule_printer {
		static AGAT_scheduler scheduler = null;
		static boolean was_called_before = false;// indicates if the function update_cpu_schdule has been called before

		static void update_cpu_schdule(AGAT_process p) {
			if (was_called_before) {
				scheduler.cpu_schedule += ("|--" + p.process_name + "--|" + p.execution_end_time);
			} else {
				scheduler.cpu_schedule += (p.execution_start_time + "|--" + p.process_name + "--|"
						+ p.execution_end_time);
				was_called_before = true;
			}
		}

		static void print_cpu_schdule() {
			scheduler.cpu_schedule += "\n\n";
			System.out.println(scheduler.cpu_schedule);
			System.out.println();// prints a new line
		}
	}

	void print_processes_info() {
		Processes_stats += ("Preemptive SJF Scheduling statistics :\n");
		Processes_stats += String.format("%20s%20s%20s%20s\n", "ProcessName", "ProcessId", "WaitingTime",
				"TurnAroundTime");
		for (int i = 0; i < list_of_processes.length; i++) {
			Processes_stats += String.format("%20s%20s%20s%20s\n", list_of_processes[i].process_name,
					list_of_processes[i].process_id, list_of_processes[i].waiting_time,
					list_of_processes[i].turnaround_time);
		}
		Processes_stats += String.format("%40s%20f%20f\n", "Average", AVG_waiting_time, AVG_turnaround_time);
		System.out.println(Processes_stats);
		System.out.println();// prints a new line
	}

	void start_Scheduling() {
		// must be reset before starting the Schedule
		current_time = 0;
		cpu_schedule = "";
		Processes_stats = "";
		AVG_waiting_time = 0;
		AVG_turnaround_time = 0;
		AGAT_scheduler.dispatcher.scheduler = this;
		AGAT_scheduler.dispatcher.ready_queue = new ArrayDeque<AGAT_process>();
		AGAT_scheduler.AGAT_calculator.scheduler = this;
		AGAT_scheduler.cpu_schedule_printer.scheduler = this;
		AGAT_scheduler.cpu_schedule_printer.was_called_before = false;
		for (int i = 0; i < list_of_processes.length; i++) {
			print_quantum_history(list_of_processes[i]);
		}
		while (!allDone()) {
			for (int i = 0; i < list_of_processes.length && list_of_processes[i].arrival_time <= current_time; i++) {
				if (list_of_processes[i].isDone == false && list_of_processes[i].inReadyQueue == false) {
					list_of_processes[i].inReadyQueue = true;
					AGAT_scheduler.dispatcher.ready_queue.addLast(list_of_processes[i]);
				}
			}
			AGAT_scheduler.dispatcher.run_processes();
		}
		calculate_turnaround_time();
		calculate_waiting_time();
		cpu_schedule_printer.print_cpu_schdule();
		print_processes_info();
	}
}

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("choose one of the following options:\n");
			System.out.println("1. non preemptive priority Scheduler");
			System.out.println("2. non Preemptive SJF scheduler");
			System.out.println("3. Preemptive SJF scheduler");
			System.out.println("4. AGAT scheduler");
			System.out.println("0. exit");
			int answer = sc.nextInt();
			if (answer == 1) {
				non_preemptive_priority_Scheduler scheduler = new non_preemptive_priority_Scheduler();
				scheduler.get_process_data(sc);
				scheduler.start_Scheduling();
			} else if (answer == 2) {
				non_Preemptive_SJF_scheduler scheduler = new non_Preemptive_SJF_scheduler();
				scheduler.get_process_data(sc);
				scheduler.start_Scheduling();
			} else if (answer == 3) {
				Preemptive_SJF_scheduler scheduler = new Preemptive_SJF_scheduler();
				scheduler.get_process_data(sc);
				scheduler.start_Scheduling();
			} else if (answer == 4) {
				AGAT_scheduler scheduler = new AGAT_scheduler();
				scheduler.get_process_data(sc);
				scheduler.start_Scheduling();
			} else if (answer == 0) {
				System.exit(0);
			}
		}
	}

}

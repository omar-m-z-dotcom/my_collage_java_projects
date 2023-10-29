package non_preemptive_priority_Scheduling;

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

public class non_preemptive_priority_Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		non_preemptive_priority_Scheduler scheduler = new non_preemptive_priority_Scheduler();
		scheduler.get_process_data(sc);
		scheduler.start_Scheduling();
	}

}

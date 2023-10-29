package Preemptive_SJF_schduling;

import java.util.Scanner;

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
		//to add the last process that ran to the cpu schdule
		Preemptive_SJF_scheduler.cpu_schedule_printer.update_cpu_schdule(Preemptive_SJF_scheduler.dispatcher.prev_process);
		calculate_turnaround_time();
		calculate_waiting_time();
		Preemptive_SJF_scheduler.cpu_schedule_printer.print_cpu_schdule();
		print_processes_info();
	}
}

public class Preemptive_SJF_Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String answer = null;
		Preemptive_SJF_scheduler schduler = new Preemptive_SJF_scheduler();
		do {
			schduler.get_process_data(sc);
			schduler.start_Scheduling();
			System.out.print("do you want to continue: ");
			answer = sc.nextLine();
		}while(answer.equalsIgnoreCase("yes"));
	}

}

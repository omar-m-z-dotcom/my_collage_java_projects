package AGAT_schduling;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Scanner;

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
			System.out.printf("%5d", ((list_of_processes[i].quantum > 0) ? list_of_processes[i].AGAT : "-"));
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
				if (scheduler.list_of_processes[i].remaining_Burst_time > 
				scheduler.list_of_processes[max_remaining_burst_time_process_index].remaining_Burst_time) {
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
			for (int i = 0;i < list_of_processes.length && list_of_processes[i].arrival_time<= current_time;i++) {
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

public class AGAT_Main {

	public static void main(String[] args) {
	}

}

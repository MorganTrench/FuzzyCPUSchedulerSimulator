package main;
import java.util.ArrayList;

import Schedulers.*;

/** CPU Class attempts to model a cpu, picking work determined by a scheduler and moving processes between queues
 * Contains a 'focus' which is the process currently receiving work, a ready and waiting queue. A Context switch overhead is applied
 * between all changes of focus.
 * Keeps track of simulated time, and completed processes.
 * @author morgantrench
 */
public class CPU {
	
	// Core CPU 'Components'
	ModelProcess focus;
	ArrayList<ModelProcess> readyQueue;
	ArrayList<ModelProcess> waitingQueue;
	Scheduler scheduler;
	
	// Here for potential analysis
	ArrayList<ModelProcess> completed;
	
	// Parameters
	int sliceTime = Integer.MAX_VALUE; // Default unless updated
	public int simulationTime = 0;
	int contextSwitchOverhead; int csoPassed;
	
	// Statistics
	ArrayList<Integer> responseTimes;
	ArrayList<Integer> waitingTimes;
	ArrayList<Integer> turnaroundTimes;
	
	public CPU(int cso){
		contextSwitchOverhead = cso;
		csoPassed = 0;
		focus = null;
		readyQueue = new ArrayList<ModelProcess>();
		waitingQueue = new ArrayList<ModelProcess>();
		completed = new ArrayList<ModelProcess>();
		
		responseTimes = new ArrayList<Integer>();
		waitingTimes = new ArrayList<Integer>();
		turnaroundTimes = new ArrayList<Integer>();
	}
	
	public void setScheduler(String name){
		if (name == "FCFS")
			scheduler = new FCFS(focus, readyQueue, waitingQueue);
		else if (name == "RRobin")
			scheduler = new RoundRobin(focus, readyQueue, waitingQueue);
		else if (name == "FuzzyRR")
			scheduler = new FuzzyRR(focus, readyQueue, waitingQueue);
		else if (name == "FuzzySJF")
			scheduler = new FuzzySJF(focus, readyQueue, waitingQueue);
	}
	
	/** Returns true if there are processes that are yet to finish
	 * @return
	 */
	private boolean isWork(){
		boolean tmp = false;
		tmp |= (focus != null);
		tmp |= (readyQueue.size() > 0);
		tmp |= (waitingQueue.size() > 0);
		return tmp;
	}
	
	public void addReadyProcess(ModelProcess p){
		readyQueue.add(p);
		p.markArrival(simulationTime);
	}
	
	public void addReadyProcesses(ArrayList<ModelProcess> list){
		for (ModelProcess p : list){
			addReadyProcess(p);
		}
	}
	
	/** Updates the stats held by a particular process. To be called when a process moves from being the focus to the waiting queue
	 * @param p
	 */
	public void recordStatsOnWait(ModelProcess p){
		// - 0 forces evaluation, rather than the 'addresses' themselves being added to the ArrayList
		responseTimes.add(p.responseTime - 0);
		waitingTimes.add(p.waitingTime - 0);
		turnaroundTimes.add(p.turnaroundTime - 0);
		// Debug
		// System.out.println("Stat Log: res: " + p.responseTime + '\t' + "wait: " + p.waitingTime + '\t' + "turn: " + p.turnaroundTime);
	}
	
	public String run(){
		
		while (isWork()){
			
			/*If there is no current process, get one from the scheduler*/
			// Also include context switch overhead
			if (focus == null && (readyQueue.size() > 0)){
				if (csoPassed == contextSwitchOverhead){
					// Basic
					focus = scheduler.next();
					focus.start(simulationTime);
					csoPassed = -1;
					
					// Preemptive Code
					if (scheduler.isPremptive())
						sliceTime = scheduler.getSliceTime();
					
				}
				csoPassed += 1;
			}
			
			// Stuff for logging the state of the cpu at each timestep, optional, could instead use a StringBuilder but no need here.
			String logString = "";
			logString += "Focus: ";
			if (focus == null){
				logString += "No focus, Ready Queue: ";
				for (ModelProcess p : readyQueue){
					logString += "(" + p.id + "," + p.arrivalTime + ")";
				}
			} else
				logString += focus.printState();
			logString += '\t';
			
			
			/* Step all processes */
			// Step current process
			if (focus != null)
				focus.step();
			// Step ready processes
			for (ModelProcess process : readyQueue){
				process.step();
			}
			// Step waiting processes
			ArrayList<ModelProcess> nowReady = new ArrayList<ModelProcess>();
			for (ModelProcess process : waitingQueue){
				if (process.step())
					nowReady.add(process);
			}
			
			// Update Focus (via scheduler) and Queues, preempt if slice time is up
			/* Move around processes that have changed states */
			if (focus != null){
				// Check if the focus has changed states
				if (focus.state == ProcessState.WAITING){ // If the focus finishes a burst
					logString += "Process " + focus.id + " now waiting" + '\t';
					waitingQueue.add(focus);
					focus.markDeparture(simulationTime);
					recordStatsOnWait(focus);
					focus = null;
				} else if (focus.state == ProcessState.FINISHED){ // If the focus process terminates
					logString += "Process " + focus.id + " now complete" + '\t';
					completed.add(focus);
					focus.markDeparture(simulationTime);
					recordStatsOnWait(focus);
					focus = null;
				} else if (sliceTime == 0){ // Check if time slice is up
					logString += "Time Slice Up, " + '\t';
					focus.state = ProcessState.READY;
					readyQueue.add(focus);
					focus = null;
				}
					
			}
			
			// Decrement slice time/quantum and add it to the log
			if (scheduler.isPremptive()){
				if (focus != null){
					logString += "Slice time is: " + sliceTime + '\t';
					sliceTime -= 1;
				}
			}
			
			// Move processes that have finished waiting
			if (!nowReady.isEmpty()){
				for (ModelProcess p : nowReady){
					readyQueue.add(p);
					p.markArrival(simulationTime + 1); // + 1 as the first time step of work will occur 'next'
					logString += "Processes " + p.id + " has finished waiting" + '\t' ;
				}
				logString += '\t';
			}
			waitingQueue.removeAll(nowReady);
			
			// Print the logstring for the timestep, this is what to uncomment out if you want 'state' output shown in the report
			// Enable logging here
			// System.out.println(simulationTime + ":" + '\t' + logString);
			
			simulationTime += 1;
		}
		// CPU has finished running
		
		// Return stats / performance measures in string format
		String statString = "Simulation took: " + simulationTime + '\n';
		float temp = 0;
		for(int e : responseTimes)
			temp += e;
		statString += "Average Response Time: " + temp/responseTimes.size() + '\n';
		temp = 0;
		for(int e : waitingTimes)
			temp += e;
		statString += "Average Waiting Time: " + temp/waitingTimes.size()  + '\n';
		temp = 0;
		for(int e : turnaroundTimes)
			temp += e;
		statString += "Average Turnaround Time: " + temp/turnaroundTimes.size() + '\n';
		
		return statString;
		
		
	}
	
}

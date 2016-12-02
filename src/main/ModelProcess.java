package main;
import java.util.ArrayList;
import java.util.Random;

/** Class that attempts model the behavior of a process for our purposes
 * We pretend processes act as follows: cpu burst -> block on IO -> cpu burst -> block for some reason
 * We provide a seed for random number generation for reproducibility, and use it to generate the burst and block times.
 * A 'do work' method will be called at each timestep if the cpu is working on this process, or it is in the 'waiting'
 * queue while blocking.
 * @author morgantrench
 */
public class ModelProcess {
	
	public ProcessState state;
	int currentTimeRemaining; // Either burst time or IO/Block time remaining
	int runTime; int burstTime; int blockTime;
	public int totalRemaining;
	public ArrayList<Integer> bursts; // List of upcoming burst times 
	public ArrayList<Integer> blocks; // List of upcoming block times
	
	// Stats
	public int id;
	public int arrivalTime;
	public int waitingTime;
	public int previousBurstTime;
	public int responseTime;
	public int turnaroundTime;
	
	// Misc
	private boolean firstSlice;
	
	/**
	 * 
	 * @param inputID
	 * @param seed
	 * @param total number of alternating bursts and blocks, currently must be an odd number
	 * @param burstMean
	 * @param burstStDev
	 * @param blockMean
	 * @param blockStDev
	 */
	public ModelProcess(int inputID, long seed, int total, int burstMean, int burstStDev, int blockMean, int blockStDev){
		// Initialization
		id = inputID;
		Random rng = new Random(seed);
		bursts = new ArrayList<Integer>();
		blocks = new ArrayList<Integer>();
		totalRemaining = total;
		
		arrivalTime = 0;
		waitingTime = 0;
		previousBurstTime = boundedGaussian(rng, burstMean, burstStDev, 1, 4*burstStDev + burstMean);
		
		state = ProcessState.READY;
		for (int i = 0; i < total; i+=1){
			if (i % 2 == 0)
				bursts.add(boundedGaussian(rng, burstMean, burstStDev, 1, 4*burstStDev + burstMean));
			else
				blocks.add(boundedGaussian(rng, blockMean, blockStDev, 1, 4*blockStDev + blockMean));
		}
		runTime = 0;
		blockTime = 0;
		burstTime = bursts.remove(0);
		totalRemaining -= 1;
	}
	
	private int boundedGaussian(Random rand, int mean, int stdev, int lowerBound, int upperBound){
		int next = (int) Math.round(rand.nextGaussian()*stdev + mean);
		if (next < lowerBound)
			next = lowerBound;
		if (next > upperBound)
			next = upperBound;
		return next;
	}
	
	/** To be called whenever a process is moved to be the focus
	 *  If this is the first time the 'job' has been run, record the response time
	 * @param t
	 */
	public void start(int t){
		state = ProcessState.RUNNING;
		if (firstSlice){
			responseTime = t - arrivalTime;
			firstSlice = false;
		}
	}
	
	/** To be called after a process is moved to the readyQueue from the waitingQueue (or added from nowhere)
	 * @param t
	 */
	public void markArrival(int t){
		arrivalTime = t;
		waitingTime = 0;
		firstSlice = true;
	}
	
	/** To be called after a process is moved to the waitingQueue from the readyQueue (or added from nowhere)
	 * @param t
	 */
	public void markDeparture(int t){
		turnaroundTime = t - arrivalTime;
	}
	
	/** To be called at each time step of the CPU, updates process values
	 * Returns true if the process changes state during the step(), intended to be a flag to cause the cpu to move
	 * the process between queues
	 * @return
	 */
	public boolean step(){
		// Increment run time (run time is also used on the waiting queue)
		if (state == ProcessState.RUNNING || state == ProcessState.WAITING)
			runTime += 1;
		
		// Update Stats
		if (state == ProcessState.READY)
			waitingTime += 1;
		
		
		// Change state if completed burst or block, remain finished (shouldn't happen but hey)
		if (runTime == burstTime && (state != ProcessState.FINISHED) ){
			if (totalRemaining == 0){ // If last, mark finished
				state = ProcessState.FINISHED;
				previousBurstTime = burstTime;
			} else if (state == ProcessState.RUNNING){ // mark to be moved to waiting queue
				state = ProcessState.WAITING;
				blockTime = blocks.remove(0);
				runTime = 0;
				previousBurstTime = burstTime;
				totalRemaining -= 1;
			} else if (state == ProcessState.WAITING) { // mark to be moved to ready queue
				state = ProcessState.READY;
				burstTime = bursts.remove(0);
				runTime = 0;
				totalRemaining -= 1;
			}
			return true;
		} else 
			return false;
	}

	@Override
	public String toString() {
		return "id: " + id + "\t ModelProcess [currentTimeRemaining=" + currentTimeRemaining + ", bursts=" + bursts + ", blocks="
				+ blocks + "]";
	}
	
	public String printState() {
		return "<Id: " + id + "\t time remaining: " + (burstTime - runTime) + ">";
	}
	
}
	



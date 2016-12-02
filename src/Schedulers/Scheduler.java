package Schedulers;

import java.util.ArrayList;

import main.ModelProcess;


/** Abstract class that provides methods and a super constructor for wannabe scheduler implementations/subclasses
 * @author morgantrench
 */
public abstract class Scheduler {
	
	ModelProcess focus;
	ArrayList<ModelProcess> readyQueue;
	ArrayList<ModelProcess> waitingQueue;
	
	public Scheduler (ModelProcess f, ArrayList<ModelProcess> rQ, ArrayList<ModelProcess> wQ){
		focus = f;
		readyQueue = rQ;
		waitingQueue = wQ;
	}
	
	// Returns true if the implemented scheduler preempts tasks
	public abstract boolean isPremptive();
	
	// Returns a slice time, after which the burst should be preempted
	public abstract int getSliceTime();
	
	// removes the next process from the readyQueue and returns it
	public abstract ModelProcess next();
	
	
}

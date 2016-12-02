package Schedulers;

import java.util.ArrayList;

import main.ModelProcess;

/** Implements a FCFS Scheduler, extends the scheduler abstract class
 * @author morgantrench
 */
public class FCFS extends Scheduler {

	public FCFS(ModelProcess f, ArrayList<ModelProcess> rQ, ArrayList<ModelProcess> wQ) {
		super(f, rQ, wQ);
	}

	@Override
	public boolean isPremptive() {
		return false;
	}

	@Override
	public int getSliceTime() {
		// Should never be called as isPrememptive() returns false
		// Effectively Infinite
		return Integer.MAX_VALUE;
	}

	@Override
	public ModelProcess next() {
		// Returns the process/burst that arrived on the readyQueue first
		int min = Integer.MAX_VALUE; int index = 0;
		for (int i = 0; i < readyQueue.size(); i+=1){
			if(min < readyQueue.get(i).arrivalTime){
				min = readyQueue.get(i).arrivalTime;
				index = i;
			}
		}
		return readyQueue.remove(index);
		//return readyQueue.remove(0); is effectively the same but the code is left should representation change
	}

}

package Schedulers;

import java.util.ArrayList;
import main.ModelProcess;

/** Implements a round robin scheduler, extends the abstract scheduler class
 * @author morgantrench
 *
 */
public class RoundRobin extends Scheduler {

	public RoundRobin(ModelProcess f, ArrayList<ModelProcess> rQ, ArrayList<ModelProcess> wQ) {
		super(f, rQ, wQ);
	}

	@Override
	public boolean isPremptive() {
		return true;
	}


	@Override
	public ModelProcess next() {
		// Remove 'first' process in line, after the time slice it will be returned to the end in the CPU class
		return readyQueue.remove(0);
	}

	@Override
	public int getSliceTime() {
		// Constant time quantum/slice
		return 20;
	}

}

package Schedulers;

import java.util.ArrayList;

import main.ModelProcess;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;


/** Our fuzzy round robin scheduler, extents abstract scheduler class. Uses the jFuzzyLogic Class
 * @author morgantrench
 */
public class FuzzyRR extends Scheduler {
	
	FIS fis;
	
	public FuzzyRR(ModelProcess f, ArrayList<ModelProcess> rQ, ArrayList<ModelProcess> wQ) {
		super(f, rQ, wQ);
		try {
			// Load from 'FCL' file
	        String fileName = "fuzzyrr.fcl";
	        fis = FIS.load(fileName,true);

	        // Error while loading?
	        if( fis == null ) { 
	            System.err.println("Can't load file: '" + fileName + "'");
	            return;
	        }
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}

	@Override
	public boolean isPremptive() {
		return true;
	}

	@Override
	public int getSliceTime() {
		// This is where the fuzzy magic is called
		
		// Calculate crisp inputs
		double abt = 0;
		for (ModelProcess p : readyQueue){
			abt += p.previousBurstTime;
		}
		abt = abt / readyQueue.size(); // Crisp average burst time
		
		int workload = 100;
		if ((readyQueue.size() + waitingQueue.size()) > 0)
			workload = 100 * readyQueue.size() / (readyQueue.size() + waitingQueue.size());
		
		// System.out.println("workload: " + workload + '\t' + " r:" + readyQueue.size() + '\t' + "q: " + waitingQueue.size());
		
		// Pass in parameters
		fis.setVariable("abt", abt);
		fis.setVariable("workload", workload);
		
		fis.evaluate();
		fis.getVariable("quantum");
		
		FunctionBlock functionBlock = fis.getFunctionBlock(null);
		Variable quantum = functionBlock.getVariable("quantum");
		int output = (int) Math.round(quantum.getValue());
		// System.out.println(output);
		return output;
	}

	@Override
	public ModelProcess next() {
		// Remove 'first' process in line, after the time slice it will be returned to the end in the CPU class
		return readyQueue.remove(0);
	}

	public void showMembershipPlots(){
		// Show 
        JFuzzyChart.get().chart(fis);
	}
	
}

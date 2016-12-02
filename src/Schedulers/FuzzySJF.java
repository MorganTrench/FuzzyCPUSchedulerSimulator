package Schedulers;

import java.util.ArrayList;

import main.ModelProcess;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;


/** Our fuzzy shorest job first scheduler, extents abstract scheduler class. Uses the jFuzzyLogic Class
 * @author morgantrench
 */
public class FuzzySJF extends Scheduler {
	
	FIS fis;
	
	public FuzzySJF(ModelProcess f, ArrayList<ModelProcess> rQ, ArrayList<ModelProcess> wQ) {
		super(f, rQ, wQ);
		try {
			// Load from 'FCL' file
	        String fileName = "fuzzysjf.fcl";
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
		return false;
	}

	@Override
	public int getSliceTime() {
		return Integer.MAX_VALUE;
	}

	@Override
	public ModelProcess next() {
		// Create an estimate 'priority' that considers previous burst time and arrival time (burst)
		int index = 0; double max = 0;
		for (int i = 0; i < readyQueue.size(); i++){
			// Preprocessing
			double abt = 0;
			for (ModelProcess p : readyQueue){
				abt += p.previousBurstTime;
			}
			abt = abt / readyQueue.size();
			
			double awt = 0;
			for (ModelProcess p : readyQueue){
				awt += p.waitingTime;
			}
			awt = awt / readyQueue.size();
			
			
			
			// Fuzzify Input
			ModelProcess p = readyQueue.get(i);
			fis.setVariable("pbt", (p.previousBurstTime)/abt );
	        fis.setVariable("waiting", (p.waitingTime)/awt );
	        
	        // Defuzz Output
	        fis.evaluate();
			fis.getVariable("priority");
	        FunctionBlock functionBlock = fis.getFunctionBlock(null);
	        Variable priority = functionBlock.getVariable("priority");
	        double fuzzyPriority = priority.getValue();
	        
	        // Record max
	        if (fuzzyPriority > max){
	        	max = fuzzyPriority;
	        	index = i;
	        }
		}
		// Return maximum 'fuzzy priority' element 
		return readyQueue.remove(index);
	}

	public void showMembershipPlots(){
		// Show 
        JFuzzyChart.get().chart(fis);
	}
}

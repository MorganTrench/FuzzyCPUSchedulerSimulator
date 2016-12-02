import java.util.ArrayList;

import Schedulers.FuzzyRR;
import Schedulers.FuzzySJF;
import main.CPU;
import main.ModelProcess;

/** This class runs each of the scheduling algorithms on each set of test data. It also shows the 
 * graphical versions of the membership functions provided by jFuzzyLogic
 * @author morgantrench
 */
public class Tests {
	
	public static void main(String[] args) {
		String fcfs, rr, fuzzy, fuzsjf;
		
		// Print Generated Tests
		System.out.println("Enumerating Test Data");
		System.out.println("Test Set One");
		enumerateProcess(getTestSetOne());
		System.out.println();
		System.out.println("Test Set Two");
		enumerateProcess(getTestSetTwo());
		System.out.println();
		System.out.println("Test Set Three");
		enumerateProcess(getTestSetThree());
		System.out.println();
		System.out.println("Test Set Four");
		enumerateProcess(getTestSetFour());
		System.out.println();
		System.out.println("Test Set Five");
		enumerateProcess(getTestSetFive());
		System.out.println();
		
		// Display membership functions
		FuzzyRR frr = new FuzzyRR(null, null, null);
		FuzzySJF fsjf = new FuzzySJF(null, null, null);
		System.out.println("Showing Fuzzy Round Robin membership functions");
		frr.showMembershipPlots();
		System.out.println("Showing Fuzzy SJF membership functions");
		fsjf.showMembershipPlots();
		System.out.println();
		
		// Test Set 1
		System.out.println("Running tests for test set one ...");
		fcfs = testFCFSCPU(getTestSetOne());
		rr = testRRobinCPU(getTestSetOne());
		fuzzy = testFuzzyRR(getTestSetOne());
		fuzsjf = testFuzzySJF(getTestSetOne());
		System.out.println();
		System.out.println("Results:");
		System.out.println("RR: "); System.out.println(rr);
		System.out.println("FUZZRR: "); System.out.println(fuzzy);
		System.out.println("FCFS: "); System.out.println(fcfs);
		System.out.println("FUZZSJF: "); System.out.println(fuzsjf);
		
		// Test Set 2
		System.out.println("Running tests for test set two...");
		fcfs = testFCFSCPU(getTestSetTwo());
		rr = testRRobinCPU(getTestSetTwo());
		fuzzy = testFuzzyRR(getTestSetTwo());
		fuzsjf = testFuzzySJF(getTestSetTwo());
		System.out.println();
		System.out.println("Results:");
		System.out.println("RR: "); System.out.println(rr);
		System.out.println("FUZZRR: "); System.out.println(fuzzy);
		System.out.println("FCFS: "); System.out.println(fcfs);
		System.out.println("FUZZSJF: "); System.out.println(fuzsjf);
		
		// Test Set 3
		System.out.println("Running tests for test set three...");
		fcfs = testFCFSCPU(getTestSetThree());
		rr = testRRobinCPU(getTestSetThree());
		fuzzy = testFuzzyRR(getTestSetThree());
		fuzsjf = testFuzzySJF(getTestSetThree());
		System.out.println();
		System.out.println("Results:");
		System.out.println("RR: "); System.out.println(rr);
		System.out.println("FUZZRR: "); System.out.println(fuzzy);
		System.out.println("FCFS: "); System.out.println(fcfs);
		System.out.println("FUZZSJF: "); System.out.println(fuzsjf);
		
		// Test Set 4
		System.out.println("Running tests for test set four...");
		fcfs = testFCFSCPU(getTestSetFour());
		rr = testRRobinCPU(getTestSetFour());
		fuzzy = testFuzzyRR(getTestSetFour());
		fuzsjf = testFuzzySJF(getTestSetFour());
		System.out.println();
		System.out.println("Results:");
		System.out.println("RR: "); System.out.println(rr);
		System.out.println("FUZZRR: "); System.out.println(fuzzy);
		System.out.println("FCFS: "); System.out.println(fcfs);
		System.out.println("FUZZSJF: "); System.out.println(fuzsjf);
		
		// Test Set 5
		System.out.println("Running tests for test set five...");
		fcfs = testFCFSCPU(getTestSetFive());
		rr = testRRobinCPU(getTestSetFive());
		fuzzy = testFuzzyRR(getTestSetFive());
		fuzsjf = testFuzzySJF(getTestSetFive());
		System.out.println();
		System.out.println("Results:");
		System.out.println("RR: "); System.out.println(rr);
		System.out.println("FUZZRR: "); System.out.println(fuzzy);
		System.out.println("FCFS: "); System.out.println(fcfs);
		System.out.println("FUZZSJF: "); System.out.println(fuzsjf);

		
	}
	
	public static String testFuzzyRR(ArrayList<ModelProcess> testProcesses){
		CPU cpu = new CPU(5);
		System.out.println('\t' + "Fuzzy Round Robin Scheduler...");
		cpu.setScheduler("FuzzyRR");
		cpu.addReadyProcesses(testProcesses);
		String temp = cpu.run();
		return temp;
	}
	
	public static String testFuzzySJF(ArrayList<ModelProcess> testProcesses){
		CPU cpu = new CPU(5);
		System.out.println('\t' + "Fuzzy SJF Scheduler...");
		cpu.setScheduler("FuzzySJF");
		cpu.addReadyProcesses(testProcesses);
		String temp = cpu.run();
		return temp;
	}
	
	public static String testFCFSCPU(ArrayList<ModelProcess> testProcesses){
		// Log
		System.out.println('\t' + "FCFS Scheduler...");
		CPU cpu = new CPU(5);
		cpu.setScheduler("FCFS");
		cpu.addReadyProcesses(testProcesses);
		String temp = cpu.run();
		return temp;
	}

	public static String testRRobinCPU(ArrayList<ModelProcess> testProcesses){
		System.out.println('\t' + "Round Robin Scheduler...");
		CPU cpu = new CPU(5);
		cpu.setScheduler("RRobin");
		cpu.addReadyProcesses(testProcesses);
		String temp = cpu.run();
		return temp;
	}

	/** Returns an array of 6 processes with a 'mix' of burst/block ratios
	 * @return
	 */
	public static ArrayList<ModelProcess> getTestSetOne(){
		ArrayList<ModelProcess> testSet = new ArrayList<ModelProcess>();
		// ModelProcess(int inputID, long seed, int total, int burstMean, int burstStDev, int blockMean, int blockStDev)
		// Relatively Large Burst, small block
		testSet.add(new ModelProcess(0, 0, 15, 30, 10, 10, 5));
		testSet.add(new ModelProcess(1, 1, 21, 25, 6, 8, 3));
		// Relatively Small Burst, Large Block
		testSet.add(new ModelProcess(2, 2, 17, 15, 5, 25, 5));
		testSet.add(new ModelProcess(3, 3, 19, 10, 5, 30, 10));
		// Similar block and burst times
		testSet.add(new ModelProcess(4, 4, 13, 20, 5, 20, 5));
		testSet.add(new ModelProcess(5, 5, 17, 30, 8, 30, 8));
		return testSet;
	}
	
	/** Returns an array of 6 processes with a high of burst/block ratios.
	 * That is long bursts, short blocks
	 * @return
	 */
	public static ArrayList<ModelProcess> getTestSetTwo(){
		ArrayList<ModelProcess> testSet = new ArrayList<ModelProcess>();
		// ModelProcess(int inputID, long seed, int total, int burstMean, int burstStDev, int blockMean, int blockStDev)
		// Relatively Large Burst, small block
		testSet.add(new ModelProcess(0, 6, 15, 30, 10, 10, 5));
		testSet.add(new ModelProcess(1, 7, 21, 25, 10, 8, 5));
		testSet.add(new ModelProcess(2, 8, 17, 35, 12, 13, 4));
		testSet.add(new ModelProcess(3, 9, 19, 25, 6, 8, 3));
		testSet.add(new ModelProcess(4, 10, 25, 28, 10, 10, 5));
		testSet.add(new ModelProcess(5, 11, 23, 32, 6, 13, 5));
		
		return testSet;
	}
	
	/** Returns an array of 6 processes with a low of burst/block ratios.
	 * That is short bursts, long blocks
	 * @return
	 */
	public static ArrayList<ModelProcess> getTestSetThree(){
		ArrayList<ModelProcess> testSet = new ArrayList<ModelProcess>();
		// ModelProcess(int inputID, long seed, int total, int burstMean, int burstStDev, int blockMean, int blockStDev)
		// Relatively Large Burst, small block
		testSet.add(new ModelProcess(0, 12, 15, 10, 5, 30, 10));
		testSet.add(new ModelProcess(1, 13, 21, 8, 5, 25, 10));
		testSet.add(new ModelProcess(2, 14, 17, 13, 4, 35, 12));
		testSet.add(new ModelProcess(3, 15, 19, 8, 3, 25, 6));
		testSet.add(new ModelProcess(4, 16, 25, 10, 5, 28, 10));
		testSet.add(new ModelProcess(5, 17, 23, 8, 5, 32, 6));
		return testSet;
	}
	
	/** Returns an array 12 processes with a 'mix' of burst/block ratios
	 * @return
	 */
	public static ArrayList<ModelProcess> getTestSetFour(){
		ArrayList<ModelProcess> testSet = new ArrayList<ModelProcess>(12);
		// ModelProcess(int inputID, long seed, int total, int burstMean, int burstStDev, int blockMean, int blockStDev)
		// Relatively Large Burst, small block
		testSet.add(new ModelProcess(0, 0, 15, 30, 6, 10, 5));
		testSet.add(new ModelProcess(1, 1, 21, 25, 8, 8, 3));
		testSet.add(new ModelProcess(2, 2, 17, 33, 7, 10, 5));
		testSet.add(new ModelProcess(3, 3, 19, 27, 5, 8, 3));
		
		// Relatively Small Burst, Large Block
		testSet.add(new ModelProcess(4, 4, 17, 15, 5, 25, 5));
		testSet.add(new ModelProcess(5, 5, 19, 11, 5, 30, 10));
		testSet.add(new ModelProcess(6, 6, 17, 13, 5, 27, 5));
		testSet.add(new ModelProcess(7, 7, 19, 12, 5, 29, 10));
		
		// Similar block and burst times
		testSet.add(new ModelProcess(8, 8, 13, 20, 5, 20, 5));
		testSet.add(new ModelProcess(9, 9, 17, 30, 8, 30, 8));
		testSet.add(new ModelProcess(10, 10, 15, 23, 5, 23, 5));
		testSet.add(new ModelProcess(11, 11, 19, 33, 8, 33, 8));
		
		return testSet;
	}
	
	/** Returns an array 18 processes with a 'mix' of burst/block ratios
	 * @return
	 */
	public static ArrayList<ModelProcess> getTestSetFive(){
		ArrayList<ModelProcess> testSet = new ArrayList<ModelProcess>(12);
		// ModelProcess(int inputID, long seed, int total, int burstMean, int burstStDev, int blockMean, int blockStDev)
		// Relatively Large Burst, small block
		testSet.add(new ModelProcess(0, 0, 15, 30, 6, 10, 5));
		testSet.add(new ModelProcess(1, 1, 21, 25, 8, 8, 3));
		testSet.add(new ModelProcess(2, 2, 17, 33, 7, 10, 5));
		testSet.add(new ModelProcess(3, 3, 19, 27, 5, 8, 3));
		testSet.add(new ModelProcess(2, 2, 17, 31, 7, 10, 5));
		testSet.add(new ModelProcess(3, 3, 19, 29, 5, 8, 4));
		
		// Relatively Small Burst, Large Block
		testSet.add(new ModelProcess(4, 4, 17, 15, 5, 25, 5));
		testSet.add(new ModelProcess(5, 5, 19, 11, 5, 30, 10));
		testSet.add(new ModelProcess(6, 6, 17, 13, 5, 27, 5));
		testSet.add(new ModelProcess(7, 7, 19, 12, 5, 29, 10));
		testSet.add(new ModelProcess(6, 6, 17, 10, 4, 28, 5));
		testSet.add(new ModelProcess(7, 7, 19, 13, 5, 31, 7));
		
		// Similar block and burst times
		testSet.add(new ModelProcess(8, 8, 13, 20, 5, 20, 5));
		testSet.add(new ModelProcess(9, 9, 17, 30, 8, 30, 8));
		testSet.add(new ModelProcess(10, 10, 15, 23, 5, 23, 5));
		testSet.add(new ModelProcess(11, 11, 19, 33, 8, 33, 8));
		testSet.add(new ModelProcess(10, 10, 15, 23, 5, 28, 5));
		testSet.add(new ModelProcess(11, 11, 19, 34, 7, 31, 8));
		
		return testSet;
	}
	
	public static void enumerateProcess(ArrayList<ModelProcess> list){
		for (ModelProcess p : list){
			String pString = "ID: " + p.id + ", Burst-Block Sequence: ";
			for (int i = 0; i < p.totalRemaining; i += 1){
				if (i % 2 == 0){
					pString += p.bursts.get(i/2) + ", ";
				} else {
					pString += p.blocks.get(i/2) + ", ";
				}
			}
			pString = pString.substring(0, pString.length()-2);
			System.out.println(pString);
		}
	}
}

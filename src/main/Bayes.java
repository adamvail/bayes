package main;

import naive.NaiveBayes;
import tan.Tan;
import arff.Data;

public class Bayes {
	
	static boolean learningCurve = false;

	public static void main(String[] args) {
		if(args.length != 3){
			System.out.println("Usage: ./bayes <train> <test> <n|t>");
			System.exit(1);
		}
		
		Data train = new Data(args[0]);
		Data test = new Data(args[1]);
	
		if(args[2].equalsIgnoreCase("n")){
			// Create naive bayes network
			
			if(learningCurve){
				LearningCurve lc = new LearningCurve(train, test, true);
				lc.printAvgAccuracies();
				return;
			}
			
			NaiveBayes nb = new NaiveBayes(train, test);
			
			
			//nb.printCPDs();
		}
		else if(args[2].equalsIgnoreCase("t")){
			// Create TAN network
			
			if(learningCurve){
				LearningCurve lc = new LearningCurve(train, test, false);
				lc.printAvgAccuracies();
				return;
			}
			
			Tan tan = new Tan(train, test);
			
		}
		else {
			System.out.println("Usage: ./bayes <train> <test> <n|t>");
		}
		
	}

}

package main;

import naive.NaiveBayes;
import arff.Data;

public class Bayes {

	public static void main(String[] args) {
		if(args.length != 3){
			System.out.println("Usage: ./bayes <train> <test> <n|t>");
			System.exit(1);
		}
		
		Data train = new Data(args[0]);
		Data test = new Data(args[1]);

		if(args[2].equals("n")){
			// Create naive bayes network
			NaiveBayes nb = new NaiveBayes(train, test);
			//nb.printCPDs();
		}
		else if(args[2].equals("t")){
			// Create TAN network
		}
		else {
			System.out.println("Usage: ./bayes <train> <test> <n|t>");
		}
		
	}

}

package main;

import arff.Data;

public class Bayes {

	public static void main(String[] args) {
		if(args.length != 3){
			System.out.println("Usage: ./bayes <train> <test> <n|t>");
			System.exit(1);
		}
		
		Data data = new Data(args[0]);

		if(args[2].equals("n")){
			// Create naive bayes network
		}
		else if(args[2].equals("t")){
			// Create TAN network
		}
		else {
			System.out.println("Usage: ./bayes <train> <test> <n|t>");
		}
		
	}

}

package main;

public class Prediction {

	String prediction;
	double probability = 0.0;
	
	public Prediction(String prediction, double probability){
		this.prediction = prediction;
		this.probability = probability;
	}
	
	public String getPrediction(){
		return prediction;
	}
	
	public double getProbability(){
		return probability;
	}
}

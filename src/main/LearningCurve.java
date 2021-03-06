package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import naive.NaiveBayes;
import tan.Tan;
import arff.Data;
import arff.Instance;

public class LearningCurve {

	Data train;
	Data test;
	Integer[] mVals = {25, 50, 100};
	HashMap<Integer, HashSet<Data>> allTrain = new HashMap<Integer, HashSet<Data>>();
	HashMap<Integer, Double> avgProbabilites = new HashMap<Integer, Double>();
	HashMap<Integer, ArrayList<Double>> probs = new HashMap<Integer, ArrayList<Double>>();
	
	public LearningCurve(Data train, Data test, boolean naive){
		this.train = train;
		this.test = test;
		
		splitData();
		for(Integer setSize : mVals){
			ArrayList<Double> pr = new ArrayList<Double>();
			HashSet<Data> data = allTrain.get(setSize);
			double accuracy = 0.0;
			for(Data d : data){
				if(naive){
					NaiveBayes nb = new NaiveBayes(d, test);
					accuracy += nb.getPercentageCorrect();
					pr.add(nb.getPercentageCorrect());
				}
				else {
					Tan tan = new Tan(d, test);
					accuracy += tan.getPercentageCorrect();
					pr.add(tan.getPercentageCorrect());
				}
			}
			
			probs.put(setSize, pr);
			double avgAccuracy = accuracy / data.size();
			avgProbabilites.put(setSize, avgAccuracy);
		}
	}
	
	public void printAvgAccuracies(){
		System.out.println();
		for(Integer s : avgProbabilites.keySet()){
			System.out.println(s + " : " + avgProbabilites.get(s));
			for(Double d : probs.get(s)){
				System.out.println(d);
			}
			System.out.println();
		}
	}
	
	private void splitData(){
		ArrayList<Instance> instances = train.getData();
		Random generator = new Random();
		
		for(Integer setSize : mVals) {
			allTrain.put(setSize, new HashSet<Data>());
			for(int i = 0; i < 4; i++){
				ArrayList<Instance> newInstances = new ArrayList<Instance>();
				while(newInstances.size() != setSize){
					// get a random index
					int instanceIndex = generator.nextInt(instances.size());
					newInstances.add(instances.get(instanceIndex));
				}
				HashSet<Data> curSet = allTrain.get(setSize);
				curSet.add(new Data(newInstances, train.getFeatures()));
				allTrain.put(setSize, curSet);
			}
		}
	}
}

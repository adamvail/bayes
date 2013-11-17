package naive;

import main.Prediction;
import arff.Data;
import arff.Feature;
import arff.Instance;

public class NaiveBayes {
	
	Data train;
	Data test;
	NaiveRoot root;
	
	public NaiveBayes(Data train, Data test){
		this.train = train;
		this.test = test;
		
		createNaiveBayesNetwork();
		printNetwork();
		System.out.println();
		testNetwork();
	}
	
	public void createNaiveBayesNetwork(){
		createRoot();
		createLeaves();
	}
	
	public void printNetwork(){
		//System.out.println(root.getName());
		for(NaiveUnit u : root.getChildren()){
			System.out.println(u.getName() + " " + u.getParent().getName());
		}
	}
	
	public void printCPDs(){
		root.printCPD();
		for(NaiveUnit u : root.getChildren()){
			u.printCPD();
		}
	}
	
	/**
	 * Create the root node of the Naive Bayes net
	 * This is the classification node
	 */
	private void createRoot(){
		Feature classificationFeature = train.getFeatures().get(train.getFeatures().size() - 1);
		root = new NaiveRoot(classificationFeature, train);
	}
	
	private void createLeaves(){
		
		for(Feature f : train.getFeatures()){
			// Don't want a leaf that is the same as the root
			if(f.getName().equals(root.getName())){
				continue;
			}
			
			NaiveUnit leaf = new NaiveUnit(f, train);
			leaf.setParent(root);
			root.addChild(leaf);	
		}		
	}

	private void testNetwork(){
		
		int correct = 0;
		
		for(Instance i : test.getData()){
			Prediction prediction = classify(i);
			String actual = i.getValue(test.getFeatures().get(test.getFeatures().size() - 1));
			System.out.println(prediction.getPrediction() + " " + actual + " " + prediction.getProbability());
			if(prediction.getPrediction().equals(actual)){
				correct++;
			}
		}
		
		System.out.println("\n" + correct);
		//System.out.println(correct + " / " + train.getData().size() + " : " + (correct / train.getData().size()));
	}
	
	private Prediction classify(Instance i){
		Feature classificationFeature = train.getFeatures().get(train.getFeatures().size() - 1);
		String classification = "";
		double highestProb = 0.0;
		double normalization = 0.0;
		
		for(String cVal : classificationFeature.getValues()){
			double prob = root.getProbability(cVal);
			for(NaiveUnit u : root.getChildren()){
				prob = prob * u.getProbability(i.getValue(u.getName()), cVal);
			}
			
			normalization += prob;
			
			if(prob > highestProb){
				highestProb = prob;
				classification = cVal;
			}
		}
		
		return new Prediction(classification, highestProb / normalization);
	}
}

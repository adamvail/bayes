package naive;

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
		testNetwork();
	}
	
	public void createNaiveBayesNetwork(){
		createRoot();
		createLeaves();
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
		
		for(Instance i : test.getData()){
			
		}
	}
	
	private String classify(Instance i){
		
		return null;
	}
}

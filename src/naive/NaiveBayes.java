package naive;

import arff.Data;
import arff.Feature;

public class NaiveBayes {
	
	Data data;
	NaiveRoot root;
	
	public NaiveBayes(Data data){
		this.data = data;
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
		Feature classificationFeature = data.getFeatures().get(data.getFeatures().size() - 1);
		root = new NaiveRoot(classificationFeature, data);
	}
	
	private void createLeaves(){
		
		for(Feature f : data.getFeatures()){
			// Don't want a leaf that is the same as the root
			if(f.getName().equals(root.getName())){
				continue;
			}
			
			NaiveUnit leaf = new NaiveUnit(f, data);
			leaf.setParent(root);
			root.addChild(leaf);	
		}		
	}

}

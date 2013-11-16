package naive;

import java.util.ArrayList;
import java.util.HashMap;

import arff.Data;
import arff.Feature;
import arff.Instance;

public class NaiveRoot {

	Feature feature;
	ArrayList<NaiveUnit> children = new ArrayList<NaiveUnit>();
	HashMap<String, Double> probabilities = new HashMap<String, Double>();
	
	public NaiveRoot(Feature feature, Data data){
		this.feature = feature;
		setProbabilities(data);
	}
	
	private void setProbabilities(Data data){
		Feature classification = data.getFeatures().get(data.getFeatures().size() - 1);
		for(String val : feature.getValues()){
			int occurances = 0;
			for(Instance i : data.getData()){
				if(i.getValue(classification).equals(val)){
					occurances++;
				}
			}
			// use Laplace pseudocounts to make sure no prob is ever zero
			double probability = (occurances + 1.0) / (data.getData().size() + 1.0);
			
			// create the hashmap
			probabilities.put(val, probability);
		}
	}
	
	public double getProbability(String value){
		return probabilities.get(value);
	}
	
	public void addChild(NaiveUnit child){
		children.add(child);
	}
	
	public void addChildren(ArrayList<NaiveUnit> children){
		this.children.addAll(children);
	}
	
	public ArrayList<NaiveUnit> getChildren(){
		return children;
	}
	
	public String getName(){
		return feature.getName();
	}
	
}

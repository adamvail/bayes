package tan;

import java.util.ArrayList;

import arff.Data;
import arff.Feature;
import arff.Instance;

public class TanUnit {
	Feature feature;
	ArrayList<Double> prob;
	ArrayList<TanUnit> parents = new ArrayList<TanUnit>();
	ArrayList<TanUnit> children = new ArrayList<TanUnit>();
	ArrayList<TanUnit> connections = new ArrayList<TanUnit>();
	
	Feature classificationFeature;
	Data data;
	
	public TanUnit(Feature feature, Data data){
		this.feature = feature;
		this.data = data;
		classificationFeature = data.getFeatures().get(data.getFeatures().size() - 1);
		
		// sanity check that I'm grabbing the right feature
		//System.out.println("Classification feature: " + classificationFeature.getName());
	}
		
	public void addConnection(TanUnit connection){
		this.connections.add(connection);
	}
	
	public ArrayList<TanUnit> getConnections(){
		return connections;
	}
	
	
	private double rootProbability(String cVal){
		int occurances = 0;
		for(Instance i : data.getData()){
			if(i.getValue(feature).equals(cVal)){
				occurances++;
			}
		}
		// use Laplace pseudocounts to make sure no prob is ever zero
		return (occurances + 1.0) / (data.getData().size() + feature.getValues().size());
	}
	
	public double getProbability(Instance i, String cVal){
		if(feature.getName().equals("class")){
			return rootProbability(cVal);
		}
		
		ArrayList<String> parentValues = new ArrayList<String>();
		String fValue = i.getValue(feature);
		
		for(int k = 0; k < parents.size(); k++){
			TanUnit parent = parents.get(k); 
			if(parent.getName().equals(classificationFeature.getName())){
				parentValues.add(cVal);
			}
			
			parentValues.add(i.getValue(parent.getName()));
		}
		
		return calculateProbability(fValue, parentValues);
	}
	
	public ArrayList<String> getFeatureValues(){
		return feature.getValues();
	}
	
	public void computeProbability(){
		System.out.println("\nComputing Feature: " + feature.getName());
		if(feature.getName().equals("class")){
			for(int l = 0; l < feature.getValues().size(); l++){
				String cVal = feature.getValues().get(l);
				System.out.println("Pr(" + data.getFeatures().indexOf(feature) + 
						"=" + l + ") = " + rootProbability(cVal));
			}
			return;
		}
		
		System.out.println();
		ArrayList<Integer> pointers = new ArrayList<Integer>();
		boolean done = false;
		for(int i = 0; i < parents.size(); i++){
			pointers.add(0);
		}
		
		while(!done){
			ArrayList<String> parentValues = new ArrayList<String>();
			for(int i = 0; i < parents.size(); i++){
				parentValues.add(parents.get(i).getFeatureValues().get(pointers.get(i)));
			}
			
			for(int k = 0; k < feature.getValues().size(); k++){
				String fVal = feature.getValues().get(k);
				double probability = calculateProbability(fVal, parentValues);
				System.out.print("P(" + data.getFeatures().indexOf(feature) + "=" +
						k + " | ");
				for(int j = 0; j < pointers.size(); j++){
					System.out.print(data.getFeatures().indexOf(parents.get(j).feature) + "=" +
							pointers.get(j));
					if(j < pointers.size() - 1){
						System.out.print(", ");
					}
				}
				System.out.println(") = " + probability);
			}
			
			int parentPosition = parents.size() - 1;
			
			while(true){
				if(pointers.get(parentPosition) == parents.get(parentPosition).getFeatureValues().size() - 1){
					pointers.remove(parentPosition);
					pointers.add(parentPosition, 0);
					if(parentPosition == 0){
						done = true;
						break;
					}
					else {
						parentPosition--;
					}
				}
				else {
					int incrementedVal = pointers.get(parentPosition) + 1;
					pointers.remove(parentPosition);
					pointers.add(parentPosition, incrementedVal);
					break;
				}
			}
		}
		
	}
	
	private double calculateProbability(String fValue, ArrayList<String> parentValues){
		int occurances = 0;
		int total = 0;
		
		for(Instance i : data.getData()){
			
			boolean match = true;
			
			for(int k = 0; k < parentValues.size(); k++){
				if(!i.getValue(parents.get(k).feature).equals(parentValues.get(k))){
					match = false;
					break;
				}
			}
			
			if(match){
				// This instance has all parent values
				total++;
				// check if this instance has this feature's value
				if(i.getValue(feature).equals(fValue)){
					occurances++;
				}
			}
		}	
		// Here laplace pseudocount for denominator takes the possible values that
		// the feature could've taken on
		return (occurances + 1.0) / (total + feature.getValues().size());
	}
	
//	public double getProbability(String fVal, String cVal){
//		return cpd[feature.getValues().indexOf(fVal)][classificationFeature.getValues().indexOf(cVal)];
//	}
	
	public void addParent(TanUnit parent){
		this.parents.add(parent);
	}

	public String getName(){
		return feature.getName();
	}
	
	public ArrayList<TanUnit> getParents(){
		return parents;
	}
	
	public void addChild(TanUnit child){
		this.children.add(child);
	}
	
	public ArrayList<TanUnit> getChildren(){
		return children;
	}
}

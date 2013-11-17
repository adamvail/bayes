package naive;

import java.util.ArrayList;

import arff.Data;
import arff.Feature;
import arff.Instance;


public class NaiveUnit {
	
	Feature feature;
	ArrayList<Double> prob;
	NaiveRoot parent;
	double[][] cpd;
	Feature classificationFeature;
	Data data;
	
	public NaiveUnit(Feature feature, Data data){
		this.feature = feature;
		this.data = data;
		classificationFeature = data.getFeatures().get(data.getFeatures().size() - 1);
		
		// sanity check that I'm grabbing the right feature
		//System.out.println("Classification feature: " + classificationFeature.getName());
		
		cpd = new double[feature.getValues().size()][classificationFeature.getValues().size()];
		
		populateCPD();
	}
	
	private void populateCPD(){
		for(int i = 0; i < feature.getValues().size(); i++){
			String fVal = feature.getValues().get(i);
			for(int k = 0; k < classificationFeature.getValues().size(); k++){
				String cVal = classificationFeature.getValues().get(k);
				cpd[i][k] = calculateProbability(fVal, cVal);
			}
		}
	}
	
	private double calculateProbability(String featureValue, String givenValue){
		int occurances = 0;
		int total = 0;
		
		for(Instance i : data.getData()){
			if(i.getValue(classificationFeature).equals(givenValue)){
				total++;
				if(i.getValue(feature).equals(featureValue)){
					occurances++;
				}
			}
		}
		
		// Here laplace pseudocount for denominator takes the possible values that
		// the feature could've taken on
		return (occurances + 1.0) / (total + feature.getValues().size());
	}
	
	public double getProbability(String fVal, String cVal){
		return cpd[feature.getValues().indexOf(fVal)][classificationFeature.getValues().indexOf(cVal)];
	}
	
	public void setParent(NaiveRoot parent){
		this.parent = parent;
	}

	public String getName(){
		return feature.getName();
	}
	
	public NaiveRoot getParent(){
		return parent;
	}
	
	public void printCPD(){
		System.out.println(getName());
		// iterate the rows
		for(int i = 0; i < cpd.length; i++){
			// iterate the columns
			for(int k = 0; k < cpd[0].length; k++){
				System.out.println(feature.getValues().get(i) + " | " + classificationFeature.getValues().get(k) + " : " + cpd[i][k]);
			}
		}
		System.out.println();
	}
}

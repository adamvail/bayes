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
		System.out.println("Classification feature: " + classificationFeature.getName());
		
		cpd = new double[feature.getValues().size()][classificationFeature.getValues().size()];
		
		populateCPD();
	}
	
	private void populateCPD(){
		for(int i = 0; i < feature.getValues().size(); i++){
			String fVal = feature.getValues().get(i);
			for(int k = 0; k < classificationFeature.getValues().size(); k++){
				String cVal = classificationFeature.getValues().get(k);
				cpd[i][k] = getProbability(fVal, cVal);
			}
		}
	}
	
	private double getProbability(String featureValue, String givenValue){
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
		
		return (occurances + 1.0) / (total + 1.0);
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
}

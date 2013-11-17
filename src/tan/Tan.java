package tan;

import java.util.ArrayList;

import arff.Data;
import arff.Feature;
import arff.Instance;

public class Tan {

	Data train;
	Data test;
	
	public Tan(Data train, Data test){
		this.train = train;
		this.test = test;
	}
	
	private void createTanNetwork(){
		
	}
	
	private void calculateEdgeWeights(){
		
		// cycle through every feature (except the class feature)
		for(int i = 0; i < train.getFeatures().size() - 1; i++){
			Feature f1 = train.getFeatures().get(i);
			if(f1.getName().equals("class")){
				System.out.println("Didn't cycle through features correctly");
				System.exit(1);
			}
			for(int k = i + 1; k < train.getFeatures().size() - 1; k++){
				Feature f2 = train.getFeatures().get(k);
				
				if(f2.getName().equals("class")){
					System.out.println("Didn't cycle through features correctly");
					System.exit(1);
				}
				
				// have two features, calculate cmi between the two
				double cmi = conditionalMutualInformation(f1, f2);
				// TODO put the cmi's somehwere useful
			}
		}
		
	}
	
	private double conditionalMutualInformation(Feature f1, Feature f2){
		Feature classification = train.getFeatures().get(train.getFeatures().size() - 1);
		
		double cmi = 0.0;
		
		for(String f1Val : f1.getValues()){
			for(String f2Val : f2.getValues()){
				for(String cVal : classification.getValues()){
					cmi += calculateProbX(f1, f1Val, f2, f2Val, classification, cVal);
				}
			}
		}
		
		return cmi;
	}
	
	private double calculateProbX(Feature f1, String f1Val, Feature f2, String f2Val,
			Feature classification, String cVal){
		
		int occurances = 0;
		int total = 0;
		
		for(Instance i : train.getData()){
			if(i.getValue(f1).equals(f1Val) &&
					i.getValue(f2).equals(f2Val) &&
					i.getValue(classification).equals(cVal)){
				occurances++;
			}
			total++;
		}
		
		// Laplace estimates need to be normalized over all possible values these three features
		// could've taken on
		return (occurances + 1.0) / (total + f1.getValues().size() + f2.getValues().size() + 
				classification.getValues().size());
	}
	
	private double calculateProbXGivenY(Feature f1, String f1Val, Feature f2, String f2Val,
			Feature classification, String cVal){
		
		int occurances = 0;
		int total = 0;
		
		for(Instance i : train.getData()){
			if(i.getValue(classification).equals(cVal)){
				total++;
				if(i.getValue(f1).equals(f1Val) && i.getValue(f2).equals(f2Val)){
					occurances++;
				}
			}
		}
		
		return (occurances + 1.0) / (total + f1.getValues().size() + f2.getValues().size());
		
	}
	
	private double calculateProbXGivenY(Feature f, String fVal, Feature classification, String cVal){
	
		int occurances = 0;
		int total = 0;
		
		for(Instance i : train.getData()){
			if(i.getValue(classification).equals(cVal)){
				total++;
				if(i.getValue(f).equals(fVal)){
					occurances++;
				}
			}
		}
		
		return (occurances + 1.0) / (total + f.getValues().size());
	}
}
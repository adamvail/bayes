package tan;

import arff.Feature;

public class Edge {

	Feature endpoint1;
	Feature endpoint2;
	double conditionalMutualInformation;
	
	public Edge(Feature endpoint1, Feature endpoint2, double cmi){
		this.endpoint1 = endpoint1;
		this.endpoint2 = endpoint2;
		this.conditionalMutualInformation = cmi;
	}
	
	public String getEnpoint1Name(){
		return endpoint1.getName();
	}
	
	public Feature getEndpoint1(){
		return endpoint1;
	}
	
	public Feature getEndpoint2(){
		return endpoint2;
	}
	
	public String getEndpoint2Name(){
		return endpoint2.getName();
	}
	
	public double getCMI(){
		return conditionalMutualInformation;
	}
	
	public boolean isEdgeBetweenF1F2(Feature f1, Feature f2){
		
		if(endpoint1.getName().equals(f1.getName()) && endpoint2.getName().equals(f2.getName())){
			return true;
		}
		else if(endpoint1.getName().equals(f2.getName()) && endpoint2.getName().equals(f1.getName())){
			return true;
		}
		return false;
	}
}

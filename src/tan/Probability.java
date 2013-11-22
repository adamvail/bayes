package tan;

import java.util.ArrayList;

public class Probability {

	String fVal;
	ArrayList<String> parentValues;
	ArrayList<TanUnit> parents;
	double probability;
	
	public Probability(String fVal, ArrayList<String> parentValues, 
			ArrayList<TanUnit> parents, double probability){
		this.fVal = fVal;
		this.parentValues = parentValues;
		this.parents = parents;
		this.probability = probability;
	}
	
	public ArrayList<String> getParentValues(){
		return parentValues;
	}
	
	public double getProbability(){
		return probability;
	}
	
}

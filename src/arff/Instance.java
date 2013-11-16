package arff;

import java.util.ArrayList;

public class Instance {
	
	ArrayList<Pair> values = new ArrayList<Pair>();
	
	public Instance(){
		//values.putAll(featureValues);
	}
	
	public void addFeatureValue(Feature feature, String value){
		values.add(new Pair(feature.getName(), value));
	}
	
	public String getValue(Feature feature){
		for(Pair p : values){
			if(p.getName().equals(feature.getName())){
				return p.getValue();
			}
		}
		System.out.println("COULDN't FIND VALUE FOR FEATURE: " + feature.getName());
		System.exit(1);
		return null;
	}
	
	public String getValue(String featureName){
		for(Pair p : values){
			if(p.getName().equals(featureName)){
				return p.getValue();
			}
		}
		
		System.out.println("COULDN't FIND VALUE FOR FEATURE: " + featureName);
		System.exit(1);
		return null;
	}
	
	@Override
	public String toString(){
		String output = "";
		for(int i = 0; i < values.size(); i++){
			output += values.get(i).getName() + "=" + values.get(i).getValue() + " ";
		}
		return output;
	}

}

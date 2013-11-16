package arff;

import java.util.ArrayList;

public class Feature {

	String name;
	ArrayList<String> values;
	
	public Feature(String name, ArrayList<String> values){
		this.name = name;
		this.values = values;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<String> getValue(){
		return values;
	}
}

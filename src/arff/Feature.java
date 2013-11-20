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
	
	public ArrayList<String> getValues(){
		return values;
	}
	
	@Override
	public boolean equals(Object o){
		if(((Feature)o).getName().equals(name)){
			return true;
		}
		return false;
	}
}

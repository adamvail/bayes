package arff;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Data {

	ArrayList<Instance> data = new ArrayList<Instance>();
	ArrayList<Feature> features = new ArrayList<Feature>();
	
	public Data(String filename){
		parseFile(filename);
		//printData();
	}
	
	public void parseFile(String filename){
		
		BufferedReader reader = null;
		boolean readingData = false;
		
		try{
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while(line != null){
				// trim whitespace
				line = line.trim();
				
				// check for comments
				if(line.startsWith("%")){
					continue;
				}
				
				// check to see if we're reading data yet
				if(readingData){
					String[] vals = line.split("[,]");
					Instance inst = new Instance();
					for(int i = 0; i < vals.length; i++){
						inst.addFeatureValue(features.get(i), vals[i].trim());
					}
					
					data.add(inst);
					
					line = reader.readLine();
					continue;
				}
				
				// check to see if this is an attribute
				if(line.startsWith("@attribute")){
					// Then this is an attribute
					
					String name = line.split("[ ]")[1].trim();
					if(name.startsWith("'")){
						name = name.substring(1);
					}
					
					if(name.endsWith("'")){
						name = name.substring(0, name.length() - 1);
					}
					
					String[] vals = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split("[,]");
					ArrayList<String> values = new ArrayList<String>();
					for(String s : vals){
						values.add(s.trim());
					}
										
					features.add(new Feature(name, values));
					
				}
				// check to see if we're done with attributes
				else if(line.startsWith("@data")){
					readingData = true;
				}
				
				line = reader.readLine();
			}
		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			System.out.println("Could not close input file");
		}
	}
	
	public ArrayList<Instance> getData(){
		return data;
	}
	
	public ArrayList<Feature> getFeatures(){
		return features;
	}
	
	public void printData(){
		for(Instance inst : data){
			System.out.println(inst.toString());
		}
	}
	
}

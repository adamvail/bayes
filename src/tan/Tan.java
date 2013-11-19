package tan;

import java.util.ArrayList;
import java.util.HashSet;

import naive.NaiveUnit;
import arff.Data;
import arff.Feature;
import arff.Instance;

public class Tan {

	Data train;
	Data test;
	HashSet<Edge> edges = new HashSet<Edge>();
	double[][] edge_weights = {{1,5,3,4}, {5,1,4,2}, {3,4,1,6}, {4,2,6,1}};
	
	public Tan(Data train, Data test){
		this.train = train;
		this.test = test;
		
		//edge_weights = new double[train.getFeatures().size() - 1][train.getFeatures().size() - 1];
		
		createTanNetwork();
	}
	
	private void createTanNetwork(){
		//calculateEdgeWeights();
		
		//printWeights();
		//System.out.println("\n");
		TanUnit root = determineStructure();
		printNetwork(root);
	}
	
	public void printNetwork(TanUnit node){
		//System.out.println(root.getName());
		for(TanUnit u : node.getChildren()){
			System.out.println(u.getName() + " " + u.getParents().get(0).getName());
		}
		for(TanUnit u : node.getChildren()){
			printNetwork(u);
		}
	}
	
	private void printWeights(){
		for(int i = 0; i < edge_weights.length; i++){
			for(int k = 0; k < edge_weights[0].length; k++){
				System.out.print(edge_weights[i][k] + " ");
			}
			System.out.println();
		}
	}
	
	private void printEdges(){
		
		for(int i = 0; i < train.getFeatures().size(); i++){
			System.out.print(train.getFeatures().get(i).getName() + " ");
		}
		System.out.println();
		
		for(int i = 0; i < train.getFeatures().size() - 1; i++){
			Feature ep1 = train.getFeatures().get(i);
			for(int k = 0; k < train.getFeatures().size() - 1; k++){
				Feature ep2 = train.getFeatures().get(k);
				
				if(ep1.getName().equals(ep2.getName())){
					System.out.print(1 + " ");
				}
				else {
					System.out.print(getEdge(ep1, ep2).getCMI() + " ");
				}
			}
			System.out.println();
		}
	}
	
	private Edge getEdge(Feature ep1, Feature ep2){
		
		for(Edge e : edges){
			if(e.isEdgeBetweenF1F2(ep1, ep2)){
				return e;
			}
		}
		System.out.println("\n" + ep1.getName() + " -> " + ep2.getName());
		return null;
	}
	
	private void calculateEdgeWeights(){
		
		// cycle through every feature (except the class feature)
		for(int i = 0; i < train.getFeatures().size() - 1; i++){
			Feature f1 = train.getFeatures().get(i);
			
			for(int k = 0; k < train.getFeatures().size() - 1; k++){
				Feature f2 = train.getFeatures().get(k);
				
				// have two features, calculate cmi between the two
				double cmi = conditionalMutualInformation(f1, f2);
				edge_weights[i][k] = cmi;
				//edges.add(new Edge(f1, f2, cmi));				
			}
		}
		
	}
	
	private double conditionalMutualInformation(Feature f1, Feature f2){
		Feature classification = train.getFeatures().get(train.getFeatures().size() - 1);
		
		double cmi = 0.0;
		
		for(String f1Val : f1.getValues()){
			for(String f2Val : f2.getValues()){
				for(String cVal : classification.getValues()){
					double probX1X2Y = calculateProbX(f1, f1Val, f2, f2Val, classification, cVal);
					double probX1X2GivenY = calculateProbXGivenY(f1, f1Val, f2, f2Val, classification, cVal);
					double probX1GivenY = calculateProbXGivenY(f1, f1Val, classification, cVal);
					double probX2GivenY = calculateProbXGivenY(f2, f2Val, classification, cVal);
					
					cmi += probX1X2Y * Math.log10(probX1X2GivenY / (probX1GivenY * probX2GivenY));
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
	
	private TanUnit determineStructure(){
		ArrayList<TanUnit> mst = new ArrayList<TanUnit>();
		TanUnit root = prim();
		
		mst.add(root);
		for(TanUnit child : root.getConnections()){
			root.addChild(child);
			child.addParent(root);
			createMST(child);
		}
		
		return root;
	}
	
	private void createMST(TanUnit node){
		for(int i = 0; i < node.getConnections().size(); i++){
			TanUnit conn = node.getConnections().get(i);
			if(conn.getName().equals(node.getParents().get(0).getName())){
				continue;
			}
			node.addChild(conn);
			conn.addParent(node);
			createMST(conn);
		}
	}
	
	private TanUnit prim(){
		// Set of which features are currently in the selected graph
		HashSet<Integer> vNew = new HashSet<Integer>();
		TanUnit[] nodes = new TanUnit[train.getFeatures().size() - 1];
		
		// put the first feature in the vNew set
		vNew.add(0);
		nodes[0] = new TanUnit(train.getFeatures().get(0), train);
		
		while(vNew.size() < train.getFeatures().size() - 1){
		
			double highestWeight = 0.0;
			int sourceInGraph = -1;
			int endpointOutsideGraph = -1;
			
			for(Integer fIndex : vNew){
				// cycle through the row corresponding to this index
				for(int i = 0; i < edge_weights[fIndex].length; i++){
					// Can't pick an edge between two features already in the graph
					if(vNew.contains(i)){
						continue;
					}
					
					if(edge_weights[fIndex][i] > highestWeight){
						highestWeight = edge_weights[fIndex][i];
						sourceInGraph = fIndex;
						endpointOutsideGraph = i;
					}
					// handle possible ties
					else if(edge_weights[fIndex][i] == highestWeight){
						// This means that the current feature we are looking
						// at comes before the current highest source point.
						// Therefore prefer the earlier one.
						if(fIndex < sourceInGraph){
							highestWeight = edge_weights[fIndex][i];
							sourceInGraph = fIndex;
							endpointOutsideGraph = i;
						}
						// If they are equal, and this is the same feature we're searching
						// then simply take the one that comes first. This prefers the endpoint
						// feature that shows up first in the feature list.
						else if(fIndex == sourceInGraph && i < endpointOutsideGraph){
							highestWeight = edge_weights[fIndex][i];
							sourceInGraph = fIndex;
							endpointOutsideGraph = i;
						}
					}
				}
			}
			vNew.add(endpointOutsideGraph);
			
			nodes[endpointOutsideGraph] = new TanUnit(train.getFeatures().get(endpointOutsideGraph), train);
			
			// Add a new connection between these features to be used later when determining
			// edge direction.
			nodes[sourceInGraph].addConnection(nodes[endpointOutsideGraph]);
			nodes[endpointOutsideGraph].addConnection(nodes[sourceInGraph]);
			
			System.out.println(train.getFeatures().get(sourceInGraph).getName() + " -> " + 
					train.getFeatures().get(endpointOutsideGraph).getName() + " : " + highestWeight);
			
		}
		// only return the root since the rest of the graph can be created through the connections
		// in each node.
		return nodes[0];
	}
}
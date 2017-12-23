package ann;

import java.util.List;

public class SimpleNode extends Node {
	//List<Float> weightsToThis;
	List<SimpleEdge> edgesToThis;
	//List<SimpleEdge> edgesFromThis;
	
	public SimpleNode() {
		value = 0;
		bias = 0;
	}
	
	public void addEdge(float weight) {
		edgesToThis.add(new SimpleEdge(weight));
	}
}

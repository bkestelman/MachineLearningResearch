package ann;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	List<Node> nodes;
	private float initWeight;
	
	public Layer(int size) {
		nodes = new ArrayList<>(size);
		for(int i = 0; i < size; i++) {
			nodes.add(new SimpleNode());
		}
		initWeight = 0;
	}
	
	public void connect(Layer prevLayer) {
		if(prevLayer == null) return;
		for(Node myNodes : this.nodes) {
			for(Node prevNodes : prevLayer.nodes) {
				myNodes.addEdge(initWeight);
			}
		}
	}
}

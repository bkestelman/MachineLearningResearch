package ann;

public abstract class Node {
	float value;
	float bias;
	
	public abstract void addEdge(float weight);
}

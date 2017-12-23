package ann;

import java.util.ArrayList;
import java.util.List;

public class SimpleANN implements ArtificialNeuralNetwork<Float, Float> {
	private float stepSize;
	private List<Layer> layers;
	
	public SimpleANN(int[] layerSizes) {
		layers = new ArrayList<Layer>();
		Layer prevLayer = null;
		for(int i = 0; i < layerSizes.length; i++) {
			Layer nextLayer = new Layer(layerSizes[i]);
			layers.add(nextLayer);
			nextLayer.connect(prevLayer);
		}
	}

	@Override
	public void train(List<Float> input) {
		
	}

	@Override
	public Float run(List<Float> input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStepSize(float stepSize) {
		this.stepSize = stepSize;
	}

}

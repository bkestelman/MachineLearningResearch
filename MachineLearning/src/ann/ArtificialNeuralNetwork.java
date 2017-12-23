package ann;

import java.util.List;

public interface ArtificialNeuralNetwork<InputT, OutputT> {
	
	public void train(List<InputT> input);
	public OutputT run(List<InputT> input);
	
	public void setStepSize(float stepSize);
}

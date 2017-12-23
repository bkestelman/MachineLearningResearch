package ann;

public class SigmoidFunction implements ActivationFunction {
	public float func(float z) {
		return (float) (1 / (Math.pow(Math.E, -z) + 1)); // sigmoid function
	}
}


package ann;

public class SigmoidFunction implements ActivationFunction {
	public Number func(Number z) {
		return (1 / (Math.pow(Math.E, -z.doubleValue()) + 1)); // sigmoid function
	}
}


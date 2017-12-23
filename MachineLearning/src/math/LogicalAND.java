package math;

public class LogicalAND {
	public static final Number[][] possibleInputs = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };

	public static Number[] randomInputs() {
		return new Number[] { (int) (Math.random() * 2), (int) (Math.random() * 2) };
	}

	public static Number[] output(Number[] inputs) {
		if (inputs[0].doubleValue() < 1.05 && inputs[0].doubleValue() > 0.95 && inputs[1].doubleValue() < 1.05
				&& inputs[1].doubleValue() > 0.95)
			return new Number[] { 1 };
		else
			return new Number[] { 0 };
	}
}

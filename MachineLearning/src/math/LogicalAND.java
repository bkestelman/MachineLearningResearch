package math;

public class LogicalAND {
	public static final float[][] possibleInputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
	
	public static float[] randomInputs() {
		return new float[] {(int) (Math.random()*2), (int) (Math.random()*2)};
	}
	public static float[] output(float[] inputs) {
		if(inputs[0] < 1.05 && inputs[0] > 0.95 && inputs[1] < 1.05 && inputs[1] > 0.95) return new float[] {1};
		else return new float[] {0};
	}
}

package ann;

public class SquareDiffError<E extends Number> implements ErrorFunction<E> {

	@Override
	public double error(E[] a, E[] b) {
		double err = 0;
		for (int i = 0; i < a.length; i++) {
			double diff = a[i].doubleValue() - b[i].doubleValue();
			err += diff * diff;
		}
		return err;
	}

}

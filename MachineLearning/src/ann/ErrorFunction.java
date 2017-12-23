package ann;

public interface ErrorFunction<E extends Number> {
	double error(E[] a, E[] b);
}

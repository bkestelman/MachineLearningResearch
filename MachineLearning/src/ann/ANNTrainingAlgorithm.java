package ann;

/**
 * Interface for pluggable training algorithms for MatrixANN
 * This is not for setting parameters. Those are set in the ANN, as are the error and activation functions. 
 * This is for modifying the actual algorithm for adjusting weights and biases, using preset parameters.
 * 
 * @author Benito
 *
 * @param <E>
 */
public interface ANNTrainingAlgorithm<E extends Number> {
	public void train(MatrixANN<E> ann); // MatrixANN should implement an ANN interface, to make this more general
	public void adjustWeights(MatrixANN<E> ann);
	public void adjustBiases(MatrixANN<E> ann);
}

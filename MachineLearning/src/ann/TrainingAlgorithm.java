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
public interface TrainingAlgorithm<E extends Number> {
	void trainOne(MatrixANN<E> ann, E[] input, E[] correctOutput); // MatrixANN should implement an ANN interface, to make this more general
	void trainBatch(MatrixANN<E> ann, E[][] inputBatch, E[][] outputLabels);
	void adjustWeights(MatrixANN<E> ann, E[] correctOutput);
	void adjustBiases(MatrixANN<E> ann, E[] correctOutput);
}

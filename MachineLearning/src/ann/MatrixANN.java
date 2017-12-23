package ann;

import java.util.ArrayList;
import java.util.List;

import math.ArrayConversionUtils;
import math.JaggedMatrix;
import math.LogicalAND;
import math.Matrix;

/**
 * MatrixANN
 * 
 * Artificial Neural Nets work off simple concepts.
 * 
 * For now, think of the ANN as a black box which takes some input vector, does
 * some calculations with it, and outputs another vector. These calculations
 * depend mainly of the weights of the network (the network is a bunch of nodes
 * connected by weights). Since the weights are initialized to 0, at first the
 * ANN basically spits out nonsense for any input. You have to train it to solve
 * a particular problem.
 * 
 * When you give the ANN some data as its input, it spits out an output. There
 * is some error between the output it gives and the correct output expected for
 * the input. To minimize this error, the ANN tries adjusting each of its
 * weights slightly and sees which adjustments minimize the error.
 * 
 * Repeating this process over many inputs results in a trained ANN that can
 * make predictions about similar data to what it was trained on.
 * 
 * @author Benito
 *
 * @param <E>
 */
public class MatrixANN<E extends Number> {
	private ANNLayers layers;
	private Matrix[] weights; // there is a matrix of weights between each pair of adjacent layers
	private Number[] biases; // the bias gets added after the weight calculation
	private Number[][][] weightChanges; // flag changes before making them directly on weights, to change all weights
										// "simultaneously"
	private Number[] biasChanges;

	// default params
	private boolean simultaneousChanges = true;
	private double testStepSize = 0.1;
	private double stepFactor = 1000;
	private ActivationFunction activationFunction = null;

	/**
	 * Builds MatrixANN, one parameter at a time
	 */
	public static class MatrixANNBuilder<E> {
		private MatrixANN ann;

		public MatrixANNBuilder() {
			ann = new MatrixANN();
		}

		public MatrixANNBuilder simultaneousChanges(boolean bool) {
			ann.simultaneousChanges = bool;
			return this;
		}

		public MatrixANNBuilder testStepSize(double testStepSize) {
			ann.testStepSize = testStepSize;
			return this;
		}

		public MatrixANNBuilder stepFactor(double stepFactor) {
			ann.stepFactor = stepFactor;
			return this;
		}

		public MatrixANNBuilder activationFunction(ActivationFunction func) {
			ann.activationFunction = func;
			return this;
		}

		/**
		 * When you're done setting parameters, call build
		 * 
		 * @param layerSizes
		 * @return
		 */
		public MatrixANN build(int[] layerSizes) {
			ann.init(layerSizes);
			return ann;
		}
	}

	/**
	 * Private constructor for MatrixANNBuilder layerSizes are required to use
	 * MatrixANN
	 */
	private MatrixANN() {
	}

	/**
	 * Simple constructor from layerSizes. Uses default parameters.
	 * 
	 * @param layerSizes
	 */
	public MatrixANN(int[] layerSizes) {
		init(layerSizes);
	}

	/**
	 * Initializes layers, weights, and biases
	 * 
	 * @param layerSizes
	 */
	public void init(int[] layerSizes) {
		layers = new ANNLayers(layerSizes);
		initWeights();
		initBiases();
	}

	/**
	 * Sets values of nodes in layer 0
	 * 
	 * @param inputs
	 */
	public void setInputs(E[] inputs) {
		layers.setLayer(0, inputs);
	}

	/**
	 * Gets values of nodes in last (output) layer
	 * 
	 * @return output vector
	 */
	public E[] getOutput() {
		return (E[]) ArrayConversionUtils.numbersToDoubles(layers.getLayer(layers.numLayers() - 1));
	}

	/**
	 * Creates Matrix of weights between adjacent layers
	 */
	public void initWeights() {
		weights = new Matrix[layers.numLayers() - 1]; // there is a weight matrix between each layer
		if (simultaneousChanges)
			weightChanges = new Number[weights.length][][];
		for (int w = 0; w < weights.length; w++) {
			int rows = layers.getLayer(w + 1).length;
			int cols = layers.getLayer(w).length;
			weights[w] = new Matrix(rows, cols);
			initWeightChanges(w, rows, cols);
		}
	}

	/**
	 * Initializes corresponding matrices for marking changes before commiting
	 * 
	 * @param w
	 * @param rows
	 * @param cols
	 */
	public void initWeightChanges(int w, int rows, int cols) {
		if (simultaneousChanges)
			weightChanges[w] = new Number[rows][cols];
	}

	/**
	 * The biases are just an array. There is one for each weight matrix.
	 */
	public void initBiases() {
		biases = new Number[layers.numLayers() - 1];
		for (int i = 0; i < biases.length; i++) {
			biases[i] = 0;
		}
		if (simultaneousChanges)
			biasChanges = new Number[weights.length];
	}

	/**
	 * Starting from the values of layer 0 (input), calculate values for each
	 * consecutive layer
	 */
	public void processLayers() {
		if (activationFunction != null) {
			processLayers(activationFunction);
			return;
		}
		for (int w = 0; w < weights.length; w++) {
			processLayer(w);
		}
	}

	public void processLayers(ActivationFunction func) {
		for (int w = 0; w < weights.length; w++) {
			processLayer(w, func);
		}
	}

	public void processLayer(int layer) {
		layers.setLayer(layer + 1, weights[layer].multAdd(layers.getLayer(layer), biases[layer]));
	}

	/**
	 * Multiply weight Matrix by layer, add bias, and apply activation function
	 * 
	 * @param layer
	 * @param func
	 */
	public void processLayer(int layer, ActivationFunction func) {
		layers.setLayer(layer + 1, weights[layer].multFunc(layers.getLayer(layer), biases[layer], func));
	}

	/**
	 * Error function between two arrays (sum of square differences)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double error(E[] a, E[] b) {
		double err = 0;
		for (int i = 0; i < a.length; i++) {
			double diff = a[i].doubleValue() - b[i].doubleValue();
			err += diff * diff;
		}
		return err;
	}

	/**
	 * Try adjusting each weight, processLayers, compare error to previous error
	 * {@code prevErr}, mark weights for change. They will be changed simultaneously 
	 * in commitChanges()
	 * 
	 * @param prevErr
	 * @param correctOutput
	 */
	public void adjustWeights(double prevErr, E[] correctOutput) {
		System.out.println("correctOutput " + correctOutput[0]);
		for (int w = 0; w < weights.length; w++) { // loop through each weight matrix in ann
			for (int r = 0; r < weights[w].numRows(); r++) { // loop through each weight in weight matrix
				for (int c = 0; c < weights[w].numCols(); c++) {
					System.out.println("Before increase weight: " + getOutput()[0]);
					weights[w].addTo(r, c, testStepSize);
					processLayers();
					double err = error(correctOutput, getOutput());
					System.out.println("After increase weight: " + getOutput()[0]);
					weights[w].addTo(r, c, -testStepSize);
					if (simultaneousChanges) {
						if (err < prevErr)
							weightChanges[w][r][c] = stepSize(err, prevErr);
						else
							weightChanges[w][r][c] = -stepSize(err, prevErr);
					} else {
						if (err < prevErr)
							weights[w].addTo(r, c, stepSize(err, prevErr));
						else
							weights[w].addTo(r, c, -stepSize(err, prevErr));
						prevErr = err;
					}
				}
			}
		}
	}

	/**
	 * Calculate appropriate step size based on difference in errors. Multiply by stepFactor
	 * @param err
	 * @param prevErr
	 * @return
	 */
	public double stepSize(double err, double prevErr) {
		return Math.abs(stepFactor * (err - prevErr) * (err - prevErr));
	}

	/**
	 * Same as adjustWeights, for biases
	 * @param prevErr
	 * @param correctOutput
	 */
	public void adjustBiases(double prevErr, E[] correctOutput) {
		for (int b = 0; b < biases.length; b++) {
			biases[b] = biases[b].doubleValue() + testStepSize;
			processLayers();
			biases[b] = biases[b].doubleValue() - testStepSize;
			double err = error(correctOutput, getOutput());
			if (simultaneousChanges) {
				if (err < prevErr)
					biasChanges[b] = stepSize(err, prevErr);
				else
					biasChanges[b] = -stepSize(err, prevErr);
			} else {
				if (err < prevErr)
					biases[b] = biases[b].doubleValue() + stepSize(err, prevErr);
				else
					biases[b] = biases[b].doubleValue() - stepSize(err, prevErr);
				prevErr = err;
			}
		}
	}

	/** 
	 * Make all marked changes
	 */
	public void commitChanges() {
		if (!simultaneousChanges)
			return;
		for (int w = 0; w < weights.length; w++) { // loop through each weight matrix in ann
			for (int r = 0; r < weights[w].numRows(); r++) { // loop through each weight in weight matrix
				for (int c = 0; c < weights[w].numCols(); c++) {
					weights[w].addTo(r, c, weightChanges[w][r][c]);
				}
			}
		}
		for (int b = 0; b < biases.length; b++) {
			biases[b] = biases[b].doubleValue() + biasChanges[b].doubleValue();
		}
	}

	/**
	 * Try adjusting parts of the ANN, processLayers, minimize error
	 * @param input
	 * @param correctOutput
	 */
	public void train(E[] input, E[] correctOutput) {
		setInputs(input);
		processLayers();
		double prevErr = error(correctOutput, getOutput());
		adjustWeights(prevErr, correctOutput);
		adjustBiases(prevErr, correctOutput);
		commitChanges();
	}

	/**
	 * Prints ANNLayers
	 */
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(layers.toString());
		return ret.toString();
	}

	public void printWeights() {
		for (int w = 0; w < weights.length; w++) {
			printWeights(w);
		}
	}

	public void printWeights(int w) {
		System.out.println(weights[w]);
	}

	public void printBiases() {
		for (int i = 0; i < biases.length; i++) {
			System.out.print(biases[i] + "  ");
		}
		System.out.println();
	}

	public static void printArr(Number[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + "  ");
		}
		System.out.println();
	}

	public static void test() {
		System.out.println("Testing MatrixANN");
		System.out.println("-----------------");
		int[] layerSizes = { 2, 2, 1 };
		MatrixANN<Double> ann = new MatrixANN(layerSizes);
		System.out.println("Creating MatrixANN");
		System.out.println(ann);
		System.out.println("Printing Weights");
		ann.printWeights();
		System.out.println("Setting inputs");
		ann.setInputs(new Double[] { 1., 0. });
		System.out.println(ann);
		System.out.println("Changing weights");
		ann.weights[0].setRow(0, new Double[] { 1., 2. });
		ann.weights[0].setRow(1, new Double[] { 3., 4. });
		ann.printWeights();
		System.out.println("Changing biases");
		ann.biases[0] = 2;
		ann.biases[1] = 1;
		ann.printBiases();
		System.out.println("Processing layers");
		ann.processLayers();
		System.out.println(ann);

		System.out.println("Testing on logical AND with sigmoid function and simultaneousChanges");
		ann = new MatrixANN(new int[] { 2, 1 });
		ann.activationFunction = new SigmoidFunction();
		int trainSize = 100;
		double accuracy = 0;
		int runs = 10;
		for (int j = 0; j < runs; j++) {
			for (int i = 0; i < trainSize; i++) {
				Double[] input = ArrayConversionUtils.numbersToDoubles(LogicalAND.randomInputs());
				System.out.println("Training on input");
				printArr(input);
				System.out.println("Setting inputs");
				ann.setInputs(input);
				ann.processLayers();
				System.out.println(ann);
				System.out.println("before training: ");
				System.out.println(ann);
				System.out.println("weights:");
				ann.printWeights();
				System.out.println("biases:");
				ann.printBiases();
				ann.train(input, ArrayConversionUtils.numbersToDoubles(LogicalAND.output(input)));
				System.out.println("after training: ");
				ann.processLayers();
				System.out.println(ann);
				System.out.println("weights:");
				ann.printWeights();
				System.out.println("biases:");
				ann.printBiases();
			}

			for (int i = 0; i < LogicalAND.possibleInputs.length; i++) {
				Double[] input = ArrayConversionUtils.numbersToDoubles(LogicalAND.possibleInputs[i]);
				System.out.println("Testing input");
				printArr(input);
				ann.setInputs(input);
				ann.processLayers();
				System.out.println(ann);
				accuracy += 1
						- ann.error(ArrayConversionUtils.numbersToDoubles(LogicalAND.output(input)), ann.getOutput());
			}
		}
		accuracy /= (runs * LogicalAND.possibleInputs.length);
		System.out.println("accuracy: " + accuracy);

	}

	public static void main(String[] args) {
		JaggedMatrix.test();
		Matrix.test();
		test();
	}
}
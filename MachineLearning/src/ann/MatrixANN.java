package ann;

import java.util.ArrayList;
import java.util.List;

import math.ArrayConversionUtils;
import math.JaggedMatrix;
import math.LogicalAND;
import math.Matrix;

public class MatrixANN<E extends Number> {
	private ANNLayers layers;
	private Matrix[] weights;
	private Number[] biases;
	private Number[][][] weightChanges;
	private Number[] biasChanges;

	private double testStepSize = 0.1;
	private double stepFactor = 500;
	private ActivationFunction activationFunction;

	public MatrixANN(int[] layerSizes) {
		layers = new ANNLayers(layerSizes);
		initWeights();
		initBiases();
	}

	public void setInputs(E[] inputs) {
		layers.setLayer(0, inputs);
	}

	public E[] getOutput() {
		return (E[]) layers.getLayer(layers.numLayers() - 1);
	}

	public void initWeights() {
		weights = new Matrix[layers.numLayers() - 1]; // there is a weight matrix between each layer
		weightChanges = new Number[weights.length][][];
		biasChanges = new Number[weights.length];
		for (int w = 0; w < weights.length; w++) {
			int rows = layers.getLayer(w + 1).length;
			int cols = layers.getLayer(w).length;
			weights[w] = new Matrix(rows, cols);
			initWeightChanges(w, rows, cols);
		}
	}

	public void initWeightChanges(int w, int rows, int cols) {
		weightChanges[w] = new Number[rows][cols];
	}

	public void initBiases() {
		biases = new Number[layers.numLayers() - 1];
		for(int i = 0; i < biases.length; i++) {
			biases[i] = 0;
		}
	}

	public void processLayers() {
		if(activationFunction != null) {
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

	public void processLayer(int layer, ActivationFunction func) {
		layers.setLayer(layer + 1, weights[layer].multFunc(layers.getLayer(layer), biases[layer], func));
	}

	public double error(E[] a, E[] b) {
		double err = 0;
		for (int i = 0; i < a.length; i++) {
			double diff = a[i].doubleValue() - b[i].doubleValue();
			err += diff * diff;
		}
		return err;
	}

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
					if (err < prevErr)
						weightChanges[w][r][c] = stepSize(err, prevErr);
						// weights[w].addTo(r, c, stepSize(err, prevErr)); 
					else
						weightChanges[w][r][c] = -stepSize(err, prevErr);
						// weights[w].addTo(r, c, -stepSize(err, prevErr)); 
				}
			}
		}
	}
	
	public double stepSize(double err, double prevErr) {
		return Math.abs(stepFactor * (err-prevErr)*(err-prevErr));
	}

	public void adjustBiases(double prevErr, E[] correctOutput) {
		for (int b = 0; b < biases.length; b++) {
			biases[b] = biases[b].doubleValue() + testStepSize;
			processLayers();
			biases[b] = biases[b].doubleValue() - testStepSize;
			double err = error(correctOutput, getOutput());
			if (err < prevErr)
				biasChanges[b] = stepSize(err, prevErr);
				// biases[b] += stepSize(err, prevErr);
			else
				biasChanges[b] = -stepSize(err, prevErr);
				// biases[b] -= stepSize(err, prevErr);
		}
	}
	
	public void commitChanges() {
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

	public void train(E[] input, E[] correctOutput) {
		setInputs(input);
		processLayers();
		double prevErr = error(correctOutput, getOutput());
		adjustWeights(prevErr, correctOutput);
		adjustBiases(prevErr, correctOutput);
		commitChanges();
	}

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

		System.out.println("Testing on logical AND with sigmoid function");
		ann = new MatrixANN(new int[] { 2, 1 });
		ann.activationFunction = new SigmoidFunction();
		int trainSize = 10000;
		for (int i = 0; i < trainSize; i++) {
			Double[] input = ArrayConversionUtils.numbersToDoubles(LogicalAND.randomInputs());
			System.out.println("Training on input");
			printArr(input);
			System.out.println("Setting inputs");
			ann.setInputs(input);
			System.out.println(ann);
			System.out.println("before training: ");
			System.out.println(ann);
			System.out.println("weights:");
			ann.printWeights();
			System.out.println("biases:");
			ann.printBiases();
			ann.processLayers();
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
		}
	}

	public static void main(String[] args) {
		JaggedMatrix.test();
		Matrix.test();
		test();
	}
}
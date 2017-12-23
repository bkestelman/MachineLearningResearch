package ann;

import math.JaggedMatrix;
import math.MinorVectorException;

/**
 * Layers for ANN
 * Implemented as COL_ROW JaggedMatrix
 * 
 * @author Benito
 *
 * @param <E>
 */
public class ANNLayers<E extends Number> extends JaggedMatrix<E> {

	/**
	 * Set up JaggedMatrix of appropriate size and initialize nodes to 0
	 * @param layerSizes array containing size of each layer
	 */
	public ANNLayers(int[] layerSizes) {
		super(layerSizes.length, COL_ROW);
		initLayers(layerSizes);
	}
	
	/**
	 * Initialize nodes to 0
	 * @param layerSizes array containing size of each layer
	 */
	public void initLayers(int[] layerSizes) {
		for(int layer = 0; layer < layerSizes.length; layer++) {
			matrix[layer] = new Number[layerSizes[layer]];
			for(int node = 0; node < matrix[layer].length; node++) {
				matrix[layer][node] = 0;
			}
		}
	}
	
	/**
	 * Set a layer
	 * 
	 * This should throw an exception if attempting to change layer size (as that would mean some weight matrices
	 * in the ANN need to be fixed)
	 * @param c
	 * @param col
	 */
	public void setLayer(int c, E[] col) {
		try {
			super.setCol(c, col);
		} catch (MinorVectorException e) {
			// Since ANNLayers are oriented COL_ROW, setCol should never actually throw a MinorVectorException
			e.printStackTrace(); // if it somehow does happen, something went horribly wrong and the program should crash
		}
	}
	
	public int numLayers() {
		return matrix.length;
	}
	
	public E[] getLayer(int layer) {
		return super.getCol(layer);
	}
	
	/**
	 * This doesn't belong here
	 * @param arr
	 * @return
	 */
	public static int max(int[] arr) {
		int max = arr[0];
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] > arr[0]) max = arr[i];
		}
		return max;
	}

}

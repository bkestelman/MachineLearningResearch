package ann;

import math.JaggedMatrix;
import math.MinorVectorException;

public class ANNLayers extends JaggedMatrix {

	public ANNLayers(int[] layerSizes) {
		super(layerSizes.length, COL_ROW);
		initLayers(layerSizes);
	}
	
	public void initLayers(int[] layerSizes) {
		for(int layer = 0; layer < layerSizes.length; layer++) {
			matrix[layer] = new float[layerSizes[layer]];
		}
	}
	
	public void setLayer(int c, float[] col) {
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
	
	public float[] getLayer(int layer) {
		return super.getCol(layer);
	}
	
	/*
	public String toString() {
		StringBuilder ret = new StringBuilder();
		for(int r = 0; r < matrix.length; r++) {
			for(int c = 0; c < matrix[r].length; c++) {
				ret.append(matrix[r][c] + "  ");
			}
			ret.append("\n");
		}
		return ret.toString();
	}*/
	
	public static int max(int[] arr) {
		int max = arr[0];
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] > arr[0]) max = arr[i];
		}
		return max;
	}
	
	public static void test() {
		
	}
}

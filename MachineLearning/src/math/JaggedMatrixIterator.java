package math;

import java.util.Iterator;

public class JaggedMatrixIterator implements Iterator<Float> {
	private float[][] matrix;
	private int currentMajor, currentMinor;
	
	public JaggedMatrixIterator(float[][] matrix) {
		this.matrix = matrix;
		currentMajor = currentMinor = 0;
	}

	@Override
	public boolean hasNext() {
		if(currentMajor < matrix.length) return true;
		return false;
	}

	@Override
	public Float next() {
		Float ret = matrix[currentMajor][currentMinor++];
		if(currentMinor >= matrix[currentMajor].length) {
			currentMinor = 0;
			currentMajor++;
		}
		return ret;
	}

}

package math;

import java.util.Iterator;

public class JaggedMatrixIterator<E> implements Iterator<E> {
	private Object[][] matrix;
	private int currentMajor, currentMinor;
	
	public JaggedMatrixIterator(Object[][] matrix) {
		this.matrix = matrix;
		currentMajor = currentMinor = 0;
	}

	@Override
	public boolean hasNext() {
		if(currentMajor < matrix.length) return true;
		return false;
	}

	@Override
	public E next() {
		Object ret = matrix[currentMajor][currentMinor++];
		if(currentMinor >= matrix[currentMajor].length) {
			currentMinor = 0;
			currentMajor++;
		}
		return (E) ret;
	}

}

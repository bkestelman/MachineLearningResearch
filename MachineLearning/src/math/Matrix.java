package math;

import ann.ActivationFunction;

public class Matrix<E extends Number> extends JaggedMatrix<E> {
	
	public Matrix() {
	}
	
	public Matrix(int rows, int cols) {
		matrix = new Number[rows][cols];
		for(int r = 0; r < rows; r++) {
			matrix[r] = new Number[cols];
			for(int c = 0; c < cols; c++) {
				matrix[r][c] = 0;
			}
		}
	}
	
	@Override
	public void setRow(int r, E[] row) {
		try {
			super.setRow(r, row);
		} catch (MinorVectorException e) {
			// Since all MajorVectors are the same length and all MinorVectors are the same length in a Matrix, setRow should never actually throw a MinorVectorException
			e.printStackTrace(); // if it somehow does happen, something went horribly wrong and the program should crash
		}
	}
	@Override
	public void setCol(int c, E[] col) {
		try {
			super.setCol(c, col);
		} catch (MinorVectorException e) {
			// Since all MajorVectors are the same length and all MinorVectors are the same length in a Matrix, setCol should never actually throw a MinorVectorException
			e.printStackTrace(); // if it somehow does happen, something went horribly wrong and the program should crash
		}
	}
	
	public int numRows() {
		return matrix.length;
	}
	public int numCols() {
		return matrix[0].length;
	}
	
	public void addTo(int r, int c, Number x) {
		matrix[r][c] = matrix[r][c].doubleValue() + x.doubleValue();
	}
	
	public E[] mult(E[] vector) {
		Number[] ans = new Number[matrix.length];
		for(int r = 0; r < matrix.length; r++) {
			ans[r] = (E) VectorUtils.dot(matrix[r], vector);
		}
		return (E[]) ans;
	}
	public E[] multAdd(Number[] vector, Number bias) {
		Number[] ans = new Number[matrix.length];
		for(int r = 0; r < matrix.length; r++) {
			ans[r] = VectorUtils.dot(matrix[r], vector).doubleValue() + bias.doubleValue();
		}
		return (E[]) ans;
	}
	public E[] multFunc(E[] vector, ActivationFunction func) {
		Number[] ans = new Number[matrix.length];
		for(int r = 0; r < matrix.length; r++) {
			ans[r] = func.func(VectorUtils.dot(matrix[r], vector));
		}
		return (E[]) ans;
	}
	public E[] multFunc(E[] vector, E bias, ActivationFunction func) {
		Number[] ans = new Number[matrix.length];
		for(int r = 0; r < matrix.length; r++) {
			ans[r] = func.func(VectorUtils.dot(matrix[r], vector).doubleValue() + bias.doubleValue());
			//if(ans[r] > 1) ans[r] = 1 / 0;
		}
		return (E[]) ans;
	}

	public static void test() {
		System.out.println("Testing Matrix");
		System.out.println("--------------");
		Matrix M = new Matrix(3, 4);
		System.out.println(M);
		Number[] inputs = {1, 2, 3};
		M.setCol(0, inputs);
		System.out.println(M);
	}
	
}

package math;

import ann.ActivationFunction;

public class Matrix extends JaggedMatrix {
	
	public Matrix() {
	}
	
	public Matrix(int rows, int cols) {
		matrix = new float[rows][cols];
		for(int r = 0; r < rows; r++) {
			matrix[r] = new float[cols];
		}
	}
	
	@Override
	public void setRow(int r, float[] row) {
		try {
			super.setRow(r, row);
		} catch (MinorVectorException e) {
			// Since all MajorVectors are the same length and all MinorVectors are the same length in a Matrix, setRow should never actually throw a MinorVectorException
			e.printStackTrace(); // if it somehow does happen, something went horribly wrong and the program should crash
		}
	}
	@Override
	public void setCol(int c, float[] col) {
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
	
	public void addTo(int r, int c, float x) {
		matrix[r][c] += x;
	}
	
	public float[] mult(float[] vector) {
		float[] ans = new float[matrix.length];
		for(int r = 0; r < matrix.length; r++) {
			ans[r] = dot(matrix[r], vector);
		}
		return ans;
	}
	public float[] multAdd(float[] vector, float bias) {
		float[] ans = new float[matrix.length];
		for(int r = 0; r < matrix.length; r++) {
			ans[r] = dot(matrix[r], vector) + bias;
		}
		return ans;
	}
	public float[] multFunc(float[] vector, ActivationFunction func) {
		float[] ans = new float[matrix.length];
		for(int r = 0; r < matrix.length; r++) {
			ans[r] = func.func(dot(matrix[r], vector));
		}
		return ans;
	}
	public float[] multFunc(float[] vector, float bias, ActivationFunction func) {
		float[] ans = new float[matrix.length];
		for(int r = 0; r < matrix.length; r++) {
			ans[r] = func.func(dot(matrix[r], vector) + bias);
			//if(ans[r] > 1) ans[r] = 1 / 0;
		}
		return ans;
	}
	
	public float dot(float[] a, float[] b) {
		if(a.length != b.length) throw new DotProductException("Vector lengths differ");
		float ans = 0;
		for(int i = 0; i < a.length; i++) {
			ans += a[i]*b[i];
		}
		return ans;
	}

	public static void test() {
		System.out.println("Testing Matrix");
		System.out.println("--------------");
		Matrix M = new Matrix(3, 4);
		System.out.println(M);
		float[] inputs = {1, 2, 3};
		M.setCol(0, inputs);
		System.out.println(M);
	}
	
}

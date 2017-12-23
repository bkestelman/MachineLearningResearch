/**
 * JaggedMatrix
 * 
 * Convenient class for manipulating jagged 2D arrays
 * Not as mathematically functional as child class Matrix
 * 
 * Orientations
 * Has two possible orientations which determine its internal and external structure:
 * ROW_COL: 
 * matrix is created as float[rows][cols]
 * It is easy to access rows, with matrix[row]
 * COL_ROW:
 * matrix is created as float[cols][rows]
 * It is easy to access cols, with matrix[col]
 * 
 * All the internals of JaggedMatrix work the same regardless of orientation. No switch statements are needed
 * except in constructors to set the orientation, in toString() to print the JaggedMatrix as expected, and in 
 * methods specific to rows or cols, which should contain no actual code of their own other than defering to the 
 * more general axis based methods. Axes are described below.
 * 
 * Axes
 * Methods in this class refer to a major axis and a minor axis. These depend on the orientation.
 * In ROW_COL, the major axis is the rows
 * In COL_ROW, the major axis is the cols
 */

package math;

import java.util.Iterator;

public class JaggedMatrix implements Iterable<Float> {
	protected float[][] matrix;
	private String orientation;

	// possible orientations
	public static final String ROW_COL = "row.col";
	public static final String COL_ROW = "col.row";

	/**
	 * Constructs empty JaggedMatrix. Does not initialize matrix. Default
	 * orientation is ROW_COL
	 */
	public JaggedMatrix() {
		orientation = ROW_COL;
	}

	/**
	 * Constructs JaggedMatrix and partially initializes matrix to store
	 * {@code majors} major vectors. Does not initialize each row.
	 * 
	 * @param majors
	 */
	public JaggedMatrix(int majors) {
		this();
		initMajorAxis(majors);
	}

	/**
	 * Constructs JaggedMatrix and partially initializes matrix to store
	 * {@code majors} major vector. Sets orientation to given value. Does not
	 * initialize each major vector.
	 * 
	 * @param majors
	 * @param orientation
	 */
	public JaggedMatrix(int majors, String orientation) {
		this(majors);
		this.orientation = orientation;
	}

	/**
	 * Initializes matrix to store {@code majors} major vectors (either row or
	 * column vectors). Does not initialize each major vector.
	 * 
	 * @param majors
	 */
	private void initMajorAxis(int majors) {
		matrix = new float[majors][];
	}

	public void setMajorVector(int majorNum, float[] majorVector) {
		matrix[majorNum] = majorVector;
	}

	public void setMinorVector(int minorNum, float[] minorVector) throws MinorVectorException {
		if(minorVector.length > matrix.length) 
			throw new LongMinorVectorException();
		for (int majorNum = 0; majorNum < matrix.length; majorNum++) {
			if (majorNum >= minorVector.length) {
				throw new ShortMinorVectorException();
			}
			if (minorNum >= matrix[majorNum].length) {
				throw new LongMinorVectorException();
			}
			matrix[majorNum][minorNum] = minorVector[majorNum];
		}
	}

	public void setRow(int r, float[] row) throws MinorVectorException {
		switch (orientation) {
		case ROW_COL:
			setMajorVector(r, row);
			break;
		case COL_ROW:
			setMinorVector(r, row);
			break;
		default:
			throw new InvalidOrientationException();
		}
	}

	public void setCol(int c, float[] col) throws MinorVectorException {
		switch (orientation) {
		case ROW_COL:
			setMinorVector(c, col);
			break;
		case COL_ROW:
			setMajorVector(c, col);
			break;
		default:
			throw new InvalidOrientationException();
		}
	}
	
	public float[] getMajorVector(int major) {
		return matrix[major];
	}
	public float[] getMinorVector(int minor) {
		int minorVectorLen = 0;
		for(int majorNum = 0; majorNum < matrix.length; majorNum++) {
			if(minor >= matrix[majorNum].length) break;
			minorVectorLen++;
		}
		float[] minorVector = new float[minorVectorLen];
		for(int majorNum = 0; majorNum < minorVectorLen; majorNum++) {
			minorVector[majorNum] = matrix[majorNum][minor];
		}
		return minorVector; 
	}
	public float[] getRow(int r) {
		switch (orientation) {
		case ROW_COL:
			return getMajorVector(r);
		case COL_ROW:
			return getMinorVector(r);
		default:
			throw new InvalidOrientationException();
		}
	}
	public float[] getCol(int c) {
		switch (orientation) {
		case ROW_COL:
			return getMinorVector(c);
		case COL_ROW:
			return getMajorVector(c);
		default:
			throw new InvalidOrientationException();
		}
	}

	public int maxMajorLen() {
		int max = 0;
		for (int major = 0; major < matrix.length; major++) {
			int len = matrix[major].length;
			if (len > max)
				max = len;
		}
		return max;
	}

	public int maxMinorLen() {
		return matrix.length;
	}

	public int maxRowLen() {
		switch (orientation) {
		case ROW_COL:
			return maxMajorLen();
		case COL_ROW:
			return maxMinorLen();
		default:
			throw new InvalidOrientationException();
		}
	}

	public int maxColLen() {
		switch (orientation) {
		case ROW_COL:
			return maxMinorLen();
		case COL_ROW:
			return maxMajorLen();
		default:
			throw new InvalidOrientationException();
		}
	}

	/**
	 * Returns a String representation of matrix from top to bottom, accounting for
	 * the orientation
	 */
	public String toString() {
		StringBuilder ret = new StringBuilder();
		// to print nicely, toString has to loop over rows
		int maxCol = maxColLen();
		for (int row = 0; row < maxCol; row++) {
			ret.append(rowStringBuilder(row));
		}
		return ret.toString();
	}

	private StringBuilder rowStringBuilder(int row) {
		StringBuilder ret = new StringBuilder(); // more efficient to calculate size of StringBuilder based on length of
													// row
		switch (orientation) {
		case ROW_COL:
			for (int col = 0; col < matrix[row].length; col++) {
				ret.append(matrix[row][col]);
				ret.append("  ");
			}
			break;
		case COL_ROW:
			for (int col = 0; col < matrix.length; col++) {
				if (row < matrix[col].length)
					ret.append(matrix[col][row]);
				else
					ret.append("   "); // generalize formatting
				ret.append("  ");
			}
			break;
		default:
			throw new InvalidOrientationException();
		}
		return ret.append("\n");
	}

	public static void test() {
		System.out.println("Testing JaggedMatrix");
		System.out.println("--------------------");
		System.out.println("Creating ROW_COL JaggedMatrix");
		JaggedMatrix M = new JaggedMatrix(3);
		float[][] floats = new float[3][];
		floats[0] = new float[] { 0, 1, 2 };
		floats[1] = new float[] { 3, 4 };
		floats[2] = new float[] { 5, 6, 7, 8 };
		for (int i = 0; i < floats.length; i++) {
			M.setMajorVector(i, floats[i]); // setMajorVector is recommended over setRow and setCol because it is always safe
		}
		System.out.println(M);
		System.out.println("Setting row");
		try {
			M.setRow(0, new float[] { 9, 10, 11, 12 });
		} catch (MinorVectorException e) {
			System.err.println(e.getMessage());
		}
		System.out.println(M);
		System.out.println("Setting col");
		try {
			M.setCol(1, new float[] { 13, 14, 15 });
		} catch (MinorVectorException e) {
			System.err.println(e.getMessage());
		}
		System.out.println(M);
		System.out.println("Setting col too long");
		try {
			M.setCol(1, new float[] { 16, 17, 18, 19 });
		} catch (MinorVectorException e) {
			System.err.println(e.getMessage());
		}
		System.out.println(M);
		System.out.println("Setting col too short");
		try {
			M.setCol(1, new float[] { 20, 21 });
		} catch (MinorVectorException e) {
			System.err.println(e.getMessage());
		}
		System.out.println(M);
		System.out.println("Setting far col (col too long)");
		try {
			M.setCol(3, new float[] { 22, 23 });
		} catch (MinorVectorException e) {
			System.err.println(e.getMessage());
		}
		System.out.println(M);

		floats = new float[3][];
		floats[0] = new float[] { 0, 1, 2 };
		floats[1] = new float[] { 3, 4 };
		floats[2] = new float[] { 5, 6, 7, 8 };
		System.out.println("Creating COL_ROW JaggedMatrix");
		M = new JaggedMatrix(3, COL_ROW);
		for (int i = 0; i < floats.length; i++) {
			M.setMajorVector(i, floats[i]);
		}
		System.out.println(M);
	}

	@Override
	public Iterator<Float> iterator() {
		return new JaggedMatrixIterator(matrix);
	}
}

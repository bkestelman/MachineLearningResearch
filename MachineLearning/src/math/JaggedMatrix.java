package math;

import java.util.Iterator;

/**
 * JaggedMatrix
 * 
 * Convenient class for manipulating jagged 2D arrays
 * Not as mathematically functional as child class Matrix
 * 
 * Orientations
 * Has two possible orientations which determine its internal and external structure:
 * ROW_COL: 
 * matrix is created as Number[rows][cols]
 * It is easy and efficient to access rows, with matrix[row]
 * COL_ROW:
 * matrix is created as Number[cols][rows]
 * It is easy and efficient to access cols, with matrix[col]
 * 
 * All the internals of JaggedMatrix work the same regardless of orientation. No switch statements are needed
 * except in constructors to set the orientation, and in methods specific to rows or cols, which should contain 
 * no actual code of their own, but defer to the more general axis based methods. Axes are described below.
 * 
 * Axes
 * Some methods in this class refer to a major axis and a minor axis. These depend on the orientation.
 * In ROW_COL, the major axis is the rows
 * In COL_ROW, the major axis is the cols
 * 
 * Warning: Initializing values is the programmer's responsibility! 
 * Will write convenient methods to initialize everything to 0 or "" 
 * 
 * @author Benito
 *
 * @param <E>
 */
public class JaggedMatrix<E extends Number> implements Iterable<E> { // E may not need to extend Number
	protected Number[][] matrix; // This may make more sense as Object[][]
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
		matrix = new Number[majors][];
	}

	/** 
	 * Sets major axis vector {@code majorNum} to the given array {@code majorVector}
	 * 
	 * @param majorNum
	 * @param majorVector
	 */
	public void setMajorVector(int majorNum, E[] majorVector) {
		matrix[majorNum] = majorVector;
	}

	/**
	 * Sets minor axis {@code minorNum} vector to the given array {@code minorVector}
	 * 
	 * @param minorNum
	 * @param minorVector
	 * @throws MinorVectorException
	 */
	public void setMinorVector(int minorNum, E[] minorVector) throws MinorVectorException {
		if (minorVector.length > matrix.length)
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

	/**
	 * Sets row {@code r} to the given array {@code row}
	 * 
	 * @param r
	 * @param row
	 * @throws MinorVectorException
	 */
	public void setRow(int r, E[] row) throws MinorVectorException {
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

	/**
	 * Sets column {@code c} to the given array {@code row}
	 * 
	 * @param c
	 * @param col
	 * @throws MinorVectorException
	 */
	public void setCol(int c, E[] col) throws MinorVectorException {
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

	public E[] getMajorVector(int major) {
		return (E[]) matrix[major];
	}

	public E[] getMinorVector(int minor) {
		int minorVectorLen = 0;
		for (int majorNum = 0; majorNum < matrix.length; majorNum++) {
			if (minor >= matrix[majorNum].length)
				break;
			minorVectorLen++;
		}
		Object[] minorVector = new Object[minorVectorLen];
		for (int majorNum = 0; majorNum < minorVectorLen; majorNum++) {
			minorVector[majorNum] = matrix[majorNum][minor];
		}
		return (E[]) minorVector;
	}

	public E[] getRow(int r) {
		switch (orientation) {
		case ROW_COL:
			return getMajorVector(r);
		case COL_ROW:
			return getMinorVector(r);
		default:
			throw new InvalidOrientationException();
		}
	}

	public E[] getCol(int c) {
		switch (orientation) {
		case ROW_COL:
			return getMinorVector(c);
		case COL_ROW:
			return getMajorVector(c);
		default:
			throw new InvalidOrientationException();
		}
	}

	/**
	 * 
	 * @return length of the longest major axis vector
	 */
	public int maxMajorLen() {
		int max = 0;
		for (int major = 0; major < matrix.length; major++) {
			int len = matrix[major].length;
			if (len > max)
				max = len;
		}
		return max;
	}

	/**
	 * 
	 * @return length of the longest minor axis vector (which is just the number of major axis vectors)
	 */
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

	/**
	 * Builds String for row {@code row}
	 * @param row
	 * @return
	 */
	private StringBuilder rowStringBuilder(int row) {
		StringBuilder ret = new StringBuilder(); // should calculate size of ret 
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

	/**
	 * Simple tests to see JaggedMatrix functionality
	 */
	public static void test() {
		System.out.println("Testing JaggedMatrix");
		System.out.println("--------------------");
		System.out.println("Creating ROW_COL JaggedMatrix");
		JaggedMatrix<Double> M = new JaggedMatrix<>(3);
		Double[][] doubles = new Double[3][];
		doubles[0] = new Double[] { 0., 1., 2. };
		doubles[1] = new Double[] { 3., 4. };
		doubles[2] = new Double[] { 5., 6., 7., 8. };
		for (int i = 0; i < doubles.length; i++) {
			M.setMajorVector(i, doubles[i]); // setMajorVector is recommended over setRow and setCol because it is
												// always safe
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new JaggedMatrixIterator<E>(matrix);
	}
}

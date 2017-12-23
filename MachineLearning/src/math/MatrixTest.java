package math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MatrixTest {
	private static Matrix M;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMatrix() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		M = new Matrix(3, 4);
		System.out.println(M);
	}

}

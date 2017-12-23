package math;

/**
 * Thrown when a minor vector is set to a value longer than possible
 * Can occur if the new minor vector is longer than the number of major axes OR
 * If the minor vector starts from a long major axis and tries to occupy a shorter major axis.
 * 
 * @author Benito
 *
 */
public class LongMinorVectorException extends MinorVectorException {
	public static final String LONG_MINOR_VECTOR_MESSAGE = "Setting minor vector longer than current, resulting in truncated data. Read the docs for more details.";

	public LongMinorVectorException() {
		super(LONG_MINOR_VECTOR_MESSAGE);
	}

	public LongMinorVectorException(String message) {
		super(message);
	}
}

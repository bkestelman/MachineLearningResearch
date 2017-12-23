package math;

/**
 * Thrown when a method expects multiple lists or arrays to have equal length, but they do not
 * 
 * @author Benito
 *
 */
public class DifferentLengthsException extends RuntimeException {
	public DifferentLengthsException(String message) {
		super(message);
	}
}

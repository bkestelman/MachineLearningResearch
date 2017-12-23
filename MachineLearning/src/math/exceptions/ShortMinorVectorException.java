package math.exceptions;

/**
 * Thrown when you try to set a minor vector to a length shorter than it previously was
 * This will result in leftover data from the old minor vector (this data cannot be simply deleted because that 
 * might leave a whole in a major axis)
 * 
 * @author Benito
 *
 */
public class ShortMinorVectorException extends MinorVectorException {
	public static final String SHORT_MINOR_VECTOR_MESSAGE = "Setting minor vector shorter than current, resulting in residue data. Read the docs for more details.";

	public ShortMinorVectorException() {
		super(SHORT_MINOR_VECTOR_MESSAGE);
	}

	public ShortMinorVectorException(String message) {
		super(message);
	}
}

package math;

public class ShortMinorVectorException extends MinorVectorException {
	public static final String SHORT_MINOR_VECTOR_MESSAGE = "Setting minor vector shorter than current, resulting in residue data. Read the docs for more details.";

	public ShortMinorVectorException() {
		super(SHORT_MINOR_VECTOR_MESSAGE);
	}

	public ShortMinorVectorException(String message) {
		super(message);
	}
}

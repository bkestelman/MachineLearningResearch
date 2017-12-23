package math;

public class LongMinorVectorException extends MinorVectorException {
	public static final String LONG_MINOR_VECTOR_MESSAGE = "Setting minor vector longer than current, resulting in truncated data. Read the docs for more details.";

	public LongMinorVectorException() {
		super(LONG_MINOR_VECTOR_MESSAGE);
	}

	public LongMinorVectorException(String message) {
		super(message);
	}
}

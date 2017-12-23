package math;

/**
 * Thrown when you do something stupid with minor vectors
 * You may want to consider choosing an orientation more appropriate for your JaggedMatrix
 * 
 * @author Benito
 *
 */
public class MinorVectorException extends Exception {
	public MinorVectorException(String message) {
		super(message);
	}
}

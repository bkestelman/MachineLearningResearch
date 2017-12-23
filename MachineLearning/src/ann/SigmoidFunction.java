package ann;

/**
 * The infamous Sigmoid Freud 
 * aka S-curve
 *                                                                **********************
 *                                                  **************
 *                                             ******
 *                                           **
 *                                          **
 *                                    ******
 *                       **************
 * **********************
 * 
 * @author Benito
 *
 */
public class SigmoidFunction implements ActivationFunction {
	public Number func(Number z) {
		return (1 / (Math.pow(Math.E, -z.doubleValue()) + 1)); // sigmoid function
	}
}


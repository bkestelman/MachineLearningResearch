package math;

/**
 * Vector math
 * 
 * @author Benito
 *
 */
public class VectorUtils {

	public static Number dot(Number[] a, Number[] b) {
		if(a.length != b.length) throw new DifferentLengthsException("Vector lengths differ");
		Number ans = 0;
		for(int i = 0; i < a.length; i++) {
			ans = ans.doubleValue() + a[i].doubleValue()*b[i].doubleValue();
		}
		return ans;
	}
	
}

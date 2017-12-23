package math;

public class ArrayConversionUtils {

	public static Double[] numbersToDoubles(Number[] numbers) {
		Double[] doubles = new Double[numbers.length];
		for(int i = 0; i < numbers.length; i++) {
			doubles[i] = numbers[i].doubleValue();
		}
		return doubles;
	}
}

package fibonacci;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 * 
 * @author Jordan Pietkewicz
 *
 */
public class Main {

	/**
	 * List holding pairs of the number in the Sequence and the timestamp for
	 * returning that number
	 */
	public static List<Pair<Long, Long>> recursivePoints, iterativePoints = new ArrayList<>();

	/**
	 * DOES NOT WORK AS INTENDED...
	 * 
	 * @param input indicates which index in Fibonacci Sequence to run until
	 * @return value of input's index in Fibonacci Sequence
	 */
	public static long recursive(long input) {
		if (input == 0 || input == 1) {
			recursivePoints.add(new Pair<Long, Long>(input, System.nanoTime()));
			return input;
		}
		long fib = recursive(input - 1) + recursive(input - 2);
		recursivePoints.add(new Pair<Long, Long>(fib, System.nanoTime()));
		return fib;
	}

	/**
	 * 
	 * @param input indicates which index in Fibonacci Sequence to run until
	 * @return value of input's index in Fibonacci Sequence
	 */
	public static long iterative(long input) {
		long num1, sum = 0;
		long num2 = 1;
		for (int i = 0; i < input; i++) {
			num1 = num2;
			num2 = sum;
			sum = num1 + num2;
		}
		iterativePoints.add(new Pair<Long, Long>(sum, System.nanoTime()));
		return sum;
	}

	/**
	 * Runs iterative method (recursive does not work) and prints out the Sequence
	 * number and timestamp for obtaining that number.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * index of Sequence to run until (used as input for methods)
		 */
		int size = 50;

		/**
		 * starting time to base difference (in nanoseconds) between beginning of method
		 * to end of method
		 */
		long startTime = System.nanoTime();
		for (int i = 0; i < size; i++) {
			iterative(i);
		}
		for (Pair<Long, Long> p : iterativePoints) {
			System.out.println(p.getKey() + ": " + (p.getValue() - startTime));
		}

	}

}

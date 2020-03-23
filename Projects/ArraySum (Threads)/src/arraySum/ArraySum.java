package arraySum;

/**
 * 
 * @see <a href="https://eddmann.com/posts/parallel-summation-in-java/">Parallel
 *      Summation</a> I used this link for some of the parallel methods, mostly
 *      for help with the breaking into smaller arrays.
 */
class Sum extends Thread {

	private int[] array;
	private int low, high, partial;

	public Sum(int[] arr, int low, int high) {
		this.array = arr;
		this.low = low;
		this.high = Math.min(high, arr.length);
	}

	@Override
	public void run() {
		partial = singleThreadSum(array, low, high);
	}

	int getPartial() {
		return partial;
	}

	int multiThreadSum(int[] arr, int numThreads) {
		int sum = 0;
		int size = (int) Math.ceil(arr.length * 1.0 / numThreads);
		Sum[] sums = new Sum[numThreads];
		for (int i = 0; i < numThreads; i++) {
			sums[i] = new Sum(arr, i * size, (i + 1) * size);
			sums[i].start();
		}
		try {
			for (Sum s : sums) {
				s.join();
			}
		} catch (InterruptedException e) {
		}

		for (Sum s : sums) {
			sum += s.getPartial();
		}

		return sum;
	}

	int singleThreadSum(int[] array, int low, int high) {
		int sum = 0;

		for (int i = low; i < high; i++) {
			sum += array[i];
		}

		return sum;
	}
}

public class ArraySum {

	private static int[] initializeArray(int size) {
		int[] array = new int[size];

		for (int i = 0; i < size; i++) {
			array[i] = (int) (Math.random() * 10 + 1);
		}

		return array;
	}

	public static void main(String[] args) {
		int size = 200000000;
		int[] arrayToSum = initializeArray(size);

		Sum s = new Sum(arrayToSum, 0, size);

		long startTime = System.currentTimeMillis();
		System.out.println("Parallel Sum: " + s.multiThreadSum(arrayToSum, Runtime.getRuntime().availableProcessors()));
		System.out.println("Parallel Threads: " + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		System.out.println("Single Sum: " + s.singleThreadSum(arrayToSum, 0, size));
		System.out.println("Single Thread: " + (System.currentTimeMillis() - startTime));

	}

}

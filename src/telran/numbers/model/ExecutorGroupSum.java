package telran.numbers.model;

import telran.numbers.tasks.OneGroupSum;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorGroupSum extends NumberSum {

	public ExecutorGroupSum(int[][] numbersGroup) {
		super(numbersGroup);
	}

	@Override
	public int computeSum() throws InterruptedException {
		OneGroupSum[] tasks = new OneGroupSum[numbersGroup.length];
		for (int i = 0; i < tasks.length; i++) {
			tasks[i] = new OneGroupSum(numbersGroup[i]);
		}
		ExecutorService executorService = Executors.newWorkStealingPool();
		for (int i = 0; i < tasks.length; i++) {
			executorService.execute(tasks[i]);
		}
		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);

		int res = Arrays.stream(tasks).mapToInt(task -> task.getSum()).sum();
		return res;
	}

}

package handlingErrors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CancellingAComputation {
	public static void main(String[] args) {

		// We can cancel a computation via the cancel(boolean mayInterruptIfRunning) method from the Future interface. 
		// For CompletableFuture, the boolean parameter is not used because the implementation does not employ interrupts to do the cancelation. 
        // Instead, cancel() is equivalent to completeExceptionally(new CancellationException()).

	    CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase,
	            CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));

	    CompletableFuture<String> cf2 = cf.exceptionally(throwable -> "cancelled message");

	    System.out.println(cf.cancel(true)); // true
	    System.out.println(cf.isCompletedExceptionally()); // true
	    System.out.println(cf2.join()); // cancelled message
	}
}

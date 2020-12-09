package runningMultipleFuturesInParallel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
   When we need to execute multiple Futures in parallel, we usually want to wait for all of them to execute and then process their combined results.
   The CompletableFuture.allOf static method allows to wait for completion of all of the Futures provided as a var-arg.
 *
 */
public class RunningMultipleFuturesInParallel {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		CompletableFuture<String> future1 = new RunningMultipleFuturesInParallel().calculateHelloAsync();
		CompletableFuture<String> future2 = new RunningMultipleFuturesInParallel().calculateBeautifulAsync();
		CompletableFuture<String> future3 = new RunningMultipleFuturesInParallel().calculateWorldAsync();

		CompletableFuture<Void> combinedFuture 
		  = CompletableFuture.allOf(future1, future2, future3);
		
		combinedFuture.get();

		System.out.println(future1.isDone()); // true
		System.out.println(future2.isDone()); // true
		System.out.println(future3.isDone()); // true
		
		// The return type of the CompletableFuture.allOf() is a CompletableFuture<Void>. 
		// The limitation of this method is that it does not return the combined results of all Futures. 
		// Instead, we have to manually get results from Futures. 
		// Fortunately, CompletableFuture.join() method and Java 8 Streams API makes it simple:
		
		String combined = Stream.of(future1, future2, future3)
				.map(CompletableFuture::join)
				.collect(Collectors.joining("-"));
		System.out.println("combined: " + combined);
		// combined: Hello-Beautiful-World
		
		// The CompletableFuture.join() method is similar to the get method, but it throws an unchecked exception in case the Future does not complete normally. 
		// This makes it possible to use it as a method reference in the Stream.map() method.
	}
	
	
	public CompletableFuture<String> calculateHelloAsync() throws InterruptedException {
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
	    return completableFuture;
	}	

	public CompletableFuture<String> calculateBeautifulAsync() throws InterruptedException {
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Beautiful");
	    return completableFuture;
	}	

	public CompletableFuture<String> calculateWorldAsync() throws InterruptedException {
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "World");
	    return completableFuture;
	}	
}

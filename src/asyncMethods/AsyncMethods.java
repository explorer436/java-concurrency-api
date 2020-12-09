package asyncMethods;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
	Most methods of the fluent API in CompletableFuture class have two additional variants with the Async postfix. 
	These methods are usually intended for running a corresponding step of execution in another thread.
	
	The methods without the Async postfix run the next execution stage using a calling thread. 
	In contrast, the Async method without the Executor argument runs a step using the common fork/join pool implementation of Executor that is accessed with the ForkJoinPool.commonPool() method. 

	Finally, the Async method with an Executor argument runs a step using the passed Executor.
	You can use this when you want to create a Thread Pool and pass it to runAsync() and supplyAsync() methods to let them execute their tasks in a thread obtained from your thread pool.
	
	All the methods in the CompletableFuture API has two variants - One which accepts an Executor as an argument and one which doesn’t -
	// Variations of runAsync() and supplyAsync() methods
	static CompletableFuture<Void>  runAsync(Runnable runnable)
	static CompletableFuture<Void>  runAsync(Runnable runnable, Executor executor)
	static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
	static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)	
	// thenApply() variants
	<U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
	<U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
	<U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)	

	Here's a modified example that processes the result of a computation with a Function instance. 
	The only visible difference is the thenApplyAsync method, but under the hood the application of a function is wrapped into a ForkJoinTask instance 
	(for more information on the fork/join framework, see the article “Guide to the Fork/Join Framework in Java” "https://www.baeldung.com/java-fork-join"). 
	This allows us to parallelize our computation even more and use system resources more efficiently:
	
	These async callback variations help you further parallelize your computations by executing the callback tasks in a separate thread.	
	
	To have more control over the thread that executes the callback task, you can use async callbacks. 
	If thenApply() is used as opposed to thenApplyAsync(), the task inside thenApply() is executed in the same thread where the supplyAsync() task is executed, or in the main thread if the supplyAsync() task completes immediately.
	If you use thenApplyAsync() callback, then it will be executed in a different thread obtained from ForkJoinPool.commonPool() -
	Moreover, if you pass an Executor to the thenApplyAsync() callback then the task will be executed in a thread obtained from the Executor’s thread pool.	
 *
 */
public class AsyncMethods {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		AsyncMethods classUnderTest = new AsyncMethods();
		CompletableFuture<String> future01 = classUnderTest.calculateHelloAsync().thenApplyAsync(s -> s + " World");
		System.out.println(future01.get()); // Hello World
		
		CompletableFuture<String> future02 = classUnderTest.calculateHelloAsync_createYourOwnExecutor().thenApplyAsync(s -> s + "!!!");
		System.out.println(future02.get()); // Hello World
	}

	public CompletableFuture<String> calculateHelloAsync() throws InterruptedException {

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
	
	    return completableFuture;
	}	

	// Here’s how you can create a thread pool and pass it to one of these methods 
	public CompletableFuture<String> calculateHelloAsync_createYourOwnExecutor() throws InterruptedException {
		Executor executor = Executors.newFixedThreadPool(10);
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
		    try {
		        TimeUnit.SECONDS.sleep(1);
		    } catch (InterruptedException e) {
		        throw new IllegalStateException(e);
		    }
		    return "Result of the asynchronous computation";
		}, executor);
	    return future;
	}	
}

package usingCompletableFutureAsASimpleFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UsingCompletableFutureAsASimpleFuture {

	/**
	 * 		The get method throws some checked exceptions.
	 * 		1. ExecutionException (encapsulating an exception that occurred during a computation) and 
	 * 		2. InterruptedException (an exception signifying that a thread executing a method was interrupted):
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		UsingCompletableFutureAsASimpleFuture classUnderTest = new UsingCompletableFutureAsASimpleFuture();
		
		// Call the calculateHelloAsync() method, receive the Future instance.
		Future<String> completableFuture = classUnderTest.calculateHelloAsync();	

		// Call the get method on the future instance when we're ready to block for the result.
		// The CompletableFuture.get() method is blocking. It waits until the Future is completed and returns the result after its completion.
		String result = completableFuture.get();
		System.out.println("result: " + result); // Hello
		
		CompletableFuture<String> completedFuture = classUnderTest.returnCompletedFuture();	
		System.out.println(completedFuture.getNow(null)); // Already completed!
		
	}
	
	/**
	 * A method that creates a CompletableFuture instance, then spins off some computation in another thread and returns the Future immediately.
	 * 
	 * When the computation is done, the method completes the Future by providing the result to the complete method.
	 * 
	 * To spin off the computation, we use the Executor API. 
	 * This method of creating and completing a CompletableFuture can be used together with any concurrency mechanism or API, including raw threads.
	 */
	public Future<String> calculateHelloAsync() throws InterruptedException {

	    CompletableFuture<String> completableFuture = new CompletableFuture<>();
	
	    // CompletableFuture.complete() method is used to manually complete a Future.
	    // The get() method blocks until the Future is complete. So, if the complete() is not implemented, the get call will be blocked forever.
	    Executors.newCachedThreadPool().submit(() -> {
	        Thread.sleep(500);
	        completableFuture.complete("Hello");
	        return null;
	    });
	
	    return completableFuture;
	}	
	
	/**
		First of all, the CompletableFuture class implements the Future interface. So, we can use it as a Future implementation, but with additional completion logic.	

		For example, we can create an instance of this class with a no-arg constructor to represent some future result, hand it out to the consumers, and complete it at some time in the future using the complete method. 
		The consumers may use the get method to block the current thread until this result is provided.
	*/
	
	/**
	 * 
	    The simplest case creates an already completed CompletableFuture with a predefined result. Usually, this may act as the starting stage in your computation. 
		The getNow(null) returns the result if completed (which is obviously the case), but otherwise returns null (the argument).

	    If we already know the result of a computation, we can use the static completedFuture method with an argument that represents a result of this computation. 
	    Consequently, the get method of the Future will never block, immediately returning this result instead:

	 *
	 */
	public CompletableFuture<String> returnCompletedFuture() throws InterruptedException {

		CompletableFuture<String> completableFuture = CompletableFuture.completedFuture("Already completed!");

        return completableFuture;
    }

}

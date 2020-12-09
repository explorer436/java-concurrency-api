package processingResultsOfAsynchronousComputations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 
   The CompletableFuture.get() method is blocking. It waits until the Future is completed and returns the result after its completion.
   But, that is not what we want. 

   For building asynchronous systems we should be able to attach a callback to the CompletableFuture which should automatically get called when the Future completes.
   That way, we won’t need to wait for the result, and we can write the logic that needs to be executed after the completion of the Future inside our callback function.
   You can attach a callback to the CompletableFuture using thenApply(), thenAccept() and thenRun() methods -
  
 */
public class ProcessingResultsOfAsynchronousComputations {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ProcessingResultsOfAsynchronousComputations classUnderTest = new ProcessingResultsOfAsynchronousComputations();
		
		// The thenApply() method accepts a CompletableFunction instance, uses it to process the result, and returns a Future that holds a value returned by a function:
		// Use thenApply() method to process and transform the result of a CompletableFuture when it arrives. 
		// It takes a Function<T,R> as an argument. 
		// Function<T,R> is a simple functional interface representing a function that accepts an argument of type T and produces a result of type R.
		
		// Attach a callback to the Future using thenApply()
		CompletableFuture<String> future01 = classUnderTest.calculateHelloAsync().thenApply(s -> s + " World");
		System.out.println(future01.get()); // Hello World
		

		// You can also write a sequence of transformations on the CompletableFuture by attaching a series of thenApply() callback methods. 
		// The result of one thenApply() method is passed to the next in the series -
		CompletableFuture<String> welcomeText = classUnderTest.calculateHelloAsync().thenApply(wishes -> {
		    return wishes + " World";
		}).thenApply(greeting -> {
		    return greeting + ", !!!!!!!!!!!";
		});	
		System.out.println(welcomeText.get()); // Hello World, !!!!!!!!!!!


		
		
		
		
		
		
		// If you don’t want to return anything from your callback function and just want to run some piece of code after the completion of the Future, then you can use thenAccept() and thenRun() methods. 
		// These methods are consumers and are often used as the last callback in the callback chain.	
		
		
		/*
			If we don't need to return a value down the Future chain, we can use an instance of the Consumer functional interface. 
			Its single method takes a parameter and returns void.

			There's a method for this use case in the CompletableFuture. 
			The thenAccept method receives a Consumer and passes it the result of the computation. 
			Then the final future.get() call returns an instance of the Void type:
		*/
		// CompletableFuture.thenAccept() takes a Consumer<T> and returns CompletableFuture<Void>. 
		// It has access to the result of the CompletableFuture on which it is attached.
		CompletableFuture<Void> future02 = classUnderTest.calculateHelloAsync().thenAccept(s -> System.out.println("Computation returned: " + s));
		future02.get(); // The type of this is "Void".
		// Computation returned: Hello
		

		
		/*
			Finally, if we neither need the value of the computation, nor want to return some value at the end of the chain, then we can pass a Runnable lambda to the thenRun method. 
			In the following example, we simply print a line in the console after calling the future.get():
		*/
		// While thenAccept() has access to the result of the CompletableFuture on which it is attached, thenRun() doesn’t even have access to the Future’s result. 
		// It takes a Runnable and returns CompletableFuture<Void> -
		CompletableFuture<Void> future03 = classUnderTest.calculateHelloAsync().thenRun(() -> System.out.println("Computation finished."));
		future03.get(); // The type of this is "Void".
		// Computation finished.
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

package combiningFutures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 
   This will show the ability to combine CompletableFuture instances in a chain of computation steps.
   
   The result of this chaining is itself a CompletableFuture that allows further chaining and combining. 
   This approach is ubiquitous in functional languages and is often referred to as a monadic design pattern.
 *
 */
public class CombiningFutures {

	/*
	   The thenCompose method, together with thenApply, implement basic building blocks of the monadic pattern. 
	   They closely relate to the map and flatMap methods of Stream and Optional classes that are available in Java 8.

	   Both methods receive a function and apply it to the computation result, but the thenCompose (flatMap) method receives a function that returns another object of the same type. 
	   This functional structure allows composing the instances of these classes as building blocks.
	 */

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CombiningFutures classUnderTest = new CombiningFutures();
		
		// SEQUENTIAL
		// We use the thenCompose method to chain two Futures sequentially.
		// This method takes a function that returns a CompletableFuture instance. 
		// The argument of this function is the result of the previous computation step. 
		// This allows us to use this value inside the next CompletableFuture‘s lambda.
		CompletableFuture<String> completableFuture01 = classUnderTest.calculateHelloAsync().thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));	
		System.out.println(completableFuture01.get());
		// Hello World
		
		
		// PARALLEL
		// If we want to execute two independent Futures and do something with their results after both are complete, 
		// we can use the thenCombine method that accepts a Future and a Function with two arguments to process both results:
		CompletableFuture<String> completableFuture02 = classUnderTest.calculateHelloAsync().thenCombine(classUnderTest.calculateWorldAsync(), (s1, s2) -> s1 + s2);
		System.out.println(completableFuture02.get());
		// Hello World
		
        // Another example:
        System.out.println("Calculating BMI.");
		CompletableFuture<Double> bmiFuture = classUnderTest.getWeightInKg()
		        .thenCombine(classUnderTest.getHeightInCm(), (weightInKg, heightInCm) -> {
		    Double heightInMeter = heightInCm/100;
		    return weightInKg/(heightInMeter*heightInMeter);
		});
		System.out.println("Your BMI is - " + bmiFuture.get()); // Your BMI is - 20.56126561232714


		// A simpler case is when we want to do something with two Futures‘ results, but don't need to pass any resulting value down a Future chain. 
		// The thenAcceptBoth method is there to help.
		CompletableFuture completableFuture03 = classUnderTest.calculateHelloAsync().thenAcceptBoth(classUnderTest.calculateWorldAsync(), (s1, s2) -> System.out.println(s1 + s2));
		// Hello World
		System.out.println(completableFuture03.get()); // null
	}

	public CompletableFuture<String> calculateHelloAsync() throws InterruptedException {
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
	    return completableFuture;
	}	

	public CompletableFuture<String> calculateWorldAsync() throws InterruptedException {
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> " World");
	    return completableFuture;
	}	

	public CompletableFuture<Double> getHeightInCm() throws InterruptedException {
		System.out.println("Retrieving height.");
		CompletableFuture<Double> heightInCmFuture = CompletableFuture.supplyAsync(() -> {
		    try {
		        TimeUnit.SECONDS.sleep(1);
		    } catch (InterruptedException e) {
		       throw new IllegalStateException(e);
		    }
		    return 177.8;
		});
	    return heightInCmFuture;
	}	

	public CompletableFuture<Double> getWeightInKg() throws InterruptedException {
		System.out.println("Retrieving weight.");
		CompletableFuture<Double> weightInKgFuture = CompletableFuture.supplyAsync(() -> {
		    try {
		        TimeUnit.SECONDS.sleep(1);
		    } catch (InterruptedException e) {
		       throw new IllegalStateException(e);
		    }
		    return 65.0;
		});
        return weightInKgFuture;
	}	
}

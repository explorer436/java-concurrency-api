package completableFutureWithEncapsulatedComputationLogic;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


/**
 * 
    The code in UsingCompletableFutureAsASimpleFuture allows us to pick any mechanism of concurrent execution, 
    but what if we want to skip this boilerplate and simply execute some code asynchronously?

	Static methods runAsync and supplyAsync allow us to create a CompletableFuture instance out of Runnable and Supplier functional types correspondingly.
	
	Both Runnable and Supplier are functional interfaces that allow passing their instances as lambda expressions.
	
	The Runnable interface is the same old interface that is used in threads and it does not allow to return a value.
    It is useful for tasks that don’t return anything. 
	
	The Supplier interface is a generic functional interface with a single method that has no arguments and returns a value of a parameterized type.
    It is useful if you want to return some result from your background task
	
	This allows us to provide an instance of the Supplier as a lambda expression that does the calculation and returns the result. 
 *
 */
public class CompletableFutureWithEncapsulatedComputationLogic {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CompletableFutureWithEncapsulatedComputationLogic classUnderTest = new CompletableFutureWithEncapsulatedComputationLogic();
		
		Future<Void> completableFuture01 = classUnderTest.useRunAsync_createRunnableInstance();	
		// Block and wait for the future to complete
		completableFuture01.get();

		Future<Void> completableFuture02 = classUnderTest.useRunAsync_WithALambdaExpression();	
		// Block and wait for the future to complete
		completableFuture02.get();

		// Call the useSupplyAsync_WithALambdaExpression() method, receive the Future instance.
		Future<String> completableFuture03 = classUnderTest.useSupplyAsync_WithALambdaExpression();	

		// Call the get method on the future instance when we're ready to block for the result.
		System.out.println("completableFuture03.get(): " + completableFuture03.get()); // Hello

		// Call the useSupplyAsync_createSupplierInstance() method, receive the Future instance.
		Future<String> completableFuture04 = classUnderTest.useSupplyAsync_createSupplierInstance();	

		// Call the get method on the future instance when we're ready to block for the result.
		System.out.println("completableFuture04.get(): " + completableFuture04.get()); // Result of the asynchronous computation
	}

	public Future<Void> useRunAsync_createRunnableInstance() throws InterruptedException {
		CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
		    @Override
		    public void run() {
		        // Simulate a long-running Job
		        try {
		            TimeUnit.SECONDS.sleep(1);
		        } catch (InterruptedException e) {
		            throw new IllegalStateException(e);
		        }
		        System.out.println("I'll run in a separate thread than the main thread.");
		    }
		});
		return future;
	}	

	public Future<Void> useRunAsync_WithALambdaExpression() throws InterruptedException {
		// Using Lambda Expression
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
		    // Simulate a long-running Job   
		    try {
		        TimeUnit.SECONDS.sleep(1);
		    } catch (InterruptedException e) {
		        throw new IllegalStateException(e);
		    }
		    System.out.println("I'll run in a separate thread than the main thread.");
		});
		return future;
	}	

	public Future<String> useSupplyAsync_WithALambdaExpression() throws InterruptedException {
		// Using Lambda Expression
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
	
	    return completableFuture;
	}	

    // CompletableFuture.supplyAsync() takes a Supplier<T> and returns CompletableFuture<T> where T is the type of the value obtained by calling the given supplier.
	public Future<String> useSupplyAsync_createSupplierInstance() throws InterruptedException {

		// Run a task specified by a Supplier object asynchronously
		CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
		    @Override
		    public String get() {
		        try {
		            TimeUnit.SECONDS.sleep(1);
		        } catch (InterruptedException e) {
		            throw new IllegalStateException(e);
		        }
		        return "Result of the asynchronous computation";
		    }
		});
	
	    return future;
	}	
}

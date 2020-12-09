package handlingErrors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 
 * For error handling in a chain of asynchronous computation steps, we have to adapt the throw/catch idiom in a similar fashion.
 * 
 * Instead of catching an exception in a syntactic block, the CompletableFuture class allows us to handle it in a special handle method. 
 * This method receives two parameters: a result of a computation (if it finished successfully), and the exception thrown (if some computation step did not complete normally).
 *
 */
public class HandlingErrors {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		HandlingErrors classUnderTest = new HandlingErrors();
		
		CompletableFuture<String> completableFuture01 = classUnderTest.calculateNameAsync_handleExceptionWithADefaultValue("Bob");
		System.out.println(completableFuture01.get()); // Hello, Bob!

		CompletableFuture<String> completableFuture02 = classUnderTest.calculateNameAsync_handleExceptionWithADefaultValue(null);
		System.out.println(completableFuture02.get()); // Hello, Stranger!

		CompletableFuture<Object> completableFuture03 = classUnderTest.calculateNameAsync_throwException("Bob");
		System.out.println(completableFuture03.get()); // Hello, Bob!

		CompletableFuture<Object> completableFuture04;
		try {
			completableFuture04 = classUnderTest.calculateNameAsync_throwException(null);
		    completableFuture04.get(); // should throw java.util.concurrent.ExecutionException
		} catch (Exception e) {
			System.out.println("exception occurred. Here are the details:");
			e.printStackTrace();
		}
	}


	/*
	 * Use the handle method to provide a default value when the asynchronous computation does not finish because of an error.
	 */
	public CompletableFuture<String> calculateNameAsync_handleExceptionWithADefaultValue(String name) {
       CompletableFuture<String> completableFuture  
         =  CompletableFuture.supplyAsync(() -> {
             if (name == null) {
                 throw new RuntimeException("Computation error!");
             }
             return "Hello, " + name + "!";
         }).handle((s, t) -> s != null ? s : "Hello, Stranger!");
		  
		  return completableFuture;
	}

	/*
	 * As an alternative scenario, we also have the ability to complete the future with an exception. 
	 * The completeExceptionally method is intended for just that. 
	 * The completableFuture.get() method in the following example throws an ExecutionException with a RuntimeException as its cause.
	 */
	public CompletableFuture<Object> calculateNameAsync_throwException(String name) {
       CompletableFuture<Object> completableFuture = new CompletableFuture<>();

       if (name == null) {
    	   completableFuture.completeExceptionally(new RuntimeException("Calculation failed!"));
       } else {
	       completableFuture = CompletableFuture.supplyAsync(() -> {
	           return "Hello, " + name + "!";
	       });
       }

       return completableFuture;
	}
}

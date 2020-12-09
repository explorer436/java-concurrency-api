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
 *
 * Use the handle method to provide a default value when the asynchronous computation does not finish because of an error.
 *
 * The API also provides a more generic method - handle() to recover from exceptions. It is called whether or not an exception occurs.
 * If an exception occurs, then the res argument will be null, otherwise, the ex argument will be null.
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

        // ---------------------------
		
	    // Let’s first understand how errors are propagated in a callback chain. 
		// Consider the following CompletableFuture callback chain -
		CompletableFuture.supplyAsync(() -> {
			// Code which might throw an exception
			return "Some result";
		}).thenApply(result -> {
			return "processed result";
		}).thenApply(result -> {
			return "result after further processing";
		}).thenAccept(result -> {
			// do something with the final result
		});
	    // If an error occurs in the original supplyAsync() task, then none of the thenApply() callbacks will be called and future will be resolved with the exception occurred. 
	    // If an error occurs in first thenApply() callback then 2nd and 3rd callbacks won’t be called and the future will be resolved with the exception occurred, and so on.

        CompletableFuture<String> maturityFuture01 = classUnderTest.handleExceptionUsingExceptionally(-1);
        System.out.println("Maturity : " + maturityFuture01.get()); 

        CompletableFuture<String> maturityFuture02 = classUnderTest.handleExceptionUsingTheGenericHandleMethod(-1);
        System.out.println("Maturity : " + maturityFuture02.get()); 

	}


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

    /*
     * The error will not be propagated further in the callback chain if you handle it once.
     */
	public CompletableFuture<String> handleExceptionUsingExceptionally(Integer age) {
		CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
		    if(age < 0) {
		        throw new IllegalArgumentException("Age can not be negative");
		    }
		    if(age > 18) {
		        return "Adult";
		    } else {
		        return "Child";
		    }
		}).exceptionally(ex -> {
		    System.out.println("Oops! We have an exception - " + ex.getMessage());
		    return "Unknown!";
		});

       return maturityFuture;
	}

	public CompletableFuture<String> handleExceptionUsingTheGenericHandleMethod(Integer age) {
		CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
		    if(age < 0) {
		        throw new IllegalArgumentException("Age can not be negative");
		    }
		    if(age > 18) {
		        return "Adult";
		    } else {
		        return "Child";
		    }
		}).handle((res, ex) -> {
		    if(ex != null) {
		        System.out.println("Oops! We have an exception - " + ex.getMessage());
		        return "Unknown!";
		    }
		    return res;
		});

       return maturityFuture;
	}

}

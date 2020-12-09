package differenceBetweenThenApplyAndThenCompose;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 
	Both thenApply() and thenCompose() APIs help chain different CompletableFuture calls, but the usage of these two functions is different.
 *
 */
public class DifferenceBetweenThenApplyAndThenCompose {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		DifferenceBetweenThenApplyAndThenCompose classUnderTest = new DifferenceBetweenThenApplyAndThenCompose();
		// thenApply()
		// We can use this method to work with a result of the previous call. 
		// However, a key point to remember is that the return type will be combined of all calls.
	
		// So this method is useful when we want to transform the result of a CompletableFuture call:
		CompletableFuture<Integer> finalResult01 = classUnderTest.compute().thenApply(s-> s + 1);
		System.out.println(finalResult01.get()); // 26

		// thenCompose()
		// The thenCompose() method is similar to thenApply() in that both return a new Completion Stage. 
		// However, thenCompose() uses the previous stage as the argument. 
		// It will flatten and return a Future with the result directly, rather than a nested future as we observed in thenApply():
	
		CompletableFuture<Integer> finalResult02 = classUnderTest.compute().thenCompose(s -> new DifferenceBetweenThenApplyAndThenCompose().computeAnother(s));
		System.out.println(finalResult02.get()); // 35

		// So if the idea is to chain CompletableFuture methods then it’s better to use thenCompose().
		// Rule of thumb here - If your callback function returns a CompletableFuture, and you want a flattened result from the CompletableFuture chain (which in most cases you would), then use thenCompose().
		// Also, note that the difference between these two methods is analogous to the difference between map() and flatMap().
		
		// -----------------
		
		// Notice the nesting here.
		CompletableFuture<CompletableFuture<Double>> resultOfUsingThenApply = classUnderTest.getUserDetail("dummyUserId").thenApply(user -> classUnderTest.getCreditRating(user));	
		System.out.println("resultOfUsingThenApply.get() : " + resultOfUsingThenApply.get().get()); // 700.1
		
		CompletableFuture<Double> resultOfUsingThenCompose = classUnderTest.getUserDetail("dummyUserId").thenCompose(user -> classUnderTest.getCreditRating(user));	
		System.out.println("resultOfUsingThenCompose.get() : " + resultOfUsingThenCompose.get()); // 700.1
	}
	
	CompletableFuture<Integer> compute() {
		return CompletableFuture.supplyAsync(() -> 25);
    }	

	CompletableFuture<Integer> computeAnother(Integer i){
	    return CompletableFuture.supplyAsync(() -> 10 + i);
	}

	CompletableFuture<User> getUserDetail(String userId) {
		return CompletableFuture.supplyAsync(() -> {
			return new UserService().getUserDetails(userId);
		});	
	}
	
	CompletableFuture<Double> getCreditRating(User user) {
		return CompletableFuture.supplyAsync(() -> {
			return new CreditRatingService().getCreditRating(user);
		});
	}	
	
}

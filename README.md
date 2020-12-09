# Examples of the functionality and use cases of the CompletableFuture class that was introduced as a Java 8 Concurrency API improvement.

## Asynchronous Computation in Java:

### Future vs CompletableFuture
CompletableFuture is an extension to Java’s Future API which was introduced in Java 5.

A Future is used as a reference to the result of an asynchronous computation. It provides an isDone() method to check whether the computation is done or not, and a get() method to retrieve the result of the computation when it is done.

The Future interface was added in Java 5 to serve as a result of an asynchronous computation, but it did not have any methods to combine these computations or handle possible errors.

Future API was a good step towards asynchronous programming in Java but it lacked some important and useful features -

#### Limitations of Future

1. It cannot be manually completed :

Let’s say that you’ve written a function to fetch the latest price of an e-commerce product from a remote API. Since this API call is time-consuming, you’re running it in a separate thread and returning a Future from your function.

Now, let’s say that If the remote API service is down, then you want to complete the Future manually by the last cached price of the product.

Can you do this with Future? No!

2. You cannot perform further action on a Future’s result without blocking:

Future does not notify you of its completion. It provides a get() method which blocks until the result is available.

You don’t have the ability to attach a callback function to the Future and have it get called automatically when the Future’s result is available.

3. Multiple Futures cannot be chained together :

Sometimes you need to execute a long-running computation and when the computation is done, you need to send its result to another long-running computation, and so on.

You can not create such asynchronous workflow with Futures.

4. You can not combine multiple Futures together :

Let’s say that you have 10 different Futures that you want to run in parallel and then run some function after all of them completes. You can’t do this as well with Future.

5. No Exception Handling :

Future API does not have any exception handling construct.

Whoa! So many limitations right? Well, That’s why we have CompletableFuture. You can achieve all of the above with CompletableFuture.

CompletableFuture implements Future and CompletionStage interfaces and provides a huge set of convenience methods for creating, chaining and combining multiple Futures. It also has a very comprehensive exception handling support.

Java 8 introduced the CompletableFuture class. Along with the Future interface, it also implemented the CompletionStage interface. This interface defines the contract for an asynchronous computation step that we can combine with other steps.

## What’s a CompletableFuture?

CompletableFuture is used for asynchronous programming in Java. Asynchronous programming is a means of writing non-blocking code by running a task on a separate thread than the main application thread and notifying the main thread about its progress, completion or failure.
This way, your main thread does not block/wait for the completion of the task and it can execute other tasks in parallel.
Having this kind of parallelism greatly improves the performance of your programs.

CompletableFuture is at the same time a building block and a framework, with about 50 different methods for composing, combining, and executing asynchronous computation steps and handling errors.
Such a large API can be overwhelming, but these mostly fall in several clear and distinct use cases.

01 Using CompletableFuture as a Simple Future 
02 CompletableFuture with Encapsulated Computation Logic
03 Processing Results of Asynchronous Computations
04 Combining Futures
05 Difference Between thenApply() and thenCompose()
06 Running multiple Futures in parallel
07 Handing Errors
08 Async Methods

#### References and TODOs:

Runnable vs. Callable in Java: 
https://www.baeldung.com/java-runnable-callable

Baeldung projects:
https://github.com/eugenp/tutorials/tree/master/core-java-modules/core-java-concurrency-basic
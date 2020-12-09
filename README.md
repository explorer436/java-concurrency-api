Examples of the functionality and use cases of the CompletableFuture class that was introduced as a Java 8 Concurrency API improvement.

Asynchronous Computation in Java:

The Future interface was added in Java 5 to serve as a result of an asynchronous computation, but it did not have any methods to combine these computations or handle possible errors.

Java 8 introduced the CompletableFuture class. Along with the Future interface, it also implemented the CompletionStage interface. This interface defines the contract for an asynchronous computation step that we can combine with other steps.

CompletableFuture is at the same time a building block and a framework, with about 50 different methods for composing, combining, and executing asynchronous computation steps and handling errors.

Such a large API can be overwhelming, but these mostly fall in several clear and distinct use cases.

01 Using CompletableFuture as a Simple Future 

References and TODOs:

Runnable vs. Callable in Java: 
https://www.baeldung.com/java-runnable-callable

Baeldung projects:
https://github.com/eugenp/tutorials/tree/master/core-java-modules/core-java-concurrency-basic
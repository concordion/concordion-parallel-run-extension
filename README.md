[![Build Status](https://travis-ci.org/concordion/concordion-parallel-run-extension.svg?branch=master)](https://travis-ci.org/concordion/concordion-parallel-run-extension)

# Introduction
Concordion's [run command]((http://concordion.org/Tutorial.html#concordion:run) runs another specification from the current specification, with the specification link updated to show success or failure. By default these are run sequentially.

This extension modifies the run command to run specifications in parallel. It configures a thread pool, limiting the number of concurrent executions to the size of the thread pool. By default the pool is configured with 4 threads.

## Installation
The extension is available from [Maven Central](http://search.maven.org/#artifactdetails%7Corg.concordion%7Cconcordion-parallel-run-extension%7C1.0.0%7Cjar).

### Dependencies
This runner introduces 2 new dependencies that will need to be on your classpath:

 * `com.google.guava:guava:17.0`
 * `org.slf4j:slf4j-api:1.7.7`

If using a dependency management tool, such as Maven, Gradle or Ivy, these will be resolved as transitive dependencies.

### slf4j runtime
Additionally, to see the logging output, you will need a [runtime binding](http://www.slf4j.org/manual.html#swapping) to a slf4j implementation.

### Example
As an example, the following Gradle dependencies will install v1.0.0 of the extension and configure it to use the Simple slf4j runtime.

```gradle
repositories {
    mavenCentral()
}

dependencies {
    testCompile 'org.concordion:concordion-parallel-run-extension:1.0.0'
    testRuntime 'org.slf4j:slf4j-simple:1.7.10'
} 
```

# Usage
 1. You will need to have your test suite structured to use the [concordion:run](http://concordion.org/Tutorial.html#concordion:run) command. This runner will submit a task to the thread pool to run each specification launched using concordion:run.
 
 2. Set the system property `concordion.extensions` to `org.concordion.ext.run.parallel.ParallelRunExtension`.
 
 3. Set the system property `concordion.run.threadCount` to the maximum number of threads you want to run concurrently. If this property is not set, the specifications will be run sequentially. Suffixing this property value with `C` will multiply the value by the number of processors available to the JVM. For example, the value `2.5C` will set the thread count to 10 when run on a 4-core machine. 

Assuming you have a logging runtime specified, a message in the logging output shows the number of threads that are configured. For example:

```[main] INFO org.concordion.ext.run.parallel - Running concordion:run commands in parallel with 2 threads```  

# Notes
* This runner runs the tests within the same JVM process. To run your tests safely in parallel, your code must be thread-safe. In particular be wary of any shared state (including tests using the same data in a database) or shared resources (eg. static references to browser instances).
* Do not rely on the same threads being used across multiple tests. This runner needs to expand and shrink the thread pool dynamically so that specifications can wait for all the specifications they have launched (using concordion:run) to be complete. Tests will be allocated to the dynamically created threads.
* This runner will run your tests in a random order. Your tests must be able to run in any order.
* The "results generated" timings shown on the specifications will show the duration from which the task was parsed until it completed execution. This will include time that any specifications that it launches spent queued.
* When this runner is launched, a message will be logged (using slf4j) to let you know how many threads have been configured in the thread pool. If you do not see this message, then your runner is not configured properly and will fall back to the standard sequential runner.

# Alternatives
Alternatively, you can run all of the JUnit tests in parallel, for example using JUnit's [ParallelComputer](https://github.com/junit-team/junit/blob/master/doc/ReleaseNotes4.6.md#test-scheduling-strategies), features in your build tool (eg. Ant, Maven, Gradle), or by running multiple suites in your CI server (eg. Jenkins, TeamCity). The downside of these approaches is:
 1. You have to decide up-front how to split the tests to run in parallel, so will not benefit from the dynamic thread allocation that this runner implements.
 2. If using concordion:run, you will have one index page per test suite, but no master index page to collate the output (there is no support in Concordion currently to collate multiple index pages into a summary page - though contributions are welcome!)
 
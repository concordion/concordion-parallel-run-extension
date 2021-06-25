[![Build and Test](https://github.com/concordion/concordion-parallel-run-extension/actions/workflows/ci.yml/badge.svg)](https://github.com/concordion/concordion-parallel-run-extension/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.concordion/concordion-parallel-run-extension.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.concordion%22%20AND%20a%3A%22concordion-parallel-run-extension%22)
[![Apache License 2.0](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# Introduction
Concordion's [run command](https://concordion.org/instrumenting/java/markdown/#run-command) runs another specification from the current specification, with the specification link updated to show success or failure. By default the linked specifications are run sequentially.

This extension modifies the run command to run the linked specifications in parallel. It configures a thread pool, limiting the number of specifications being executed concurrently to the size of the thread pool. By default the pool is configured with 2 threads.

## Installation
The extension is available from [Maven Central](http://search.maven.org/#artifactdetails%7Corg.concordion%7Cconcordion-parallel-run-extension%7C1.1.0%7Cjar). 

### Dependencies
In addition to the Concordion dependency, this extension requires 2 extra dependencies that will need to be on your classpath:

 * `com.google.guava:guava:17.0`
 * `org.slf4j:slf4j-api:1.7.7`

If using a dependency management tool, such as Maven, Gradle or Ivy, these will be automatically resolved as transitive dependencies.

### slf4j runtime
Additionally, to see the logging output, you will need a [runtime binding](http://www.slf4j.org/manual.html#swapping) to a slf4j implementation.

# Usage
 1. You will need to have your test suite structured to use the [concordion:run](https://concordion.org/instrumenting/java/markdown/#run-command) command. This runner will submit a task to the thread pool for each specification linked using concordion:run.
 
 2. Add the extension to Concordion, for example by setting the system property `concordion.extensions` to `org.concordion.ext.ParallelRunExtension`.
 
 3. Set the system property `concordion.run.threadCount` to the maximum number of threads you want to run concurrently. If this property is not set, the runner will default to 2 threads.. Suffixing this property value with `C` will multiply the value by the number of processors available to the JVM. For example, the value `2.5C` will set the thread count to 10 when run on a 4-core machine. 

Assuming you have a logging runtime specified, a message in the logging output shows the number of threads that are configured. For example:

```[main] INFO org.concordion.ext.run.parallel - Running concordion:run commands in parallel with 2 threads```  

## Example
As an example, the following build.gradle script will install v1.1.0 of the extension, configure it to use the Simple slf4j runtime and run the Index.html specification with the number of threads equal to 2.5 * number of cores, writing the concordion output to the build/reports/spec folder.

```gradle
apply plugin: 'java'

repositories {
    mavenCentral()
}

dependencies {
    testCompile 'org.concordion:concordion:2.0.2'
    testCompile 'org.concordion:concordion-parallel-run-extension:1.1.0'
    testRuntime 'org.slf4j:slf4j-simple:1.7.10'
}

test {
    include '**/Index.class'
    systemProperties['concordion.extensions'] = "org.concordion.ext.ParallelRunExtension"
    systemProperties['concordion.run.threadCount'] = "2.5C"                    // 2.5 * number of cores
    systemProperties['concordion.output.dir'] = "$reporting.baseDir/spec"
}

test.dependsOn cleanTest
```

## Example Command Support

When using the example command in Concordion 2.0.0 or later, each example runs as a separate JUnit test. In order for the JUnit results to be accurate, we must wait at the end of each example for all of its run commands to finish. This includes the "outer" example, which contains all commands in the specification that are not in a specific example command.

Where a specification contains multiple examples, each example becomes a synchronisation point, and further examples will not be executed until the current example is finished.

Note that this requires Concordion 2.0.2 or later to work correctly.

# Notes
* This runner runs the tests within the same JVM process. To run your tests safely in parallel, your code must be thread-safe. In particular be wary of any shared state (including tests using the same data in a database) or shared resources (eg. static references to browser instances).
* Do not rely on the same threads being used across multiple tests. This runner needs to expand and shrink the thread pool dynamically so that specifications can wait for all the specifications they have launched (using concordion:run) to be complete. Tests will be allocated to the dynamically created threads.
* This runner will run your tests in a random order. Your tests must be able to run in any order.
* The "results generated" timings shown on the specifications will show the duration from which the task was parsed until it completed execution. This will include time that any specifications that it launches spent queued.

# Alternatives
Alternatively, you can run all of the JUnit tests in parallel, for example using JUnit's [ParallelComputer](https://github.com/junit-team/junit/blob/master/doc/ReleaseNotes4.6.md#test-scheduling-strategies), features in your build tool (eg. Ant, Maven, Gradle), or by running multiple suites in your CI server (eg. Jenkins, TeamCity). The downside of these approaches is:
 1. You have to decide up-front how to split the tests to run in parallel, so will not benefit from the dynamic thread allocation that this runner implements.
 2. If using concordion:run, you will have one index page per test suite, but no master index page to collate the output (there is no support in Concordion currently to collate multiple index pages into a summary page - though contributions are welcome!)

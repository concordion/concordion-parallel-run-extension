# Parallel Run Extension

Concordion's [run command](http://concordion.github.io/concordion/latest/spec/command/run/Run.html)
runs another specification from the current specification,
with the specification link updated to show success or failure. By default these are run sequentially.

This extension modifies the `run` command to run specifications in parallel.
It configures a thread pool, limiting the number of concurrent executions
to the size of the thread pool. By default the pool is configured with 2 threads.

## [Example with 3 threads](- "3-threads")
Given Concordion is [configured](- "configureParallelRun()") to use the parallel run extension with [3](- "setThreadCount(#TEXT)") threads 

A specification that runs 4 tests:

[&lt;a concordion:run="concordion" href="kids/Test1_1.html"&gt;test1&lt;/a&gt;
&lt;a concordion:run="concordion" href="kids/Test1_2.html"&gt;test2&lt;/a&gt;
&lt;a concordion:run="concordion" href="kids/Test1_3.html"&gt;test3&lt;/a&gt;
&lt;a concordion:run="concordion" href="kids/Test1_4.html"&gt;test4&lt;/a&gt;](- "#result=process(#TEXT)")

where each test takes 1 second to run, results in 4 successful tests:

[&lt;a concordion:run="concordion" href="kids/Test1_1.html" class="success"&gt;test1&lt;/a&gt;
 &lt;a concordion:run="concordion" href="kids/Test1_2.html" class="success"&gt;test2&lt;/a&gt;
 &lt;a concordion:run="concordion" href="kids/Test1_3.html" class="success"&gt;test3&lt;/a&gt;
 &lt;a concordion:run="concordion" href="kids/Test1_4.html" class="success"&gt;test4&lt;/a&gt;](- "?=#result.outputFragmentXML")

and completes within [2](- "?=durationGreaterThan(#TEXT)") and [3](- "?=durationLessThan(#TEXT)") seconds.
 
## [Further details](- "")
How does the parallel runner work with [multiple examples](ParallelRunExtensionMultipleExamples.md "c:run")?

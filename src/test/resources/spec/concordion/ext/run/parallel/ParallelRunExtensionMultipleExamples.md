# Parallel Run Extension With Multiple Examples

Run commands inside an example are launched as tasks on separate threads when using the parallel runner.
In order for the JUnit results to be accurate, we must wait at the end of each example for all of its run commands to finish.
This includes the "outer" example, which contains all commands in the specification that are not in a specific example command.

Where a specification contains multiple examples, each example becomes a synchronisation point,
and further examples will not be executed until the current example is finished.

__Note:__ Examples within a specification are run serially.
Parallel running of examples in a specification is not supported since it would not be thread-safe to do so.
For example, the XOM library is not thread-safe and is used by many commands within each example.

## [Example](- "")

Given Concordion is [configured](- "configureParallelRun()") to use the parallel run extension 
with [3](- "setThreadCount(#TEXT)") threads. 

The following specification contains 3 examples:

* The "outer" example runs Test2_1 and Test2_2 
* Example1 runs Test2_3
* Example2 runs Test2_4 and Test2_5

[&lt;a concordion:run="concordion" href="kids/Test2_1.html"&gt;test1&lt;/a&gt;
&lt;a concordion:run="concordion" href="kids/Test2_2.html"&gt;test2&lt;/a&gt;
&lt;div concordion:example="Example 1"&gt;
  &lt;a concordion:run="concordion" href="kids/Test2_3.html"&gt;test3&lt;/a&gt;
&lt;/div&gt; 
&lt;div concordion:example="Example 2"&gt;
  &lt;a concordion:run="concordion" href="kids/Test2_4.html"&gt;test4&lt;/a&gt;
  &lt;a concordion:run="concordion" href="kids/Test2_5.html"&gt;test5&lt;/a&gt;
&lt;/div&gt;](- "#result=process(#TEXT)")

and each test takes 1 second to run.

When run, this results in 5 successful tests within the 3 examples:

[&lt;a concordion:run="concordion" href="kids/Test2_1.html" class="success"&gt;test1&lt;/a&gt;
 &lt;a concordion:run="concordion" href="kids/Test2_2.html" class="success"&gt;test2&lt;/a&gt;
 &lt;div concordion:example="Example 1" id="Example 1"&gt;&lt;p /&gt;
   &lt;a concordion:run="concordion" href="kids/Test2_3.html" class="success"&gt;test3&lt;/a&gt;
 &lt;/div&gt; 
 &lt;div concordion:example="Example 2" id="Example 2"&gt;&lt;p /&gt;
   &lt;a concordion:run="concordion" href="kids/Test2_4.html" class="success"&gt;test4&lt;/a&gt;
   &lt;a concordion:run="concordion" href="kids/Test2_5.html" class="success"&gt;test5&lt;/a&gt;
 &lt;/div&gt;](- "?=#result.outputFragmentXML")

and completes within [3](- "?=durationGreaterThan(#TEXT)") and [4](- "?=durationLessThan(#TEXT)") seconds, since
each example takes approx 1 second to run.

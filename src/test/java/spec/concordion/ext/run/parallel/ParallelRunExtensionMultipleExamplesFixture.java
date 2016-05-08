package spec.concordion.ext.run.parallel;

import org.concordion.api.Resource;
import org.concordion.api.extension.Extensions;
import org.concordion.ext.LoggingTooltipExtension;
import org.concordion.ext.ParallelRunExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import test.concordion.ProcessingResult;
import test.concordion.TestRig;

import java.util.logging.Logger;

@RunWith(ConcordionRunner.class)
@Extensions(LoggingTooltipExtension.class)
public class ParallelRunExtensionMultipleExamplesFixture {
    private long duration;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public ProcessingResult process(String fragment) {
        long start = System.currentTimeMillis();
        ProcessingResult result = new TestRig()
            .withFixture(this)
            .withResource(new Resource("/org/concordion/ext/resource/tooltip.css"), "")
            .withResource(new Resource("/org/concordion/ext/resource/bubble.gif"), "")
            .withResource(new Resource("/org/concordion/ext/resource/bubble_filler.gif"), "")
            .withResource(new Resource("/org/concordion/ext/resource/i16.png"), "")
            .processFragment(fragment);

        long finish = System.currentTimeMillis();
        duration = finish - start;
        return result;
    }

    public Number durationGreaterThan(int seconds) {
        if (duration > seconds * 1000) {
            return seconds;
        } else {
            return 0.001 * duration;
        }
    }

    public Number durationLessThan(int seconds) {
        if (duration < seconds * 1000) {
            return seconds;
        } else {
            return 0.001 * duration;
        }
    }

    public void configureParallelRun() {
        String name = "concordion.extensions";
        String value = ParallelRunExtension.class.getName();
        setSystemProperty(name, value);
    }

    public void setThreadCount(String count) {
        String name = ParallelRunExtension.PROPERTY_RUN_THREAD_COUNT;
        setSystemProperty(name, count);
    }

    @AfterClass
    public static void cleanUp() {
        System.clearProperty("concordion.extensions");
        System.clearProperty(ParallelRunExtension.PROPERTY_RUN_THREAD_COUNT);
    }

    private String setSystemProperty(String name, String value) {
        logger.info(String.format("Set system property '%s' to '%s'", name, value));
        return System.setProperty(name, value);
    }
}


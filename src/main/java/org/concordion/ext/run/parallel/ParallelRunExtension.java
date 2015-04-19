package org.concordion.ext.run.parallel;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

public class ParallelRunExtension implements ConcordionExtension {

    private ParallelRunStrategy extension = new ParallelRunStrategy(); 
    
    public static final String PROPERTY_RUN_THREAD_COUNT = "concordion.run.threadCount";

    static {
        String runThreadCount = System.getProperty(PROPERTY_RUN_THREAD_COUNT);
        if (runThreadCount != null) {
            ParallelRunStrategy.initialise(runThreadCount);
        }
    }
    
    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationProcessingListener(extension);
        concordionExtender.withRunStrategy(extension);
    }
}

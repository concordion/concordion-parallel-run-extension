package org.concordion.ext;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.ext.run.parallel.ParallelRunStrategy;

public class ParallelRunExtension implements ConcordionExtension {

    private ParallelRunStrategy runStrategy = new ParallelRunStrategy(); 
    
    public static final String PROPERTY_RUN_THREAD_COUNT = "concordion.run.threadCount";

    static {
        String runThreadCount = System.getProperty(PROPERTY_RUN_THREAD_COUNT);
        if (runThreadCount != null) {
            ParallelRunStrategy.initialise(runThreadCount);
        }
    }
    
    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationProcessingListener(runStrategy);
        concordionExtender.withRunStrategy(runStrategy);
    }
}

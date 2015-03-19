package uk.ac.ebi.tuzova.listeners;

import org.quartz.*;
import uk.ac.ebi.tuzova.jobs.XmlWriterJob;
import uk.ac.ebi.tuzova.utils.ExperimentId;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: catherine
 * Date: Jul 16, 2010
 *
 * adds xmlWriter job to scheduler when
 * all pub med jobs have been executed
 */
public class PubMedListener implements JobListener {
    private final String myName;
    private AtomicInteger myCounter = new AtomicInteger();
    private static Logger ourLogger = Logger.getLogger("main.log");
    
    public PubMedListener(String myName) {
        this.myName = myName;
    }

    public String getName() {
        return myName;
    }

    // skip, it's not important for this listener
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {}

    // skip, it's not important for this listener
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {}

    /**
     *
     * @param jobExecutionContext -- pubMed job context
     * @param e -- thrown exception from pubMed Job while executing
     * increment pubMed jobs counter.
     * if all pubMed jobs have already executed -- add xmlWriter job
     * to scheduler, else -- return
     */
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {

        JobDataMap pubMedDataMap = jobExecutionContext.getMergedJobDataMap();

        myCounter.incrementAndGet();
        int pubMedJobsSize = (Integer)pubMedDataMap.get("pubMedJobsSize");
        ourLogger.log(Level.INFO, "Finished " + myCounter.intValue() + " of " + pubMedJobsSize + " PubMed jobs.");
        if (myCounter.intValue() < pubMedJobsSize)
            return;

        addXmlWriterJob(jobExecutionContext);
    }

    /**
     *
     * @param jobExecutionContext -- pubMed job context
     *
     * create xml Writer job and add it to scheduler
     */
    private void addXmlWriterJob(JobExecutionContext jobExecutionContext) {
        Scheduler scheduler = jobExecutionContext.getScheduler();
        JobDataMap pubMedDataMap = jobExecutionContext.getMergedJobDataMap();
        Map<String, SortedSet<ExperimentId>> xmlMap = (Map<String, SortedSet<ExperimentId>>)pubMedDataMap.get("xmlMap");

        // add XmlWriter to scheduler
        JobDetail xmlWriterJobDetail = new JobDetail("xmlWriterJob",
                    "xmlWriterJobDetail", XmlWriterJob.class);
        Properties properties = (Properties)pubMedDataMap.get("properties");

        xmlWriterJobDetail.getJobDataMap().put("xmlMap", xmlMap);
        xmlWriterJobDetail.getJobDataMap().put("properties", properties);

        SimpleTrigger xmlWriterTrigger =
                    new SimpleTrigger("xmlWriterTrigger", "xmlWriterTriggerGroup");
        try {
            scheduler.scheduleJob(xmlWriterJobDetail, xmlWriterTrigger);
            scheduler.addJob(xmlWriterJobDetail, true);
        } catch (SchedulerException e) {
            ourLogger.log(Level.SEVERE, "Cannot schedule xmlWriterJob. "
                                    + e.getMessage());
        }
    }
}

package uk.ac.ebi.tuzova.listeners;

import org.quartz.*;
import uk.ac.ebi.tuzova.jobs.*;
import uk.ac.ebi.tuzova.owl.OntologyDistanceCalculator;
import uk.ac.ebi.tuzova.pubMed.PubMedRetriever;
import uk.ac.ebi.tuzova.utils.ExperimentId;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: catherine
 * Date: Jul 16, 2010
 *
 * Listener for zoomaMappingJobs
 *
 * add OntologySimilarityJob and pubMed jobs after all zooma mapping job executed
 */
public class MappingListener implements JobListener {
    private final String myName;
    private static Logger ourLogger = Logger.getLogger("main.log");

    public MappingListener(String myName) {
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
     * @param jobExecutionContext -- zoomaMapping context
     * @param e -- thrown exception from Job while executing
     *
     * add next jobs to scheduler
     */
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        ourLogger.log(Level.INFO, "Finished Mapping job.");

        // add OntologySimilarityJob to scheduler
        addOntologySimilarityJob(jobExecutionContext);
        
        // add PubMedJobs to scheduler
        addPubMedJobs(jobExecutionContext);
    }

    /**
     *
     * @param jobExecutionContext -- zooma Mapping context
     *
     * creates ontology similarity job and add it to scheduler 
     */
    private void addOntologySimilarityJob(JobExecutionContext jobExecutionContext) {
        JobDataMap zoomaMappingDataMap = jobExecutionContext.getMergedJobDataMap();
        Scheduler scheduler = jobExecutionContext.getScheduler();

        JobDetail ontologySimilarityJobDetail = new JobDetail("ontologySimilarityJob",
                    "ontologySimilarityJobDetail", OntologySimilarityJob.class);

        // get data from zooma mapping  context
        Map<String, SortedSet<ExperimentId>> zoomaMap = (Map<String, SortedSet<ExperimentId>>)zoomaMappingDataMap.get("zoomaMap");
        OntologyDistanceCalculator distanceCalculator = (OntologyDistanceCalculator)zoomaMappingDataMap.get("distanceCalculator");
        Map<String, SortedSet<ExperimentId>> xmlMap = (Map<String, SortedSet<ExperimentId>>)zoomaMappingDataMap.get("xmlMap");
        Properties properties = (Properties)zoomaMappingDataMap.get("properties");

        // put data to ontology similarity job context
        ontologySimilarityJobDetail.getJobDataMap().put("zoomaMap", zoomaMap);
        ontologySimilarityJobDetail.getJobDataMap().put("distanceCalculator", distanceCalculator);
        ontologySimilarityJobDetail.getJobDataMap().put("xmlMap", xmlMap);
        ontologySimilarityJobDetail.getJobDataMap().put("properties", properties);

        // create trigger for job
        SimpleTrigger ontologySimilarityTrigger =
                    new SimpleTrigger("ontologySimilarityTrigger", "ontologySimilarityTriggerGroup");
        try {
            scheduler.scheduleJob(ontologySimilarityJobDetail, ontologySimilarityTrigger);
            scheduler.addJob(ontologySimilarityJobDetail, true);
        } catch (SchedulerException e) {
            ourLogger.log(Level.SEVERE, "Cannot schedule ontologySimilarityJob. "
                                    + e.getMessage());
        }
    }

    /**
     *
     * @param jobExecutionContext -- zooma Mapping context
     *
     * creates all pub Med jobs and add them to scheduler
     */
    private void addPubMedJobs(JobExecutionContext jobExecutionContext) {
        Scheduler scheduler = jobExecutionContext.getScheduler();
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        // get data from zooma mapping  context
        Map<String, String> pubMedMap = (Map<String, String>)dataMap.get("pubMedMap");
        Map<String, SortedSet<ExperimentId>> xmlMap = (Map<String, SortedSet<ExperimentId>>)dataMap.get("xmlMap");
        Properties properties = (Properties)dataMap.get("properties");
        PubMedRetriever retriever = new PubMedRetriever();
        int i = 0;
        for (Map.Entry<String, String> entry : pubMedMap.entrySet()) {
            JobDetail pubMedJobDetail = new JobDetail("puMedJob" + i,
                    "pubMedJobDetail" + i,
                    PubMedJob.class);
            // put data to pub med job context
            pubMedJobDetail.getJobDataMap().put("xmlMap", xmlMap);
            pubMedJobDetail.getJobDataMap().put("entry", entry);
            pubMedJobDetail.getJobDataMap().put("retriever", retriever);
            pubMedJobDetail.getJobDataMap().put("pubMedMap", pubMedMap);
            pubMedJobDetail.getJobDataMap().put("pubMedJobsSize", pubMedMap.size());
            pubMedJobDetail.getJobDataMap().put("properties", properties);

            // add listener
            PubMedListener pubMedListener = new PubMedListener("pubMedListener");
            pubMedJobDetail.addJobListener("pubMedListener");
            try {
                scheduler.addJobListener(pubMedListener);
            } catch (SchedulerException ex) {
                ourLogger.log(Level.SEVERE, "Cannot add pubMed listener. " + ex.getMessage());
            }
            // create simple trigger for job
            SimpleTrigger pubMedTrigger =
                        new SimpleTrigger("pubMedTrigger" + i, "group" + i);

            try {
                scheduler.scheduleJob(pubMedJobDetail, pubMedTrigger);
            } catch (SchedulerException e) {
                ourLogger.log(Level.SEVERE, "Cannot schedule job" + e.getMessage());
            }
            ++i;
        }
    }
}

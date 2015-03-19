package uk.ac.ebi.tuzova.jobs;

import org.quartz.*;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.ResourceSet;
import uk.ac.ebi.tuzova.listeners.MappingListener;
import uk.ac.ebi.tuzova.mapping.AEReports;
import uk.ac.ebi.tuzova.owl.OntologyDistanceCalculator;
import uk.ac.ebi.tuzova.utils.ExperimentId;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: catherine
 * Date: Jul 16, 2010
 *
 * Class to schedule project jobs
 */
public class JobController {
    // Key -- ontologyOutcome (as string)
    // value -- set of experiments id which attributes mapped to key by zooma
    private static Map<String, SortedSet<ExperimentId>> ourZoomaMap =
                            new ConcurrentHashMap<String, SortedSet<ExperimentId>>();

    // Key -- id of publication, value -- experiments id
    private static Map<String, String> ourPubMedMap =
                                        new ConcurrentHashMap<String, String>();

    // Key -- experiment ID,
    // value -- set of similar experiment ids
    private static Map<String, SortedSet<ExperimentId>> ourXmlMap =
                            new ConcurrentHashMap<String, SortedSet<ExperimentId>>();

    /**
     *
     * @param properties -- similarity prorerties
     * @param experiments -- set of experiments from experiments.xml
     * @param distanceCalculator -- calculator to get distances between
     * ontology nodes
     * @param report -- zooma mapping report for zooma mapping job
     * @param scheduler -- Quartz scheduler to schedule jobs
     */
    public JobController(Properties properties, ResourceSet experiments,
                         OntologyDistanceCalculator distanceCalculator,
                         AEReports report, Scheduler scheduler) {
        // initialization block
        Logger logger = Logger.getLogger("main.log");
        logger.log(Level.INFO, "Initialization finished. ");

        // add zoomaMappingJobs to scheduler
        JobDetail zoomaMappingJobDetail = new JobDetail("zoomaMappingJob",
                    "zoomaMappingJobDetail", ZoomaMappingJob.class);

        zoomaMappingJobDetail.getJobDataMap().put("experiments", experiments);
        zoomaMappingJobDetail.getJobDataMap().put("zoomaMap", ourZoomaMap);
        zoomaMappingJobDetail.getJobDataMap().put("pubMedMap", ourPubMedMap);
        zoomaMappingJobDetail.getJobDataMap().put("report", report);
        zoomaMappingJobDetail.getJobDataMap().put("properties", properties);

        // for OntologySimilarityJob
        zoomaMappingJobDetail.getJobDataMap().put("distanceCalculator", distanceCalculator);
        zoomaMappingJobDetail.getJobDataMap().put("xmlMap", ourXmlMap);


        // add listener
        MappingListener mappingListener = new MappingListener("mappingListener");
        zoomaMappingJobDetail.addJobListener("mappingListener");
        try {
            scheduler.addJobListener(mappingListener);
        } catch (SchedulerException e) {
            logger.log(Level.SEVERE, "Cannot add mapping listener. " + e.getMessage());
        }

        // add trigger for zooma mapping jobs
        SimpleTrigger zoomaMappingTrigger =
                    new SimpleTrigger("zoomaMappingTrigger", "zoomaMappingTriggerGroup");
        try {
            scheduler.scheduleJob(zoomaMappingJobDetail, zoomaMappingTrigger);
            scheduler.addJob(zoomaMappingJobDetail, true);
        } catch (SchedulerException e) {
            logger.log(Level.SEVERE, "Cannot schedule zoomaMappingJob. "
                                    + e.getMessage());
        }

    }
}

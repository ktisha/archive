package uk.ac.ebi.arrayexpress.jobs;

import org.quartz.*;
import org.xmldb.api.base.XMLDBException;
import uk.ac.ebi.arrayexpress.app.Application;
import uk.ac.ebi.arrayexpress.components.ExistDatabase;
import uk.ac.ebi.tuzova.jobs.XmlWriterJob;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: catherine
 * Date: Jul 16, 2010
 *
 * Listener for xmlWriter Job
 * writes similarity xml to database
 */
public class SimilarityXmlWriterListener implements JobListener {
    private final String myName;

    public SimilarityXmlWriterListener(String myName) {
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
     * @param jobExecutionContext -- xmlWriterjob context
     * @param e thrown exception from job
     *
     * adds job to write result to database
     */
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();

        if (jobDetail.getJobClass().equals(XmlWriterJob.class)) {            
            Logger logger = Logger.getLogger("main.log");
            logger.log(Level.INFO, "Xml job finished. ");
            JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
            Properties properties = (Properties) dataMap.get("properties");
            final String collectionName = properties.getProperty("experiment_collection");
            final String fileName = "tmp/" + properties.getProperty("report_file");

            ExistDatabase existDatabase = (ExistDatabase) Application.getAppComponent("ExistDatabase");

            File file = new File(fileName);
            if(!file.canRead()) {
                logger.log(Level.SEVERE, "Cannot read file " + fileName);
                return;
            }
            
            try {
                existDatabase.storeDocument(collectionName, file, properties.getProperty("report_file"));
            } catch (XMLDBException e1) {
                logger.log(Level.SEVERE, "Cannot store document. " + e1.getMessage());
            }
           
            String tmpDir = file.getParent();
            boolean success = file.delete();

            if (!success)
              logger.log(Level.WARNING, "Cannot delete tmp similarity file. ");

            File dir = new File(tmpDir);
            success = dir.delete();

            if (!success)
              logger.log(Level.WARNING, "Cannot delete tmp similarity directory. ");

        }
    }
}

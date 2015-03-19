package uk.ac.ebi.arrayexpress.jobs;

import org.exist.xmldb.ResourceSetHelper;
import org.quartz.*;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import uk.ac.ebi.arrayexpress.app.Application;
import uk.ac.ebi.arrayexpress.components.ExistDatabase;
import uk.ac.ebi.tuzova.jobs.JobController;
import uk.ac.ebi.tuzova.mapping.AEReports;
import uk.ac.ebi.tuzova.owl.OntologyDistanceCalculator;
import uk.ac.ebi.tuzova.xml.XPathParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * User: catherine
 * Date: Jul 22, 2010
 *
 * Main job in similarity project.
 * Adds all other jobs to the
 * same quartz scheduler as it was started in.
 */
public class SimilarityJob implements InterruptableJob {
    private static final Logger ourLogger = Logger.getLogger("main.log");
        private static final String ourPathToProperties = "/WEB-INF/classes/similarity.properties";

    private static final String ourPathToLog = "log/similarity.log";

    // default properties for program, using if PROPERTIES_FILE broken
    private static final String[][] ourDefaultProperties = {
        {"ontology_type", "efo"},
        {"ontology_url", "http://efo.svn.sourceforge.net/viewvc/efo/trunk/src/efoinowl/efo.owl"},
        {"report_file", "similarity.xml"},
        {"pub_med_url", "http://www.ncbi.nlm.nih.gov/sites/pubmed?db=pubmed&cmd=link&linkname=pubmed_pubmed_citedin&uid="},
        {"max_ontology_distance", "3"},
        {"max_pubmed_distance", "1"},
        {"database_driver", "org.exist.xmldb.DatabaseImpl"},
        {"database_user","admin"},
        {"database_pass", ""},
        {"database_uri","xmldb:exist://"},
        {"experiment_collection", "/ae"},
        {"max_similarity", "10"},
        {"experiments_file", "experiments.xml"}
    };

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // Configure logger
        //configureLogger(ourPathToLog);
        ourLogger.log(Level.INFO, "Program starts. ");

        // load properties
        Properties properties = loadProperties();
        String efoURL = properties.getProperty("ontology_url");
        int maxOntologyDistance = Integer.parseInt(properties.getProperty("max_ontology_distance"));
        final String experimentCollection = properties.getProperty("experiment_collection");
        final String experimentsFile = properties.getProperty("experiments_file");
        try {
            //XPathParser experimentsParser = new XPathParser();

            ExistDatabase existDatabase = (ExistDatabase) Application.getAppComponent("ExistDatabase");
            
            // Parse xml file contains experiments
            ourLogger.log(Level.INFO, "Start parsing file with experiments. ");
            ResourceSet experiments = existDatabase.evaluateXPath(experimentCollection,
                                                "doc(\"" + experimentsFile + "\")//experiment");
            ourLogger.log(Level.INFO, "Got " + experiments.getSize() + " experiments.");

            // create distance calculator to search similar experiments
            OntologyDistanceCalculator distanceCalculator = new OntologyDistanceCalculator(efoURL, maxOntologyDistance);
            // create job controller to schedule other jobs
            Scheduler scheduler = jobExecutionContext.getScheduler();
            //for zooma jobs
            AEReports report = new AEReports();
            SimilarityXmlWriterListener xmlWriterListener = new SimilarityXmlWriterListener("xmlWriterListener");
            scheduler.addGlobalJobListener(xmlWriterListener);
            new JobController(properties, experiments, distanceCalculator, report, scheduler);

        } catch (XMLDBException e) {
            ourLogger.log(Level.SEVERE, "Cannot get experiments file from db."
                                        + e.getMessage());
        } catch (SchedulerException e) {
            ourLogger.log(Level.SEVERE, "Cannot add global xml writer listener."
                                        + e.getMessage());
        }
    }

    /**
     *
     * @return loaded from file properties
     * if file is broken return default Props
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            // Load props from the file
            InputStream propStream = Application.getInstance().getResource(ourPathToProperties).openStream();
            properties.load(propStream);
            propStream.close();
        } catch (IOException ex) {
            for (String[] property : ourDefaultProperties) {
                properties.setProperty(property[0], property[1]);
            }
            ourLogger.log(Level.WARNING,"Cannot open properties file "
                    + ourPathToProperties + ", using default values");
            ourLogger.log(Level.WARNING,"Got exception: " + ex.getMessage());
        }
        return properties;
    }

    public void interrupt() throws UnableToInterruptJobException {
        Thread.currentThread().interrupt();
    }

    /**
    *
    * @param pathToFile is path to file storing log
    */
    private static void configureLogger(String pathToFile) {
        try {
            // This block configure the logger with handler and formatter
            FileHandler fileHandler = new FileHandler(pathToFile, true);
            ourLogger.addHandler(fileHandler);
            ourLogger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

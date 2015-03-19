package uk.ac.ebi.tuzova;

import org.w3c.dom.NodeList;
import uk.ac.ebi.tuzova.owl.OntologyDistanceCalculator;
import uk.ac.ebi.tuzova.pubMed.PubMedRetriever;

import java.io.*;

import java.net.URL;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * User: tuzova
 * Date: Jul 5, 2010
 * Time: 9:31:31 AM
 */
public class Temp {
    private static final Logger ourLogger = Logger.getLogger("main.log");
    public static final File PROPERTIES_FILE = new File("main.properties");
    private static final String ourPathToLog = "./log/main.log";

    private static int ourLinesInInput = 0;
    private static int ourSucessCounter = 0;
    private static int ourCurationCounter = 0;
    private static int ourFailureCounter = 0;

    // default properties for program, using if PROPERTIES_FILE broken
    private static final String[][] ourDefaultProperties = {
        {"experiments_xml_file", "data/experiments.xml"},
        {"reports_directory", "data/reports"},
        {"success_report","/AutoMappingsReport.txt"},
        {"failure_report","/AutoMappingsFailureReport.txt"},
        {"text_input", "data/input"},
        {"ontology_type", "efo"},
        {"statistics_file", "data/statistics"},
        {"report_file", "/report"},
        {"ontology_url", "data/efo.owl"},
        {"similarity_file", "data/similarity"}
    };


    public static void main(String[] args) throws IOException {
        // Configure logger
        configureLogger(ourPathToLog);
        ourLogger.log(Level.INFO, "Program starts. ");

        // load properties
        Properties properties = loadProperties();

        String experimetsFile = properties.getProperty("experiments_xml_file");
        String textInput = properties.getProperty("text_input");
        String reportsDirectory = properties.getProperty("reports_directory");
        String ontologyType = properties.getProperty("ontology_type");
        String reportFile = reportsDirectory + properties.getProperty("report_file");
        String fileReportSuccessName = reportsDirectory +
                                properties.getProperty("success_report");
        String fileReportFailureName = reportsDirectory +
                                properties.getProperty("failure_report");
        String efoURL = properties.getProperty("ontology_url");
        String similarityFile = properties.getProperty("similarity_file");


        PubMedRetriever retriever = new PubMedRetriever();
        NodeList l = retriever.getSimilars(new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?db=pubmed&id=15640145"));
        for (int i = 0; i != l.getLength(); ++i) {
            System.out.println(l.item(i).getTextContent());
        }
        /*
        OWLOntologyManager myOntologyManager = OWLManager.createOWLOntologyManager();
        File file = new File(efoURL);      // from file

        //from web
        //IRI iri = IRI.create(ontologyURL");
        org.semanticweb.owlapi.model.OWLOntology myOntology = null;
        try {
            myOntology = myOntologyManager.loadOntologyFromOntologyDocument(file);
        } catch ( OWLOntologyCreationException e) {
            ourLogger.log(Level.SEVERE, "Cannot load ontology from " +
                    efoURL + " " + e.getMessage());
        }

        // Parse xml file contains experiments
        ourLogger.log(Level.INFO, "Start parsing file with experiments. ");
        XPathParser parser = new XPathParser(experimetsFile);
        NodeList nodes = parser.extractExperiments();

        // create distance calculator to search similar experiments
        ourLogger.log(Level.INFO, "Calculate distances. ");
        OntologyDistanceCalculator distanceCalculator = new OntologyDistanceCalculator(efoURL);

        // create reports
        ourLogger.log(Level.INFO, "Start creating reports. ");

        try {
            
            FileWriter reportStream = new FileWriter(reportFile);
            BufferedWriter reportWriter = new BufferedWriter(reportStream);
            int experimentCounter = 0;

            FileWriter simStream = new FileWriter(similarityFile);
            BufferedWriter simWriter = new BufferedWriter(simStream);
            ourLogger.log(Level.INFO, "Report files opened. ");
            // do zooma mapping
            for (int i = 0; i != nodes.getLength(); ++i ) {
                //if (i % 20 != 0)       // to take only 5% of all experiments
                //    continue;
                NodeList experimentChildren = nodes.item(i).getChildNodes();
                ourLinesInInput = 0;
                prepareZoomaInput(experimentChildren, textInput);

                if (ourLinesInInput <= 0)
                    continue;

                ++experimentCounter;

                //generate zooma report
                Reports.getTextReporter(new File(""))
                        .generateTextAutomaticMappingsReport(
                                new File(reportsDirectory), new File(textInput), ontologyType);

                // create general report

                generateReport(reportWriter, experimentCounter,
                                    fileReportSuccessName, fileReportFailureName, distanceCalculator, simWriter);
            }
            simWriter.close();
            reportWriter.write("Automatic mapping --  " + ourSucessCounter + "\n");
            reportWriter.write("Mapping required curation --  " + ourCurationCounter + "\n");
            reportWriter.write("Failure mapping --  " + ourFailureCounter + "\n");
            reportWriter.close();
        } catch (IOException e) {
            ourLogger.log(Level.SEVERE, e.getMessage());
        }

        /*
        OWLDataFactory fac = manager.getOWLDataFactory();
        String ontString = "NCBITaxon#NCBITaxon_10090";
        String prefix = "http://purl.org/obo/owl/";

        OWLClass cl = fac.getOWLClass(ontString, new DefaultPrefixManager(prefix));

        /*
        Query q = new Query("<http://www.ebi.ac.uk/efo/EFO_0002424>","<http://www.ebi.ac.uk/efo/EFO_0002423>");
        System.out.println(myMap.get(q));
        myMap.put(q, new Integer(20));

        System.out.println(myMap.get(q));
        */
        /*
        Set set = cl.getSubClasses(ont);
        for (Object o : set) {
            System.out.println(((OWLClass)o).getNNF());
        }
        */
        

    }
    /**
     *
     * @param reportWriter report file handler
     * @param experimentCounter number of experiment
     * @param fileReportName to extract success mapping for experiment
     * @param fileReportFailureName to extract failure mapping for experiment
     * @throws IOException while open/close different files
     */
    private static void generateReport(BufferedWriter reportWriter,
                            int experimentCounter, String fileReportName,
                            String fileReportFailureName,
                            OntologyDistanceCalculator dc, BufferedWriter simWriter) throws IOException {

        reportWriter.write("Experiment" + experimentCounter + "\n");
        reportWriter.write("Success: \n");

        FileInputStream successStream = new FileInputStream(fileReportName);
        DataInputStream successIn = new DataInputStream(successStream);
        BufferedReader successBr = new BufferedReader(new InputStreamReader(successIn));
        String strLine = successBr.readLine();
        while ((strLine = successBr.readLine()) != null)   {
            if (strLine.contains("Automatic")) {
                // write similar results to file
                reportWriter.write(strLine + "\n");
                String[] strings = strLine.split("\t");
                String ont = null;
                if (strings[strings.length - 2].contains("CHEBI"))
                    ont = "<" + "http://www.ebi.ac.uk/chebi/searchId.do;?chebiId=" + strings[strings.length - 2] + ">";
                else if (strings[strings.length - 2].contains("NCBITaxon"))
                    ont = "<" + "http://purl.org/obo/owl/NCBITaxon#" + strings[strings.length - 2] + ">";
                else
                    ont = "<" + strings[strings.length - 1] + strings[strings.length - 2] + ">";
                simWriter.write(ont + "\n");
                Set s = dc.getSimilarNodes(ont);
                if (s == null)
                    simWriter.write("No similar classes!");
                else
                    simWriter.write(dc.getSimilarNodes(ont).toString());
                simWriter.write("\n\n");
                ++ourSucessCounter;
            }
        }
        successIn.close();

        reportWriter.write("Requires curation: \n");

        FileInputStream curationStream = new FileInputStream(fileReportName);
        DataInputStream curationIn = new DataInputStream(curationStream);
        BufferedReader curationBr = new BufferedReader(new InputStreamReader(curationIn));
        strLine = curationBr.readLine();
        while ((strLine = curationBr.readLine()) != null)   {
            if (strLine.contains("Requires curation")) {
                reportWriter.write(strLine + "\n");
                ++ourCurationCounter;
            }
        }
        curationIn.close();

        reportWriter.write("Failure: \n");

        FileInputStream failureStream = new FileInputStream(fileReportFailureName);
        DataInputStream failureIn = new DataInputStream(failureStream);
        BufferedReader failureBr = new BufferedReader(new InputStreamReader(failureIn));
        strLine = failureBr.readLine();
        while ((strLine = failureBr.readLine()) != null)   {
            if (strLine.contains("Automatic")) {
                reportWriter.write(strLine + "\n");
                ++ourFailureCounter;
            }
        }
        failureIn.close();

        reportWriter.write("\n\n");

    }

    /**
     *
     * @param experimentChildren to extract text values
     * @param inputFile for zooma mapping
     * @throws java.io.IOException if can't open/close inputFile
     */
    private static void prepareZoomaInput (NodeList experimentChildren,
                                           String inputFile) throws IOException {
        for (int j = 0; j != experimentChildren.getLength(); ++j) {
            if ( experimentChildren.item(j).getNodeName().equals("sampleattribute")) {
                NodeList sampleAttrChildren = experimentChildren.item(j).getChildNodes();
                if ( !sampleAttrChildren.item(0).getTextContent().equals("Organism")) {
                    for (int k = 1; k != sampleAttrChildren.getLength(); ++k) {
                        if ( sampleAttrChildren.item(k).getNodeName().equals("value")) {
                            FileWriter fstream = new FileWriter(inputFile);
                            BufferedWriter out = new BufferedWriter(fstream);
                            out.write(sampleAttrChildren.item(k).getTextContent() + "\n");
                            out.close();
                            ++ourLinesInInput;
                        }
                    }
                }
            }
        }
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

    /**
     *
     * @return loaded from file properties
     * if file is broken return default Props
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            // Load props from the file
            properties.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException ex) {
            for (String[] property : ourDefaultProperties) {
                properties.setProperty(property[0], property[1]);
            }
            ourLogger.log(Level.WARNING,"Cannot open properties file "
                    + PROPERTIES_FILE + ", using default values");
            ourLogger.log(Level.WARNING,"Got exception: " + ex.getMessage());
        }
        return properties;
    }



}

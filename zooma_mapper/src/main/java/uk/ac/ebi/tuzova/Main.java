package uk.ac.ebi.tuzova;

import org.w3c.dom.NodeList;

import uk.ac.ebi.microarray.zooma.eval.OntologyMappingOutcome;
import uk.ac.ebi.microarray.zooma.hypothesis.OntologyMappingHypothesis;
import uk.ac.ebi.ontocat.OntologyTerm;
import uk.ac.ebi.tuzova.mapping.AEReports;
import uk.ac.ebi.tuzova.owl.OntologyDistanceCalculator;
import uk.ac.ebi.tuzova.pubMed.PubMedRetriever;
import uk.ac.ebi.tuzova.xml.XPathParser;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * User: tuzova
 * Date: Jun 30, 2010
 * Time: 9:04:56 AM
 *
 * Main class for the project
 */

public class Main {
    private static final Logger ourLogger = Logger.getLogger("main.log");
    public static final File PROPERTIES_FILE = new File("main.properties");
    private static final String ourPathToLog = "./log/main.log";

    // Key -- attribute name, value -- set of experiments id that have that attribute
    private static Map<String, Set<String>> ourAttributesMap = new HashMap();

    // Key -- id of publication, value -- experiments id 
    private static Map<String, String> ourPubMedMap = new HashMap();

    // default properties for program, using if PROPERTIES_FILE broken
    private static final String[][] ourDefaultProperties = {
        {"experiments_xml_file", "data/experiments.xml"},
        {"ontology_type", "efo"},
        {"ontology_url", "data/efo.owl"},
        {"text_input", "data/input"},
        {"reports_directory", "data/reports/"},
        {"success_report","AutoMappingsReport.txt"},
        {"failure_report","AutoMappingsFailureReport.txt"},
        {"attribute_statistics_file", "attribute_statistics"},
        {"report_file", "report"},
        {"similarity_file", "similarity"},
        {"pub_med", "pub_med"},
        {"pub_med_url", "http://www.ncbi.nlm.nih.gov/sites/pubmed?db=pubmed&cmd=link&linkname=pubmed_pubmed_citedin&uid="}
    };


    /**
     *
     * @param args -- skip it
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // Configure logger
        configureLogger(ourPathToLog);
        ourLogger.log(Level.INFO, "Program starts. ");

        // load properties
        Properties properties = loadProperties();

        String experimetsFile = properties.getProperty("experiments_xml_file");
        String reportsDirectory = properties.getProperty("reports_directory");
        String ontologyType = properties.getProperty("ontology_type");
        String reportFile = reportsDirectory + properties.getProperty("report_file");
        String efoURL = properties.getProperty("ontology_url");
        String similarityFile = reportsDirectory + properties.getProperty("similarity_file");
        String attributeStatisticsFile = reportsDirectory + properties.getProperty("attribute_statistics_file");
        String pubMedStatisticsFile = reportsDirectory + properties.getProperty("pub_med");
        String pubMedURL = properties.getProperty("pub_med_url");
        
        // Parse xml file contains experiments
        ourLogger.log(Level.INFO, "Start parsing file with experiments. ");
        XPathParser experimentsParser = new XPathParser(experimetsFile);
        NodeList experiments = experimentsParser.extractExperiments();

        // create distance calculator to search similar experiments
        ourLogger.log(Level.INFO, "Calculate distances. ");
        //OntologyDistanceCalculator distanceCalculator = new OntologyDistanceCalculator(efoURL);

        // create reports
        ourLogger.log(Level.INFO, "Start creating reports. ");
        
        try {
            AEReports report = new AEReports( new File(""));

            // open files
            /*FileWriter reportStream = new FileWriter(reportFile);
            BufferedWriter reportWriter = new BufferedWriter(reportStream);
            FileWriter simStream = new FileWriter(similarityFile);
            BufferedWriter simWriter = new BufferedWriter(simStream);
            */
            // go throw experiments set
            for (int i = 0; i != experiments.getLength(); ++i ) {
                NodeList experimentChildren = experiments.item(i).getChildNodes();
                String experimentId = experiments.item(i).getFirstChild().getTextContent();
                List<String> values = prepareZoomaInput(experimentChildren, experimentId);
                // using zooma mapping 
                //HashMap result = report.generateAutomaticMappingsReport(values, ontologyType);
                //generateReport(result, reportWriter, simWriter, distanceCalculator);
            }
            /*reportWriter.close();
            simWriter.close();
            */
            // write map of attributes -> set<experimentId> to file
            /*FileWriter statStream = new FileWriter(attributeStatisticsFile);
            BufferedWriter statWriter = new BufferedWriter(statStream);
            for (Map.Entry o : ourAttributesMap.entrySet()) {
                statWriter.write(o.getKey() + "\n");
                Set set = (Set) o.getValue();
                for (Object ob : set) {
                    statWriter.write(ob + "  ");
                }
                statWriter.write("\n\n");
            }
            System.out.println(report.getFailCount());
            System.out.println(report.getSuccessCount());
            */
        } catch (IOException e) {
            ourLogger.log(Level.SEVERE, "Cannot open/close some files. "
                                        + e.getMessage());
        }

        // Pub Med similarity
        ourLogger.log(Level.INFO, "Writing Pub Med statistics. ");
        PubMedRetriever retriever = new PubMedRetriever();
        FileWriter pubStream = new FileWriter(pubMedStatisticsFile);
        BufferedWriter pubWriter = new BufferedWriter(pubStream);
        for (Map.Entry entry : ourPubMedMap.entrySet()) {
            String publicationURL = pubMedURL + entry.getKey();

            ourLogger.log(Level.INFO, "Getting similarity from Pub Med. ");
            NodeList similarPublications = retriever.getSimilars(new URL(publicationURL));

            pubWriter.write("Experiment id:  " + entry.getValue() + "\n");
            if(similarPublications != null) {
                for (int i = 0; i != similarPublications.getLength(); ++i) {
                    String publication = similarPublications.item(i).getTextContent();
                    String experId = ourPubMedMap.get(publication);
                    if ( experId != null)
                        pubWriter.write(experId  + "  ");
                }
            }
            pubWriter.write("\n\n");
            pubWriter.flush();
        }
        pubWriter.close();
    }

    /**
     *
     * @param terms string to get similarity
     * @param simWriter writer to write similarity report
     * @param dc distance calculator
     * @throws IOException if cannot write to simFile
     */
    private static void generateSimilarityReport(String terms, BufferedWriter simWriter, OntologyDistanceCalculator dc) throws IOException {
        // write to file ontology and set of similar ontology
        String ont;
        if (terms.contains("CHEBI"))
            ont = "<" + "http://www.ebi.ac.uk/chebi/searchId.do;?chebiId=" + terms + ">";
        else if (terms.contains("NCBITaxon"))
            ont = "<" + "http://purl.org/obo/owl/NCBITaxon#" + terms + ">";
        else
            ont = "<" + "http://www.ebi.ac.uk/efo/" + terms + ">";
        simWriter.write(ont + "\n");
        Set s = dc.getSimilarNodes(ont);
        if (s == null)
            simWriter.write("No similar classes!");
        else
            simWriter.write(dc.getSimilarNodes(ont).toString());
        simWriter.write("\n\n");

    }
    /**
     *
     * @param result map from string to ontologyMappingOutcome
     * @param reportWriter writer to write report
     * @param simWriter pass to generateSimilarityReport
     * @param distanceCalculator pass to generateSimilarityReport 
     * @throws IOException while open/close different files
     */
    private static void generateReport(HashMap<String, OntologyMappingOutcome> result,
                                       BufferedWriter reportWriter, BufferedWriter simWriter, OntologyDistanceCalculator distanceCalculator) throws IOException {
        // writes to file map: initial value -> generated with zooma Outcome
        for (Map.Entry e : result.entrySet()) {
            reportWriter.write(e.getKey() + "\n");

            for (OntologyMappingHypothesis hypothesis : ((OntologyMappingOutcome)((Map.Entry)e).getValue()).getValuedHypotheses()) {
                for (OntologyTerm term : hypothesis.getOntologyTerms()) {

                    StringBuffer termStr = new StringBuffer();

                    if (term.getLabel() != null) {
                        String textOntologyTerm =
                                extractOntologyTermFromURI(term.getAccession());
                        termStr.append(textOntologyTerm).append(", ");
                    } else {
                        termStr.append(term.getAccession())
                                .append(", ");
                    }
                    String terms =
                            termStr.length() > 0
                                    ? termStr.substring(0, termStr.length() - 2)
                                    : "No Terms";
                    reportWriter.write(terms + "\n");
                    generateSimilarityReport(terms, simWriter, distanceCalculator);
                }
                reportWriter.write("\n");
            }
        }
    }

    /**
     *
     * @param experimentChildren to extract text values
     * @param experimentId is experiment id we preparing input
     * @throws java.io.IOException if can't open/close inputFile
     * @return list of variables need to be maped
     */
    private static List<String> prepareZoomaInput(NodeList experimentChildren,
                                        String experimentId) throws IOException {
        List<String> values = new ArrayList();

        for (int j = 0; j != experimentChildren.getLength(); ++j) {
            if (experimentChildren.item(j).getNodeName().equals("sampleattribute")) {
                NodeList sampleAttrChildren = experimentChildren.item(j).getChildNodes();
                if (!sampleAttrChildren.item(0).getTextContent().equals("Organism")) {
                    for (int k = 1; k != sampleAttrChildren.getLength(); ++k) {
                        if (sampleAttrChildren.item(k).getNodeName().equals("value")) {
                            String value = sampleAttrChildren.item(k).getTextContent();
                            if (!value.contains("year") && !value.contains("month")
                                    && !value.contains("week") && !value.contains("day")
                                    && !value.contains("hour") && !value.contains("minute")
                                    && !value.contains("second")) {
                                values.add(sampleAttrChildren.item(k).getTextContent());
                                Set<String> set;
                                if (!ourAttributesMap.containsKey(value)) {
                                    set = new TreeSet();
                                } else {
                                    set = ourAttributesMap.get(value);
                                }
                                set.add(experimentId);
                                ourAttributesMap.put(sampleAttrChildren.item(k).getTextContent(), set);

                            }

                        }
                    }
                }
            }
            // for Pub Med similarity
            else if (experimentChildren.item(j).getNodeName().equals("bibliography")) {
                NodeList bibliographyChildren = experimentChildren.item(j).getChildNodes();
                for (int k = 0; k != bibliographyChildren.getLength(); ++k) { 
                    if (bibliographyChildren.item(k).getNodeName().equals("accession")) {
                        String value = bibliographyChildren.item(k).getTextContent();
                        ourPubMedMap.put(value, experimentId);
                    }
                }
            }
        }

        return values;
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

    /**
     *  got from text reporter from zooma
     * @param termURI url to extract ontology term
     * @return ontology term
     */
    static protected String extractOntologyTermFromURI(String termURI) {
        // chebi uris, everything after last '='
        if (termURI.contains("http://www.ebi.ac.uk/chebi/searchId")) {
            return termURI.substring(termURI.lastIndexOf("=") + 1);
        }

        // if it's something other than chebi, we want the real URI
        URI uri = URI.create(termURI);
        if (uri.getPath().contains("efo/EFO")) {
            // efo uris, term is everything after last '/'
            return uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1);
        } else if (uri.getFragment() != null) {
            // a uri with a non-null fragment, so is probably really a uri
            return uri.getFragment();
        } else {
            // who knows what this termURI looks like?
            // Possibly, has been munged and is a already a "term"
            // just return it
            return termURI;
        }
    }
}

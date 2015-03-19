package uk.ac.ebi.tuzova.jobs;

import org.quartz.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import uk.ac.ebi.microarray.zooma.eval.OntologyMappingOutcome;
import uk.ac.ebi.microarray.zooma.hypothesis.OntologyMappingHypothesis;
import uk.ac.ebi.ontocat.OntologyTerm;
import uk.ac.ebi.tuzova.mapping.AEReports;
import uk.ac.ebi.tuzova.utils.ExperimentId;
import uk.ac.ebi.tuzova.utils.ReceivingType;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: catherine
 * Date: Jul 16, 2010
 *
 * Job that creating zooma mapping for one experiment (using it's index)
 * for zooma mapping we use AEReports
 */
public class ZoomaMappingJob implements InterruptableJob {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger("main.log");

        // get data 
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Map<String, SortedSet<ExperimentId>> zoomaMap = (Map<String, SortedSet<ExperimentId>>)dataMap.get("zoomaMap");
        Map<String, String> pubMedMap = (Map<String, String>)dataMap.get("pubMedMap");

        AEReports report = (AEReports) dataMap.get("report");
        
        Properties properties = (Properties) dataMap.get("properties");
        String ontologyType = properties.getProperty("ontology_type");

        ResourceSet experiments = (ResourceSet)dataMap.get("experiments");
        try {
            ResourceIterator it = experiments.getIterator();
            while (it.hasMoreResources()) {
                Node experiment = ((XMLResource)it.nextResource()).getContentAsDOM();
                NodeList experimentChildren = experiment.getChildNodes();
                String experimentId = experiment.getFirstChild().getNextSibling().getFirstChild().toString();

                List<String> values = prepareInputs(experimentChildren, experimentId, pubMedMap);
                // using zooma mapping
                HashMap<String, OntologyMappingOutcome> result = report.generateAutomaticMappingsReport(values, ontologyType);

                //fill the atributes map
                for ( Object e : result.values()) {
                    SortedSet<ExperimentId> experimentIds;
                    String termUrl;
                    for (OntologyMappingHypothesis hypothesis :
                            (((OntologyMappingOutcome)e).getBestHypotheses())) {
                        for (OntologyTerm term : hypothesis.getOntologyTerms()) {
                            termUrl = term.getAccession();

                            if (zoomaMap.containsKey(termUrl)) {
                                experimentIds = zoomaMap.get(termUrl);
                            } else {
                                experimentIds = Collections.synchronizedSortedSet(new TreeSet<ExperimentId>());
                            }
                            experimentIds.add(new ExperimentId(experimentId, ReceivingType.OWL));
                            zoomaMap.put(termUrl, experimentIds);

                        }
                    }

                }
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Problem with preparing input in ZoomaMappingJob"
                                            + e.getMessage() );
        } catch (XMLDBException e) {
            logger.log(Level.SEVERE, "Problem with xmldb in method get size. "
                                            + e.getMessage() );
        }

    }

    /**
     *
     * @param experimentChildren to extractFromString text values
     * @param experimentId is experiment id we preparing input
     * @param pubMedMap -- map for experiments having associated publication
     * @throws java.io.IOException if can't open/close inputFile
     * @return list of variables need to be maped
     *
     * preparing input for zooma mapping and for pubMed similarity
     * (put it to pubMedMap)
     */
    private static List<String> prepareInputs(NodeList experimentChildren,
                                      String experimentId, Map<String,
                                      String> pubMedMap) throws IOException {
        List<String> values = new ArrayList<String>();

        for (int j = 0; j != experimentChildren.getLength(); ++j) {
            if (experimentChildren.item(j).getNodeName().equals("sampleattribute")) {
                NodeList sampleAttrChildren = experimentChildren.item(j).getChildNodes();
                if (null != sampleAttrChildren) {
                    if (!sampleAttrChildren.item(0).getFirstChild().toString().equals("Organism")) {
                        for (int k = 1; k != sampleAttrChildren.getLength(); ++k) {
                            if (sampleAttrChildren.item(k).getNodeName().equals("value")) {
                                String value = sampleAttrChildren.item(k).getFirstChild().toString();
                                if (!value.contains("year") && !value.contains("month")
                                        && !value.contains("week") && !value.contains("day")
                                        && !value.contains("hour") && !value.contains("minute")
                                        && !value.contains("second") && !value.contains("female")) {
                                    values.add(sampleAttrChildren.item(k).getFirstChild().toString());
                                }

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
                        String value = bibliographyChildren.item(k).getFirstChild().toString();
                        pubMedMap.put(value, experimentId);
                    }
                }
            }
        }

        return values;
    }

    public void interrupt() throws UnableToInterruptJobException {
        Thread.currentThread().interrupt();
    }
}

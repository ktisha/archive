package uk.ac.ebi.tuzova.jobs;

import org.quartz.*;
import org.w3c.dom.NodeList;
import uk.ac.ebi.tuzova.pubMed.PubMedRetriever;
import uk.ac.ebi.tuzova.utils.ExperimentId;
import uk.ac.ebi.tuzova.utils.ReceivingType;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: catherine
 * Date: Jul 15, 2010
 *
 * calculates similar experiments for one experiment
 * using publications in PubMed (pubMed retriever)
 */
public class PubMedJob implements InterruptableJob {
    public PubMedJob() {}
    
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger("main.log");
        //get data from context
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Map<String, SortedSet<ExperimentId>> xmlMap = (Map<String, SortedSet<ExperimentId>>)dataMap.get("xmlMap");
        Map.Entry<String, String> entry = (Map.Entry<String, String>)dataMap.get("entry");
        PubMedRetriever retriever = (PubMedRetriever)dataMap.get("retriever");
        Map<String, String> pubMedMap = (Map<String, String>)dataMap.get("pubMedMap");

        Properties properties = (Properties) dataMap.get("properties");
        String pubMedURL = properties.getProperty("pub_med_url");

        int maxPubMedDist = Integer.parseInt(properties.getProperty("max_pubmed_distance")); 

        SortedSet<ExperimentId> result;
        if (xmlMap.containsKey(entry.getValue())) {
            result = xmlMap.get(entry.getValue());
        } else {
            result = Collections.synchronizedSortedSet(new TreeSet<ExperimentId>());
        }
        String publicationURL = pubMedURL + entry.getKey();

        NodeList similarPublications = null;
        try {
            similarPublications = retriever.getSimilars(new URL(publicationURL));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        // if similar publications exists
        if (similarPublications != null) {
            for (int i = 0; i != similarPublications.getLength(); ++i) {
                String publication =
                        similarPublications.item(i).getTextContent();

                // block for using publications with distance == 2
                if (maxPubMedDist == 2) {
                    String pubURL = pubMedURL + publication;
                    NodeList simPublications = null;
                    try {
                        simPublications =
                                    retriever.getSimilars(new URL(pubURL));
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, e.getMessage());
                    }

                    // add every similar result
                    if (simPublications != null) {
                        for (int j = 0; j != simPublications.getLength(); ++j) {
                            String experId = null;
                            if (pubMedMap.containsKey(simPublications.item(j).getTextContent())) {
                                experId = pubMedMap.get(simPublications.item(j).getTextContent());
                            }
                            if (experId != null && !experId.equals(entry.getValue()))
                                result.add(new ExperimentId(experId, ReceivingType.PUBMED, 2));
                        }
                    }
                }

                String experId = null;
                if (pubMedMap.containsKey(publication)) {
                    experId = pubMedMap.get(publication);
                }

                if (experId != null && !experId.equals(entry.getValue())) {
                    ExperimentId id = new ExperimentId(experId, ReceivingType.OWL);
                    ArrayList<ExperimentId> helper = new ArrayList<ExperimentId>(result); 
                    int index = helper.indexOf(id);
                    if (index != -1) {
                        ExperimentId expId = helper.get(index);
                        expId.setDistance(ReceivingType.PUBMED, 1);
                        result.remove(id);
                        result.add(expId);
                    } else {
                        result.add(new ExperimentId(experId, ReceivingType.PUBMED, 1));
                    }
                }
            }
            //put result to xmlMap
            xmlMap.put(entry.getValue(), result);
        }
    }

    public void interrupt() throws UnableToInterruptJobException {
        Thread.currentThread().interrupt();
    }
}
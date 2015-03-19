package uk.ac.ebi.tuzova.jobs;

import org.quartz.*;
import uk.ac.ebi.tuzova.owl.OntologyDistanceCalculator;
import uk.ac.ebi.tuzova.utils.ExperimentId;
import uk.ac.ebi.tuzova.utils.OntologySimilarityResult;
import uk.ac.ebi.tuzova.utils.ReceivingType;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: catherine
 * Date: Jul 16, 2010
 *
 * goes throw zooma Map, gets similar experiments
 * using distanceCalculator
 */
public class OntologySimilarityJob implements InterruptableJob {
    public OntologySimilarityJob() {}

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger("main.log");

        logger.log(Level.INFO, "Ontology similarityJob executing. ");
        // get data from context
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Map<String, SortedSet<ExperimentId>> zoomaMap = (Map<String, SortedSet<ExperimentId>>)dataMap.get("zoomaMap");
        OntologyDistanceCalculator distanceCalculator = (OntologyDistanceCalculator)dataMap.get("distanceCalculator");
        Map<String, SortedSet<ExperimentId>> xmlMap = (Map<String, SortedSet<ExperimentId>>)dataMap.get("xmlMap");

        // go throw zooma Map
        for (Map.Entry<String, SortedSet<ExperimentId>> e : zoomaMap.entrySet()) {
            
            SortedSet<ExperimentId> expers = e.getValue();
            Set<OntologySimilarityResult> similars = distanceCalculator.getSimilarNodes(e.getKey());
            SortedSet<ExperimentId> resultSim =
                    Collections.synchronizedSortedSet(new TreeSet<ExperimentId>(expers));
            if (null != similars) {
                for (OntologySimilarityResult o : similars) {
                    SortedSet<ExperimentId> similarExp = zoomaMap.get(o.getName());
                    if (null != similarExp) {
                        int distance = o.getDistance();
                        SortedSet<ExperimentId> helper =
                                Collections.synchronizedSortedSet(new TreeSet<ExperimentId>());
                        for (ExperimentId id : similarExp){

                            helper.add(id.setDistance(ReceivingType.OWL, distance));
                        }

                        resultSim.addAll(helper);
                    }
                }
            }
            //put result to xml Map
            for (ExperimentId s : expers) {
                if(xmlMap.containsKey(s.getId())) {
                    SortedSet<ExperimentId> result = xmlMap.get(s.getId());
                    result.addAll(resultSim);
                    xmlMap.put(s.getId(), result);
                } else {
                    xmlMap.put(s.getId(), resultSim);
                }
            }
        }
        zoomaMap.clear();
        logger.log(Level.INFO, "Ontology similarity jos has finished.");
    }

    public void interrupt() throws UnableToInterruptJobException {
        Thread.currentThread().interrupt();
    }
}

package uk.ac.ebi.tuzova.mapping;

import uk.ac.ebi.microarray.zooma.eval.BadOutcomeException;
import uk.ac.ebi.microarray.zooma.eval.OntologyMappingEvaluator;
import uk.ac.ebi.microarray.zooma.eval.OntologyMappingOutcome;
import uk.ac.ebi.microarray.zooma.eval.SuccessfulOutcome;
import uk.ac.ebi.microarray.zooma.formulate.OntologyMappingFormulator;
import uk.ac.ebi.microarray.zooma.hypothesis.OntologyMappingHypothesis;
import uk.ac.ebi.microarray.zooma.hypothesis.Source;
import uk.ac.ebi.microarray.zooma.lang.TextValue;
import uk.ac.ebi.microarray.zooma.lang.Value;
import uk.ac.ebi.microarray.zooma.reports.Reports;
import uk.ac.ebi.microarray.zooma.retrieval.OntologyRetriever;
import uk.ac.ebi.microarray.zooma.track.HypothesisRankingTracker;
import uk.ac.ebi.microarray.zooma.track.HypothesisTracker;
import uk.ac.ebi.microarray.zooma.track.OntologyRanker;
import uk.ac.ebi.microarray.zooma.track.SourceRanker;
import uk.ac.ebi.microarray.zooma.utils.SubsytemFactory;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: tuzova
 * Date: Jul 8, 2010
 *
 * Class to generate zooma mapping report
 * as HashMap where String -- initial value,
 * OntologyMAppingOutcome -- evaluated outcome
 */
public class AEReports extends Reports {

    /**
     *
     * @param zoomaDirectory -- fake
     * See Reports
     */
    public AEReports(File zoomaDirectory) {
        super(zoomaDirectory);
    }

    private static int failedOutcome = 0;
    private static int successOutcome = 0;

    /**
     *
     * @param values -- list of values to generate outcome
     * @param ontologyName using while generating
     * @return HashMap where String -- initial value,
     * OntologyMAppingOutcome -- evaluated outcome 
     */
    public HashMap<String, OntologyMappingOutcome> generateAutomaticMappingsReport(List<String> values,
                                                                                       String ontologyName) {
        HashMap<String, OntologyMappingOutcome> result = new HashMap();

        // create retriever for our custom ontology
        OntologyRetriever retriever = OntologyRetriever.getInstance(ontologyName);

        List<Source> sources =
                getSourcesConfig();
        LinkedHashMap<String, List<String>> ontologies =
                getOntologiesConfig();
        LinkedHashMap<String, List<String>> rejectedOntologies =
                getRejectedOntologiesConfig();

        SourceRanker sr = new SourceRanker(sources);
        OntologyRanker or = new OntologyRanker(ontologies, rejectedOntologies);

        HypothesisTracker tracker = new HypothesisRankingTracker(sr, or);

        // use the factory to generate our subsystems
        OntologyMappingFormulator nullHypothesisFormulator =
                SubsytemFactory
                        .generateNullHypothesisFormulator(tracker);
        OntologyMappingFormulator alternativeHypothesisFormulator =
                SubsytemFactory
                        .generateLocalOntologyOnlyFormulator(
                                tracker,
                                retriever);
        OntologyMappingEvaluator evaluator =
                SubsytemFactory
                        .generateMappingEvaluatorModule(sr, or, tracker);

        // create a separate mapper for this property

        AEOntologyMapper mapper =
                new AEOntologyMapper(
                        alternativeHypothesisFormulator,
                        nullHypothesisFormulator, evaluator);

        // generate an outcome for each property value
        for (String value : values) {
            Value textValue = new TextValue(value);
            try {
                // generate the outcome
                OntologyMappingOutcome outcome = mapper.generateOutcome(textValue);
                if (outcome instanceof SuccessfulOutcome){
                    for (OntologyMappingHypothesis hypothesis :
                        outcome.getValuedHypotheses()) {
                        if (hypothesis.getOntologyTerms().size() != 0){
                            result.put(value, outcome);
                            ++ successOutcome;
                        }
                        else
                            ++failedOutcome;
                    }
                }
                else
                    ++failedOutcome;
            }
            catch (BadOutcomeException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int getFailCount () {
        return failedOutcome;
    }
    public int getSuccessCount () {
        return successOutcome;
    }
}

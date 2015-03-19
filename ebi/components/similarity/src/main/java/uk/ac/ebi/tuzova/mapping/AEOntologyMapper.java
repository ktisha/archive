package uk.ac.ebi.tuzova.mapping;

import uk.ac.ebi.microarray.zooma.eval.*;
import uk.ac.ebi.microarray.zooma.formulate.AbstractFormulator;
import uk.ac.ebi.microarray.zooma.formulate.OntologyMappingFormulator;
import uk.ac.ebi.microarray.zooma.hypothesis.HypothesisFormulationException;
import uk.ac.ebi.microarray.zooma.hypothesis.OntologyMappingHypothesis;
import uk.ac.ebi.microarray.zooma.lang.Value;
import uk.ac.ebi.microarray.zooma.mapping.OntologyMapper;
import uk.ac.ebi.microarray.zooma.retrieval.OntologyTermRetriever;

import java.util.Collection;

/**
 * User: catherine
 * Date: Jul 16, 2010
 *
 * 
 * class for zooma mapping (using in AEReports)
 * using OntologyMappingFormulator and OntologyMappingEvaluator  
 */
public class AEOntologyMapper implements OntologyMapper {
    private OntologyMappingFormulator nullHypothesisFormulator;
    private OntologyMappingFormulator alternativeHypothesisFormulator;

    private OntologyMappingEvaluator evaluator;

    public void setNullHypothesisFormulator(
                            OntologyMappingFormulator nullHypothesisFormulator) {
        this.nullHypothesisFormulator = nullHypothesisFormulator;
    }

    public OntologyMappingFormulator getNullHypothesisFormulator() {
        return nullHypothesisFormulator;
    }

    public void setAlternativeHypothesisFormulator(
                    OntologyMappingFormulator alternativeHypothesisFormulator) {
        this.alternativeHypothesisFormulator = alternativeHypothesisFormulator;
    }

    public OntologyMappingFormulator getAlternativeHypothesisFormulator() {
        return alternativeHypothesisFormulator;
    }

    public void setEvaluator(OntologyMappingEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public OntologyMappingEvaluator getEvaluator() {
        return evaluator;
    }

    /**
    * Add an ontology term retriever to use to generate mapping outcomes.  By
    * default, no retrievers are setup, so outcomes will not map to anything. Add
    * more retrievers to fetch terms from more resources.
    *
    * @param retriever the retriever to add to fetch terms from
    */
    public void addRetriever(OntologyTermRetriever retriever) {
        if (getAlternativeHypothesisFormulator() instanceof AbstractFormulator) {
            // we can add new retriever to the template
            AbstractFormulator formulator =
            (AbstractFormulator) getAlternativeHypothesisFormulator();
            formulator.addRetriever(retriever);
        } else {
        // some other type, we can't add retriever
            throw new UnsupportedOperationException(
                "This mapper is configured with an immutable formulator - " +
                "new retrievers cannot be added");
        }
    }

    public OntologyMappingOutcome generateOutcome(Value value)
                                                throws BadOutcomeException {
        try {
            // get null hypotheses
            getNullHypothesisFormulator().formulateHypotheses(value);
            // use the supplied description to retrieve possible hypotheses
            Collection<OntologyMappingHypothesis> alternativeHypotheses =
              getAlternativeHypothesisFormulator().formulateHypotheses(value);

            // generate the outcome from the evaluator

            return getEvaluator().evaluateOutcome(value, alternativeHypotheses);
        }
        catch (HypothesisFormulationException e) {
            throw new BadOutcomeException(e);
        }
    }

    public void applyOutcome(OntologyMappingOutcome outcome)
                                                   throws BadOutcomeException {
        outcome.apply();
    }
}

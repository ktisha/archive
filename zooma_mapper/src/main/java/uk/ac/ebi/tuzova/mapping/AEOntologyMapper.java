package uk.ac.ebi.tuzova.mapping;

import uk.ac.ebi.microarray.zooma.eval.BadOutcomeException;
import uk.ac.ebi.microarray.zooma.eval.FailedOutcome;
import uk.ac.ebi.microarray.zooma.eval.OntologyMappingEvaluator;
import uk.ac.ebi.microarray.zooma.eval.OntologyMappingOutcome;
import uk.ac.ebi.microarray.zooma.formulate.OntologyMappingFormulator;
import uk.ac.ebi.microarray.zooma.hypothesis.HypothesisFormulationException;
import uk.ac.ebi.microarray.zooma.hypothesis.OntologyMappingHypothesis;
import uk.ac.ebi.microarray.zooma.lang.Value;
import uk.ac.ebi.microarray.zooma.mapping.OntologyMapper;

import java.util.Collection;

/**
 * User: tuzova
 * Date: Jul 7, 2010
 *
 * Class to generate Ontology Mapping outcome
 * using zooma OntologyMappingFormulator and
 * OntologyMappingEvaluator. We need it, 'cause there are
 * only one way to generate it now exists in zooma, it's
 * TextReportingMapper. TextReportingMapper provides
 * acces to outcome only by using files. AEOntologyMapper
 * provides access to outcome directly.
 */
public class AEOntologyMapper implements OntologyMapper{

    private final OntologyMappingFormulator alternativeHypothesisFormulator;
    private final OntologyMappingFormulator nullHypothesisFormulator;
    private final OntologyMappingEvaluator evaluator;

    public AEOntologyMapper(OntologyMappingFormulator alternativeHypothesisFormulator,
                            OntologyMappingFormulator nullHypothesisFormulator,
                            OntologyMappingEvaluator evaluator) {
        this.alternativeHypothesisFormulator = alternativeHypothesisFormulator;
        this.evaluator = evaluator;
        this.nullHypothesisFormulator = nullHypothesisFormulator;
    }

    /**
     *
     * @param value to generate mapping for.
     * @return evaluated outcome
     * @throws BadOutcomeException inherited from OntologyMapper
     */
    public OntologyMappingOutcome generateOutcome(Value value) throws BadOutcomeException {
        try {
            Collection<OntologyMappingHypothesis> nullHypotheses =
                nullHypothesisFormulator.formulateHypotheses(value);
            // use the supplied description to retrieve possible hypotheses
            Collection<OntologyMappingHypothesis> alternativeHypotheses =
                    alternativeHypothesisFormulator.formulateHypotheses(value);

            return evaluator.evaluateOutcome(alternativeHypotheses);
        } catch (HypothesisFormulationException e) {
            return new FailedOutcome(value);
        }
    }

    // ignore this function
    // text reporting mapper writes outcome to file
    public void applyOutcome(OntologyMappingOutcome outcome) throws BadOutcomeException {
    }
}

package uk.ac.ebi.tuzova.mapping;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.microarray.zooma.eval.*;
import uk.ac.ebi.microarray.zooma.hypothesis.OntologyMappingHypothesis;
import uk.ac.ebi.microarray.zooma.lang.TextValue;
import uk.ac.ebi.microarray.zooma.lang.Value;
import uk.ac.ebi.microarray.zooma.retrieval.OntologyRetriever;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tuzova
 * Date: Jul 8, 2010
 *
 * Class to generate zooma mapping report
 * as HashMap where String -- initial value,
 * OntologyMAppingOutcome -- evaluated outcome
 */
public class AEReports {
    private static final Logger ourLogger = Logger.getLogger("main.log");
    /**
     *
     * See Reports
     */
    AEOntologyMapper mapper;
    public AEReports() {
        // initialize by reading spring configuration
        // load spring config
        try {
          BeanFactory factory =
              new ClassPathXmlApplicationContext("zooma-text.xml");
          mapper = (AEOntologyMapper) factory.getBean("textMapper", AEOntologyMapper.class);
        }
        catch (BeanNotOfRequiredTypeException e) {
          throw new RuntimeException("Bad configuration file: 'textMapper' bean " +
              "should be a TextReportingMapper - check zooma-text.xml is correct",
                                     e);
        }
        ourLogger.log(Level.INFO, "AEReport created. ");
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
        HashMap<String, OntologyMappingOutcome> result =
                                new HashMap<String, OntologyMappingOutcome>();

        // create retriever for our custom ontology

        OntologyRetriever retriever = OntologyRetriever.getInstance(ontologyName);
        mapper.addRetriever(retriever);

        // generate an outcome for each property value
        for (String value : values) {
            Value textValue = new TextValue(value);
            try {
                // generate the outcome
                OntologyMappingOutcome outcome = mapper.generateOutcome(textValue);
                if (outcome.getBestHypotheses().size() != 0) {
                    for (OntologyMappingHypothesis hypothesis :
                        outcome.getBestHypotheses()) {
                        if (hypothesis.getOntologyTerms().size() != 0){
                            result.put(value, outcome);
                            ++ successOutcome;
                        }
                        else
                            ++failedOutcome;
                    }
                } else
                    ++failedOutcome;
            }
            catch (BadOutcomeException e) {
                ourLogger.log(Level.SEVERE, "Got Bad outcome. " + e.getMessage());
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

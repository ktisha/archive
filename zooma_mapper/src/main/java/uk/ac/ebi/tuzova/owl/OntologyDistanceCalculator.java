package uk.ac.ebi.tuzova.owl;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
//import uk.ac.ebi.tuzova.utils.Query;
import uk.ac.ebi.tuzova.utils.SimilarityResult;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tuzova
 * Date: Jul 7, 2010
 *
 * Class to calculate distances between nodes in ontology
 */
public class OntologyDistanceCalculator {
    private static final Logger ourLogger = Logger.getLogger("main.log");

    private OWLOntology myOntology;
    private OWLOntologyManager myOntologyManager;

    // Map contains result of calculation
    private Map<String, TreeSet<SimilarityResult>> myQueryMap = new HashMap();
    //private Map<Query, Integer> myQueryMap = new HashMap();

    /**
     *
     * @param ontologyURL the path to ontology
     */
    public OntologyDistanceCalculator(String ontologyURL) {
        loadOntology(ontologyURL);
        calculateDistances();
    }

    /**
     *
     * @param ontologyURL the path to ontology( in file system or in web)
     */
    private void loadOntology (String ontologyURL) {

        myOntologyManager = OWLManager.createOWLOntologyManager();
        File file = new File(ontologyURL);      // from file

        //from web
        //IRI iri = IRI.create(ontologyURL");

        try {
            myOntology = myOntologyManager.loadOntologyFromOntologyDocument(file);
        } catch ( OWLOntologyCreationException e) {
            ourLogger.log(Level.SEVERE, "Cannot load ontology from " +
                    ontologyURL + " " + e.getMessage());
        }

    }

    /**
     * calculates distances between nodes in ontology
     * using Floyd-Warshall algorithm
     */
    private void calculateDistances() {
        // to exclude sex features from distances
        String sex = "<http://www.ebi.ac.uk/efo/EFO_0000695>";
        OWLClass sexClass = null;

        // get all classes from ontology
        Set<OWLClass> ontologyClasses = myOntology.getClassesInSignature();
        
        //  create OWLClass to number reflection
        ArrayList<OWLClass> owlClassReflection = new ArrayList();
        for ( OWLClass cl : ontologyClasses) {
            if (cl.toString().equals(sex))
                sexClass = cl;
            owlClassReflection.add(cl);
        }

        //create and initialize distance Matrix for OWLClasses
        int arrSize = ontologyClasses.size();
        int [][] distanceMatrix = new int[arrSize][arrSize];
        for (int i = 0; i != arrSize; ++i) {
            for (int j = 0; j != arrSize; ++j) {
                distanceMatrix[i][j] = Integer.MAX_VALUE;
            }
        }

        // initialize distanceMatrix
        for ( OWLClass cl : ontologyClasses) {
            int clIndex = owlClassReflection.indexOf(cl);
            for (Object subClass : cl.getSubClasses(myOntology)) {
                int subClassIndex = owlClassReflection.indexOf(subClass);
                distanceMatrix[subClassIndex][clIndex] = 1;
                distanceMatrix[clIndex][subClassIndex] = 1;
            }
        }

        // set infinity for distances between sex and all its subclasses
        int sexIndex = owlClassReflection.indexOf(sexClass);
        for (Object subClass : sexClass.getSubClasses(myOntology)) {
                int subClassIndex = owlClassReflection.indexOf(subClass);
                distanceMatrix[subClassIndex][sexIndex] = Integer.MAX_VALUE;
                distanceMatrix[sexIndex][subClassIndex] = Integer.MAX_VALUE;
        }

        // Floyd-Warshall algorithm
        for (int k = 0; k != arrSize; ++k) {
            for (int i = 0; i != arrSize; ++i) {
                for (int j = 0; j != arrSize; ++j) {
                    if (distanceMatrix[i][k] != Integer.MAX_VALUE
                                && distanceMatrix[k][j] != Integer.MAX_VALUE) {
                        distanceMatrix[i][j] =
                                Math.min(distanceMatrix[i][j],
                                         distanceMatrix[i][k] + distanceMatrix[k][j]);
                    }
                }
            }
        }

        // translate distances to query map
        for (int i = 0; i != arrSize; ++i) {
            TreeSet<SimilarityResult> set = new TreeSet<SimilarityResult>();
            myQueryMap.put( owlClassReflection.get(i).toString(), set);
            for (int j = 0; j != arrSize; ++j) {
                //if (distanceMatrix[i][j] != Integer.MAX_VALUE)    too many objects
                if (distanceMatrix[i][j] < 4)    
                    set.add( new SimilarityResult(owlClassReflection.get(j).toString(), distanceMatrix[i][j]));
            }
        }

        /*
        // translate distances to query map
        for (int i = 0; i != arrSize; ++i) {
            for (int j = 0; j != arrSize; ++j) {
                if (distanceMatrix[i][j] != Integer.MAX_VALUE)
                    myQueryMap.put( new Query(owlClassReflection.get(i).toString(),
                            owlClassReflection.get(j).toString()), distanceMatrix[i][j]);
            }
        }
        */
    }
    /*
    public int getDistance(String first, String second) {
        return myQueryMap.get( new Query(first, second));
    }
    */

    public String getSimilarNode(String cl) {
        return myQueryMap.get(cl).first().getName();
    }

    /**
     *
     * @param cl the node name in ontology like "<http://www.ebi.ac.uk/efo/$NAME$>"
     * @return set of nodes, connected to cl in our result map
     */
    public Set getSimilarNodes(String cl) {
        return myQueryMap.get(cl);
    }

    /**
     * removes ontology
     */
    @Override
    protected void finalize() {
        try {
            super.finalize();
        } catch (Throwable throwable) {
            ourLogger.log(Level.INFO, "Got exception in OntologyDistanceCalculator " +
                    "finalize() " + throwable.getMessage());
        }
        myOntologyManager.removeOntology(myOntology);
    }
    
}

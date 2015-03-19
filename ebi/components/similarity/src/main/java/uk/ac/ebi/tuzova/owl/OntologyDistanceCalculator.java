package uk.ac.ebi.tuzova.owl;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import uk.ac.ebi.microarray.ontology.OntologyLoader;
import uk.ac.ebi.microarray.ontology.efo.EFOClassAnnotationVisitor;
import uk.ac.ebi.microarray.ontology.efo.EFONode;
import uk.ac.ebi.microarray.ontology.efo.EFOPartOfPropertyVisitor;
import uk.ac.ebi.tuzova.utils.OntologySimilarityResult;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    private final int myMaxDistance;

    // Map contains result of calculation
    private Map<String, SortedSet<OntologySimilarityResult>> myQueryMap =
                    new ConcurrentHashMap<String, SortedSet<OntologySimilarityResult>>();

    /**
     *
     * @param ontologyURL path to ontology
     * @param maxDistance max distance between nodes in ontology to store
     */
    public OntologyDistanceCalculator(String ontologyURL, int maxDistance) {
        ourLogger.log(Level.INFO, "Starting Ontology Distance calculator.");
        this.myMaxDistance = maxDistance;

        loadOntology(ontologyURL);
        try {
            InputStream ontologyStream = new URL(ontologyURL).openStream();
            calculateDistances(ontologyStream);
        } catch (FileNotFoundException e) {
            ourLogger.log(Level.SEVERE, "Cannot find file. " + e.getMessage());
        } catch (MalformedURLException e) {
            ourLogger.log(Level.SEVERE, "Bad URL. " + e.getMessage());
        } catch (IOException e) {
            ourLogger.log(Level.SEVERE, "Cannot get input stream. " + e.getMessage());
        }
    }

    /**
     *
     * @param ontologyURL the path to ontology( in file system or in web)
     */
    private void loadOntology (String ontologyURL) {

        myOntologyManager = OWLManager.createOWLOntologyManager();

        //from web
        IRI iri = IRI.create(ontologyURL);

        try {
            myOntology = myOntologyManager.loadOntology(iri);
        } catch ( OWLOntologyCreationException e) {
            ourLogger.log(Level.SEVERE, "Cannot load ontology from " +
                    ontologyURL + " " + e.getMessage());
        }

    }

    /**
     * calculates distances between nodes in ontology
     * using Floyd-Warshall algorithm
     * @param ontologyStream -- stream to get ontology
     */
    public void calculateDistances(InputStream ontologyStream) {
        ourLogger.log(Level.INFO, "Load ontology. ");
        Map<String, EFONode> efoMap;
        Map<String, Set<String>> partOfIdMap = new HashMap<String, Set<String>>();

        OntologyLoader<EFONode> loader = new OntologyLoader<EFONode>(ontologyStream);
        efoMap = loader.load(
                new EFOClassAnnotationVisitor()
                , new EFOPartOfPropertyVisitor(partOfIdMap)
        );
        ourLogger.log(Level.INFO, "Calculating distances");
        // to exclude sex features from distances
        final String sex = "http://www.ebi.ac.uk/efo/EFO_0000695";
        final String dose = "http://www.ebi.ac.uk/efo/EFO_0000428";
        final String unit = "http://purl.org/obo/owl/UO#UO:0000000";
        // special case
        final String cancer = "http://www.ebi.ac.uk/efo/EFO_0000311";

        OWLClass sexClass = null;
        OWLClass unitClass = null;
        OWLClass doseClass = null;
        OWLClass cancerClass = null;
        // get all classes from ontology
        Set<OWLClass> ontologyClasses = myOntology.getClassesInSignature();

        //  create OWLClass IRI to number reflection
        ArrayList<String> owlClassReflection = new ArrayList<String>();
        for ( OWLClass cl : ontologyClasses) {
            if (cl.getIRI().toString().equals(sex))
                sexClass = cl;
            else if(cl.getIRI().toString().equals(dose))
                doseClass = cl;
            else if(cl.getIRI().toString().equals(unit))
                unitClass = cl;
            else if(cl.getIRI().toString().equals(cancer))
                cancerClass = cl;
            owlClassReflection.add(cl.getIRI().toString());
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
        initializeMatrix(distanceMatrix, owlClassReflection, efoMap, partOfIdMap);

        // set min distance for all cancer children
        initializeCancer(distanceMatrix, owlClassReflection, cancerClass);

        // set infinity for sex, unit, dose distance
        SortedSet <OWLClass> toExclude = Collections.synchronizedSortedSet(new TreeSet<OWLClass>());
        if (doseClass != null)
            toExclude.add(doseClass);
        if (sexClass != null)
            toExclude.add(sexClass);
        if (unitClass != null)
            toExclude.add(unitClass);
        exclude(distanceMatrix, toExclude, owlClassReflection);

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

        // set infinity for path to itself
        for (int i = 0; i != arrSize; ++i) {
            distanceMatrix[i][i] = Integer.MAX_VALUE;
        }

        // translate distances to query map
        for (int i = 0; i != arrSize; ++i) {
            SortedSet<OntologySimilarityResult> set = 
                    Collections.synchronizedSortedSet(new TreeSet<OntologySimilarityResult>());
            myQueryMap.put( owlClassReflection.get(i), set);
            for (int j = 0; j != arrSize; ++j) {
                //if (distanceMatrix[i][j] != Integer.MAX_VALUE) // because of too many objects
                if (distanceMatrix[i][j] < myMaxDistance)    
                    set.add( new OntologySimilarityResult(owlClassReflection.get(j),
                                                            distanceMatrix[i][j]));
            }
        }
        ourLogger.log(Level.INFO, "Distances has been calculated.");
        /*
        try {
            FileWriter reportStream = new FileWriter("data/reports/similarity");
            BufferedWriter reportWriter = new BufferedWriter(reportStream);


            for (Map.Entry e: myQueryMap.entrySet()) {
                reportWriter.write(e.getKey() + "\n");
                for (Object o : (Set)e.getValue()) {
                    reportWriter.write( ((OntologySimilarityResult)o).toString());
                }
                reportWriter.write("\n\n");
            }
            reportWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        */
                
    }

    private void initializeCancer(int[][] distanceMatrix,
                    ArrayList<String> owlClassReflection, OWLClass cancerClass) {
        // set min distance for all cancer children
        int cancerIndex;
        if (null != cancerClass) {
            cancerIndex = owlClassReflection.indexOf(cancerClass.getIRI().toString());
            for (OWLClassExpression subClass : cancerClass.getSubClasses(myOntology)) {
                cancerLoop(subClass.asOWLClass().getSubClasses(myOntology),
                            cancerIndex, owlClassReflection, distanceMatrix);
            }
        }
    }

    private void cancerLoop(Set<OWLClassExpression> subClasses, int cancerIndex,
                  ArrayList<String> owlClassReflection, int[][] distanceMatrix) {
        if (subClasses != null && !subClasses.isEmpty()) {
            for (OWLClassExpression subClass : subClasses) {
                cancerLoop(subClass.asOWLClass().getSubClasses(myOntology),
                                cancerIndex, owlClassReflection, distanceMatrix);
                int subClassIndex = owlClassReflection.indexOf(subClass.asOWLClass().getIRI().toString());
                distanceMatrix[subClassIndex][cancerIndex] = 0;
                distanceMatrix[cancerIndex][subClassIndex] = 0;
            }
        }
    }

    private void exclude(int[][] distanceMatrix, Set<OWLClass> toExclude,
                                        ArrayList<String> owlClassReflection) {
        for (OWLClass clazz : toExclude) {
            // set infinity for sex, unit, dose distance
            int excludeIndex;
            if (null != clazz) {
                excludeIndex = owlClassReflection.indexOf(clazz.getIRI().toString());
                for (OWLClassExpression subClass : clazz.getSubClasses(myOntology)) {
                        int subClassIndex =
                                owlClassReflection.indexOf(subClass.asOWLClass().getIRI().toString());
                        distanceMatrix[subClassIndex][excludeIndex] = Integer.MAX_VALUE;
                        distanceMatrix[excludeIndex][subClassIndex] = Integer.MAX_VALUE;
                }
            }
        }
    }

    private void initializeMatrix(int[][] distanceMatrix, ArrayList<String> owlClassReflection,
                                  Map<String, EFONode> efoMap, Map<String, Set<String>> partOfIdMap) {
        // initialize distanceMatrix
        for (Map.Entry<String, EFONode> e : efoMap.entrySet()) {
            int clIndex = owlClassReflection.indexOf(e.getKey());
            if (e.getValue().hasChildren()) {
                for (EFONode node : e.getValue().getChildren()) {
                    int subClassIndex = owlClassReflection.indexOf(node.getId());
                    if (subClassIndex == -1) {
                        ourLogger.log(Level.INFO, "Cannot find node in owlClass reflection "
                                            + node.getId());
                        continue;
                    }
                    distanceMatrix[subClassIndex][clIndex] = 1;
                    distanceMatrix[clIndex][subClassIndex] = 1;
                }
            }
        }

        for (Map.Entry<String, Set<String>> e : partOfIdMap.entrySet()) {
            int clIndex = owlClassReflection.indexOf(e.getKey());
            for (String node : e.getValue()) {
                int subClassIndex = owlClassReflection.indexOf(node);
                if (subClassIndex == -1) {
                    ourLogger.log(Level.INFO, "Cannot find node in owlClass reflection "
                                        + node);
                    continue;
                }
                distanceMatrix[subClassIndex][clIndex] = 1;
                distanceMatrix[clIndex][subClassIndex] = 1;
            }
        }
    }

    /**
     *
     * @param cl the node name in ontology like "http://www.ebi.ac.uk/efo/$NAME$"
     * @return set of nodes, connected to cl in our result map
     */
    public Set<OntologySimilarityResult> getSimilarNodes(String cl) {
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

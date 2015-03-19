package uk.ac.ebi.tuzova.jobs;

import org.quartz.*;
import uk.ac.ebi.tuzova.utils.ExperimentId;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: catherine
 * Date: Jul 15, 2010
 *
 * Job to create temp xml file from xmlMap
 */
public class XmlWriterJob implements InterruptableJob {
    public XmlWriterJob() {}

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger("main.log");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Map<String, SortedSet<ExperimentId>> xmlMap = (Map<String, SortedSet<ExperimentId>>)dataMap.get("xmlMap");
        Properties properties = (Properties) dataMap.get("properties");
        final String tmpDirName = "tmp";
        final String fileName = tmpDirName + "/" + properties.getProperty("report_file");
        final int maxSimilarity = Integer.parseInt(properties.getProperty("max_similarity"));
        logger.log(Level.INFO, "There are " + xmlMap.size() + " experiments to write.");

        // create tmp directory
        File dir = new File(tmpDirName);
        if (!dir.exists()) {
            boolean success = dir.mkdir();
            if (!success) {
              logger.log(Level.SEVERE, "Cannot create tmp directory. ");
            }
        }

        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter;
        try {
            xmlWriter = outputFactory.createXMLStreamWriter(new FileOutputStream(fileName));
            xmlWriter.writeStartDocument();
            xmlWriter.writeStartElement("similarity");

            for (Map.Entry<String, SortedSet<ExperimentId>> e : xmlMap.entrySet()) {
                if (e.getValue() != null && !e.getValue().isEmpty()) {
                    xmlWriter.writeStartElement("experiment");
                    xmlWriter.writeStartElement("accession");
                    xmlWriter.writeCharacters(e.getKey());
                    xmlWriter.writeEndElement();

                    xmlWriter.writeStartElement("similarExperiments");
                    
                    Object[] array =  e.getValue().toArray();
                    ExperimentId[] subArray = new ExperimentId[maxSimilarity];
                    if (array.length >= maxSimilarity)
                        System.arraycopy(array, 0, subArray, 0, maxSimilarity);
                    else
                        System.arraycopy(array, 0, subArray, 0, array.length);
                    
                    for (ExperimentId id : subArray) {
                        if ((id != null) && !id.getId().equals(e.getKey())) {
                            xmlWriter.writeStartElement("similarExperiment");
                            xmlWriter.writeStartElement("accession");
                            xmlWriter.writeCharacters(id.getId());
                            xmlWriter.writeEndElement();

                            if (id.getOWLDistance() != Integer.MAX_VALUE) {
                                xmlWriter.writeStartElement("receivedFrom");
                                xmlWriter.writeAttribute("distance", Integer.toString(id.getOWLDistance()));
                                xmlWriter.writeCharacters("OWL");
                                xmlWriter.writeEndElement();
                            }
                            if (id.getPubMedDistance() != Integer.MAX_VALUE) {
                                xmlWriter.writeStartElement("receivedFrom");
                                xmlWriter.writeAttribute("distance", Integer.toString(id.getPubMedDistance()));
                                xmlWriter.writeCharacters("PUBMED");
                                xmlWriter.writeEndElement();
                            }

                            xmlWriter.writeEndElement();
                        }
                    }
                    xmlWriter.writeEndElement();
                    xmlWriter.writeEndElement();
                }
            }
            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.close();
        } catch (XMLStreamException e) {
            logger.log(Level.SEVERE, "Cannot get xml Stream writer. " + e.getMessage());
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Cannot find file. " + e.getMessage());
        }

        logger.log(Level.INFO, "Xml has been written. ");
    }

    public void interrupt() throws UnableToInterruptJobException {
        Thread.currentThread().interrupt();
    }
}

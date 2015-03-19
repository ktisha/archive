package uk.ac.ebi.tuzova.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tuzova
 * Date: Jun 30, 2010
 * Time: 9:05:26 AM
 *
 * Parse xml file with experiments and extracts sampleattribute and experiments tags
 */
public class XPathParser {
    private String myXmlFile = null;

    public XPathParser( String xmlFile) {
        myXmlFile = xmlFile;
    }

    /**
     *
     * @return NodeList contains nodes with path as pattern
     */
    public NodeList extract( String pattern ){
        Logger logger = Logger.getLogger("main.log");

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);

        NodeList result = null;

        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(myXmlFile.getBytes()));

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr =
                        xpath.compile(pattern);

            result = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);

        } catch (ParserConfigurationException e) {
            logger.log(Level.SEVERE,
                        "Cannot get new document builder. " + e.getMessage());
        } catch (SAXException e) {
            logger.log(Level.SEVERE, "Cannot parse xml document. "
                                                            + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot find/open xml document. "
                                                            + e.getMessage());
        } catch (XPathExpressionException e) {
            logger.log(Level.SEVERE, "Cannot compile xPath expression. "
                                                            + e.getMessage());
        }
        return result;
    }
    
    /*
    deprecated
     */
    public NodeList extractExperiments(){
        Logger logger = Logger.getLogger("main.log");

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);

        NodeList result = null;

        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(myXmlFile);

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr =
                        xpath.compile("//experiment");

            result = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
        } catch (ParserConfigurationException e) {
            logger.log(Level.SEVERE,
                        "Cannot get new document builder. " + e.getMessage());
        } catch (SAXException e) {
            logger.log(Level.SEVERE, "Cannot parse xml document. "
                                                            + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot find/open xml document. "
                                                            + e.getMessage());
        } catch (XPathExpressionException e) {
            logger.log(Level.SEVERE, "Cannot compile xPath expression. "
                                                            + e.getMessage());
        }
        return result;
    }

}

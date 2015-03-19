package uk.ac.ebi.tuzova.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tuzova
 * Date: Jun 30, 2010
 * Time: 9:05:26 AM
 *
 * Parse xml file and extracts NodeList using xpath technique
 *
 */
public class XPathParser {

    public XPathParser() {}


    /**
     * extract from File contains xml
     * @param xmlFile path to xml file
     * @param pattern to find in file
     * @return NodeList contains nodes with path as pattern
     */
    public NodeList extractFromFile( String xmlFile, String pattern ){
        Logger logger = Logger.getLogger("main.log");

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);

        NodeList result = null;

        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

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

    /**
     * extract from String contains xml
     * @param xmlString xml as a string
     * @param pattern to find in string
     * @return NodeList contains nodes with path as pattern
     */
    public NodeList extractFromString(String xmlString, String pattern ){
        Logger logger = Logger.getLogger("main.log");

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);

        NodeList result = null;

        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));

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
   
}

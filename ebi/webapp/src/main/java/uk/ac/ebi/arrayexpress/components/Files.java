package uk.ac.ebi.arrayexpress.components;

/*
 * Copyright 2009-2010 European Molecular Biology Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.xpath.XPathEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.arrayexpress.app.ApplicationComponent;
import uk.ac.ebi.arrayexpress.utils.DocumentTypes;
import uk.ac.ebi.arrayexpress.utils.RegexHelper;
import uk.ac.ebi.arrayexpress.utils.saxon.ExtFunctions;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.util.List;

public class Files extends ApplicationComponent
{
    // logging machinery
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String rootFolder;
    private SaxonEngine saxon;

    private DocumentContainer documentContainer;

    public Files()
    {
        super("Files");
    }

    public void initialize() throws Exception
    {
        saxon = (SaxonEngine)getComponent("SaxonEngine");
        documentContainer = (DocumentContainer) getComponent("DocumentContainer");

        updateAccelerators();
    }

    public void terminate() throws Exception
    {
        saxon = null;
    }

    private synchronized void setFiles( DocumentInfo doc ) throws Exception
    {
        if (null != doc) {
            documentContainer.putDocument(DocumentTypes.FILES, doc);
            updateAccelerators();
        } else {
            this.logger.error("Files NOT updated, NULL document passed");
        }
    }

    public void reload( String xmlString ) throws Exception
    {
        DocumentInfo doc = loadFilesFromString(xmlString);
        if (null != doc) {
            setFiles(doc);
        }
    }

    private DocumentInfo loadFilesFromString( String xmlString ) throws Exception
    {
        return saxon.transform(xmlString, "preprocess-files-xml.xsl", null);
    }

    private void updateAccelerators()
    {
        logger.debug("Will re-created accelerators for files");

        ExtFunctions.clearAccelerator("raw-files");
        ExtFunctions.clearAccelerator("fgem-files");

        try {
            XPath xp = new XPathEvaluator(documentContainer.getDocument(DocumentTypes.FILES).getConfiguration());
            XPathExpression xpe = xp.compile("/files/folder[@kind = 'experiment']");
            List documentNodes = (List)xpe.evaluate(documentContainer.getDocument(DocumentTypes.FILES), XPathConstants.NODESET);

            XPathExpression accessionXpe = xp.compile("@accession");
            XPathExpression rawFilePresentXpe = xp.compile("count(file[@kind = 'raw'])");
            XPathExpression fgemFilePresentXpe = xp.compile("count(file[@kind = 'fgem'])");
            for (Object node : documentNodes) {

                try {
                    // get all the expressions taken care of
                    String accession = (String)accessionXpe.evaluate(node);
                    ExtFunctions.addAcceleratorValue("raw-files", accession, rawFilePresentXpe.evaluate(node));
                    ExtFunctions.addAcceleratorValue("fgem-files", accession,  fgemFilePresentXpe.evaluate(node));
                } catch (XPathExpressionException x) {
                    logger.error("Caught an exception:", x);
                }
            }
            logger.debug("Accelerators updated");
        } catch (Exception x) {
            logger.error("Caught an exception:", x);
        }
    }

    public synchronized void setRootFolder( String folder )
    {
        if (null != folder && 0 < folder.length()) {
            if (folder.endsWith(File.separator)) {
                rootFolder = folder;
            } else {
                rootFolder = folder + File.separator;
            }
        } else {
            logger.error("setRootFolder called with null or empty parameter, expect problems down the road");
        }
    }

    public String getRootFolder()
    {
        if (null == rootFolder) {
            rootFolder = getPreferences().getString("ae.files.root.location");
        }
        return rootFolder;
    }

    // returns true is file is registered in the registry
    public boolean doesExist( String accession, String name ) throws Exception
    {
        if (null != accession && accession.length() > 0) {
            return Boolean.parseBoolean(
                    saxon.evaluateXPathSingle(
                            documentContainer.getDocument(DocumentTypes.FILES)
                            , "exists(//folder[@accession = '" + accession + "']/file[@name = '" + name + "'])"
                    )
            );
        } else {
            return Boolean.parseBoolean(
                    saxon.evaluateXPathSingle(
                            documentContainer.getDocument(DocumentTypes.FILES)
                            , "exists(//file[@name = '" + name + "'])"
                    )
            );
        }
    }

    // returns absolute file location (if file exists, null otherwise) in local filesystem
    public String getLocation( String accession, String name ) throws Exception
    {
        String folderLocation;

        if (null != accession && accession.length() > 0) {
            folderLocation = saxon.evaluateXPathSingle(
                    documentContainer.getDocument(DocumentTypes.FILES)
                    , "//folder[@accession = '" + accession + "' and file/@name = '" + name + "']/@location"
            );
        } else {
            folderLocation = saxon.evaluateXPathSingle(
                    documentContainer.getDocument(DocumentTypes.FILES)
                    , "//folder[file/@name = '" + name + "']/@location"
            );
        }

        if (null != folderLocation && folderLocation.length() > 0) {
            return folderLocation + File.separator + name;
        } else {
            return null;
        }
    }

    public String getAccession( String fileLocation ) throws Exception
    {
        String[] nameFolder = new RegexHelper("^(.+)/([^/]+)$", "i")
                .match(fileLocation);
        if (null == nameFolder || 2 != nameFolder.length) {
            logger.error("Unable to parse the location [{}]", fileLocation);
            return null;
        }

        return saxon.evaluateXPathSingle(
                documentContainer.getDocument(DocumentTypes.FILES)
                , "//folder[file/@name = '" + nameFolder[1] + "' and @location = '" + nameFolder[0] + "']/@accession"
            );
    }
}

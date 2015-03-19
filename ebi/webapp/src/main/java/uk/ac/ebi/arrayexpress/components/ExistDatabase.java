package uk.ac.ebi.arrayexpress.components;

import org.exist.EXistException;
import org.exist.storage.BrokerPool;
import org.exist.util.Configuration;
import org.exist.util.DatabaseConfigurationException;
import org.exist.xmldb.XQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import uk.ac.ebi.arrayexpress.app.ApplicationComponent;

import javax.xml.transform.OutputKeys;
import java.io.File;


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

public class
        ExistDatabase extends ApplicationComponent
{
    // logging machinery
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Configuration configuration = null;

    public final String AE_COLLECTION = "/ae";

    // database uri
    // TODO: move to properties
    private final String myDatabaseURI = "xmldb:exist://";

    public ExistDatabase()
    {
        super("ExistDatabase");
    }

    public void initialize() throws Exception
    {
        if (BrokerPool.isConfigured()) {
            logger.warn("eXist database already started");
        } else {

            try {
                String dbHome = getPreferences().getString("ae.experiments.exist.dbhome");

                // here we need to check this directory exists and create a tree if necessary
                if (!new File(dbHome).exists()) {
                    if (!new File(dbHome + File.separator + "data").mkdirs()) {
                        logger.error("System was unable to create necessary directories for eXist starting with [{}]", dbHome);
                    }
                }
                String confFile = "exist-conf.xml";
                this.configuration = new Configuration(confFile, dbHome);
                startup();
            } catch (DatabaseConfigurationException dce) {
                throw new RuntimeException("Error in database configuration: " + dce.getMessage());
            }
        }
    }

    public void terminate() throws Exception
    {
        BrokerPool.stopAll(false);
    }


    private void startup() throws Exception
    {
        if (configuration == null)
            throw new RuntimeException("Database has not been configured");

        logger.debug("Configuring eXist instance");
        try {
            if (!BrokerPool.isConfigured())
                BrokerPool.configure(1, 5, configuration);
        } catch (EXistException e) {
            throw new RuntimeException(e.getMessage());
        } catch (DatabaseConfigurationException e) {
            throw new RuntimeException(e.getMessage());
        }
        logger.debug("Registering XMLDB driver");
        Class clazz = Class.forName("org.exist.xmldb.DatabaseImpl");
        Database database = (Database) clazz.newInstance();
        database.setProperty("auto-create", "true");
        DatabaseManager.registerDatabase(database);
    }

    /**
     *
     * @param collectionName -- name of collection to retrieve content
     * @param xpath -- xpath as string
     * @return -- org.xmldb.api.base.ResourceSet -- set of Resource.
     * Iterate ResourceSet using ResourceIterator.
     * @throws XMLDBException -- if cannot get Collection with collectionName
     * or can't evaluate xPath
     */
    public ResourceSet evaluateXPath(String collectionName, String xpath)
                                                        throws XMLDBException {
        Collection collection =
                DatabaseManager.getCollection(myDatabaseURI + collectionName);
        XQueryService service =
            (XQueryService) collection.getService("XQueryService", "1.0");
        service.setProperty("indent", "yes");

        CompiledExpression compiled = service.compile(xpath);
        return service.execute(compiled);
    }

    /**
     *
     * @param collectionName -- name of collection to store content
     * @param content -- the content value to set for the resource.
     * It can be whether file or String. 
     * @param id == null is automatic id
     * @throws XMLDBException -- if cannot get Collection with collectionName
     * or cannot store document
     */
    public void storeDocument(String collectionName, Object content, String id)
                                                        throws XMLDBException {
        // try to get collection with name collectionName
        //TODO: get admin & password from properties
        Collection collection =
                   DatabaseManager.getCollection(myDatabaseURI
                                            + collectionName, "admin", "");
        if(null == collection) {
           // collectionName does not exist: get root collectionName and create
           Collection root = DatabaseManager.getCollection(myDatabaseURI + "/db",
                                                                    "admin", "");
           CollectionManagementService mgtService =
                    (CollectionManagementService)root.getService("CollectionManagementService", "1.0");
           collection = mgtService.createCollection(collectionName);
        }

        // create new XMLResource
        if (null != collection) {
            XMLResource document = (XMLResource)collection.createResource(id, "XMLResource");

            document.setContent(content);
            collection.storeResource(document);
            logger.info("Document stored. " + document.getId());
            collection.close();
        }
    }

     /**
     *
     * @param collectionName -- name of collection to retrieve content
     * @param documentID -- id of document to retrieve
     * @return content of document as string
     * @throws XMLDBException -- if no resources available in collection
     */
    public String getDocument(String collectionName, String documentID) throws XMLDBException {
        Collection collection =
                DatabaseManager.getCollection(myDatabaseURI + collectionName);
        collection.setProperty(OutputKeys.INDENT, "no");
        XMLResource res = (XMLResource)collection.getResource(documentID);
        if(res == null) {
            throw new XMLDBException();
        } else
            return (String) res.getContent();
    }
}
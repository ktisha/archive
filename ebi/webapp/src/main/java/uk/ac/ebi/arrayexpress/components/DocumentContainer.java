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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.arrayexpress.app.Application;
import uk.ac.ebi.arrayexpress.app.ApplicationComponent;
import uk.ac.ebi.arrayexpress.utils.DocumentTypes;
import uk.ac.ebi.arrayexpress.utils.persistence.DocumentPersister;

import java.util.EnumMap;

public class DocumentContainer extends ApplicationComponent {

    // logging machinery
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private EnumMap<DocumentTypes, DocumentInfo> documents = new EnumMap<DocumentTypes, DocumentInfo>(DocumentTypes.class);

    //ToDo: it's better to use dependency injection
    private DocumentPersister documentPersister = new DocumentPersister();
    private SearchEngine searchEngine;

    public DocumentContainer() {
        super("DocumentContainer");
    }

    /**
     * Implements ApplicaitonComponent.initialize()
     *
     * @throws Exception
     */
    public void initialize() throws Exception {
        searchEngine = (SearchEngine) getComponent("SearchEngine");
    }

    /**
     * Implements ApplicationComponent.terminate()
     *
     * @throws Exception
     */
    public void terminate() throws Exception {
    }

    /**
     * Returns Saxon TinyTree document object by its document ID
     *
     * @param documentId
     * @return
     */
    public DocumentInfo getDocument(DocumentTypes documentId) throws Exception {
        DocumentInfo document = documents.get(documentId);
        if (document == null) {

            document = documentPersister.loadObject(getPreferences().getString(documentId.getPersistenceDocumentLocation()));

            if (isEmptyDocument(document, documentId)) {
                document = createDocument(documentId);
            }

            this.documents.put(documentId, document);
            indexDocument(documentId, document);
        }
        return document;
    }

    /**
     * Puts Saxon TinyTree document associated with its ID
     *
     * @param documentId
     * @param document
     */
    public void putDocument(DocumentTypes documentId, DocumentInfo document) {
        this.documents.put(documentId, document);
        indexDocument(documentId, document);

        //persist document
        documentPersister.saveObject(document, getPreferences().getString(documentId.getPersistenceDocumentLocation()));
    }

    /**
     * Checks if the document with given name exists in the storage container
     *
     * @param documentName
     * @return
     */
    public boolean hasDocument(String documentName) {
        return documents.containsKey(DocumentTypes.getInstanceByName(documentName));
    }

    /**
     * Checks if the document with given ID exists in the storage container
     *
     * @param documentId
     * @return
     */
    public boolean hasDocument(DocumentTypes documentId) {
        return documents.containsKey(documentId);
    }

    private void indexDocument(DocumentTypes documentId, DocumentInfo document) {
        if (searchEngine.getController().hasEnvironment(documentId.getTextName())) {
            searchEngine.getController().index(documentId.getTextName(), document);
        }
    }

    private boolean isEmptyDocument(DocumentInfo document, DocumentTypes type) throws Exception {
        if (null == document)
            return true;

        String total = ((SaxonEngine) Application.getAppComponent("SaxonEngine")).evaluateXPathSingle(document, type.getCountDocXpath());

        return (null == total || total.equals("0"));
    }

    private DocumentInfo createDocument(DocumentTypes type) {
        DocumentInfo document = null;
        try {
            document = ((SaxonEngine) Application.getAppComponent("SaxonEngine")).buildDocument(type.getEmptyDocument());
        } catch (Exception x) {
            logger.error("Caught an exception:", x);
        }

        if (null == document) {
            logger.error("The document WAS NOT created, expect problems down the road");
        }

        return document;
    }

}

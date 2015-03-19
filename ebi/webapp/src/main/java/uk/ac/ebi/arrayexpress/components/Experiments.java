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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.arrayexpress.app.ApplicationComponent;
import uk.ac.ebi.arrayexpress.utils.RegexHelper;
import uk.ac.ebi.arrayexpress.utils.StringTools;
import uk.ac.ebi.arrayexpress.utils.persistence.PersistableString;
import uk.ac.ebi.arrayexpress.utils.persistence.PersistableStringList;
import uk.ac.ebi.arrayexpress.utils.persistence.TextFilePersistence;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Experiments extends ApplicationComponent
{
    // logging machinery
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RegexHelper arrayAccessionRegex = new RegexHelper("^[aA]-\\w{4}-\\d+$", "");

    private String dataSource;

    private TextFilePersistence<PersistableStringList> experimentsInAtlas;
    private TextFilePersistence<PersistableString> species;
    private TextFilePersistence<PersistableString> arrays;
    private Map<String, String> assaysByMolecule;
    private Map<String, String> assaysByInstrument;

    private SaxonEngine saxon;
    private SearchEngine search;
    private Autocompletion autocompletion;
    private ExistDatabase existDb;

    public Experiments()
    {
        super("Experiments");
    }

    public void initialize() throws Exception
    {
        saxon = (SaxonEngine) getComponent("SaxonEngine");
        search = (SearchEngine) getComponent("SearchEngine");
        autocompletion = (Autocompletion) getComponent("Autocompletion");
        existDb = (ExistDatabase) getComponent("ExistDatabase");

        // TODO
        // this.experiments = new TextFilePersistence<PersistableDocument>(
        //        new PersistableDocument()
        //        , new File(getPreferences().getString("ae.experiments.file.location"))
        //);

        this.experimentsInAtlas = new TextFilePersistence<PersistableStringList>(
                new PersistableStringList()
                , new File(getPreferences().getString("ae.atlasexperiments.file.location"))
        );

        this.species = new TextFilePersistence<PersistableString>(
                new PersistableString()
                , new File(getPreferences().getString("ae.species.file.location"))

        );

        this.arrays = new TextFilePersistence<PersistableString>(
                new PersistableString()
                , new File(getPreferences().getString("ae.arraylist.file.location"))
        );

        this.assaysByMolecule = new HashMap<String, String>();
        assaysByMolecule.put("", "<option value=\"\">All assays by molecule</option><option value=\"DNA assay\">DNA assay</option><option value=\"metabolomic profiling\">Metabolite assay</option><option value=\"protein assay\">Protein assay</option><option value=\"RNA assay\">RNA assay</option>");
        assaysByMolecule.put("array assay", "<option value=\"\">All assays by molecule</option><option value=\"DNA assay\">DNA assay</option><option value=\"RNA assay\">RNA assay</option>");
        assaysByMolecule.put("high throughput sequencing assay", "<option value=\"\">All assays by molecule</option><option value=\"DNA assay\">DNA assay</option><option value=\"RNA assay\">RNA assay</option>");
        assaysByMolecule.put("proteomic profiling by mass spectrometer", "<option value=\"protein assay\">Protein assay</option>");

        this.assaysByInstrument = new HashMap<String, String>();
        assaysByInstrument.put("", "<option value=\"\">All technologies</option><option value=\"array assay\">Array</option><option value=\"high throughput sequencing assay\">High-throughput sequencing</option><option value=\"proteomic profiling by mass spectrometer\">Mass spectrometer</option>");
        assaysByInstrument.put("DNA assay", "<option value=\"\">All technologies</option><option value=\"array assay\">Array</option><option value=\"high throughput sequencing assay\">High-throughput sequencing</option>");
        assaysByInstrument.put("metabolomic profiling", "<option value=\"\">All technologies</option>");
        assaysByInstrument.put("protein assay", "<option value=\"\">All technologies</option><option value=\"proteomic profiling by mass spectrometer\">Mass spectrometer</option>");
        assaysByInstrument.put("RNA assay", "<option value=\"\">All technologies</option><option value=\"array assay\">Array</option><option value=\"high throughput sequencing assay\">High-throughput sequencing</option>");
    }

    public void terminate() throws Exception
    {
        saxon = null;
    }

    public boolean isAccessible( String accession, String userId ) throws Exception
    {
        if ("0".equals(userId)) {
            return true;
        } else if (arrayAccessionRegex.test(accession)) {
            return true; // we allow array queries
        } else {
            return false;
//todo
//             return Boolean.parseBoolean(
//                    saxon.evaluateXPathSingle(
//                            documentContainer.getDocument(DocumentTypes.EXPERIMENTS)
//                            , "exists(//experiment[accession = '" + accession + "' and user = '" + userId + "'])"
//                    )
//            );
        }
    }

    public boolean isInAtlas( String accession ) throws Exception
    {
        return this.experimentsInAtlas.getObject().contains(accession);
    }

    public String getSpecies() throws Exception
    {
        return this.species.getObject().get();
    }

    public String getArrays() throws Exception
    {
        return this.arrays.getObject().get();
    }

    public String getAssaysByMolecule( String key )
    {
        return this.assaysByMolecule.get(key);
    }

    public String getAssaysByInstrument( String key )
    {
        return this.assaysByInstrument.get(key);
    }

    public String getDataSource()
    {
        if (null == this.dataSource) {
            this.dataSource = StringTools.arrayToString(
                    getPreferences().getStringArray("ae.experiments.db.datasources")
                    , ","
            );
        }

        return this.dataSource;
    }

    public void reload( String xmlString ) throws Exception
    {
        String processedXml = saxon.transformToString(xmlString, "preprocess-experiments-xml.xsl", null);
        if (null != processedXml) {
            existDb.storeDocument(existDb.AE_COLLECTION, processedXml, "experiments.xml");
        } else {
            this.logger.error("Experiments NOT updated, NULL document passed");
        }
    }

    public void setExperimentsInAtlas( List<String> expList ) throws Exception
    {
        this.experimentsInAtlas.setObject(new PersistableStringList(expList));
    }

//    private void indexExperiments()
//    {
//        try {
//            search.getController().index(DocumentTypes.EXPERIMENTS.getTextName(), documentContainer.getDocument(DocumentTypes.EXPERIMENTS));
//            autocompletion.rebuild();
//        } catch (Exception x) {
//            this.logger.error("Caught an exception:", x);
//        }
//    }
}

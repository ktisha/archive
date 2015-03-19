package uk.ac.ebi.arrayexpress.app;

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

import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

public class ApplicationPreferences
{
    // logging machinery
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String prefsFileName;
    private PropertiesConfiguration prefs;

    public ApplicationPreferences( String fileName )
    {
        this.prefsFileName = fileName;
    }

    public void initialize()
    {
        load();
    }

    public void terminate()
    {
        if (null != prefs) {
            prefs = null;
        }
    }

    public String getString( String key )
    {
        return prefs.getString(key);
    }

    public String[] getStringArray( String key )
    {
        return prefs.getStringArray(key);
    }

    public Long getLong( String key )
    {
        Long value = null;
        try {
            value = prefs.getLong(key);
        } catch (ConversionException x) {
            logger.error(x.getMessage());
        } catch (NoSuchElementException x) {
            logger.error(x.getMessage());            
        } catch (Exception x) {
            logger.error("Caught an exception:", x);
        }
        return value;
    }

    public Boolean getBoolean( String key )
    {
        Boolean value = null;
        try {
            value = prefs.getBoolean(key);
        } catch (NoSuchElementException x) {
            logger.error(x.getMessage());
        } catch (Exception x) {
            logger.error("Caught an exception:", x);
        }

        return value;
    }

    private void load()
    {
        // todo: what to do if file is not there? must be a clear error message + shutdown
        InputStream prefsStream = null;
        try {
            prefs = new PropertiesConfiguration();
            prefsStream = Application.getInstance().getResource(
                    "/WEB-INF/classes/" + prefsFileName + ".properties"
            ).openStream();

            prefs.load(prefsStream);
        } catch (Exception x) {
            logger.error("Caught an exception:", x);
        } finally {
            if (null != prefsStream) {                      
                try {
                    prefsStream.close();
                } catch (IOException x) {
                    logger.error("Caught an exception:", x);
                }
            }
        }
    }
}

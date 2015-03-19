package uk.ac.ebi.arrayexpress.utils.persistence;

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

public class PersistableString implements Persistable
{
    private String string;

    public PersistableString()
    {
        string = "";
    }

    public PersistableString( String str )
    {
        string = str;
    }

    public String get()
    {
        return string;
    }

    public void set( String str )
    {
        string = str;
    }

    public String toPersistence()
    {
        return string;
    }

    public void fromPersistence( String str )
    {
        string = str;
    }

    public boolean isEmpty()
    {
        return (0 == string.length());
    }
}

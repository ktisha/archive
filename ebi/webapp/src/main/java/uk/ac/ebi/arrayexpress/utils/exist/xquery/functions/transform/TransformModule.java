package uk.ac.ebi.arrayexpress.utils.exist.xquery.functions.transform;

import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;

public class TransformModule extends AbstractInternalModule
{

    public final static String NAMESPACE_URI = "http://www.ebi.ac.uk/arrayexpress/exist/xquery/transform";

    public final static String PREFIX = "transform";
    public final static String RELEASED_IN_VERSION = "&lt; eXist-1.0";

    private final static FunctionDef functions[] = {
            new FunctionDef(Transform.signatures[0], Transform.class),
            new FunctionDef(Transform.signatures[1], Transform.class),
            new FunctionDef(Transform.signatures[2], Transform.class),
            new FunctionDef(Transform.signatures[3], Transform.class)
    };

    public TransformModule()
    {
        super(functions);
    }

    public String getDescription()
    {
        return "A module for dealing with XSL transformations.";
    }

    public String getNamespaceURI()
    {
        return NAMESPACE_URI;
    }

    public String getDefaultPrefix()
    {
        return PREFIX;
    }

    public String getReleaseVersion()
    {
        return RELEASED_IN_VERSION;
    }

}
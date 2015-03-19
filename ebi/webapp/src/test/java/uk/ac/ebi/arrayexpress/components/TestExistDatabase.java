package uk.ac.ebi.arrayexpress.components;

import junit.framework.TestCase;
import org.junit.Test;
import org.xmldb.api.base.ResourceSet;
import uk.ac.ebi.arrayexpress.app.Application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: catherine
 * Date: Aug 2, 2010
 */
public class TestExistDatabase extends TestCase {
    @Test
    public void testStoreDocumentFromString() {
        ExistDatabase dataBase = new ExistDatabase();
        new TestApplication();
        try {
            final String content = "<test><id>1</id><id>2</id></test>";
            dataBase.initialize();
            dataBase.storeDocument("/test", content, "test.xml");
            ResourceSet result = dataBase.evaluateXPath("/test", "doc(\"test.xml\")//test/id");
            dataBase.terminate();
            assertEquals(2, result.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testStoreDocumentFromFile() {
        ExistDatabase dataBase = new ExistDatabase();
        new TestApplication();
        try {
            File file = new File("webapp/resources/test/test.xml");
            dataBase.initialize();
            dataBase.storeDocument("/test", file, "test1.xml");
            ResourceSet result = dataBase.evaluateXPath("/test", "doc(\"test1.xml\")//test/id");
            dataBase.terminate();
            assertEquals(3, result.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class TestApplication extends Application {
    public TestApplication() {
        super("arrayexpress");

        addComponent(new ExistDatabase());
        addComponent(new JobsController());

        initialize();
    }

    public URL getResource(String path) throws MalformedURLException {
        return getClass().getResource(path.replaceFirst("/WEB-INF/classes", ""));
    }
}
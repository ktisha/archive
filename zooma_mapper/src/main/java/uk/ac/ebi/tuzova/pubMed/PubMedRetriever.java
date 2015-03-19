package uk.ac.ebi.tuzova.pubMed;

import org.w3c.dom.NodeList;
import uk.ac.ebi.tuzova.http.HttpRetriever;
import uk.ac.ebi.tuzova.xml.XPathParser;

import java.io.IOException;
import java.net.URL;

/**
 * User: tuzova
 * Date: Jul 12, 2010
 *
 * Class to retrieve information from PubMed
 */
public class PubMedRetriever {

    public PubMedRetriever() {}

    public NodeList getSimilars(URL pubMedUrl) throws IOException {
        HttpRetriever retriever = new HttpRetriever();
        StringBuffer page = retriever.getPage(pubMedUrl);

        XPathParser p = new XPathParser(page.toString());
        return p.extract("//LinkSetDb/Link[../LinkName/text()='pubmed_pubmed_citedin']");

    }

    /*
    public PubMedRetriever(URL pubMedUrl) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(pubMedUrl.openStream()));
        StringBuffer str = new StringBuffer();

        while (in.ready()) {
            str.append(in.readLine());
        }
        in.close();

        System.out.println(str);
    }
    */
}

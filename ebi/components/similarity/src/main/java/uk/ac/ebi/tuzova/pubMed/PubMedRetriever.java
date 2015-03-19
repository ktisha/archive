package uk.ac.ebi.tuzova.pubMed;

import org.w3c.dom.NodeList;
import uk.ac.ebi.tuzova.http.HttpPageGetter;
import uk.ac.ebi.tuzova.xml.XPathParser;

import java.io.IOException;
import java.net.URL;

/**
 * User: tuzova
 * Date: Jul 12, 2010
 *
 * Class to retrieve information about
 * similar publications from PubMed
 */
public class PubMedRetriever {

    public PubMedRetriever() {}

    /**
     *
     * @param pubMedUrl is URL to extract similar publications
     * @return list of nodes contains PMID of similar publications
     * @throws IOException from HttpPageGetter
     */
    public NodeList getSimilars(URL pubMedUrl) throws IOException {
        HttpPageGetter getter = new HttpPageGetter();
        StringBuffer page = getter.getPage(pubMedUrl);

        if (page == null)
            return null;
        XPathParser parser = new XPathParser();
        return parser.extractFromString(page.toString(), "//LinkSetDb/Link[../LinkName/text()='pubmed_pubmed_citedin'] |" +
                "//LinkSetDb/Link[../LinkName/text()='pubmed_pubmed'][position()<12] ");

    }
}

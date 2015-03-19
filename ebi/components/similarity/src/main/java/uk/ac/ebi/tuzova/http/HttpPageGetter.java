package uk.ac.ebi.tuzova.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tuzova
 * Date: Jul 12, 2010
 *
 * Establish connection and get the page
 */
public class HttpPageGetter {

    private HttpURLConnection myPubMedConnection = null;

    public HttpPageGetter() {}

    /**
     *
     * @param pubMedUrl -- url of page to get
     * @return - StringBuffer contains page
     */
    public StringBuffer getPage(URL pubMedUrl) {
        Logger logger = Logger.getLogger("main.log");
        StringBuffer text = null;
        try {
            myPubMedConnection = (HttpURLConnection) pubMedUrl.openConnection();
            //myPubMedConnection.setRequestProperty("User-Agent", "Browsershots");
            //myPubMedConnection.setRequestMethod("GET");
            if(myPubMedConnection == null)
                return null;
            myPubMedConnection.connect();
            InputStream in;
            try {
                in = myPubMedConnection.getInputStream();
            } catch (Exception e) {
                return null;
            }
            if(in == null)
                return null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            text = new StringBuffer();
            String line;
            while (null != (line = reader.readLine())) {
                text.append(line);
            }
            reader.close();
        } catch (IOException e) {
            logger.log(Level.INFO, "Problem with connection " + e.getMessage());
        }
        myPubMedConnection.disconnect();
        return text;
    }

    
}

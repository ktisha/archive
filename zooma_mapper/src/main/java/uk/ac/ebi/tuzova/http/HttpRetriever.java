package uk.ac.ebi.tuzova.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User: tuzova
 * Date: Jul 12, 2010
 */
public class HttpRetriever {

    private HttpURLConnection myPubMedConnection = null;

    public HttpRetriever() {}

    public StringBuffer getPage(URL pubMedUrl) throws IOException {
        myPubMedConnection = (HttpURLConnection) pubMedUrl.openConnection();
        //myPubMedConnection.setRequestProperty("User-Agent", "Browsershots");
        //myPubMedConnection.setRequestMethod("GET");
        myPubMedConnection.connect();
        InputStream in = myPubMedConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer text = new StringBuffer();
        while(reader.ready()) {
            text.append(reader.readLine());
        }
        reader.close();
        return text;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            myPubMedConnection.disconnect();
        } finally {
            super.finalize();
        }
    }
    
}

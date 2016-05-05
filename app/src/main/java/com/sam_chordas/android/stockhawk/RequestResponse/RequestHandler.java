package com.sam_chordas.android.stockhawk.RequestResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Swati Agarwal on 01-08-2015.
 */
public class RequestHandler {

    String url;

    public RequestHandler(){

    }

    public RequestHandler(String url){
        this.url = url;
    }

    public String getResponse(){
        String response="";

        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

        return response;
    }
}

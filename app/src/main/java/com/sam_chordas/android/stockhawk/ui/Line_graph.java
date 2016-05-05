package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sam_chordas.android.stockhawk.R;
import android.util.Log;
import com.sam_chordas.android.stockhawk.RequestResponse.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Swati Agarwal on 09-04-2016.
 */
public class Line_graph extends ActionBarActivity {

    String url = "https://query.yahooapis.com/v1/public/yql";
    String search = "format";
    String search_val = "json";
    String query_key = "q";
    String dia ="diagnostics";
    String dia_val="true";
    String env="env";
    String env_val="store://datatables.org/alltableswithkeys";
    String call="callback";
    String call_val="";
    LineChart lc;
    Uri uri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        Intent i = getIntent();
        String sym = i.getStringExtra("symbol");
        String query = "select * from yahoo.finance.historicaldata where symbol ='" + sym + "' and startDate = '2016-01-01' and endDate = '2016-01-24'";

        uri = Uri.parse(url).buildUpon().appendQueryParameter(query_key, query).appendQueryParameter(search,search_val).
                appendQueryParameter(dia,dia_val).appendQueryParameter(env,env_val).appendQueryParameter(call,call_val).build();
        Log.v("url",uri.toString());
        lc = (LineChart) findViewById(R.id.linechart);

        AsyncTaskForLineGraph asyncTaskForLineGraph = new AsyncTaskForLineGraph();
        asyncTaskForLineGraph.execute(uri.toString());

    }

    public class AsyncTaskForLineGraph extends AsyncTask<String,String,String>{

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        @Override
        protected String doInBackground(String... params) {
            RequestHandler requestHandler = new RequestHandler(params[0]);
            String response = requestHandler.getResponse();

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("query");
                JSONObject jsonObject3 = jsonObject1.getJSONObject("results");
                JSONArray jsonArray = jsonObject3.getJSONArray("quote");
                Log.v("array",jsonArray.length()+"");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    entries.add(new Entry(Float.parseFloat(jsonObject2.getString("Adj_Close")),i+1));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            labels.add("1");
            labels.add("2");
            labels.add("3");
            labels.add("4");
            labels.add("5");
            labels.add("6");
            labels.add("7");
            labels.add("8");
            labels.add("9");
            labels.add("10");
            labels.add("11");
            labels.add("12");
            labels.add("13");
            labels.add("14");

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LineDataSet dataset = new LineDataSet(entries, "Stock Values over time");
            dataset.setDrawCircles(true);
            dataset.setDrawValues(true);
            LineData data = new LineData(labels,dataset);
            lc.setDescription("Stock Values");
            lc.setData(data);
            lc.animateY(5000);

        }
    }

}

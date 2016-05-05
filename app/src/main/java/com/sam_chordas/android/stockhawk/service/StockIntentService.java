package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;

import android.os.Handler;
import android.widget.Toast;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {

  public StockIntentService(){
    super(StockIntentService.class.getName());
  }

  public StockIntentService(String name) {
    super(name);
  }

  @Override protected void onHandleIntent(Intent intent) {
    Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
    StockTaskService stockTaskService = new StockTaskService(this);
    Bundle args = new Bundle();
    if (intent.getStringExtra("tag").equals("add")){
      args.putString("symbol", intent.getStringExtra("symbol"));
    }
    // We can call OnRunTask from the intent service to force it to run immediately instead of
    // scheduling a task.
    Handler mHandler = new Handler(getMainLooper());
    if (stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args)) == GcmNetworkManager.RESULT_FAILURE) {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          Toast.makeText(getApplicationContext(), "Error, symbol Not found", Toast.LENGTH_LONG).show();
        }
      });
    } else {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          Toast.makeText(getApplicationContext(), "Success! added new Symbol", Toast.LENGTH_LONG).show();
        }
      });
    }
  }
}

package com.isel.ps.Find_My_Beacon.task;

import android.os.AsyncTask;
import android.util.Log;
import com.isel.ps.Find_My_Beacon.beacon.BeaconManager;
import com.isel.ps.Find_My_Beacon.beacon.IBeaconResult;
import com.isel.ps.Find_My_Beacon.communication.answer.AnswerCommunication;
import com.isel.ps.Find_My_Beacon.exception.ConnectorException;
import com.isel.ps.Find_My_Beacon.json.JsonConverter;
import com.isel.ps.Find_My_Beacon.model.Answer;
import com.isel.ps.Find_My_Beacon.model.Beacon;
import com.isel.ps.Find_My_Beacon.model.SearchRequestReceived;

import java.util.Arrays;
import java.util.List;

public class RequestProcessorTask extends AsyncTask<String, SearchRequestReceived,Void> {
    private static final String DEBUG_TAG = "REQUEST_PROCESSOR";


    public RequestProcessorTask() {

    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d(DEBUG_TAG, "Processing requests : " + Arrays.toString(params));
        for (String answer : params) {
            final SearchRequestReceived requestObject = JsonConverter.parseToObject(SearchRequestReceived.class, answer);
            onProgressUpdate(requestObject);
        }
        Log.d(DEBUG_TAG, "Leaving RequestProcessorTask.");
        return null;
    }

    @Override
    protected void onProgressUpdate(SearchRequestReceived... values) {
        for (final SearchRequestReceived requestReceived : values) {
            Beacon toFind = new Beacon(requestReceived.getUuid(),requestReceived.getMajor(),requestReceived.getMinor());
            BeaconManager.getInstance().getBeacon(toFind, new IBeaconResult() {
                @Override
                public void onResult(List<Beacon> beacons) {
                    Log.d(DEBUG_TAG, "OnResult called. beacons found : " + beacons.toString());
                    if(beacons.size() > 1) {
                        Log.d(DEBUG_TAG, "This was unexpected. Found more than one beacon");
                        return;
                    }

                    Answer toSent = new Answer();
                    if(beacons.size() == 1) {
                        toSent.setSuccess(true);
                        toSent.setCoorX(0);
                        toSent.setCoorY(0);
                    }else {
                        toSent.setSuccess(false);
                    }

                    try {
                        new AnswerCommunication(requestReceived.getRequestId()).post(toSent);
                    } catch (ConnectorException e) {
                        Log.e(DEBUG_TAG, "Error posting answer.");
                    }
                }
            });
        }
    }

}

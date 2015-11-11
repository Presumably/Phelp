package com.isel.ps.Find_My_Beacon.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.isel.ps.Find_My_Beacon.database.FindMyBeaconDb;
import com.isel.ps.Find_My_Beacon.database.table.AnswerTable;
import com.isel.ps.Find_My_Beacon.exception.DatabaseException;
import com.isel.ps.Find_My_Beacon.json.JsonConverter;
import com.isel.ps.Find_My_Beacon.model.Answer;

public class AnswerProcessorTask extends AsyncTask<String,Answer,Void> {

    private static final String DEBUG_TAG = "ANSWER_PROCESSOR";
    private Context context;

    public AnswerProcessorTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d(DEBUG_TAG, "Processing answers : " + params);
        for (String answer : params) {
            final Answer requestObject = JsonConverter.parseToObject(Answer.class, answer);
            onProgressUpdate(requestObject);
        }
        Log.d(DEBUG_TAG, "Leaving AnswerProcessorTask.");
        return null;
    }

    @Override
    protected void onProgressUpdate(Answer... answers) {
        Log.d(DEBUG_TAG, "Processing Answer: " + answers);
        final FindMyBeaconDb db = new FindMyBeaconDb(this.context);
        for (Answer answer : answers) {
            try {
                AnswerTable.addAnswerToRequest(db,answer);
            } catch (DatabaseException e) {
                Log.d(DEBUG_TAG, "Failed to insert answer : " + answer.toString());
            }
        }
    }
}

package com.isel.ps.Find_My_Beacon.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.isel.ps.Find_My_Beacon.database.table.AnswerTable;
import com.isel.ps.Find_My_Beacon.database.table.BeaconTable;
import com.isel.ps.Find_My_Beacon.database.table.SearchRequestTable;

public class FindMyBeaconDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "ISEL.PS.FIND.MY.BEACON.db";
    private static final int CURRENT_VERSION = 10;

    public FindMyBeaconDb(Context context) {
        super(context, DB_NAME, null, CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BeaconTable.SQL_CREATE_BEACONS);
        db.execSQL(SearchRequestTable.SQL_CREATE_TABLE);
        db.execSQL(AnswerTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AnswerTable.SQL_DROP_TABLE);
        db.execSQL(SearchRequestTable.SQL_DROP_TABLE);
        db.execSQL(BeaconTable.SQL_DROP_BEACONS);
        this.onCreate(db);
    }
}

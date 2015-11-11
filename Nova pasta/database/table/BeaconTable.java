package com.isel.ps.Find_My_Beacon.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import com.isel.ps.Find_My_Beacon.database.FindMyBeaconDb;
import com.isel.ps.Find_My_Beacon.model.Beacon;
import com.isel.ps.Find_My_Beacon.model.Proximity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public final class BeaconTable extends BaseTable {

    private static final String DEBUG_TAG = "BeaconTable";

    public BeaconTable() {

    }

    protected static abstract class BeaconEntry implements BaseColumns {
        public static final String TABLE_NAME = "Beacon";
        public static final String BEACON_NAME = "Name";
        public static final String BEACON_UUID = "UUID";
        public static final String BEACON_MAJOR = "Major";
        public static final String BEACON_MINOR = "Minor";
    }

    public static long addBeacon(final FindMyBeaconDb db, final Beacon b){
        Log.d(DEBUG_TAG, "Adding beacon to Database : " + b);
        final SQLiteDatabase writableDatabase = db.getWritableDatabase();
        final ContentValues cv = new ContentValues();
        cv.put(BeaconEntry.BEACON_NAME, b.getName());
        cv.put(BeaconEntry.BEACON_UUID, b.getUUID());
        cv.put(BeaconEntry.BEACON_MAJOR, String.valueOf(b.getMajor()));
        cv.put(BeaconEntry.BEACON_MINOR, String.valueOf(b.getMinor()));

        Log.d(DEBUG_TAG, "Content values to insert : " + cv.toString());
        return writableDatabase.insert(BeaconEntry.TABLE_NAME, null, cv);
    }


    public static List<Beacon> getBeacons(final FindMyBeaconDb db){
        Log.d(DEBUG_TAG, "Starting Get from db for all beacons");
        final SQLiteDatabase readDb = db.getReadableDatabase();
        final Cursor queryResult = readDb.query(BeaconEntry.TABLE_NAME, null, null, null, null, null, null);
        if(!queryResult.moveToFirst()){
            Log.d(DEBUG_TAG, "No result for query! Returning null for result -> " + queryResult.toString());
            return Collections.emptyList();
        }
        final LinkedList<Beacon> result = new LinkedList<Beacon>();
        do {
            final String beaconName = queryResult.getString(0);
            final String beaconUUID = queryResult.getString(1);
            final int major = queryResult.getInt(2);
            final int minor = queryResult.getInt(3);
            Log.d(DEBUG_TAG, "Adding beacon with values: Name-> " + beaconName + " uuid -> " + beaconUUID
                                + " major -> " + major + " minor -> " + minor);
            result.add(new Beacon(beaconName, beaconUUID, Proximity.UNKNOWN, major, minor));
        } while (queryResult.moveToNext());

        Log.d(DEBUG_TAG, "Returning List with size count = " + result.size());
        return result;
    }


    public static final String SQL_CREATE_BEACONS =
            CREATE_TABLE + BeaconEntry.TABLE_NAME + " (" +
                    BeaconEntry.BEACON_NAME + TEXT_TYPE + COMMA_SEP +
                    BeaconEntry.BEACON_UUID + TEXT_TYPE + COMMA_SEP +
                    BeaconEntry.BEACON_MAJOR + INTEGER_TYPE + COMMA_SEP +
                    BeaconEntry.BEACON_MINOR + INTEGER_TYPE + COMMA_SEP +
                    PRIMARY_KEY + "(" +
                    BeaconEntry.BEACON_UUID + COMMA_SEP +
                    BeaconEntry.BEACON_MAJOR + COMMA_SEP +
                    BeaconEntry.BEACON_MINOR +
                    ") )";
    public static final String SQL_DROP_BEACONS =
            "DROP TABLE IF EXISTS " + BeaconEntry.TABLE_NAME;

}



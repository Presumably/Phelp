package com.isel.ps.Find_My_Beacon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.isel.ps.Find_My_Beacon.R;
import com.isel.ps.Find_My_Beacon.adapter.BeaconAdapter;
import com.isel.ps.Find_My_Beacon.beacon.BeaconManager;
import com.isel.ps.Find_My_Beacon.beacon.IBeaconResult;
import com.isel.ps.Find_My_Beacon.communication.beacon.BeaconCommunication;
import com.isel.ps.Find_My_Beacon.communication.response.ICommunicationResponse;
import com.isel.ps.Find_My_Beacon.database.FindMyBeaconDb;
import com.isel.ps.Find_My_Beacon.database.table.BeaconTable;
import com.isel.ps.Find_My_Beacon.exception.ConnectorException;
import com.isel.ps.Find_My_Beacon.model.Beacon;

import java.util.LinkedList;
import java.util.List;

public final class AddBeaconActivity extends Activity implements IBeaconResult {

    private static final String DEBUG_TAG = "ADD_BEACON";
    private ListView beaconList;
    private ProgressDialog progress;
    private BeaconAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activateBluetooth();
        setContentView(R.layout.list_layout);
        progress = ProgressDialog.show(this,getString(R.string.searchTitle),getString(R.string.searchMessage),false,false,null);
        beaconList = (ListView) findViewById(R.id.list);

        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Object itemAtPosition = parent.getItemAtPosition(position);
                if (itemAtPosition instanceof Beacon) {
                    final Beacon selectedBeacon = (Beacon) itemAtPosition;
                    confirmAndAdd(selectedBeacon);
                } else {
                    Log.d(DEBUG_TAG, "Object was not a beacon. That was unexpected! " + itemAtPosition.toString());
                    Toast.makeText(getBaseContext(), R.string.unexpectedError, Toast.LENGTH_LONG).show();
                }
            }
        });
        adapter = new BeaconAdapter(this, R.layout.beacon_item, new LinkedList<Beacon>());
        beaconList.setAdapter(adapter);
        Log.d(DEBUG_TAG, "Requesting all beacons!");

            BeaconManager.getInstance().getAll(this);


    }

    private void confirmAndAdd(final Beacon selectedBeacon) {
        final AlertDialog confirmationDialog = new AlertDialog.Builder(this).create();
        Log.d(DEBUG_TAG, "Starting confirmation dialog for beacon : " + selectedBeacon.toString());
        confirmationDialog.setMessage(getString(R.string.confirmBeacon) + " " + selectedBeacon.getName());

        confirmationDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(DEBUG_TAG, "Beacon confirmed positivily!");
                try {
                    new BeaconCommunication().post(selectedBeacon, new ICommunicationResponse<Beacon>() {
                        @Override
                        public void onResponse(Beacon beacon) {
                            BeaconTable.addBeacon(new FindMyBeaconDb(getBaseContext()), selectedBeacon);
                        }
                    });
                } catch (ConnectorException e) {
                    Toast.makeText(getBaseContext(), "Error adding beacon", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

        confirmationDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(DEBUG_TAG, "Beacon rejectd!");
                //Does nothing.
            }
        });
        confirmationDialog.show();
    }

    private void activateBluetooth() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d(DEBUG_TAG, "Checking if bluetooth is up!");
        if(!bluetoothAdapter.isEnabled()){
            Log.d(DEBUG_TAG, "Bluetooth is not up! Starting intent to activate bluetooth");
            final Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetooth);
        }
        Log.d(DEBUG_TAG, "Bluetooth is up!");   
    }

    @Override
    public void onResult(List<Beacon> beacons) {
        if(this.isDestroyed()){
            Log.d(DEBUG_TAG, "Activity in invalid state!");
            return;
        }

        if(beacons == null || beacons.isEmpty()) {
            Log.d(DEBUG_TAG, "No beacon found in search!");
            Toast.makeText(this,R.string.noBeacons,Toast.LENGTH_LONG).show();
            finish();
        }
        Log.d(DEBUG_TAG, "Adding beacons to adapter! ");
        adapter.addAll(beacons);
        Log.d(DEBUG_TAG, "Dismissing processing dialog");
        if(progress.isShowing()) {
            Log.d(DEBUG_TAG, "Progress dialog still showing in onResult! dismissing..");
            progress.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(progress.isShowing()){
            Log.d(DEBUG_TAG, "Progress dialog still showing in onStop! dismissing..");
            progress.dismiss();
        }
    }

}
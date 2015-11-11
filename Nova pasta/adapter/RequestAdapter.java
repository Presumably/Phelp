package com.isel.ps.Find_My_Beacon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.isel.ps.Find_My_Beacon.R;
import com.isel.ps.Find_My_Beacon.model.SearchRequest;

import java.util.List;

public class RequestAdapter extends ArrayAdapter<SearchRequest> {

    private List<SearchRequest> searchRequests;

    public RequestAdapter(Context context, int resource, List<SearchRequest> searchRequests) {
        super(context, resource, searchRequests);
        this.searchRequests = searchRequests;
        this.searchRequests = searchRequests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.search_request_item, null);
        }

        SearchRequest searchRequest = searchRequests.get(position);

        if (searchRequest != null) {

            TextView beaconName = (TextView) v.findViewById(R.id.searchRequestBeacon);

            if (beaconName != null){
                beaconName.setText(searchRequest.getBeaconId());
            }
        }

        return v;
    }
}

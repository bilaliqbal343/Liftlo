package com.app.liftlo.Ride.FairComparisson;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.liftlo.R;

import java.util.ArrayList;

public class FareComparisonAdapter extends BaseAdapter {
    Activity con;

    private ArrayList<FareComparisonModel> mDisplayedValues;    // Values to be displayed


    public FareComparisonAdapter(Activity con, ArrayList<FareComparisonModel> mFareArrayList) {
        this.con = con;
        this.mDisplayedValues = mFareArrayList;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class Viewholder {

        TextView TVstartloc, TVdestination, TVcost;


    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        FareComparisonAdapter.Viewholder viewholder;
        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new FareComparisonAdapter.Viewholder();
            convertView = inflater.inflate(R.layout.items_fare_comparison, null);

            viewholder.TVstartloc = (TextView) convertView.findViewById(R.id.fc_start_loc);

            viewholder.TVdestination = (TextView) convertView.findViewById(R.id.fc_end_loc);
            viewholder.TVcost = (TextView) convertView.findViewById(R.id.fc_cost);


            convertView.setTag(viewholder);

        } else {

            viewholder = (FareComparisonAdapter.Viewholder) convertView.getTag();
        }
        viewholder.TVstartloc.setText(mDisplayedValues.get(position).startLoc);
        viewholder.TVdestination.setText("+" + mDisplayedValues.get(position).endLoc);
        viewholder.TVcost.setText(mDisplayedValues.get(position).cost);
        return convertView;
    }
}

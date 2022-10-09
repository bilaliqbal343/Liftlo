package com.app.liftlo.Driver.History;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.app.liftlo.Driver.Home.FragmentRideInfo;
import com.app.liftlo.R;
import com.app.liftlo.Ride.Home.FragmentAllRides;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bilal on 17-Feb-17.
 */

public class RideHistoryAdapter extends BaseAdapter {

    Boolean isride;
    Activity con;
    Fragment fragment;
    String[] date, time, name, driver_id, startName, destName, seats, cost;

    public RideHistoryAdapter(Activity contex, String[] driver_id, String[] name
            , String[] date, String[] time, String[] start_nAme, String[] dest_name
            , String[] seats, String[] cost, Boolean isride) {

        con = contex;
        this.driver_id = driver_id;
        this.name = name;
        this.isride = isride;
        this.date = date;
        this.time = time;
        this.startName = start_nAme;
        this.destName = dest_name;
        this.seats = seats;
        this.cost = cost;
    }

    @Override
    public int getCount() {
        return driver_id.length;
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

        TextView TVdate, TVname, TVnumber, TVstatus, TVstartName, TVdestName, TVseats, TVcost;
        Button returnRide, btnDecline;
        ImageButton call, map;
        CircleImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.items_driver_history, null);

            viewholder.TVdate = (TextView) convertView.findViewById(R.id.time);
            viewholder.TVname = (TextView) convertView.findViewById(R.id.name);
            viewholder.TVstartName = (TextView) convertView.findViewById(R.id.start_loc);
            viewholder.TVdestName = (TextView) convertView.findViewById(R.id.end_loc);
            viewholder.TVseats = (TextView) convertView.findViewById(R.id.seat);
            viewholder.TVcost = (TextView) convertView.findViewById(R.id.cost);
            viewholder.returnRide = (Button) convertView.findViewById(R.id.btn_return_ride);
            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }


        viewholder.TVdate.setText(con.getResources().getString(R.string.date) + " " + date[position] + "     " + con.getResources().getString(R.string.time) + " " + time[position]);
        viewholder.TVstartName.setText(startName[position]);
        viewholder.TVname.setText(name[position]);
        viewholder.TVdestName.setText(destName[position]);
        viewholder.TVseats.setText(con.getResources().getString(R.string.seats_no) + " " + seats[position]);
        viewholder.TVcost.setText(con.getResources().getString(R.string.cost_per_seat) + " " + cost[position]);
        if (isride == true) {
            viewholder.returnRide.setVisibility(View.VISIBLE);
        } else {
            viewholder.returnRide.setVisibility(View.GONE);
        }
        viewholder.returnRide.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 1);


            }
        });


        return convertView;
    }


}

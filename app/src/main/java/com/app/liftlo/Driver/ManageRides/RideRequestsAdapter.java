package com.app.liftlo.Driver.ManageRides;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.liftlo.utils.ServerURL;
import com.app.liftlo.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bilal on 17-Feb-17.
 */

public class RideRequestsAdapter extends BaseAdapter {


    Activity con;
    String[] status,
            date, time, ride_name, ride_number, startName, destName, seats, cost, r_image;

    RideRequestsAdapter(Activity contex, String[] status, String[] ride_name, String[] ride_number
                        ,String[] date, String[] time, String[] startNAme, String[] destNAme
            , String[] seats, String[] cost, String[] r_image) {

        con = contex;
        this.status = status;
        this.ride_name = ride_name;
        this.ride_number = ride_number;
        this.date = date;
        this.time = time;
        this.startName = startNAme;
        this.destName = destNAme;
        this.seats = seats;
        this.cost = cost;
        this.r_image = r_image;
    }

    @Override
    public int getCount() {
        return ride_name.length;
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

        TextView  TVdate, TVname, TVnumber, TVstatus, TVstartName, TVdestName, TVseats, TVcost;
        Button btnAccept, btnDecline;
        ImageButton call, map;
        CircleImageView image;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.items_ride_request, null);

            viewholder.TVnumber = (TextView) convertView.findViewById(R.id.number);
            viewholder.TVstatus = (TextView) convertView.findViewById(R.id.status);
            viewholder.TVdate = (TextView) convertView.findViewById(R.id.time);
            viewholder.TVname = (TextView) convertView.findViewById(R.id.name);
            viewholder.TVstartName = (TextView) convertView.findViewById(R.id.start_loc);
            viewholder.TVdestName = (TextView) convertView.findViewById(R.id.end_loc);
            viewholder.TVseats = (TextView) convertView.findViewById(R.id.seat);
            viewholder.TVcost = (TextView) convertView.findViewById(R.id.cost);
            viewholder.btnAccept = (Button) convertView.findViewById(R.id.accept);
            viewholder.btnDecline = (Button) convertView.findViewById(R.id.decline);
            viewholder.call = (ImageButton) convertView.findViewById(R.id.call);
            viewholder.map = (ImageButton) convertView.findViewById(R.id.map);
            viewholder.image = (CircleImageView) convertView.findViewById(R.id.profile_image);


            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }


        if (status[position].equals("00")){
            viewholder.TVstatus.setTextColor(Color.parseColor("#E91E63"));
            viewholder.TVstatus.setText(con.getResources().getString(R.string.rejected));

            viewholder.btnAccept.setVisibility(View.GONE);
            viewholder.btnDecline.setVisibility(View.GONE);
        }
        else if (status[position].equals("1")){
            viewholder.TVstatus.setTextColor(Color.parseColor("#FF9800"));
            viewholder.TVstatus.setText(con.getResources().getString(R.string.pending));
        }
        else if (status[position].equals("2")){
            viewholder.TVstatus.setTextColor(Color.parseColor("#25C777"));
            viewholder.TVstatus.setText(con.getResources().getString(R.string.accepted));

            viewholder.btnDecline.setVisibility(View.GONE);
            viewholder.btnAccept.setVisibility(View.GONE);
        }


        viewholder.TVdate.setText(con.getResources().getString(R.string.date) + " " + date[position] + "     " + con.getResources().getString(R.string.time) + " " + time[position]);
        viewholder.TVname.setText(ride_name[position]);
        viewholder.TVnumber.setText(ride_number[position]);
        viewholder.TVstartName.setText(startName[position]);
        viewholder.TVdestName.setText(destName[position]);
        viewholder.TVseats.setText(con.getResources().getString(R.string.seats_no) +" "+seats[position]);
        viewholder.TVcost.setText(con.getResources().getString(R.string.cost_per_seat) +" "+cost[position]);


        if (r_image[position].equals("null")){
            Glide.with(con).load(R.drawable.logo).into(viewholder.image);
        }else {
            Glide.with(con).load(ServerURL.load_image+r_image[position]).into(viewholder.image);
        }


        viewholder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 1);
            }
        });



        viewholder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 2);
            }
        });



        viewholder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 3);
            }
        });



        viewholder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 4);
            }
        });


        return convertView;
    }



}

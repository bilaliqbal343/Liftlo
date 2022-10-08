package com.app.liftlo.Driver.MyRides;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.liftlo.utils.ServerURL;
import com.app.liftlo.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bilal on 17-Feb-17.
 */

public class MyRidesAdapter extends BaseAdapter {


    Activity con;
    String[] image,
            date, time, name, startName, destName, seats, cost, seats_booked;

    MyRidesAdapter(Activity contex, String[] image, String[] name,
                   String[] date, String[] time, String[] startNAme, String[] destNAme
            , String[] seats, String[] seats_booked, String[] cost) {

        con = contex;
        this.image = image;
        this.name = name;
        this.date = date;
        this.time = time;
        this.startName = startNAme;
        this.destName = destNAme;
        this.seats = seats;
        this.cost = cost;
        this.seats_booked = seats_booked;
    }

    @Override
    public int getCount() {
        return name.length;
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

        TextView TVdate, TVname, TVstartName, TVdestName, TVseats, TVcost;
        CircleImageView imageView;
        Button btnRequests, btnStart, btnEdit, btnCancel;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.items_my_rides, null);

            viewholder.TVdate = (TextView) convertView.findViewById(R.id.time);
            viewholder.TVname = (TextView) convertView.findViewById(R.id.name);
            viewholder.TVstartName = (TextView) convertView.findViewById(R.id.start_loc);
            viewholder.TVdestName = (TextView) convertView.findViewById(R.id.end_loc);
            viewholder.TVseats = (TextView) convertView.findViewById(R.id.seats);
            viewholder.TVcost = (TextView) convertView.findViewById(R.id.cost);
            viewholder.btnRequests = (Button) convertView.findViewById(R.id.btn);
            viewholder.btnStart = (Button) convertView.findViewById(R.id.start);
//            viewholder.btnEdit = (Button) convertView.findViewById(R.id.edit);
            viewholder.btnCancel = (Button) convertView.findViewById(R.id.cancel);
            viewholder.imageView = (CircleImageView) convertView.findViewById(R.id.profile_image);


            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }

        viewholder.TVdate.setText(con.getResources().getString(R.string.date) + " " + date[position] + "     " + con.getResources().getString(R.string.time) + " " + time[position]);
        viewholder.TVname.setText(name[position]);
        viewholder.TVstartName.setText(startName[position]);
        viewholder.TVdestName.setText(destName[position]);
        viewholder.TVcost.setText(con.getResources().getString(R.string.cost_per_seat) +" "+cost[position]);

        Glide.with(con).load(ServerURL.load_image+image[position]).into(viewholder.imageView);


        if (seats_booked[position].equals("00")) {
            viewholder.TVseats.setText(con.getResources().getString(R.string.seats_no) +" " + "0"  + " / "    + seats[position]);
        } else {
            viewholder.TVseats.setText(con.getResources().getString(R.string.seats_no) +" " + seats_booked[position] + " / " + seats[position] );
        }


        viewholder.btnRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 1);
            }
        });


        viewholder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 2);
            }
        });


//        viewholder.btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ((ListView) parent).performItemClick(v, position, 3);
//            }
//        });


        viewholder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 4);
            }
        });


        return convertView;
    }


}

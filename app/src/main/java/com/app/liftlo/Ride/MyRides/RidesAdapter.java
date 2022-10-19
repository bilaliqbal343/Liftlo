package com.app.liftlo.Ride.MyRides;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.liftlo.R;
import com.app.liftlo.utils.ServerURL;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoStartCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bilal on 17-Feb-17.
 */

public class RidesAdapter extends BaseAdapter {


    Activity con;
    String[] status, date, time, driver_name, driver_number, car_number,car_color
            ,car_model, start_lat, start_lan, start_name, dest_lat, dest_lan, dest_name
            ,ac, music, smoking, booked_seats, cost, driver_image, rating, request_status, ride_started;


    public RidesAdapter(Activity con, String[] status, String[] date, String[] time, String[] driver_name, String[] driver_number, String[] car_number, String[] car_color, String[] car_model, String[] start_lat, String[] start_lan, String[] start_name, String[] dest_lat, String[] dest_lan, String[] dest_name, String[] ac, String[] music, String[] smoking, String[] booked_seats, String[] cost, String[] driver_image, String[] rating, String[] request_status, String[] ride_started) {
        this.con = con;
        this.status = status;
        this.date = date;
        this.time = time;
        this.driver_name = driver_name;
        this.driver_number = driver_number;
        this.car_number = car_number;
        this.car_color = car_color;
        this.car_model = car_model;
        this.start_lat = start_lat;
        this.start_lan = start_lan;
        this.start_name = start_name;
        this.dest_lat = dest_lat;
        this.dest_lan = dest_lan;
        this.dest_name = dest_name;
        this.ac = ac;
        this.music = music;
        this.smoking = smoking;
        this.booked_seats = booked_seats;
        this.cost = cost;
        this.driver_image = driver_image;
        this.rating = rating;
        this.request_status = request_status;
        this.ride_started = ride_started;
    }

    @Override
    public int getCount() {
        return driver_name.length;
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

        TextView  TVdate, TVname, TVnumber, TVstatus, TVstartName, TVdestName, TVseats, TVcost
                , TVmodel, TVac, TVmusic, TVsmoking;
        Button track, cancel;
        ImageButton  map;
        ZegoStartCallInvitationButton call;
        RatingBar ratingBar;
        CircleImageView imageView;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.items_customer_rides, null);

            viewholder.TVnumber = (TextView) convertView.findViewById(R.id.number);
            viewholder.TVstatus = (TextView) convertView.findViewById(R.id.status);
            viewholder.TVdate = (TextView) convertView.findViewById(R.id.time);
            viewholder.TVname = (TextView) convertView.findViewById(R.id.name);
            viewholder.TVstartName = (TextView) convertView.findViewById(R.id.start_loc);
            viewholder.TVdestName = (TextView) convertView.findViewById(R.id.end_loc);
            viewholder.TVseats = (TextView) convertView.findViewById(R.id.seat);
            viewholder.TVcost = (TextView) convertView.findViewById(R.id.cost);
            viewholder.track = (Button) convertView.findViewById(R.id.btn);
            viewholder.cancel = (Button) convertView.findViewById(R.id.cancel);
            viewholder.call = (ZegoStartCallInvitationButton) convertView.findViewById(R.id.call);
            viewholder.map = (ImageButton) convertView.findViewById(R.id.map);
            viewholder.TVmodel = (TextView) convertView.findViewById(R.id.car);
            viewholder.TVac = (TextView) convertView.findViewById(R.id.ac);
            viewholder.TVmusic = (TextView) convertView.findViewById(R.id.music);
            viewholder.TVsmoking = (TextView) convertView.findViewById(R.id.smoke);
            viewholder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            viewholder.imageView = (CircleImageView) convertView.findViewById(R.id.profile_image);


            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }


        if (status[position].equals("00")){
            viewholder.TVstatus.setTextColor(Color.parseColor("#E91E63"));
            viewholder.TVstatus.setText(con.getResources().getString(R.string.rejected));
            viewholder.track.setVisibility(View.GONE);
        }
        else if (status[position].equals("1")){
            viewholder.TVstatus.setTextColor(Color.parseColor("#FF9800"));
            viewholder.TVstatus.setText(con.getResources().getString(R.string.pending));
            viewholder.track.setVisibility(View.GONE);
        }
        else if (status[position].equals("2")){
            viewholder.TVstatus.setTextColor(Color.parseColor("#25C777"));
            viewholder.TVstatus.setText(con.getResources().getString(R.string.accepted));
            viewholder.track.setVisibility(View.VISIBLE);

            if(ride_started[position].equals("1")) {
                viewholder.cancel.setVisibility(View.GONE);
            }
        }


        Glide.with(con).load(ServerURL.load_image+driver_image[position]).into(viewholder.imageView);


        viewholder.ratingBar.setIsIndicator(false);
        viewholder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        if (rating[position].equals("null")){
            viewholder.ratingBar.setRating(Float.parseFloat("5.0"));
        }else {
            viewholder.ratingBar.setRating(Float.parseFloat(rating[position]));
        }

        viewholder.TVdate.setText(con.getResources().getString(R.string.date) + " " + date[position] + "     " + con.getResources().getString(R.string.time) + " " + time[position]);
        viewholder.TVname.setText(driver_name[position]);
        viewholder.TVnumber.setText("+"+driver_number[position]);
        viewholder.TVstartName.setText(start_name[position]);
        viewholder.TVdestName.setText(dest_name[position]);
        viewholder.TVseats.setText(con.getResources().getString(R.string.seats_booked) +" "+booked_seats[position]);
        viewholder.TVcost.setText(con.getResources().getString(R.string.total_cost) +" "+cost[position]);
        viewholder.TVmodel.setText(car_model[position] +" | "+ car_color[position]
                +" | "+ car_number[position]);

        if (music[position].equals("yes")){

            viewholder.TVmusic.setTextColor(Color.parseColor("#25C777"));
        }
        if (smoking[position].equals("yes")){

            viewholder.TVsmoking.setTextColor(Color.parseColor("#25C777"));
        }
        if (ac[position].equals("yes")){

            viewholder.TVac.setTextColor(Color.parseColor("#25C777"));
        }




        viewholder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number=driver_number[position].replaceAll("92","");
                viewholder.call.setInvitees(Collections.singletonList(new ZegoUIKitUser(phone_number)));
                ((ListView) parent).performItemClick(v, position, 1);
            }
        });



        viewholder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 2);
            }
        });



        viewholder.track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 3);
            }
        });



        viewholder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 4);
            }
        });



        return convertView;
    }



}

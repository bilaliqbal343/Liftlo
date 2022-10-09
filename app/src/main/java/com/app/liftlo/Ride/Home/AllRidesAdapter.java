package com.app.liftlo.Ride.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.app.liftlo.R;
import com.app.liftlo.utils.ServerURL;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bilal on 17-Feb-17.
 */

public class AllRidesAdapter extends BaseAdapter implements Filterable {


    Activity con;
    String user_id;
    String[]
    booked_seats, status,
            date, time, driver_name, driver_number, startName, destName, seats, cost
            ,car_model, car_color, ac, music, smoking, rating, driver_image;
    private ArrayList<Filter_model> mOriginalValues; // Original Values
    private ArrayList<Filter_model> mDisplayedValues;    // Values to be displayed


    public AllRidesAdapter(Activity con, ArrayList<Filter_model> mProductArrayList) {
        this.con = con;
        this.mOriginalValues = mProductArrayList;
        this.mDisplayedValues = mProductArrayList;
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

        TextView  TVdate, TVname, TVnumber, TVstatus, TVstartName, TVdestName,
                TVseats, TVcost, TVcar, TVac, TVmusic, TVsmoke;
        Button btnAccept;
        ImageButton call, map;
        RatingBar ratingBar;
        CircleImageView imageView;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;
        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.items_all_rides, null);

            viewholder.TVnumber = (TextView) convertView.findViewById(R.id.number);
            viewholder.TVstatus = (TextView) convertView.findViewById(R.id.status);
            viewholder.TVdate = (TextView) convertView.findViewById(R.id.time);
            viewholder.TVname = (TextView) convertView.findViewById(R.id.name);
            viewholder.TVstartName = (TextView) convertView.findViewById(R.id.start_loc);
            viewholder.TVdestName = (TextView) convertView.findViewById(R.id.end_loc);
            viewholder.TVseats = (TextView) convertView.findViewById(R.id.seat);
            viewholder.TVcost = (TextView) convertView.findViewById(R.id.cost);
            viewholder.btnAccept = (Button) convertView.findViewById(R.id.btn);
            viewholder.call = (ImageButton) convertView.findViewById(R.id.call);
            viewholder.map = (ImageButton) convertView.findViewById(R.id.map);
            viewholder.TVac = (TextView) convertView.findViewById(R.id.ac);
            viewholder.TVcar = (TextView) convertView.findViewById(R.id.car);
            viewholder.TVmusic = (TextView) convertView.findViewById(R.id.music);
            viewholder.TVsmoke = (TextView) convertView.findViewById(R.id.smoke);
            viewholder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            viewholder.imageView = (CircleImageView) convertView.findViewById(R.id.profile_image);


            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }

//        if (id[position].equals(user_id)){
//
//            viewholder.TVstatus.setTextColor(Color.parseColor("#25C777"));
//            viewholder.TVstatus.setText("Ride Requested");
//
//            viewholder.btnAccept.setVisibility(View.GONE);
//        }
//        else {
//            viewholder.TVstatus.setVisibility(View.GONE);
//        }


        viewholder.ratingBar.setIsIndicator(true);
        viewholder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });

        if (mDisplayedValues.get(position).rating.equals("null")){
            viewholder.ratingBar.setRating(Float.parseFloat("0.0"));
        }else {
            viewholder.ratingBar.setRating(Float.parseFloat(mDisplayedValues.get(position).rating));
        }

        viewholder.TVdate.setText(con.getResources().getString(R.string.date) + " " + mDisplayedValues.get(position).date + "     " + con.getResources().getString(R.string.time) + " " + mDisplayedValues.get(position).time);
        viewholder.TVname.setText(mDisplayedValues.get(position).driver_name);
        viewholder.TVnumber.setText("+"+mDisplayedValues.get(position).driver_number);
        viewholder.TVstartName.setText(mDisplayedValues.get(position).start_name);
        viewholder.TVdestName.setText(mDisplayedValues.get(position).dest_name);

        Glide.with(con).load(ServerURL.load_image+mDisplayedValues.get(position).driver_image).into(viewholder.imageView);


        if (mDisplayedValues.get(position).booked_seats.equals("00")){
            viewholder.TVseats.setText(con.getResources().getString(R.string.seats_no) +": 0"+ " / "+ mDisplayedValues.get(position).seat_no);
        }
        else{
            viewholder.TVseats.setText(con.getResources().getString(R.string.seats_no) +": " + mDisplayedValues.get(position).booked_seats + " / " + mDisplayedValues.get(position).seat_no);
        }
        viewholder.TVcost.setText(con.getResources().getString(R.string.cost_per_seat) +": "+mDisplayedValues.get(position).seat_cost);
        viewholder.TVcar.setText(mDisplayedValues.get(position).car_name +"  |  "+ mDisplayedValues.get(position).car_color);


        if (mDisplayedValues.get(position).music.equals("yes")){

            viewholder.TVmusic.setTextColor(Color.parseColor("#25C777"));
        }
        if (mDisplayedValues.get(position).smoking.equals("yes")){

            viewholder.TVsmoke.setTextColor(Color.parseColor("#25C777"));
        }
        if (mDisplayedValues.get(position).ac.equals("yes")){

            viewholder.TVac.setTextColor(Color.parseColor("#25C777"));
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




        return convertView;
    }





    //filter arraylist
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<Filter_model>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Filter_model> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Filter_model>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;

                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getStart_name();
                        if (data.toLowerCase().contains(constraint.toString())) {

                            FilteredArrList.add(new Filter_model(mOriginalValues.get(i).booked_seats
                                    , mOriginalValues.get(i).status
                                    , mOriginalValues.get(i).date
                                    ,mOriginalValues.get(i).time
                                    ,mOriginalValues.get(i).driver_name
                                    ,mOriginalValues.get(i).driver_number
                                    ,mOriginalValues.get(i).start_name
                                    ,mOriginalValues.get(i).dest_name
                                    ,mOriginalValues.get(i).seat_no
                                    ,mOriginalValues.get(i).seat_cost
                                    ,mOriginalValues.get(i).car_name
                                    ,mOriginalValues.get(i).car_color
                                    ,mOriginalValues.get(i).ac
                                    ,mOriginalValues.get(i).music
                                    ,mOriginalValues.get(i).smoking
                                    ,mOriginalValues.get(i).rating
                                    ,mOriginalValues.get(i).driver_image) );


                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }






    public Filter getFilter2() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<Filter_model>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Filter_model> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Filter_model>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;

                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data2 = mOriginalValues.get(i).getDest_name();
                        if (data2.toLowerCase().contains(constraint.toString())) {

                            FilteredArrList.add(new Filter_model(mOriginalValues.get(i).booked_seats
                                    , mOriginalValues.get(i).status
                                    , mOriginalValues.get(i).date
                                    ,mOriginalValues.get(i).time
                                    ,mOriginalValues.get(i).driver_name
                                    ,mOriginalValues.get(i).driver_number
                                    ,mOriginalValues.get(i).start_name
                                    ,mOriginalValues.get(i).dest_name
                                    ,mOriginalValues.get(i).seat_no
                                    ,mOriginalValues.get(i).seat_cost
                                    ,mOriginalValues.get(i).car_name
                                    ,mOriginalValues.get(i).car_color
                                    ,mOriginalValues.get(i).ac
                                    ,mOriginalValues.get(i).music
                                    ,mOriginalValues.get(i).smoking
                                    ,mOriginalValues.get(i).rating
                                    ,mOriginalValues.get(i).driver_image) );


                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
    public ArrayList<Filter_model> getFilter3(String source, String destination) {
                ArrayList<Filter_model> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Filter_model>(mDisplayedValues); // saves the original data in mOriginalValues
                }


                  /*  // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;*/
        int size=mOriginalValues.size();
                    for (int i = 0; i < size; i++) {
                        String data2 = mOriginalValues.get(i).getDest_name();
                        String data1 = mOriginalValues.get(i).getStart_name();
                        if ((data1.toLowerCase().trim().contains(source.toLowerCase().trim()))&&(data2.toLowerCase().contains(destination.toLowerCase().trim()))) {
                            FilteredArrList.add(new Filter_model(mOriginalValues.get(i).booked_seats
                                    , mOriginalValues.get(i).status
                                    , mOriginalValues.get(i).date
                                    ,mOriginalValues.get(i).time
                                    ,mOriginalValues.get(i).driver_name
                                    ,mOriginalValues.get(i).driver_number
                                    ,mOriginalValues.get(i).start_name
                                    ,mOriginalValues.get(i).dest_name
                                    ,mOriginalValues.get(i).seat_no
                                    ,mOriginalValues.get(i).seat_cost
                                    ,mOriginalValues.get(i).car_name
                                    ,mOriginalValues.get(i).car_color
                                    ,mOriginalValues.get(i).ac
                                    ,mOriginalValues.get(i).music
                                    ,mOriginalValues.get(i).smoking
                                    ,mOriginalValues.get(i).rating
                                    ,mOriginalValues.get(i).driver_image) );


                        }
                    }
                    if (FilteredArrList.size()<=0)
                    {
                        Toast.makeText(con, "No rides available ", Toast.LENGTH_SHORT).show();
                    }

                    // set the Filtered result to return


        return FilteredArrList;
    }
    public ArrayList<Filter_model> emptyArray() {
        ArrayList<Filter_model> FilteredArrList = new ArrayList<>();

        if (mOriginalValues == null) {
            mOriginalValues = new ArrayList<Filter_model>(mDisplayedValues); // saves the original data in mOriginalValues
        }
        return FilteredArrList;
    }


}

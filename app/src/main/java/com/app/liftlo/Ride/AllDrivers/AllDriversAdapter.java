package com.app.liftlo.Ride.AllDrivers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.liftlo.R;
import com.app.liftlo.utils.ServerURL;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bilal on 17-Feb-17.
 */

public class AllDriversAdapter extends BaseAdapter implements Filterable {


    Activity con;
    String user_id;
    String[]
    booked_seats, status,
            date, time, driver_name, driver_number, startName, destName, seats, cost
            ,car_model, car_color, ac, music, smoking, rating, driver_image;
    private ArrayList<DriverFilter_model> mOriginalValues; // Original Values
    private ArrayList<DriverFilter_model> mDisplayedValues;    // Values to be displayed


    public AllDriversAdapter(Activity con, ArrayList<DriverFilter_model> mProductArrayList) {
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
            convertView = inflater.inflate(R.layout.items_all_drivers, null);

            viewholder.TVnumber = (TextView) convertView.findViewById(R.id.number);
            viewholder.TVname = (TextView) convertView.findViewById(R.id.name);
            viewholder.TVcar = (TextView) convertView.findViewById(R.id.car);
            viewholder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            viewholder.imageView = (CircleImageView) convertView.findViewById(R.id.profile_image);


            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }




        viewholder.ratingBar.setIsIndicator(false);
        viewholder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });

        if (mDisplayedValues.get(position).rating.equals("null")){
            viewholder.ratingBar.setRating(Float.parseFloat("5.0"));
        }else {
            viewholder.ratingBar.setRating(Float.parseFloat(mDisplayedValues.get(position).rating));
        }

        viewholder.TVname.setText(mDisplayedValues.get(position).driver_name);
        viewholder.TVnumber.setText("+"+mDisplayedValues.get(position).driver_number);

        Glide.with(con).load(ServerURL.load_image+mDisplayedValues.get(position).driver_image).into(viewholder.imageView);


        viewholder.TVcar.setText(mDisplayedValues.get(position).car_name +"  |  "+ mDisplayedValues.get(position).car_color);



        return convertView;
    }





    //filter arraylist
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<DriverFilter_model>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<DriverFilter_model> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<DriverFilter_model>(mDisplayedValues); // saves the original data in mOriginalValues
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
                        String data = mOriginalValues.get(i).getDriver_name();
                        if (data.toLowerCase().startsWith(constraint.toString())) {

                            FilteredArrList.add(new DriverFilter_model(mOriginalValues.get(i).driver_name
                                    ,mOriginalValues.get(i).driver_number
                                    ,mOriginalValues.get(i).car_name
                                    ,mOriginalValues.get(i).car_color
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




}

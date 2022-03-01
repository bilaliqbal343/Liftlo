package com.app.liftlo.Driver.ManageRides;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.liftlo.R;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;


public class FragmentRidesRequests extends Fragment {

    View v;
    ListView listView;
    SharedPreferences sharedPreferences;
    String driver_id, share_ride_id, clicked_id, clicked_ride_id, clicked_status, total_seats, booked,
            clicked_seat, reject="no";
    int seatCount;
    JSONObject jp_obj;
    JSONArray jar_array;
    String[] ride_name, ride_number, status, date, time, start_lat,
            start_lan,
            start_name,
            dest_lat,
            dest_lan,
            dest_name, seat_no, seat_cost, table_id, ride_id, r_image;
    Boolean server_check = false;
    RelativeLayout relativeLayout;
    RotateLoading rotateLoading;
    int remaining;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_manage_rides, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            share_ride_id = bundle.getString("share_ride_id");
            total_seats = bundle.getString("total_seats");
            booked = bundle.getString("booked");

            remaining = Integer.parseInt(total_seats) - Integer.parseInt(booked);
        }

        init();

        return v;
    }


    public void init() {


        sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        driver_id = sharedPreferences.getString("id", "No value");

        listView = v.findViewById(R.id.listview);
        relativeLayout = v.findViewById(R.id.r);
        rotateLoading = v.findViewById(R.id.rotateloading);


        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        relativeLayout.setVisibility(View.VISIBLE);
        rotateLoading.start();

        new GetRideRequests().execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listview_click(id, position);
            }
        });


    }


    //listView.setOnItemClickListener handled here///////////////////////////
    public void listview_click(long id, int position) {

        reject = "no";

        if (id == 1) {//call
            make_call(ride_number[position]);
        } else if (id == 2) {//map

            Fragment fragment = new SeeLocation();

            Bundle bundle = new Bundle();
            bundle.putString("Startlat", start_lat[position]);
            bundle.putString("Startlon", start_lan[position]);
            bundle.putString("StartName", start_name[position]);
            bundle.putString("Destlat", dest_lat[position]);
            bundle.putString("Destlon", dest_lan[position]);
            bundle.putString("Destname", dest_name[position]);
            fragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                    addToBackStack("tag").commit();
        } else if (id == 3) {//accept

            if (remaining < 1){
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.limit_reached_cant_book_more), Toast.LENGTH_SHORT).show();
            }
            else if (Integer.parseInt(seat_no[position]) > remaining){
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.only) +remaining+ getActivity().getResources().getString(R.string.seats_left), Toast.LENGTH_SHORT).show();
            }
            else {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                relativeLayout.setVisibility(View.VISIBLE);
                rotateLoading.start();
                clicked_seat   = seat_no[position];
                seatCount = Integer.parseInt(clicked_seat) + Integer.parseInt(booked);
                clicked_id     = table_id[position];
                clicked_status = "2";
                clicked_ride_id = ride_id[position];
                new UpdateRideRequest().execute();
            }
        } else if (id == 4) {//reject

            reject = "yes";
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            relativeLayout.setVisibility(View.VISIBLE);
            rotateLoading.start();
            clicked_id     = table_id[position];
            clicked_status = "00";
            clicked_ride_id = ride_id[position];
            new UpdateRideRequest().execute();
        }
    }


    String server_response = "0", server_response_text;

    //ASYNTASK Getting Data From Server/////////////////////////////////////
    public class GetRideRequests extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "driver_requests");
                obj.put("driver_id", driver_id);
                obj.put("share_ride_id", share_ride_id);


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                ride_id = new String[(jar_array.length() - 1)];
                ride_name = new String[(jar_array.length() - 1)];
                ride_number = new String[(jar_array.length() - 1)];
                date = new String[(jar_array.length() - 1)];
                time = new String[(jar_array.length() - 1)];
                start_lat = new String[(jar_array.length() - 1)];
                start_lan = new String[(jar_array.length() - 1)];
                start_name = new String[(jar_array.length() - 1)];
                dest_lat = new String[(jar_array.length() - 1)];
                dest_lan = new String[(jar_array.length() - 1)];
                dest_name = new String[(jar_array.length() - 1)];
                seat_no = new String[(jar_array.length() - 1)];
                seat_cost = new String[(jar_array.length() - 1)];
                status = new String[(jar_array.length() - 1)];
                table_id = new String[(jar_array.length() - 1)];
                r_image = new String[(jar_array.length() - 1)];


                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }

                int j = 1;
                Log.e("length", jar_array.length() + "");

                if (server_response.equals("1")) {
                    for (int i = 0; j < jar_array.length(); i++) {

                        c = jar_array.getJSONObject(j);

                        if (c.length() > 0) {


                            status[i] = c.getString("status");
                            ride_id[i] = c.getString("ride_id");
                            ride_name[i] = c.getString("ride_name");
                            ride_number[i] = c.getString("ride_number");
                            date[i] = c.getString("date");
                            time[i] = c.getString("time");
                            start_lat[i] = c.getString("start_lat");
                            start_lan[i] = c.getString("start_lan");
                            start_name[i] = c.getString("start_name");
                            dest_lat[i] = c.getString("dest_lat");
                            dest_lan[i] = c.getString("dest_lan");
                            dest_name[i] = c.getString("dest_name");
                            seat_no[i] = c.getString("booked_seats");
                            seat_cost[i] = c.getString("cost");
                            table_id[i] = c.getString("id");
                            r_image[i] = c.getString("ride_image");


                        }

                        j++;
                    }
                }


                server_check = true;


            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {


            rotateLoading.stop();
            relativeLayout.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            if (server_check) {


                if (server_response.equals("1")) {


                    if (ride_name.length > 0) {


                        final RideRequestsAdapter adapter = new RideRequestsAdapter(getActivity(),
                                status, ride_name, ride_number, date, time, start_name, dest_name
                                , seat_no, seat_cost, r_image);

                        listView.setAdapter(adapter);


                    } else {
                        Toast.makeText(getActivity(), server_response_text, Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_rides_found), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }



    public class UpdateRideRequest extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "change_status");
                obj.put("id", clicked_id);
                obj.put("status", clicked_status);
                obj.put("ride_id", clicked_ride_id);


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }


                server_check = true;


            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {



            if (server_check) {


                if (server_response.equals("1")) {


                    if (clicked_status.equals("00")) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.request_rejected), Toast.LENGTH_SHORT).show();
                    } else if (clicked_status.equals("2")) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.request_accepted), Toast.LENGTH_SHORT).show();
                    }


                    if (reject.equals("yes")){
                        Log.e("log", "booking canceled");
                        rotateLoading.stop();
                        relativeLayout.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        new GetRideRequests().execute();
                    }
                    else {
                        new UpdateBookedSeats().execute();
                    }


                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }





    public class UpdateBookedSeats extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "update_seat");
                obj.put("id", share_ride_id);
                obj.put("booked_seats", seatCount+"");


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }


                server_check = true;


            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {


            rotateLoading.stop();
            relativeLayout.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new GetRideRequests().execute();

        }
    }




    public void make_call(String number) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            // permission has been granted, continue as usual
//            Toast.makeText(this, "accessed", Toast.LENGTH_SHORT).show();

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));

            startActivity(callIntent);
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getResources().getString(R.string.ride_requests));
    }
}

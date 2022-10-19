package com.app.liftlo.Ride.MyRides;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.liftlo.Driver.ManageRides.SeeLocation;
import com.app.liftlo.R;
import com.app.liftlo.RideTrackLive;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;


public class FragmentMyRides extends Fragment {

    View v;
    SharedPreferences sharedPreferences;
    String id, Sshare_ride_id, SRequestRideId, Sstatus, SseatTotal;
    JSONObject jp_obj;
    JSONArray jar_array;
    String[] status, date, time, driver_name, driver_id, driver_number, car_number
            , car_color, car_model, start_lat, start_lan, start_name, dest_lat
            , dest_lan, dest_name, ac, music, smoking, booked_seats, cost
            , driver_image, rating, request_status, ride_started, share_ride_id
            , RequestRideId, seats_no;
    String server_response, server_response_text, ride_name, Sdriver_name
            ,Sbooked_seats;
    Boolean server_check = false;
    RelativeLayout relativeLayout;
    RotateLoading rotateLoading;
    ListView listView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_my_rides, container, false);

        init();


        return v;
    }


    public void init() {

        sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "No value");
        ride_name = sharedPreferences.getString("name", "No value");

        relativeLayout = v.findViewById(R.id.r);
        rotateLoading = v.findViewById(R.id.rotateloading);
        listView = v.findViewById(R.id.listview);


        if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            relativeLayout.setVisibility(View.VISIBLE);
            rotateLoading.start();

            new GetRideRequests().execute();

        } else {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listview_click(id, position);
            }
        });

    }


    public void listview_click(long id, int position) {

        if (id == 1) {//call
            //make_call(driver_number[position]);
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

            if (ride_started[position].equals("00")) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ride_not_started_yet), Toast.LENGTH_SHORT).show();
            } else {

                Sshare_ride_id = share_ride_id[position];
                //Creating a shared preference
                sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("live", "yes");
                editor.putString("share_ride_id", Sshare_ride_id);
                editor.putString("start_name", start_name[position]);
                editor.putString("dest_name", dest_name[position]);
                editor.putString("start_lat", start_lat[position]);
                editor.putString("dest_lat", dest_lat[position]);
                editor.putString("start_lan", start_lan[position]);
                editor.putString("dest_lan", dest_lan[position]);
                editor.putString("date", date[position]);
                editor.putString("time", time[position]);
                editor.putString("cost", cost[position]);
                editor.putString("driver_name", driver_name[position]);
                editor.putString("seats", booked_seats[position]);
                editor.putString("driver_image", driver_image[position]);
                editor.putString("driver_id", driver_id[position]);
                editor.apply();

                Fragment fragment = new RideTrackLive();

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                        addToBackStack("tag").commit();


            }
        }
        else if (id == 4){

            Sdriver_name = driver_name[position];
            SseatTotal = seats_no[position];
            Sbooked_seats = booked_seats[position];
            Sshare_ride_id = share_ride_id[position];
            SRequestRideId = RequestRideId[position];
            Sstatus = status[position];

            CancelRideDialog();

            Log.e("data1", Sdriver_name+"/"+Sbooked_seats+"/"+Sshare_ride_id
            +"/"+ride_name+"/"+SRequestRideId+"/"+Sstatus+"/"+SseatTotal);

        }

    }


    //ASYNTASK Getting Data From Server/////////////////////////////////////
    public class GetRideRequests extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "ride_requests");
                obj.put("ride_id", id);


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                seats_no = new String[(jar_array.length() - 1)];
                RequestRideId = new String[(jar_array.length() - 1)];
                status = new String[(jar_array.length() - 1)];
                date = new String[(jar_array.length() - 1)];
                time = new String[(jar_array.length() - 1)];
                start_lat = new String[(jar_array.length() - 1)];
                start_lan = new String[(jar_array.length() - 1)];
                start_name = new String[(jar_array.length() - 1)];
                dest_lat = new String[(jar_array.length() - 1)];
                dest_lan = new String[(jar_array.length() - 1)];
                dest_name = new String[(jar_array.length() - 1)];
                driver_name = new String[(jar_array.length() - 1)];
                driver_id = new String[(jar_array.length() - 1)];
                driver_number = new String[(jar_array.length() - 1)];
                car_color = new String[(jar_array.length() - 1)];
                car_number = new String[(jar_array.length() - 1)];
                car_model = new String[(jar_array.length() - 1)];
                ac = new String[(jar_array.length() - 1)];
                music = new String[(jar_array.length() - 1)];
                smoking = new String[(jar_array.length() - 1)];
                booked_seats = new String[(jar_array.length() - 1)];
                cost = new String[(jar_array.length() - 1)];
                driver_image = new String[(jar_array.length() - 1)];
                rating = new String[(jar_array.length() - 1)];
                request_status = new String[(jar_array.length() - 1)];
                ride_started = new String[(jar_array.length() - 1)];
                share_ride_id = new String[(jar_array.length() - 1)];


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

                            seats_no[i] = c.getString("seat_no");
                            RequestRideId[i] = c.getString("id");
                            status[i] = c.getString("status");
                            driver_id[i] = c.getString("driver_id");
                            driver_name[i] = c.getString("driver_name");
                            driver_number[i] = c.getString("driver_number");
                            date[i] = c.getString("date");
                            time[i] = c.getString("time");
                            start_lat[i] = c.getString("start_lat");
                            start_lan[i] = c.getString("start_lan");
                            start_name[i] = c.getString("start_name");
                            dest_lat[i] = c.getString("dest_lat");
                            dest_lan[i] = c.getString("dest_lan");
                            dest_name[i] = c.getString("dest_name");
                            car_number[i] = c.getString("car_number");
                            car_color[i] = c.getString("car_color");
                            car_model[i] = c.getString("car_model");
                            ac[i] = c.getString("ac");
                            music[i] = c.getString("music");
                            smoking[i] = c.getString("smoking");
                            booked_seats[i] = c.getString("booked_seats");
                            cost[i] = c.getString("cost");
                            driver_image[i] = c.getString("driver_image");
                            rating[i] = c.getString("rating");
                            request_status[i] = c.getString("request_status");
                            ride_started[i] = c.getString("ride_started");
                            share_ride_id[i] = c.getString("share_ride_id");


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


                    if (driver_name.length > 0) {


                        final RidesAdapter adapter = new RidesAdapter(getActivity(),
                                status, date, time, driver_name, driver_number, car_number
                                , car_color, car_model, start_lat, start_lan, start_name
                                , dest_lat, dest_lan, dest_name, ac, music, smoking
                                , booked_seats, cost, driver_image, rating
                                , request_status, ride_started);

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




    int seats, tseats;
    public class CancelRide extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "cancel_ride");

                if (Sstatus.equals("1")){
                    obj.put("booked_seats", "00");
                }else {
//                    seats = Integer.parseInt(SseatTotal) - Integer.parseInt(Sbooked_seats);
//                    tseats = Integer.parseInt(Sbooked_seats) + seats;
                    obj.put("booked_seats", Sbooked_seats);
                }

                obj.put("ride_name", ride_name);
                obj.put("driver_id", Sdriver_name);
                obj.put("status", "3");
                obj.put("share_ride_id", Sshare_ride_id);
                obj.put("id", SRequestRideId);

                Log.e("data", seats+""+"/"+ride_name+"/"+Sdriver_name+"/"+
                        Sshare_ride_id+"/"+SRequestRideId+"/"+SseatTotal
                        +""+"/"+Sbooked_seats+"/"+tseats);



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


                    rotateLoading.stop();
                    relativeLayout.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.request_successful), Toast.LENGTH_SHORT).show();

                    //remove all fragments from backstack
                    Fragment fragment;
                    FragmentManager fragmentManager;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                        fragmentManager.popBackStack();
                    }
                    //load fragment again
                    fragment = new FragmentMyRides();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();


                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }



    private void CancelRideDialog() {

        //Creating an alert dialog to confirm logout

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(getActivity().getResources().getString(R.string.cancel));
        alertDialogBuilder.setIcon(R.drawable.logo_black);
        alertDialogBuilder.setMessage(getActivity().getResources().getString(R.string.are_you_sure_you_want_to_cancel_ride));
        alertDialogBuilder.setPositiveButton(getActivity().getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        rotateLoading.start();
                        new CancelRide().execute();


                    }
                });

        alertDialogBuilder.setNegativeButton(getActivity().getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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
        getActivity().setTitle(getActivity().getResources().getString(R.string.my_rides));
    }
}

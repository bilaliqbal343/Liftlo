package com.app.liftlo.Driver.MyRides;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
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

import com.app.liftlo.DriverTrackLive;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.app.liftlo.Driver.ManageRides.FragmentRidesRequests;
import com.app.liftlo.R;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;


public class FragmentMyRides extends Fragment {

    View v;
    ListView listView;
    SharedPreferences sharedPreferences;
    String driver_id, clicked_id, StartName, StartLat, StartLan, DestLat, DestLan, DestName
            ,cost, seats, Sdate, Stime;
    JSONObject jp_obj;
    JSONArray jar_array;
    String[] driver_name, driver_number, date, time, start_lat,
            start_lan,
            start_name,
            dest_lat,
            dest_lan,
            dest_name, seat_no, seat_cost, image, share_ride_id, seats_booked;
    Boolean server_check = false;
    RelativeLayout relativeLayout; RotateLoading rotateLoading;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_my_rides, container, false);

        init();

        return v;
    }


    public void init() {


        sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        driver_id = sharedPreferences.getString("id", "No value");

        listView = v.findViewById(R.id.listview);
        relativeLayout = v.findViewById(R.id.r);
        rotateLoading = v.findViewById(R.id.rotateloading);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listview_click(id,position);
            }
        });


        if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            relativeLayout.setVisibility(View.VISIBLE);
            rotateLoading.start();

            new GetMyRides().execute();

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.check_internet_connection)
                    , Toast.LENGTH_LONG).show();
        }


    }




    //listView.setOnItemClickListener handled here///////////////////////////
    public void listview_click(long id,int position) {

        if (id == 1) {

            Fragment fragment = new FragmentRidesRequests();
            Bundle bundle = new Bundle();
            bundle.putString("share_ride_id", share_ride_id[position]);
            bundle.putString("booked", seats_booked[position]);
            bundle.putString("total_seats", seat_no[position]);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                    addToBackStack("tag").commit();
        }
        else if (id == 2){

            clicked_id = share_ride_id[position];
            StartLat = start_lat[position];
            StartLan = start_lan[position];
            StartName = start_name[position];
            DestLat = dest_lat[position];
            DestLan = dest_lan[position];
            DestName = dest_name[position];
            Sdate = date[position];
            Stime = time[position];
            cost = seat_cost[position];
            seats = seats_booked[position];


            if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                relativeLayout.setVisibility(View.VISIBLE);
                rotateLoading.start();
                new SendLiveLoc().execute();

            } else {
                Toast.makeText(getActivity(),
                        getActivity().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
            }

        }
        else if (id == 4){

            clicked_id = share_ride_id[position];
            CancelRideDialog();
        }


    }






    String server_response = "0", server_response_text;

    //ASYNTASK Getting Data From Server/////////////////////////////////////
    public class GetMyRides extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "driver_rides");

                obj.put("driver_id", driver_id);


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                driver_name = new String[(jar_array.length() - 1)];
                driver_number = new String[(jar_array.length() - 1)];
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
                image = new String[(jar_array.length() - 1)];
                seats_booked = new String[(jar_array.length() - 1)];
                share_ride_id = new String[(jar_array.length() - 1)];


                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }

                int j = 1;

                if (server_response.equals("1")) {
                    for (int i = 0; j < jar_array.length(); i++) {

                        c = jar_array.getJSONObject(j);

                        if (c.length() > 0) {


                            image[i] = c.getString("driver_image");
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
                            seat_no[i] = c.getString("seat_no");
                            seat_cost[i] = c.getString("seat_cost");
                            share_ride_id[i] = c.getString("id");
                            seats_booked[i] = c.getString("booked_seats");

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

                        final MyRidesAdapter adapter = new MyRidesAdapter(getActivity(),
                                image, driver_name, date, time, start_name, dest_name
                                , seat_no, seats_booked, seat_cost);

                        listView.setAdapter(adapter);


                    } else {
                        Toast.makeText(getActivity(), server_response_text, Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_rides_found), Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }





    public class StartRide extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "start_ride");
                obj.put("id", clicked_id);
                obj.put("request_status", "1");


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


                    //Creating a shared preference
                    sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("live", "yes");
                    editor.putString("share_ride_id", clicked_id);
                    editor.putString("start_name", StartName);
                    editor.putString("dest_name", DestName);
                    editor.putString("start_lat", StartLat);
                    editor.putString("dest_lat", DestLat);
                    editor.putString("start_lan", StartLan);
                    editor.putString("dest_lan", DestLan);
                    editor.putString("date", Sdate);
                    editor.putString("time", Stime);
                    editor.putString("cost", cost);
                    editor.putString("seats", seats);
                    editor.apply();

                    Fragment fragment = new DriverTrackLive();

                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                            addToBackStack("tag").commit();


                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }





    public class SendLiveLoc extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "live_loc");
                obj.put("id", clicked_id);
                obj.put("live_lat", StartLat);
                obj.put("live_lan", StartLan);


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

                    new StartRide().execute();


                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }





    public class CancelRide extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "delete_ride");
                obj.put("id", clicked_id);
                obj.put("status", "3");
                obj.put("driver_name", driver_name[0]);


                Log.e("daata", clicked_id+"/"+driver_name[0]);

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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getResources().getString(R.string.my_rides));
    }
}

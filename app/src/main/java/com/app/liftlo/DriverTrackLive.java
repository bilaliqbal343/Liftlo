package com.app.liftlo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.app.liftlo.Driver.History.FragmentHistory;
import com.app.liftlo.utils.GPSTracker;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


public class DriverTrackLive extends Fragment implements OnMapReadyCallback {

    View v;
    String StartingLat, StartingLan, DestinationLat, DestinationLan, DestName, StartingName
            ,share_ride_id, driver_id, driver_name, date, time, seats, cost;
    GoogleMap mMap;
    Button endRIde;
    Marker startMarker, destMarker;
    Handler handler = new Handler();
    Bitmap smallMarker, smallMarker1;
    BitmapDrawable bitmapdraw, bitmapdraw1;
    Bitmap b, b1;
    double dblLat, dblLon;
    RotateLoading rotateLoading;

    Timer timer, timer2;
    SharedPreferences sharedPreferences;

    private TimerTask timerTask, timerTask2;
    Boolean server_check = false;
    JSONObject jp_obj;
    JSONArray jar_array;
    String server_response = "0", server_response_text;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_live, container, false);


        endRIde = v.findViewById(R.id.end);
        rotateLoading = v.findViewById(R.id.rotateloading);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);

        sharedPreferences.getString("live", "");
        StartingName = sharedPreferences.getString("start_name", "");
        DestName = sharedPreferences.getString("dest_name", "");
        StartingLat = sharedPreferences.getString("start_lat", "");
        DestinationLat = sharedPreferences.getString("dest_lat", "");
        StartingLan = sharedPreferences.getString("start_lan", "");
        DestinationLan = sharedPreferences.getString("dest_lan", "");
        share_ride_id = sharedPreferences.getString("share_ride_id", "");
        driver_id = sharedPreferences.getString("id", "");
        driver_name = sharedPreferences.getString("name", "");
        date = sharedPreferences.getString("date", "");
        time = sharedPreferences.getString("time", "");
        seats = sharedPreferences.getString("seats", "");
        cost = sharedPreferences.getString("cost", "");




        endRIde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating a shared preference
                sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("live", "no");
                editor.putString("start_name", "");
                editor.putString("dest_name", "");
                editor.putString("start_lat", "");
                editor.putString("dest_lat", "");
                editor.putString("start_lan", "");
                editor.putString("dest_lan", "");
                editor.putString("share_ride_id", "");
                editor.putString("date", "");
                editor.putString("time", "");
                editor.putString("seats", "");
                editor.putString("cost", "");
                editor.apply();
                stopTimer();


                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                rotateLoading.setVisibility(View.VISIBLE);
                rotateLoading.start();

                new EndRide().execute();
                new UploadHistory().execute();

            }

        });





        //change image to bitmap and set as marker
        bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.car_marker);
        b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 100, 130, false);


        //change image to bitmap and set as marker
        bitmapdraw1 = (BitmapDrawable) getResources().getDrawable(R.drawable.flag);
        b1 = bitmapdraw1.getBitmap();
        smallMarker1 = Bitmap.createScaledBitmap(b1, 100, 130, false);


        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_json));
            if (!success) {
                // Handle map style load failure
            }
        } catch (Resources.NotFoundException e) {
            // Oops, looks like the map style resource couldn't be found!
        }





        LatLng latLng = new LatLng(Double.parseDouble(StartingLat), Double.parseDouble(StartingLan));
        startMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(StartingName)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(Double.parseDouble(StartingLat), Double.parseDouble(StartingLan)),
                        12.0f));


        LatLng latLng2 = new LatLng(Double.parseDouble(DestinationLat), Double.parseDouble(DestinationLan));
        destMarker = mMap.addMarker(new MarkerOptions().position(latLng2).title(DestName)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker1)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(Double.parseDouble(DestinationLat), Double.parseDouble(DestinationLan)),
                        11.0f));


    }


    public void getLocationAndMoveCamera() {

        SmartLocation.with(getActivity()).location()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        dblLat = location.getLatitude();
                        dblLon = location.getLongitude();
                    }
                });
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        dblLat = gpsTracker.getLatitude();
        dblLon = gpsTracker.getLongitude();


        //remove marker from map if exists
        if (startMarker != null) {
            startMarker.remove();
        }


        LatLng latLng = new LatLng(dblLat, dblLon);
        startMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));


        //moving camera to marker
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(dblLat, dblLon), 16.5f));




        Log.e("zma Location : ", "" + dblLat + " " + dblLon);
    }


    public void getLocation() {

        SmartLocation.with(getActivity()).location()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        dblLat = location.getLatitude();
                        dblLon = location.getLongitude();
                    }
                });
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        dblLat = gpsTracker.getLatitude();
        dblLon = gpsTracker.getLongitude();


    }


    //show dialog if gps is off
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setMessage(getActivity().getResources().getString(R.string.turn_on_location))
                .setCancelable(true)
                .setPositiveButton(getActivity().getResources().getString(R.string.turn_on), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        builder.create();
        builder.show();
    }





    //To start timer
    private void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run(){
                        //your code is here
                        Log.e("timer", "called");
                        new SendLiveLoc().execute();
                    }
                });
            }
        };
        timer.schedule(timerTask, 2000, 2000);
    }


    //To start timer
    private void startTimer2(){
        timer2 = new Timer();
        timerTask2 = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run(){
                        //your code is here
                        getLocationAndMoveCamera();
                    }
                });
            }
        };
        timer2.schedule(timerTask2, 1000, 1000);
    }



    //To stop timer
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer.purge();
        }


        if(timer2 != null){
            timer2.cancel();
            timer2.purge();
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
                obj.put("id", share_ride_id);
                obj.put("live_lat", dblLat+"");
                obj.put("live_lan", dblLon+"");


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




                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }







    public class EndRide extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "end_ride");
                obj.put("id", share_ride_id);
                obj.put("status", "00");


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


                    new EndRide2().execute();


                } else {
                    rotateLoading.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

            } else {
                rotateLoading.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }







    public class EndRide2 extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "end_ride2");
                obj.put("id", share_ride_id);
                obj.put("request_status", "00");


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





                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }







    public class UploadHistory extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "driver_history");
                obj.put("driver_id", driver_id);
                obj.put("name", driver_name);
                obj.put("time", time);
                obj.put("date", date);
                obj.put("start_name", StartingName);
                obj.put("dest_name", DestName);
                obj.put("seats", seats);
                obj.put("cost", cost);

                Log.e("history_data", driver_id+"/"+ driver_name+"/"+time+"/"+date
                        +"/"+StartingName+"/"+DestName+"/"+seats+"/"+cost);


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


                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ride_ended), Toast.LENGTH_SHORT).show();


                    rotateLoading.stop();
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



                    //remove fragments from backstack
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }


                    Fragment fragment = new FragmentHistory();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                            addToBackStack("tag").commit();


                } else {
                    rotateLoading.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

            } else {
                rotateLoading.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }
    }





    @Override
    public void onResume() {
        super.onResume();

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {

            getLocation();


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    getLocationAndMoveCamera();

                }
            }, 1000);


            //start time and get location
            startTimer();
            startTimer2();


        }


    }

}


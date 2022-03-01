package com.app.liftlo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.liftlo.Ride.History.FragmentRideHistory;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class RideTrackLive extends Fragment implements OnMapReadyCallback {

    View v;
    String StartingLat, StartingLan, DestinationLat, DestinationLan, DestName, StartingName,
            share_ride_id, ride_id, driver_name, date, time, seats, cost, driver_image
            ,driver_id;
    GoogleMap mMap;
    Marker startMarker, destMarker;
    Handler handler = new Handler();
    Bitmap smallMarker, smallMarker1;
    BitmapDrawable bitmapdraw, bitmapdraw1;
    Bitmap b, b1;
    double dblLat, dblLon;
    String[] live_lat, live_lan,status;
    RotateLoading rotateLoading;

    Timer timer;
    SharedPreferences sharedPreferences;

    private TimerTask timerTask;
    Boolean server_check = false;
    JSONObject jp_obj;
    float s_rating;
    JSONArray jar_array;
    String server_response = "0", server_response_text;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_ride_live, container, false);



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
        ride_id = sharedPreferences.getString("id", "");
        driver_name = sharedPreferences.getString("driver_name", "");
        date = sharedPreferences.getString("date", "");
        time = sharedPreferences.getString("time", "");
        seats = sharedPreferences.getString("seats", "");
        cost = sharedPreferences.getString("cost", "");
        driver_image = sharedPreferences.getString("driver_image", "");
        driver_id = sharedPreferences.getString("driver_id", "");




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
    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //your code is here
                        Log.e("timer", "called");
                        new GetDriverLiveLoc().execute();
                    }
                });
            }
        };
        timer.schedule(timerTask, 5000, 5000);
    }


    //To stop timer
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }


    public class GetDriverLiveLoc extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "driver_live");
                obj.put("share_ride_id", share_ride_id);


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                live_lat = new String[(jar_array.length() - 1)];
                live_lan = new String[(jar_array.length() - 1)];
                status = new String[(jar_array.length() - 1)];



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


                            live_lat[i] = c.getString("live_lat");
                            live_lan[i] = c.getString("live_lan");
                            status[i] = c.getString("status");

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


            if (server_check) {


                if (server_response.equals("1")) {


                    if (status[0].equals("00")) {
                        stopTimer();

                        RatingDialog();

                    } else {


                        //remove marker from map if exists
                        if (startMarker != null) {
                            startMarker.remove();
                        }


                        LatLng latLng = new LatLng(Double.parseDouble(live_lat[0]), Double.parseDouble(live_lan[0]));
                        startMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(driver_name)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                                (new LatLng(Double.parseDouble(live_lat[0]), Double.parseDouble(live_lan[0])),
                                        16.5f));

                    }


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

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "ride_history");
                obj.put("ride_id", ride_id);
                obj.put("driver_name", driver_name);
                obj.put("time", time);
                obj.put("date", date);
                obj.put("start_name", StartingName);
                obj.put("dest_name", DestName);
                obj.put("seats", seats);
                obj.put("cost", cost);

                Log.e("history_data", ride_id + "/" + driver_name + "/" + time + "/" + date
                        + "/" + StartingName + "/" + DestName + "/" + seats + "/" + cost);


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


                    new Update_Rating().execute();


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






    //ASYNCTASK UPDATE RATING///////////////////////////////////////////////////
    public class Update_Rating extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... params) {

            server_response="";
            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "rating");

                obj.put("id", driver_id);
                obj.put("rating", s_rating);

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

                server_check=false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {


            if (server_check) {

                if (server_response.equals("1")) {


                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ride_ended), Toast.LENGTH_SHORT).show();


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
                    editor.putString("driver_name", "");
                    editor.putString("driver_image", "");

                    editor.apply();
                    rotateLoading.stop();
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                    //remove fragments from backstack
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }


                    Fragment fragment = new FragmentRideHistory();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                            addToBackStack("tag").commit();




                } else {
                    Toast.makeText(getActivity(), server_response_text, Toast.LENGTH_SHORT).show();

                }

            } else {

                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }








    public void RatingDialog(){

        //CUSTOM DIALOG///////////////////////////////
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_rating);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
        CircleImageView imageView = (CircleImageView) dialog.findViewById(R.id.profile_image);
        TextView TVname = (TextView) dialog.findViewById(R.id.name);
        Button btn_update_rating = (Button) dialog.findViewById(R.id.rate);


        TVname.setText(driver_name);


        btn_update_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ratingBar.getRating() == 0) {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.kindly_provide_ratings), Toast.LENGTH_SHORT).show();

                }
                else {

                    s_rating = ratingBar.getRating();
                    new UploadHistory().execute();

                    dialog.dismiss();
                }

            }
        });


        dialog.show();

    }


    @Override
    public void onResume() {
        super.onResume();

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {


            //start time and get location
            startTimer();


        }


    }

}


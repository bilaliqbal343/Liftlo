package com.app.liftlo.Ride.Home;

import android.Manifest;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.liftlo.Driver.ManageRides.SeeLocation;
import com.app.liftlo.R;
import com.app.liftlo.Ride.InAppCall.InAppCallFragment;
import com.app.liftlo.Ride.MyRides.FragmentMyRides;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.victor.loading.rotate.RotateLoading;
import com.zegocloud.uikit.components.invite.ZegoInvitationType;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoCallInvitationData;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoStartCallInvitationButton;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallConfigProvider;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class FragmentAllRides extends Fragment {


    private ArrayList<Filter_model> arrayList = new ArrayList<Filter_model>();

    AllRidesAdapter adapter = null;
    View v;
    Dialog dialog;
    ListView listView;
    RelativeLayout relativeLayout;
    RotateLoading rotateLoading;
    SharedPreferences sharedPreferences;
    String ride_id, ride_name, ride_number, share_ride_id, clicked_id,
            clicked_status, total_seats, booked, currentDateandTime,

    id_, driver_id_, booked_seats_, driver_name_, driver_number_, status_, date_, time_,
            start_lat_,
            start_lan_,
            start_name_,
            dest_lat_,
            dest_lan_,
            dest_name_, seat_no_, seat_cost_, table_id_, car_number_, car_name_, car_color_,
            ac_, music_, smoking_, rating_, driver_image_, ride_image, search_value = "";
    JSONObject jp_obj;
    JSONArray jar_array;
    String[] iid, driver_id, booked_seats, driver_name, driver_number, status, date, time,
            start_lat,
            start_lan,
            start_name,
            dest_lat,
            dest_lan,
            dest_name, seat_no, seat_cost, table_id, car_number, car_name, car_color,
            ac, music, smoking, rating, driver_image;
    Boolean server_check = false;
    int remaining, addSeats, total_cost;
    EditText EtSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton filter, search;
    FloatingActionButton scheduleTrip;
    EditText source, destination;
    String sourceCity, destinationCity, returnDriverName, returnStartLocation, returnDestinationLocation;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_rides_requests, container, false);


        //get date and time if required ddMMyyyy_HHmmss
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());
        Log.e("date", currentDateandTime);
        sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            returnDriverName = bundle.getString("returnDriverName", "");
            returnStartLocation = bundle.getString("returnStartName", "");
            returnDestinationLocation = bundle.getString("returnDestinationName", "");
            if (!returnDriverName.equals("") && !returnStartLocation.equals("") && !returnDestinationLocation.equals("")) {
                relativeLayout = v.findViewById(R.id.r);
                rotateLoading = v.findViewById(R.id.rotateloading);
                EtSearch = v.findViewById(R.id.et_search);
                swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
                filter = v.findViewById(R.id.btnfilter);
                search = v.findViewById(R.id.btnsearch);
                scheduleTrip = v.findViewById(R.id.btn_schedule_trip);
                getActivity().setTitle("Return Ride");
                arrayList.clear();

                returnRideFeatures(returnDriverName, returnStartLocation, returnDestinationLocation);


            }
        } else {
            init();
        }

        return v;
    }


    public void init() {


        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ride_id = sharedPreferences.getString("id", "No value");
        ride_name = sharedPreferences.getString("name", "No value");
        ride_number = sharedPreferences.getString("number", "No value");
        ride_image = sharedPreferences.getString("profile_pic", "No value");
        listView = v.findViewById(R.id.listview);
        relativeLayout = v.findViewById(R.id.r);
        rotateLoading = v.findViewById(R.id.rotateloading);
        EtSearch = v.findViewById(R.id.et_search);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        filter = v.findViewById(R.id.btnfilter);
        search = v.findViewById(R.id.btnsearch);
        scheduleTrip = v.findViewById(R.id.btn_schedule_trip);
        getActivity().setTitle(getActivity().getResources().getString(R.string.all_available_rides));


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Filterdialog();
            }
        });
        scheduleTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScheduleTripdialog();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

            new GetAllRides().execute();

        } else {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listview_click(id, position, view);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new Check_internet_connection(getActivity().getApplicationContext()).isNetworkAvailable()) {

                    relativeLayout.setVisibility(View.VISIBLE);
                    rotateLoading.start();
                    arrayList.clear();
                    new GetAllRides().execute();
                    swipeRefreshLayout.setRefreshing(false);

                } else {

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Check your Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void returnRideFeatures(String driverName, String startLoaction, String destination) {
        //this is to get the city names from the locations
        String[] splitStartLocation = startLoaction.split(",");
        String startLocationCity = splitStartLocation[1];
        String[] splitdestLocation = destination.split(",");
        String destLocationCity = splitdestLocation[1];

        listView = v.findViewById(R.id.listview);
        new GetAllRides().execute();
        adapter = new AllRidesAdapter(getActivity(), arrayList);
        RelativeLayout searchfilter = (RelativeLayout) v.findViewById(R.id.search_filter);
        CoordinatorLayout floatButton = (CoordinatorLayout) v.findViewById(R.id.rootLayout_floatbtn);
        searchfilter.setVisibility(View.GONE);
        floatButton.setVisibility(View.GONE);
        //ArrayList<Filter_model> returnRideLisst=new ArrayList<Filter_model>();
        //returnRideLisst=adapter.returnRideFilter(driverName,startLoaction,destination);
        if (arrayList != null && !arrayList.isEmpty()) {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    arrayList=adapter.returnRideFilter(driverName, startLocationCity, destLocationCity);

                }
            }, 100);
            AllRidesAdapter returnRideAdapter = new AllRidesAdapter(getActivity(), arrayList);
            listView.setAdapter(returnRideAdapter);
        } else {
            arrayList.clear();
            AllRidesAdapter emptyadapter = new AllRidesAdapter(getActivity(),
                    arrayList);
            listView.setAdapter(emptyadapter);
            listView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Ask the driver to schedule the ride first", Toast.LENGTH_SHORT).show();

        }

    }


    public void listview_click(long id, int position, View view) {

        if (id == 1) {//call

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
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                    addToBackStack("tag").commit();
        } else if (id == 3) {//request

            remaining = Integer.parseInt(seat_no[position]) - Integer.parseInt(booked_seats[position]);

            if (remaining < 1) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.limit_reached_cant_book_more), Toast.LENGTH_SHORT).show();
            } else {


                id_ = iid[position];
                driver_id_ = driver_id[position];
                booked_seats_ = booked_seats[position];
                driver_name_ = driver_name[position];
                driver_number_ = driver_number[position];
                status_ = status[position];
                date_ = date[position];
                time_ = time[position];
                start_lat_ = start_lat[position];
                start_lan_ = start_lan[position];
                start_name_ = start_name[position];
                dest_lat_ = dest_lat[position];
                dest_lan_ = dest_lan[position];
                dest_name_ = dest_name[position];
                seat_no_ = seat_no[position];
                seat_cost_ = seat_cost[position];
                car_number_ = car_number[position];
                car_name_ = car_name[position];
                car_color_ = car_color[position];
                ac_ = ac[position];
                music_ = music[position];
                smoking_ = smoking[position];
                rating_ = rating[position];
                driver_image_ = driver_image[position];


                if (driver_id_.equals(ride_id)) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.cant_book_your_own_ride), Toast.LENGTH_SHORT).show();
                } else {
                    openDialog();
                }
            }
        }

    }


    String server_response_text;
    Intent intent;

    public class RequestRide extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();


                obj.put("operation", "request_ride");
                obj.put("status", "1");
                obj.put("request_status", "1");
                obj.put("driver_id", driver_id_);
                obj.put("driver_name", driver_name_);
                obj.put("driver_number", driver_number_);
                obj.put("car_number", car_number_);
                obj.put("car_model", car_name_);
                obj.put("car_color", car_color_);
                obj.put("start_lat", start_lat_);
                obj.put("start_lan", start_lan_);
                obj.put("start_name", start_name_);
                obj.put("dest_lat", dest_lat_);
                obj.put("dest_lan", dest_lan_);
                obj.put("dest_name", dest_name_);
                obj.put("seat_no", seat_no_);
                obj.put("cost", total_cost + "");
                obj.put("time", time_);
                obj.put("date", date_);
                obj.put("music", music_);
                obj.put("smoking", smoking_);
                obj.put("ac", ac_);
                obj.put("driver_image", driver_image_);
                obj.put("rating", rating_);
                obj.put("booked_seats", booked);//
                obj.put("share_ride_id", id_);
                obj.put("ride_id", ride_id);
                obj.put("ride_name", ride_name);
                obj.put("ride_number", ride_number);
                obj.put("ride_image", ride_image);


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

                //server response/////////////////////////
                server_check = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            rotateLoading.stop();
            relativeLayout.setVisibility(View.GONE);
//            Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            if (server_check) {

                if (server_response.equals("1")) {

                    dialog.dismiss();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ride_requested_successfully),
                            Toast.LENGTH_SHORT).show();

                    Fragment fragment = new FragmentMyRides();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    //new GetAllRides().execute();


                } else {
                    Toast.makeText(getActivity(), server_response_text, Toast.LENGTH_SHORT).show();

                }

            } else {

                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


    public class UpdateRideId extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();
                obj.put("operation", "update_seat");
                obj.put("id", id_);
                obj.put("booked_seats", addSeats + "");//


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

                //server response/////////////////////////
                server_check = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {


            if (server_check) {

                if (server_response.equals("1")) {

                    new RequestRide().execute();

                } else {
                    Toast.makeText(getActivity(), server_response_text, Toast.LENGTH_SHORT).show();

                }

            } else {

                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


    String server_response = "0";

    //ASYNTASK Getting Data From Server/////////////////////////////////////
    public class GetAllRides extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "all_rides");
                obj.put("date", currentDateandTime);


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                ac = new String[(jar_array.length() - 1)];
                music = new String[(jar_array.length() - 1)];
                smoking = new String[(jar_array.length() - 1)];
                car_color = new String[(jar_array.length() - 1)];
                car_number = new String[(jar_array.length() - 1)];
                car_name = new String[(jar_array.length() - 1)];
                iid = new String[(jar_array.length() - 1)];
                driver_id = new String[(jar_array.length() - 1)];
                driver_name = new String[(jar_array.length() - 1)];
                driver_number = new String[(jar_array.length() - 1)];
                driver_image = new String[(jar_array.length() - 1)];
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
                booked_seats = new String[(jar_array.length() - 1)];
                rating = new String[(jar_array.length() - 1)];


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


                            iid[i] = c.getString("id");
                            seat_cost[i] = c.getString("seat_cost");
                            driver_id[i] = c.getString("driver_id");
                            car_name[i] = c.getString("car_model");
                            car_color[i] = c.getString("car_color");
                            car_number[i] = c.getString("car_number");
                            booked_seats[i] = c.getString("booked_seats");
                            ac[i] = c.getString("ac");
                            music[i] = c.getString("music");
                            smoking[i] = c.getString("smoking");
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
                            rating[i] = c.getString("rating");
                            driver_image[i] = c.getString("driver_image");

                        }

                        j++;
                    }


                    //setting vslue to arrayist
                    for (int k = 0; k < iid.length; k++) {
                        Filter_model contacts = new Filter_model(booked_seats[k], status[k],
                                date[k], time[k], driver_name[k]
                                , driver_number[k], start_name[k], dest_name[k]
                                , seat_no[k], seat_cost[k], car_name[k], car_color[k],
                                ac[k], music[k]
                                , smoking[k], rating[k], driver_image[k]);
                        contacts.setBooked_seats(booked_seats[k]);
                        contacts.setStatus(status[k]);
                        contacts.setDate(date[k]);
                        contacts.setTime(time[k]);
                        contacts.setDriver_name(driver_name[k]);
                        contacts.setDriver_number(driver_number[k]);
                        contacts.setSeat_no(seat_no[k]);
                        contacts.setStart_name(start_name[k]);
                        contacts.setSeat_cost(seat_cost[k]);
                        contacts.setDest_name(dest_name[k]);
                        contacts.setCar_name(car_name[k]);
                        contacts.setCar_color(car_color[k]);
                        contacts.setAc(ac[k]);
                        contacts.setMusic(music[k]);
                        contacts.setSmoking(smoking[k]);
                        contacts.setRating(rating[k]);
                        contacts.setDriver_image(driver_image[k]);

                        arrayList.add(contacts);
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
//            Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            if (server_check) {


                if (server_response.equals("1")) {


                    if (driver_name.length > 0) {


                        adapter = new AllRidesAdapter(getActivity(),
                                arrayList);

                        listView.setAdapter(adapter);


                        //filtering data
                        EtSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                                if (search_value.equals("source")) {
                                    adapter.getFilter().filter(charSequence);
                                } else if (search_value.equals("destination")) {
                                    adapter.getFilter2().filter(charSequence);
                                } else {
                                    Toast.makeText(getActivity(), "please select source or destination", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });


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


    //filter dialog
    public void Filterdialog() {


        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_search);

        Button cross, call;
        final CheckBox source, destination;
        cross = dialog.findViewById(R.id.cross);
        call = dialog.findViewById(R.id.btn);
        source = dialog.findViewById(R.id.source);
        destination = dialog.findViewById(R.id.destination);


        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                destination.setChecked(false);

            }
        });


        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                source.setChecked(false);

            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (destination.isChecked()) {
                    search_value = "destination";
                    dialog.dismiss();
                } else if (source.isChecked()) {
                    search_value = "source";
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Select Search Category", Toast.LENGTH_SHORT).show();
                }


            }
        });


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    //Schedule Trip dialog
    public void ScheduleTripdialog() {


        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_ride_details);

        Button btnDone;

        btnDone = dialog.findViewById(R.id.btn_done);
        source = dialog.findViewById(R.id.source_city);
        destination = dialog.findViewById(R.id.destination_city);

        final CheckBox fare, rating;
        fare = dialog.findViewById(R.id.fare);
        rating = dialog.findViewById(R.id.rating);


        fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rating.setChecked(false);

            }
        });


        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fare.setChecked(false);

            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isfare=false;
                Boolean isRating=false;
                if (fare.isChecked())
                {
                    isfare=true;
                }
                else if (rating.isChecked())
                {
                    isRating=true;
                }
                if (!source.getText().toString().equals("") && !destination.getText().toString().equals("")) {
                    sourceCity = source.getText().toString();
                    destinationCity = destination.getText().toString();
                    AllRidesAdapter emptyadapter = new AllRidesAdapter(getActivity(),
                            adapter.emptyArray());
                    listView.setAdapter(emptyadapter);
                    AllRidesAdapter scheduleRideAdapter = new AllRidesAdapter(getActivity(), adapter.secheduleRideFilter(sourceCity, destinationCity,isRating,isfare));
                    listView.setAdapter(scheduleRideAdapter);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Enter Source & Destination", Toast.LENGTH_SHORT).show();

                }

            }
        });


        dialog.show();
    }


    public void openDialog() {

        //CUSTOM DIALOG///////////////////////////////
        dialog = new Dialog(getActivity(), R.style.dialog_theme);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_seats);


        final EditText seats = dialog.findViewById(R.id.seats);
        Button button = dialog.findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                booked = seats.getText().toString();

                if (booked.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_no_of_seat), Toast.LENGTH_SHORT).show();
                } else {

                    if (Integer.parseInt(booked) > Integer.parseInt(remaining + "")) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.max_limit) + remaining, Toast.LENGTH_SHORT).show();
                    } else {

                        //total cost of seats taken
                        total_cost = Integer.parseInt(seat_cost_) * Integer.parseInt(booked);

//                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        rotateLoading.start();

                        //add number of seats
                        addSeats = Integer.parseInt(booked) + Integer.parseInt(booked_seats_);
                        Log.e("total_seats", addSeats + "");
                        dialog.dismiss();
                        new RequestRide().execute();

                    }
                }

            }
        });


        dialog.show();

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

        if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {
//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            relativeLayout.setVisibility(View.VISIBLE);
            rotateLoading.start();
        } else {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }
        // String uid = generateUserID();
//        initCallInviteService("");
        //you can set the title for your toolbar here for different fragments different titles
    }


}

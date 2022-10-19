package com.app.liftlo.Driver.Home;

import android.app.Application;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.app.liftlo.R;
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.victor.loading.rotate.RotateLoading;
import com.zegocloud.uikit.components.invite.ZegoInvitationType;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoCallInvitationData;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallConfigProvider;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class FragmentRideInfo extends Fragment {


    View v;
    String StartingLat, StartingLan, DestinationLat, DestinationLan, id, type, name, dob, gender, car_color, car_model, car_number, city, StartingName, DestName, Sac = "no", Smusic = "no", Ssmoke = "no", cal_age, number, Sseat_no, Scost, Stime, Sdate, pic, rating;
    SharedPreferences sharedPreferences;
    EditText start_loc, dest_loc, model, plate, color, seats, cost;
    Button ac, music, smoke, send;
    RelativeLayout relativeLayout;
    RotateLoading rotateLoading;
    int ac_click = 0, music_click = 0, smoke_click = 0, daysBetween;
    TimePickerDialog timePickerDialog;
    TextView date, time;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_ride_info, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            StartingLat = bundle.getString("Startlat");
            StartingLan = bundle.getString("Startlon");
            DestinationLat = bundle.getString("Destlat");
            DestinationLan = bundle.getString("Destlon");
            DestName = bundle.getString("Destname");
            StartingName = bundle.getString("StartName");


            Log.e("startLatLan", StartingLat + "," + StartingLan + "\n" + DestName + "," +
                    StartingName + "," +
                    DestinationLat + "," + DestinationLan);
        }


        init();


        return v;
    }


    public void init() {


        sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "No value");
        type = sharedPreferences.getString("type", "No value");
        name = sharedPreferences.getString("name", "No value");
        dob = sharedPreferences.getString("dob", "No value");
        gender = sharedPreferences.getString("gender", "No value");
        car_color = sharedPreferences.getString("car_color", "No value");
        car_model = sharedPreferences.getString("car_model", "No value");
        city = sharedPreferences.getString("city", "No value");
        car_number = sharedPreferences.getString("car_number", "No value");
        number = sharedPreferences.getString("number", "No value");
        pic = sharedPreferences.getString("profile_pic", "null");
        rating = sharedPreferences.getString("rating", "null");


        start_loc = v.findViewById(R.id.start_loc);
        dest_loc = v.findViewById(R.id.des_loc);
        relativeLayout = v.findViewById(R.id.r);
        rotateLoading = v.findViewById(R.id.rotateloading);
        model = v.findViewById(R.id.model);
        plate = v.findViewById(R.id.plate);
        color = v.findViewById(R.id.color);
        seats = v.findViewById(R.id.seats);
        cost = v.findViewById(R.id.cost);
        time = v.findViewById(R.id.time);
        date = v.findViewById(R.id.date);
        ac = v.findViewById(R.id.ac);
        music = v.findViewById(R.id.music);
        smoke = v.findViewById(R.id.smoke);
        send = v.findViewById(R.id.add);
        start_loc.setText(StartingName);
        dest_loc.setText(DestName);
        model.setText(car_model);
        plate.setText(car_number);
        color.setText(car_color);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sseat_no = seats.getText().toString().trim();
                Scost = cost.getText().toString().trim();
                Stime = time.getText().toString().trim();
                Sdate = date.getText().toString().trim();

                if (Sseat_no.equals("")) {
                    seats.setError(getActivity().getResources().getString(R.string.enter_no_of_seat));
                    seats.requestFocus();
                } else if (Scost.equals("")) {
                    seats.setError(getActivity().getResources().getString(R.string.enter_seat_cost));
                    cost.requestFocus();
                } else if (Stime.equals("")) {
                    seats.setError(getActivity().getResources().getString(R.string.enter_time));
                    time.requestFocus();
                } else if (Sdate.equals("")) {
                    seats.setError(getActivity().getResources().getString(R.string.enter_date));
                    date.requestFocus();
                } else {

                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    rotateLoading.start();

                    new ShareRide().execute();

                }


            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);


                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String status = "AM";

                                if (hourOfDay > 11) {
                                    // If the hour is greater than or equal to 12
                                    // Then the current AM PM status is PM
                                    status = "PM";
                                }

                                // Initialize a new variable to hold 12 hour format hour value
                                int hour_of_12_hour_format;

                                if (hourOfDay > 11) {

                                    // If the hour is greater than or equal to 12
                                    // Then we subtract 12 from the hour to make it 12 hour format time
                                    hour_of_12_hour_format = hourOfDay - 12;
                                } else {
                                    hour_of_12_hour_format = hourOfDay;
                                }

                                // Get the calling activity TextView reference
                                // Display the 12 hour format time in app interface
                                if (hour_of_12_hour_format == 0) {
                                    time.setText(12 + " : " + minute + " : " + status);
                                } else {
                                    time.setText(hour_of_12_hour_format + " : " + minute + " : " + status);
                                }


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }

        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobDialog();
            }
        });


        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ac_click == 0) {
                    Sac = "yes";
                    ac.setTextColor(Color.parseColor("#25C777"));
                    ac_click = 1;
                } else {
                    Sac = "no";
                    ac.setTextColor(Color.parseColor("#A7A5A5"));
                    ac_click = 0;
                }

            }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (music_click == 0) {
                    Smusic = "yes";
                    music.setTextColor(Color.parseColor("#25C777"));
                    music_click = 1;
                } else {
                    Smusic = "no";
                    music.setTextColor(Color.parseColor("#A7A5A5"));
                    music_click = 0;
                }
            }
        });


        smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (smoke_click == 0) {
                    Ssmoke = "yes";
                    smoke.setTextColor(Color.parseColor("#25C777"));
                    smoke_click = 1;
                } else {
                    Ssmoke = "no";
                    smoke.setTextColor(Color.parseColor("#A7A5A5"));
                    smoke_click = 0;
                }
            }
        });


    }


    DatePicker datePicker;

    public void dobDialog() {

        //CUSTOM DIALOG///////////////////////////////
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_dob);

        datePicker = dialog.findViewById(R.id.datePicker1);
        final Button Bdob = dialog.findViewById(R.id.btn);

        datePicker.setMinDate(System.currentTimeMillis() - 1000);


        Bdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //set date to textview
                String date_ = getDOB();
                date.setText(date_);

                //calulate months
                calculate_age(getDOB());

                dialog.dismiss();

            }
        });


        dialog.show();
    }


    //set dob to textview
    public String getDOB() {

        StringBuilder builder = new StringBuilder();
        builder.append(datePicker.getDayOfMonth() + "-");
        builder.append((datePicker.getMonth() + 1) + "-");//month is 0 based
        builder.append(datePicker.getYear());

        return builder.toString();
    }


    //calculate age
    public String calculate_age(String dob) {

        //get current date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
//        Toast.makeText(this, formattedDate+"", Toast.LENGTH_SHORT).show();


        //calculate days
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date dateBefore = myFormat.parse(dob);
            Date dateAfter = myFormat.parse(formattedDate);

            daysBetween = DateTimeUtils.getDateDiff(dateAfter, dateBefore, DateTimeUnits.DAYS);
//            Toast.makeText(this, diff+"", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cal_age;
    }


    String server_response_text, server_response;
    JSONObject jp_obj;
    JSONArray jar_array;
    boolean server_check = false;
    Intent intent;

    //ASYNTASK REGISTER USER////////////////////////////////////
    public class ShareRide extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();


                obj.put("operation", "share_ride");
                obj.put("status", "1");
                obj.put("booked_seats", "00");
                obj.put("driver_id", id);
                obj.put("driver_name", name);
                obj.put("driver_number", number);
                obj.put("car_number", car_number);
                obj.put("car_model", car_model);
                obj.put("car_color", car_color);
                obj.put("city", city);
                obj.put("start_lat", StartingLat);
                obj.put("start_lan", StartingLan);
                obj.put("start_name", StartingName);
                obj.put("dest_lat", DestinationLat);
                obj.put("dest_lan", DestinationLan);
                obj.put("dest_name", DestName);
                obj.put("seat_no", Sseat_no);
                obj.put("seat_cost", Scost);
                obj.put("time", Stime);
                obj.put("date", Sdate);
                obj.put("music", Smusic);
                obj.put("smoking", Ssmoke);
                obj.put("ac", Sac);
                obj.put("profile_pic", pic);
                obj.put("rating", rating);

                Log.e("allData", id + "/" + name + "/" + number + "/" + car_number + "/" + car_model + "/" +
                        car_color + "/" + city + "/" + StartingLat + "/" + StartingLan + "/" +
                        StartingName + "/" + DestinationLat + "/" + DestinationLan + "/" + DestName + "/" +
                        Sseat_no + "/" + Scost + "/" + Stime + "/" + Sdate + "/" + Smusic
                        + "/" + Ssmoke + "/" + Sac + "/" + rating + "/" + pic);


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
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            if (server_check) {

                if (server_response.equals("1")) {
                   // initCallInviteService(number,name);

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.ride_shared_successfully),
                            Toast.LENGTH_SHORT).show();


                    //remove fragments from backstack
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }


                    Fragment fragment = new FragmentDestination();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                            remove(fragment).addToBackStack(null).commit();

                } else {
                    Toast.makeText(getActivity(), server_response_text, Toast.LENGTH_SHORT).show();

                }

            } else {

                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getResources().getString(R.string.share_ride));
    }

}

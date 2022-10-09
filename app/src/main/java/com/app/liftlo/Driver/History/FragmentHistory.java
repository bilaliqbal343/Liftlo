package com.app.liftlo.Driver.History;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
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

import com.app.liftlo.Driver.ManageRides.FragmentRidesRequests;
import com.app.liftlo.Driver.MyRides.FragmentMyRides;
import com.app.liftlo.Ride.Home.FragmentAllRides;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.app.liftlo.R;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;


public class FragmentHistory extends Fragment {

    View v;
    SharedPreferences sharedPreferences;
    String driverId;
    ListView listView;
    RelativeLayout relativeLayout;
    RotateLoading rotateLoading;
    JSONObject jp_obj;
    JSONArray jar_array;
    String[] driver_id, name, time, date, start_name, dest_name, cost, seats, driver_image;
    Boolean server_check = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_history, container, false);

        init();

        return v;
    }


    public void init() {


        sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        driverId = sharedPreferences.getString("id", "No value");

        listView = v.findViewById(R.id.listview);
        relativeLayout = v.findViewById(R.id.r);
        rotateLoading = v.findViewById(R.id.rotateloading);


        if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            relativeLayout.setVisibility(View.VISIBLE);
            rotateLoading.start();

            new GetHistory().execute();
        } else {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }


    }




    String server_response = "0", server_response_text;

    //ASYNTASK Getting Data From Server/////////////////////////////////////
    public class GetHistory extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "d_history");
                obj.put("driver_id", driverId);


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                driver_id = new String[(jar_array.length() - 1)];
                name = new String[(jar_array.length() - 1)];
                date = new String[(jar_array.length() - 1)];
                time = new String[(jar_array.length() - 1)];
                start_name = new String[(jar_array.length() - 1)];
                dest_name = new String[(jar_array.length() - 1)];
                seats = new String[(jar_array.length() - 1)];
                cost = new String[(jar_array.length() - 1)];


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


                            driver_id[i] = c.getString("driver_id");
                            name[i] = c.getString("name");
                            date[i] = c.getString("date");
                            time[i] = c.getString("time");
                            start_name[i] = c.getString("start_name");
                            dest_name[i] = c.getString("dest_name");
                            seats[i] = c.getString("seats");
                            cost[i] = c.getString("cost");


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


                    if (driver_id.length > 0) {


                        final RideHistoryAdapter adapter = new RideHistoryAdapter(getActivity(),
                                driver_id, name, date, time, start_name, dest_name
                                , seats, cost,false);

                        listView.setAdapter(adapter);


                    } else {
                        Toast.makeText(getActivity(), server_response_text, Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
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
        getActivity().setTitle(getActivity().getResources().getString(R.string.ride_history));
    }
}

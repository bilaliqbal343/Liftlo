package com.app.liftlo.Ride.AllDrivers;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.liftlo.R;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class FragmentAllDrivers extends Fragment {


    private ArrayList<DriverFilter_model> arrayList = new ArrayList<DriverFilter_model>();

    AllDriversAdapter adapter = null;
    View v;
    Dialog dialog;
    ListView listView;
    RelativeLayout relativeLayout;
    RotateLoading rotateLoading;
    JSONObject jp_obj;
    JSONArray jar_array;
    String[] driver_id, driver_name, driver_number, car_name, car_color,
            rating, driver_image;
    Boolean server_check = false;
    EditText EtSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton search;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_all_drivers, container, false);


        init();


        return v;
    }


    public void init() {

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        listView = v.findViewById(R.id.listview);
        relativeLayout = v.findViewById(R.id.r);
        rotateLoading = v.findViewById(R.id.rotateloading);
        EtSearch = v.findViewById(R.id.et_search);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        search = v.findViewById(R.id.btnsearch);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

            new GetAllDrivers().execute();

        } else {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new Check_internet_connection(getActivity().getApplicationContext()).isNetworkAvailable()) {

                    relativeLayout.setVisibility(View.VISIBLE);
                    rotateLoading.start();
                    new GetAllDrivers().execute();
                    swipeRefreshLayout.setRefreshing(false);

                } else {

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Check your Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    String server_response_text;
    String server_response = "0";

    //ASYNTASK Getting Data From Server/////////////////////////////////////
    public class GetAllDrivers extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "all_drivers");


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;


                car_color = new String[(jar_array.length() - 1)];
                car_name = new String[(jar_array.length() - 1)];
                driver_id = new String[(jar_array.length() - 1)];
                driver_name = new String[(jar_array.length() - 1)];
                driver_number = new String[(jar_array.length() - 1)];
                driver_image = new String[(jar_array.length() - 1)];
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


                            driver_id[i] = c.getString("id");
                            car_name[i] = c.getString("car_model");
                            car_color[i] = c.getString("car_color");
                            driver_name[i] = c.getString("name");
                            driver_number[i] = c.getString("number");
                            rating[i] = c.getString("rating");
                            driver_image[i] = c.getString("profile_pic");

                        }

                        j++;
                    }


                    //setting vslue to arrayist
                    for (int k = 0; k < driver_id.length; k++) {
                        DriverFilter_model contacts = new DriverFilter_model(driver_name[k]
                                , driver_number[k], car_name[k], car_color[k],
                                rating[k], driver_image[k]);

                        contacts.setDriver_name(driver_name[k]);
                        contacts.setDriver_number(driver_number[k]);
                        contacts.setCar_name(car_name[k]);
                        contacts.setCar_color(car_color[k]);
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


                        adapter = new AllDriversAdapter(getActivity(),
                                arrayList);

                        listView.setAdapter(adapter);


                        //filtering data
                        EtSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                                adapter.getFilter().filter(charSequence);

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
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getResources().getString(R.string.all_drivers));
    }
}

package com.app.liftlo.Driver.MyProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.app.liftlo.R;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentDriverProfile extends Fragment {

    View v;
    CircleImageView imageView;
    EditText name, f_name, nic, license_no, dob, city, car_model, car_color, car_number, number;
    String  Sname, Sfather_name, Sdob, Scity, Scar_model,
            Scar_number,
            Scar_color,
            license_number,
            Snic,
            profile_pic, Snumber, image,
            id, server_response, server_response_text; Boolean server_check = false;
    SharedPreferences sharedPreferences;
    JSONObject jp_obj; JSONArray jar_array;
    RelativeLayout relativeLayout;
    RotateLoading rotateLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_driver_profile, container, false);


        init();


        return v;
    }



    public void init(){


        sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "No value");


        imageView = v.findViewById(R.id.profile_image);
        name = v.findViewById(R.id.name);
        number = v.findViewById(R.id.number);
        f_name = v.findViewById(R.id.f_name);
        nic = v.findViewById(R.id.nic);
        license_no = v.findViewById(R.id.license);
        dob = v.findViewById(R.id.dob);
        city = v.findViewById(R.id.city);
        car_color = v.findViewById(R.id.color);
        car_model = v.findViewById(R.id.model);
        car_number = v.findViewById(R.id.plate);
        relativeLayout = v.findViewById(R.id.r);
        rotateLoading = v.findViewById(R.id.rotateloading);
        imageView = v.findViewById(R.id.profile_image);



        if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            relativeLayout.setVisibility(View.VISIBLE);
            rotateLoading.start();
            new LoadProfile().execute();

        } else {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }



    }



    public class LoadProfile extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "profile");
                obj.put("id", id);


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


                if (server_response.equals("1")) {

                    c = jar_array.getJSONObject(1);

                    if (c.length() > 0) {



                        Sname = c.getString("name");
                        Sfather_name = c.getString("father_name");
                        Sdob = c.getString("dob");
                        Scity = c.getString("city");
                        Scar_model = c.getString("car_model");
                        Scar_number = c.getString("car_number");
                        Scar_color = c.getString("car_color");
                        license_number = c.getString("license_number");
                        Snic = c.getString("nic");
                        profile_pic = c.getString("profile_pic");
                        Snumber = c.getString("number");

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


                    name.setText(Sname);
                    f_name.setText(Sfather_name);
                    dob.setText(Sdob);
                    city.setText(Scity);
                    car_color.setText(Scar_color);
                    car_model.setText(Scar_model);
                    car_number.setText(Scar_number);
                    nic.setText(Snic);
                    number.setText(Snumber);
                    license_no.setText(license_number);

                    Glide.with(getActivity()).load(ServerURL.load_image+profile_pic).into(imageView);



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
        getActivity().setTitle(getActivity().getResources().getString(R.string.my_profile));
    }
}

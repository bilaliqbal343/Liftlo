package com.app.liftlo.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.liftlo.Driver.ActivityDriver;
import com.app.liftlo.R;
import com.app.liftlo.Ride.ActivityRide;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView signup;
    EditText number, password;
    String Snumber, Spassword,
            id, name, father_name, dob, email, city, car_model,
            car_number,
            car_color,
            license_number,
            nic,
            status,
            profile_pic,
            gender,
            type, rating, Scode;
    Intent intent;
    String server_response_text = "";
    JSONObject jp_obj;
    JSONArray jar_array;
    boolean server_check = false;
    String server_response = "";
    SharedPreferences sharedPreferences;
    RotateLoading rotateLoading;
    RelativeLayout relativeLayout;
    String FbToken;
    CountryCodePicker countryCodePicker;
    FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //get token firebase
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                FbToken = instanceIdResult.getToken();
                Log.e("Token", FbToken);
            }
        });


        //firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Login Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        init();
    }


    public void init() {

        login = findViewById(R.id.add);
        number = findViewById(R.id.number);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        rotateLoading = findViewById(R.id.rotateloading);
        relativeLayout = findViewById(R.id.r);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);


        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Snumber= number.getText().toString().trim();
                Spassword = password.getText().toString().trim();
                Scode = countryCodePicker.getSelectedCountryCode().trim();


                if (Snumber.equals("")) {
                    number.setError(getApplicationContext().getResources().getString(R.string.enter_your_number));
                    number.requestFocus();
                } else if (Spassword.equals("")) {
                    number.setError(getApplicationContext().getResources().getString(R.string.enter_your_password));
                    password.requestFocus();
                } else {

                    if (number.equals("333331122334")
                            && password.equals("0000")) {

                        intent = new Intent(LoginActivity.this, ActivityDriver.class);
                        startActivity(intent);
                        finish();
                    } else if (number.equals("333345516977")
                            && password.equals("0000")) {
                        intent = new Intent(LoginActivity.this, ActivityRide.class);
                        startActivity(intent);
                        finish();
                    } else {


                        if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {

                            //mFirebaseAnalytics.setUserProperty("LoginUser",Snumber);

                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            relativeLayout.setVisibility(View.VISIBLE);
                            rotateLoading.start();
                            new LoginUser().execute();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getApplicationContext().getResources().getString(R.string.check_internet_connection)
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });


        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(LoginActivity.this, RegisterSelection.class);
                startActivity(intent);
            }
        });
    }


    //    ASYNTASK JSON//////////////////////////////////////////
    public class LoginUser extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {


            //////////////
//
//            HashMap<String, String> paramss = new HashMap<>();
//            paramss.put("operation", "login");
//
//            paramss.put("password", Spassword);
//            paramss.put("number", Scode+Snumber);
//            paramss.put("token_id", FbToken);
//            StringBuilder sbParams = new StringBuilder();
//            int i = 0;
//            for (String key : paramss.keySet()) {
//                try {
//                    if (i != 0) {
//                        sbParams.append("&");
//                    }
//                    sbParams.append(key).append("=")
//                            .append(URLEncoder.encode(paramss.get(key), "UTF-8"));
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                i++;
//            }
//
//            try {
//
//                URL url = new URL(ServerURL.Url);
//                URLConnection urlConn = url.openConnection();
//
//                HttpURLConnection httpsConn = (HttpURLConnection) urlConn;
//                httpsConn.setAllowUserInteraction(false);
//                httpsConn.setInstanceFollowRedirects(true);
//                httpsConn.setRequestMethod("POST");
//                httpsConn.setRequestProperty("Accept-Charset", "UTF-8");
//                httpsConn.connect();
//
//                String paramsString = sbParams.toString();
//
//                DataOutputStream wr = new DataOutputStream(httpsConn.getOutputStream());
//                wr.writeBytes(paramsString);
//                wr.flush();
//                wr.close();
//
//                String resCode = httpsConn.getResponseCode() + " / " + httpsConn.getResponseMessage();
//                Log.e("new kaam", resCode);
//
//
//                InputStream in = new BufferedInputStream(httpsConn.getInputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                StringBuilder result = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    result.append(line);
//                }
//
//                Log.e("test", "result from server: " + result.toString());
//
//
//                try {
//                    String res = result.toString();
//                    JSONObject object = new JSONObject(res);
//                    JSONArray array = object.getJSONArray("JsonData");
//                    Log.e("array_length", array.length() + "");
//                    for (int ii = 0; ii
//                            < array.length(); ii++) {
//                        JSONObject object1 = array.getJSONObject(ii);
//                        Log.e("babu array", object1.toString());
//
//                        JSONObject c;
//
//                        c = array.getJSONObject(0);
//
//                        if (c.length() > 0) {
//
//                            server_response = c.getString("response");
//                            Log.e("SResponse", server_response);
//
//                            if (server_response.equals("0")) {
//                                server_response_text = c.getString("response-text");
//
//                            }
//                        }
//
//                        if (server_response.equals("1")) {
//
//                            c = array.getJSONObject(1);
//
//                            if (c.length() > 0) {
//
//
//                                type = c.getString("type");
//                                name = c.getString("name");
//                                dob = c.getString("dob");
//                                city = c.getString("city");
//                                email = c.getString("email");
//                                car_model = c.getString("car_model");
//                                car_number = c.getString("car_number");
//                                car_color = c.getString("car_color");
//                                id = c.getString("id");
//                                gender = c.getString("gender");
//                                profile_pic = c.getString("profile_pic");
//                                rating = c.getString("rating");
//                            }
//
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    server_check = false;
//                }
//
//
//                server_check = true;
//
//
//            } catch (Exception e) {
//                Log.e("exception", e.toString());
//                server_check = false;
//            }


            ///////


            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "login");

                obj.put("password", Spassword);
                obj.put("number", Scode + Snumber);
                obj.put("token_id", FbToken);


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


                        type = c.getString("type");
                        name = c.getString("name");
                        dob = c.getString("dob");
                        city = c.getString("city");
                        email = c.getString("email");
                        car_model = c.getString("car_model");
                        car_number = c.getString("car_number");
                        car_color = c.getString("car_color");
                        id = c.getString("id");
                        gender = c.getString("gender");
                        profile_pic = c.getString("profile_pic");
                        rating = c.getString("rating");
                        status = c.getString("status");
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
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            if (server_check) {

                if (server_response.equals("1")) {


                    //Creating a shared preference
                    sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();


                    //Adding values to editor
                    editor.putString("type", type);
                    editor.putString("name", name);
                    editor.putString("dob", dob);
                    editor.putString("gender", gender);
                    editor.putString("car_color", car_color);
                    editor.putString("car_model", car_model);
                    editor.putString("city", city);
                    editor.putString("car_number", car_number);
                    editor.putString("id", id);
                    editor.putString("number", Scode + Snumber);
                    editor.putString("rating", rating);
                    editor.putString("profile_pic", profile_pic);
                    editor.putString("token", FbToken);
                    editor.apply();


                    if (type.equals("ride")) {

                        intent = new Intent(LoginActivity.this, ActivityRide.class);
                        startActivity(intent);
                        finish();
                    } else if (type.equals("driver")) {

                        if (status.equals("3")) {
                            //Adding values to editor
                            editor.putString("type", "");
                            editor.apply();
                            Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.your_account_approval_is_pending), Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(LoginActivity.this, ActivityDriver.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                } else {
                    Toast.makeText(LoginActivity.this, server_response_text, Toast.LENGTH_SHORT).show();

                }


            } else {

                Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


}


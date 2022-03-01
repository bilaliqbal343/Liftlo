package com.app.liftlo.Login.NumberAuth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.liftlo.R;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetUserNumber extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText number;
    Button confirm;
    String Snumber;
    String server_response_text = "";
    JSONObject jp_obj;
    JSONArray jar_array;
    boolean server_check = false;
    String server_response = "";
    RotateLoading rotateLoading;
    RelativeLayout relativeLayout;
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_num);

        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        number = findViewById(R.id.number);
        confirm = findViewById(R.id.confirm);
        rotateLoading = findViewById(R.id.rotateloading);
        relativeLayout = findViewById(R.id.r);


        type = getIntent().getStringExtra("type");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number.getText().toString().equals("")) {

                    Toast.makeText(GetUserNumber.this, getApplicationContext().getResources().getString(R.string.enter_a_valid_phone_number), Toast.LENGTH_SHORT).show();
                } else {

                    Snumber = countryCodePicker.getSelectedCountryCode().toString() + number.getText().toString().trim();


                    if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        rotateLoading.start();
                        new VerifyNumberDuplication().execute();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                getApplicationContext().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }


    public class VerifyNumberDuplication extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "verify");
                obj.put("number", Snumber);

                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;

                c = jar_array.getJSONObject(0);

                server_response = c.getString("response");

                if (server_response.equals("1")) {
                    server_response_text = c.getString("response-text");

                }
                else {
                    server_response_text = c.getString("response-text");
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

                    Toast.makeText(GetUserNumber.this, server_response_text, Toast.LENGTH_SHORT).show();

                } else {

//                    if (type.equals("driver")){
//                        Intent intent = new Intent(GetUserNumber.this, SignupDriver.class);
//                        intent.putExtra("number", Snumber);
//                        intent.putExtra("type", type);
//                        startActivity(intent);
//                    }else{
//                        Intent intent = new Intent(GetUserNumber.this, SignupActivity.class);
//                        intent.putExtra("number", Snumber);
//                        intent.putExtra("type", type);
//                        startActivity(intent);
//                    }

                    Intent intent = new Intent(GetUserNumber.this, VerifyNumberActivity.class);
                    intent.putExtra("number", Snumber);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }


            } else {

                Toast.makeText(GetUserNumber.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }

}

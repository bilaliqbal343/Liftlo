package com.app.liftlo.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;

import com.app.liftlo.Login.NumberAuth.GetUserNumber;

import com.app.liftlo.R;

public class RegisterSelection extends AppCompatActivity {

    ImageButton driver, passenger;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_selection);

        driver = findViewById(R.id.ldriver);
        passenger = findViewById(R.id.lpassenger);


        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "driver";
                callIntent();
            }
        });



        passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "passenger";
                callIntent();
            }
        });

    }


    public void callIntent(){


        Intent intent = new Intent(this, GetUserNumber.class);
        intent.putExtra("type", type);
        startActivity(intent);

    }

}

package com.app.liftlo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.app.liftlo.Driver.ActivityDriver;
import com.app.liftlo.Login.LoginActivity;
import com.app.liftlo.Ride.ActivityRide;

public class Splash extends AppCompatActivity {

    Intent myintent;
    ImageView imageView;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        PropertyValuesHolder donutAlphaProperty    = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder donutProgressProperty = PropertyValuesHolder.ofInt("donut_progress", 0, 100);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, donutAlphaProperty, donutProgressProperty);
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();


    }


    @Override
    protected void onResume() {
        super.onResume();

        //In onresume fetching value from sharedpreference
        sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("type", "").equals("")) {


            myintent = new Intent(this, LoginActivity.class);

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    startActivity(myintent);
                    finish();
                }
            }, 2000);


        }
        else if(sharedPreferences.getString("type", "").equals("driver")){

            Intent intent = new Intent(this, ActivityDriver.class);
            startActivity(intent);
            this.finish();

        }
        else {

//            Toast.makeText(this, "Data Found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ActivityRide.class);
            startActivity(intent);
            this.finish();
        }
    }
}

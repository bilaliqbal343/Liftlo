package com.app.liftlo.Ride;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.app.liftlo.Ride.FairComparisson.FairComparisonFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.liftlo.Login.LoginActivity;
import com.app.liftlo.R;
import com.app.liftlo.Ride.AllDrivers.FragmentAllDrivers;
import com.app.liftlo.Ride.History.FragmentRideHistory;
import com.app.liftlo.Ride.Home.FragmentAllRides;
import com.app.liftlo.Ride.MyProfile.FragmentRideProfile;
import com.app.liftlo.Ride.MyRides.FragmentMyRides;
import com.app.liftlo.RideTrackLive;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ServerURL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ActivityRide extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    SharedPreferences sharedPreferences;
    String token, id;
    String server_response, server_response_text;
    Boolean server_check = false;
    JSONObject jp_obj;
    JSONArray jar_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_menu1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void displaySelectedScreen(int itemId) {

        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);

        //creating fragment object
        Fragment fragment = null;
        FragmentManager fm;

        android.app.Fragment fragment1 = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:

                fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

                //see if there is a current ride
                if (sharedPreferences.getString("live", "").equals("yes")) {
                    fragment = new RideTrackLive();
                }else
                fragment = new FragmentAllRides();
                break;
            case R.id.nav_menu2:

                fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new FragmentMyRides();
                break;
            case R.id.nav_menu3:

                fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new FragmentRideHistory();
                break;
            case R.id.nav_menu4:

                fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new FragmentRideProfile();
                break;
            case R.id.nav_menu6:

                fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
            case R.id.nav_menu7:

                fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new FairComparisonFragment();
                break;
            case R.id.nav_menu5:
                logout();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }



    private void logout() {

        //Creating an alert dialog to confirm logout

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(getApplicationContext().getResources().getString(R.string.logout));
        alertDialogBuilder.setIcon(R.drawable.logo_black);
        alertDialogBuilder.setMessage(getApplicationContext().getResources().getString(R.string.are_you_sure_to_logout));
        alertDialogBuilder.setPositiveButton(getApplicationContext().getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        new Logout().execute();


                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("number", "");
                        //Saving the sharedpreferences
                        editor.clear();
                        editor.apply();
                        finish();

                        //Starting login activity
                        Intent intent = new Intent(ActivityRide.this, LoginActivity.class);
                        ActivityRide.this.finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                });

        alertDialogBuilder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }





    public class Logout extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... params) {


            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "logout");

                obj.put("id", id);
                obj.put("token_id", "empty");

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








                } else {
                    Toast.makeText(ActivityRide.this, server_response_text, Toast.LENGTH_SHORT).show();

                }

            } else {

                Toast.makeText(ActivityRide.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


}

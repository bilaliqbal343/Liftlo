package com.app.liftlo.Driver;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import com.app.liftlo.Driver.MyProfile.FragmentDriverProfile;
import com.app.liftlo.Driver.MyRides.FragmentMyRides;
import com.app.liftlo.Login.LoginActivity;
import com.app.liftlo.Driver.History.FragmentHistory;
import com.app.liftlo.Driver.Home.FragmnetDriverLocation;
import com.app.liftlo.DriverTrackLive;
import com.app.liftlo.Ride.Home.FragmentAllRides;
import com.app.liftlo.R;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoCallInvitationData;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallConfigProvider;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.util.Arrays;

public class ActivityDriver extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String number = sharedPreferences.getString("number", "");
        initCallInviteService(number, name);

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

        //creating fragment object
        Fragment fragment = null;
        FragmentManager fm;


//In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:

                //remove fragments from backstack
                fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

                //see if there is a current ride
                if (sharedPreferences.getString("live", "").equals("yes")) {
                    fragment = new DriverTrackLive();
                } else
                    fragment = new FragmnetDriverLocation();
                break;
            case R.id.nav_menu2:

                fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new FragmentMyRides();
                break;
            case R.id.nav_menu3:

                fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new FragmentHistory();
                break;
            case R.id.nav_menu4:

                fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new FragmentDriverProfile();
                break;
            case R.id.nav_menu6:

                fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new FragmentAllRides();
                break;
            case R.id.nav_menu7:

                fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fragment = new com.app.liftlo.Ride.MyRides.FragmentMyRides();
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


                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("number", "");
                        //Saving the sharedpreferences
                        editor.clear();
                        editor.apply();
                        finish();

                        //Starting login activity
                        Intent intent = new Intent(ActivityDriver.this, LoginActivity.class);
                        ActivityDriver.this.finish();
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

    public void initCallInviteService(String number, String name) {
        long appID = 2045343670;
        String appSign = "3789fdd89be894a239a0667858fff7389be2d70bf0f4028094009d191c7ee87d";
        String phone_number = number.replaceAll("92", "");
        String userID = phone_number;
        String userName = name;
        //Application appCtx = ((Application) getActivity().getApplication());
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName);
        ZegoUIKitPrebuiltCallInvitationService.setPrebuiltCallConfigProvider(new ZegoUIKitPrebuiltCallConfigProvider() {
            @Override
            public ZegoUIKitPrebuiltCallConfig requireConfig(ZegoCallInvitationData invitationData) {
                ZegoUIKitPrebuiltCallConfig callConfig = new ZegoUIKitPrebuiltCallConfig();
                /*boolean isVideoCall = invitationData.type == ZegoInvitationType.VIDEO_CALL.getValue();
                callConfig.turnOnCameraWhenJoining = isVideoCall;
                if (!isVideoCall) {*/
                callConfig.bottomMenuBarConfig.buttons = Arrays.asList(
                        ZegoMenuBarButtonName.TOGGLE_MICROPHONE_BUTTON,
                        ZegoMenuBarButtonName.SWITCH_AUDIO_OUTPUT_BUTTON,
                        ZegoMenuBarButtonName.HANG_UP_BUTTON);

                return callConfig;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.logout();
    }


}

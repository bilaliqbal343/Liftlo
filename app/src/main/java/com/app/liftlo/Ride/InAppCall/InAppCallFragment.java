package com.app.liftlo.Ride.InAppCall;


import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.liftlo.Driver.ActivityDriver;
import com.app.liftlo.Driver.History.FragmentHistory;
import com.app.liftlo.Driver.Home.FragmnetDriverLocation;
import com.app.liftlo.Driver.MyProfile.FragmentDriverProfile;
import com.app.liftlo.Driver.MyRides.FragmentMyRides;
import com.app.liftlo.DriverTrackLive;
import com.app.liftlo.Login.LoginActivity;
import com.app.liftlo.R;
import com.app.liftlo.Ride.Home.FragmentAllRides;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.zegocloud.uikit.components.invite.ZegoInvitationType;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoCallInvitationData;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoStartCallInvitationButton;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallConfigProvider;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class InAppCallFragment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_in_app_call);
        ZegoStartCallInvitationButton call;
        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        call = (ZegoStartCallInvitationButton) findViewById(R.id.new_voice_call);
        String phone_number;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            phone_number= null;
        } else {
            phone_number= extras.getString("phone_number");
        }
        String name = sharedPreferences.getString("name", "");
        String uid = generateUserID();
        initCallInviteService(uid, name);
        call.setInvitees(Collections.singletonList(new ZegoUIKitUser(phone_number)));

    }
    public void initCallInviteService(String number, String name) {
        long appID = 2045343670;
        String appSign = "3789fdd89be894a239a0667858fff7389be2d70bf0f4028094009d191c7ee87d";
        String userID = number;
        String userName = name;
        //Application appCtx = ((Application) getActivity().getApplication());
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName);
        ZegoUIKitPrebuiltCallInvitationService.setPrebuiltCallConfigProvider(new ZegoUIKitPrebuiltCallConfigProvider() {
            @Override
            public ZegoUIKitPrebuiltCallConfig requireConfig(ZegoCallInvitationData invitationData) {
                ZegoUIKitPrebuiltCallConfig callConfig = new ZegoUIKitPrebuiltCallConfig();
                boolean isVideoCall = invitationData.type == ZegoInvitationType.VIDEO_CALL.getValue();
                callConfig.turnOnCameraWhenJoining = isVideoCall;
                if (!isVideoCall) {
                    callConfig.bottomMenuBarConfig.buttons = Arrays.asList(
                            ZegoMenuBarButtonName.TOGGLE_MICROPHONE_BUTTON,
                            ZegoMenuBarButtonName.SWITCH_AUDIO_OUTPUT_BUTTON,
                            ZegoMenuBarButtonName.HANG_UP_BUTTON);
                }
                return callConfig;
            }
        });
    }

    private String generateUserID() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        while (builder.length() < 5) {
            int nextInt = random.nextInt(10);
            if (builder.length() == 0 && nextInt == 0) {
                continue;
            }
            builder.append(nextInt);
        }
        return builder.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.logout();
    }


}
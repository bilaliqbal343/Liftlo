package com.app.liftlo.Ride.InAppCall;

import static io.agora.base.internal.ContextUtils.getApplicationContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.liftlo.R;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.ChannelMediaOptions;


public class InAppCallFragment extends Fragment {
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS =
            {
                    Manifest.permission.RECORD_AUDIO
            };
    View v;

    void showMessage(String message) {

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Fill the App ID of your project generated on Agora Console.
    private final String appId = "0e278e808140490599162ea990e46558";
    // Fill the channel name.
    private String channelName = "bilal";
    // Fill the temp token generated on Agora Console.
    private String token = "0486d341658a486e9a316c7845763eb4";
    // An integer that identifies the local user.
    private int uid = 0;
    // Track the status of your connection
    private boolean isJoined = false;

    // Agora engine instance
    private RtcEngine agoraEngine;
    // UI elements
    private TextView infoText;
    private Button joinLeaveButton;

    private void setupVoiceSDKEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = requireContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(config);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_in_app_call, container, false);

        init();
// If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(requireActivity(), REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        setupVoiceSDKEngine();
        return v;
    }

    public void init() {
// Set up access to the UI elements
        joinLeaveButton = (Button) v.findViewById(R.id.joinLeaveButton);
        infoText = v.findViewById(R.id.infoText);
        joinLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinLeaveChannel(view);
            }
        });

    }

    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), REQUESTED_PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote user joining the channel.
        public void onUserJoined(int uid, int elapsed) {
          infoText.setText("Remote user joined: " + uid);
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            // Successfully joined a channel
            isJoined = true;
            showMessage("Joined Channel " + channel);
            infoText.setText("Waiting for a remote user to join");
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            // Listen for remote users leaving the channel
            showMessage("Remote user offline " + uid + " " + reason);
            if (isJoined) {
                infoText.setText("Waiting for a remote user to join");
            }
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            // Listen for the local user leaving the channel
            infoText.setText("Press the button to join a channel");
            isJoined = false;
        }
    };
    private void joinChannel() {
        ChannelMediaOptions options = new ChannelMediaOptions();
        options.autoSubscribeAudio = true;
        // Set both clients as the BROADCASTER.
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        // Set the channel profile as BROADCASTING.
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;

        // Join the channel with a temp token.
        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
        agoraEngine.joinChannel(token, channelName, uid, options);
    }
    public void joinLeaveChannel(View view) {
        if (isJoined) {
            agoraEngine.leaveChannel();
            joinLeaveButton.setText("Join");
        } else {
            joinChannel();
            joinLeaveButton.setText("Leave");
        }
    }
    public void onDestroy() {
        super.onDestroy();
        agoraEngine.leaveChannel();

        // Destroy the engine in a sub-thread to avoid congestion
            RtcEngine.destroy();
            agoraEngine = null;
    }


}
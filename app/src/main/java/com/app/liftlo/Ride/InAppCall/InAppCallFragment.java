package com.app.liftlo.Ride.InAppCall;


import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.liftlo.R;
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


public class InAppCallFragment extends Fragment {

    View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_in_app_call, container, false);
        TextView yourUserID = v.findViewById(R.id.your_user_id);
        String generateUserID = generateUserID();
        yourUserID.setText("Your User ID :" + generateUserID);

        initCallInviteService(generateUserID);

        initVoiceButton();

        initVideoButton();

        return v;
    }

    private void initVideoButton() {
        ZegoStartCallInvitationButton newVideoCall = v.findViewById(R.id.new_video_call);
        newVideoCall.setIsVideoCall(true);
        newVideoCall.setOnClickListener(v -> {

            String targetUserID = "030023233";
            newVideoCall.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID)));
        });
    }

    private void initVoiceButton() {
        ZegoStartCallInvitationButton newVoiceCall = v.findViewById(R.id.new_voice_call);
        newVoiceCall.setIsVideoCall(false);
        newVoiceCall.setOnClickListener(v -> {
            String targetUserID = "030023233";
            newVoiceCall.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID)));
        });
    }

    public void initCallInviteService(String generateUserID) {
        long appID = 2045343670;
        String appSign = "3789fdd89be894a239a0667858fff7389be2d70bf0f4028094009d191c7ee87d";
        String userID = generateUserID;
        String userName = "Bilal";

        ZegoUIKitPrebuiltCallInvitationService.init(new Application(), appID, appSign, userID, userName);
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




}
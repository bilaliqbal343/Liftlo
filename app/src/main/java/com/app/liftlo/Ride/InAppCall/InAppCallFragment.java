package com.app.liftlo.Ride.InAppCall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.liftlo.R;


public class InAppCallFragment extends Fragment {
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_in_app_call, container, false);

        init();
        return v;
    }
    public void init() {


    }



}
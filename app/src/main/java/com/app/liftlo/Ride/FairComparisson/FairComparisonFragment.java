package com.app.liftlo.Ride.FairComparisson;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.liftlo.R;
import com.app.liftlo.Ride.Home.AllRidesAdapter;
import com.app.liftlo.Ride.Home.Filter_model;
import com.app.liftlo.Ride.Home.FragmentAllRides;
import com.app.liftlo.utils.Check_internet_connection;

import java.util.ArrayList;
import java.util.Objects;


public class FairComparisonFragment extends Fragment {
    FareComparisonAdapter adapter;
    View v;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fair_comparison, container, false);
        init();
        return v;
    }

    public void init() {

        adapter=new FareComparisonAdapter(getActivity(),getDummyFareComaprison());
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         listView = v.findViewById(R.id.listview);
         listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

    public ArrayList<FareComparisonModel> getDummyFareComaprison() {
        ArrayList<FareComparisonModel> fareComparisonList = new ArrayList<FareComparisonModel>();
        fareComparisonList.add(new FareComparisonModel("Lahore", "Kasur", "100Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Sahiwal", "400Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Multan", "900Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Gujranwala", "300Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Islamabad", "700Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Kashmir", "2000Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Jehlum", "700Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Gujrat", "500Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Khanewal", "900Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Okara", "400Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Chakwal", "700Rs"));
        fareComparisonList.add(new FareComparisonModel("Lahore", "Pindi", "1000Rs"));

        return fareComparisonList;


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Fair Comparison");
    }


}
package com.app.liftlo.Driver.ManageRides;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.liftlo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class SeeLocation extends Fragment implements OnMapReadyCallback {

    View v;
    String StartingLat, StartingLan, DestinationLat, DestinationLan, DestName, StartingName;
    GoogleMap mMap;
    Marker startMarker, destMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_see_loc, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            StartingLat = bundle.getString("Startlat");
            StartingLan = bundle.getString("Startlon");
            DestinationLat = bundle.getString("Destlat");
            DestinationLan = bundle.getString("Destlon");
            DestName = bundle.getString("Destname");
            StartingName = bundle.getString("StartName");

            Log.e("bundle", StartingLat+"/"+StartingLan+"/"+StartingName+"/"+
                    DestinationLat+"/"+DestinationLan+"/"+DestName);
        }


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_json));
            if (!success) {
                // Handle map style load failure
            }
        } catch (Resources.NotFoundException e) {
            // Oops, looks like the map style resource couldn't be found!
        }



        //change image to bitmap and set as marker
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.car_marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 130, false);


        LatLng latLng = new LatLng(Double.parseDouble(StartingLat), Double.parseDouble(StartingLan));
        startMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(StartingName)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(Double.parseDouble(StartingLat), Double.parseDouble(StartingLan)),
                        12.0f));


        //change image to bitmap and set as marker
        BitmapDrawable bitmapdraw1=(BitmapDrawable)getResources().getDrawable(R.drawable.flag);
        Bitmap b1=bitmapdraw1.getBitmap();
        Bitmap smallMarker1 = Bitmap.createScaledBitmap(b1, 100, 130, false);


        LatLng latLng2 = new LatLng(Double.parseDouble(DestinationLat), Double.parseDouble(DestinationLan));
        destMarker = mMap.addMarker(new MarkerOptions().position(latLng2).title(DestName)
        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker1)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(Double.parseDouble(DestinationLat), Double.parseDouble(DestinationLan)),
                        11.0f));


    }

}


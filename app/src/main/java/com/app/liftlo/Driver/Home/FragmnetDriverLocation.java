package com.app.liftlo.Driver.Home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.liftlo.R;
import com.app.liftlo.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


public class FragmnetDriverLocation extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    double dblLat, dblLon;
    Handler handler;
    EditText loc;
    ImageView currentLoc, getloc;
    Button next;
    View v;
    Fragment fragment;
    String location;
    LatLng Search_latLng;
    Marker marker;
    RotateLoading rotateLoading;
    Bitmap smallMarker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_maps, container, false);


        init();

        //change image to bitmap and set as marker
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.car_marker);
        Bitmap b=bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 100, 130, false);


        askLocation();


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return v;

    }


    public void onMapSearch(View view) {

        location = loc.getText().toString();
        List<Address> addressList = null;

        if (!location.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

                if (addressList.size() > 0) {

                    rotateLoading.stop();
                    rotateLoading.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Address address = addressList.get(0);
                    Search_latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    //remove marker from map if exists
                    if (marker != null) {
                        marker.remove();
                    }

                    marker = mMap.addMarker(new MarkerOptions().position(Search_latLng).title(location)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                    dblLat = Search_latLng.latitude;
                    dblLon = Search_latLng.longitude;

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                            (new LatLng(dblLat, dblLon), 16.5f));

                    Log.e("onMapSearch", dblLat + "/" + dblLon);
                } else {
                    rotateLoading.stop();
                    rotateLoading.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                rotateLoading.stop();
                rotateLoading.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.e("searchResult", e.toString());
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
            }

        }
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


        if (dblLat == 0.0) {
            SmartLocation.with(getActivity()).location()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {

                            dblLat = location.getLatitude();
                            dblLon = location.getLongitude();
                        }
                    });
        }

        Log.e("Location : ", "" + dblLat + " " + dblLon);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.allow_permission), Toast.LENGTH_SHORT).show();

            return;
        }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setIndoorEnabled(true);
//        mMap.setTrafficEnabled(true);





        LatLng latLng = new LatLng(dblLat, dblLon);
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(dblLat, dblLon), 16.0f));

    }


    //ask location
    public void askLocation() {

        //ask all permissions
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            getLocation();
        }
    }


    //all permissions
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void getLocation() {

        SmartLocation.with(getActivity()).location()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        dblLat = location.getLatitude();
                        dblLon = location.getLongitude();
                    }
                });
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        dblLat = gpsTracker.getLatitude();
        dblLon = gpsTracker.getLongitude();


        Log.e("zma Location : ", "" + dblLat + " " + dblLon);
    }


    //show dialog if gps is off
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setMessage(getActivity().getResources().getString(R.string.turn_on_location))
                .setCancelable(true)
                .setPositiveButton(getActivity().getResources().getString(R.string.turn_on_location), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        builder.create();
        builder.show();
    }













    @Override
    public void onResume() {
        super.onResume();

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {

            getLocation();

//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    getLocationAndMoveCamera();
//
//                }
//            }, 2000);


        }


    }


    public void getLocationAndMoveCamera() {

        SmartLocation.with(getActivity()).location()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        dblLat = location.getLatitude();
                        dblLon = location.getLongitude();
                    }
                });
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        dblLat = gpsTracker.getLatitude();
        dblLon = gpsTracker.getLongitude();


        //remove marker from map if exists
        if (marker != null) {
            marker.remove();
        }


        LatLng latLng = new LatLng(dblLat, dblLon);
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("")
        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));


        //moving camera to marker
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(dblLat, dblLon), 17.0f));


        Log.e("zma Location : ", "" + dblLat + " " + dblLon);
    }


    //initialize
    public void init() {

        handler = new Handler();
        loc = v.findViewById(R.id.current_loc);
        currentLoc = v.findViewById(R.id.myloc);
        next = v.findViewById(R.id.next);
        getloc = v.findViewById(R.id.getloc);
        rotateLoading = v.findViewById(R.id.rotateloading);


        getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocationAndMoveCamera();

            }
        });


        currentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                location = loc.getText().toString().trim();

                if (location.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_location_to_search), Toast.LENGTH_SHORT).show();
                } else {
                    rotateLoading.setVisibility(View.VISIBLE);
                    rotateLoading.start();
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    onMapSearch(v);
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dblLat == 0.0) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.unable_to_locate), Toast.LENGTH_SHORT).show();
                    getLocationAndMoveCamera();
                } else {

                    fragment = new FragmentDestination();

                    Bundle bundle = new Bundle();
                    bundle.putString("lat", dblLat + "");
                    bundle.putString("lon", dblLon + "");
                    bundle.putString("StartName",location);
                    fragment.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).
                            addToBackStack("tag").commit();
                }

            }
        });

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getResources().getString(R.string.share_ride));
    }
}

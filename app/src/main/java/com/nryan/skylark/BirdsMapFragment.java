package com.nryan.skylark;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Nathan Ryan x13448212 on 19/02/2017.
 *
 * reference https://www.youtube.com/watch?v=k2KXnT4ZecU
 */

public class BirdsMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    protected static final String TAG = "MapsActivity";
    /**
     * Placing markers on map
     */
    private static final LatLng TURVEY_HIDE = new LatLng(53.498664, -6.171644);
    private final static int MY_PERMISSION_FINE_LOCATION = 101; //permissions check
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    //retrieve Locations child elements from Firebase
    DatabaseReference databaseLocReference = databaseReference.child("Locations");
    Button textBtn; //send current location by SMS
    Double userLatitude = null;
    Double userLongitude = null;
    private GoogleMap mMap;
    private Marker mTurvey;
    private Marker mBirdLoc;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.birds_map_fragment, container, false);

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(15 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        textBtn = (Button) view.findViewById(R.id.btText);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS(); //send current locaton via SMS
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment = initFragment(mapFragment);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //move camera when user clicks on map
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                });
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        //create new marker when user long clicks
                        MarkerOptions options = new MarkerOptions().position(latLng);
                        options.title("Bird Find: " + latLng.toString());

                        options.icon(BitmapDescriptorFactory.defaultMarker());

                        DatabaseReference locRef = databaseReference.child("Locations");
                        locRef.child(user.getUid()).push().setValue(latLng);

                        mMap.addMarker(options);
                    }
                });
                databaseLocReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String latitude = child.child("latitude").getValue().toString();
                            String longitude = child.child("longitude").getValue().toString();

                            double location_right = Double.parseDouble(latitude);
                            double location_left = Double.parseDouble(longitude);
                            LatLng cod = new LatLng(location_right, location_left);

                            MarkerOptions options = new MarkerOptions().position(cod);
                            options.title("Bird Find: ");

                            options.icon(BitmapDescriptorFactory.defaultMarker());

                            mMap.addMarker(options);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //map type
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                //zoom in on Dublin
                LatLng dublinZoom = new LatLng(53.3498, -6.2603);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dublinZoom, 8));

                //permissions check
                if (checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //enable MyLocation layer on map
                    mMap.setMyLocationEnabled(true);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
                    }
                }
                addMarkersToMap();
            }
        });
    }

    private SupportMapFragment initFragment(SupportMapFragment supportMapFragment) {
        if (supportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.FragmentMap, supportMapFragment).commit();
        }
        return supportMapFragment;
    }

  private void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", "");
        smsIntent.putExtra("sms_body", "http://maps.google.com/?q=" + userLatitude + userLongitude);

        try {
            startActivity(smsIntent);
            getActivity().finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(),
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMarkersToMap() {
        mTurvey = mMap.addMarker(new MarkerOptions()
                .position(TURVEY_HIDE)
                .title("Turvey Hide")
                .snippet("The Frank McManus Hide in Turvey Park")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hide_locator))
                .infoWindowAnchor(0.5f, 0.5f));

        mTurvey = mMap.addMarker(new MarkerOptions()
                .position(TURVEY_HIDE)
                .title("Turvey Hide")
                .snippet("The Frank McManus Hide in Turvey Park")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hide_locator))
                .infoWindowAnchor(0.5f, 0.5f));
    }

    //define requests code for each permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) { //if permission has been granted MyLocation is set to enabled
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //enable MyLocation layer on map
                    if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else { //if permissions denied we quit the app
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}

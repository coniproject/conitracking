package com.example.ayabeltran.conitracking;

import android.Manifest;
import android.app.Dialog;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapviewPage extends AppCompatActivity {

    private static final String TAG = "UserProfile";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mfusedlocationproviderclient;

    //Map essentials
    GoogleMap mGoogleMap;
    private static final float DEFAULT_ZOOM = 15f;

    //layout
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
//    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview_page);

//        mDrawerlayout= findViewById(R.id.profilenav);

//        mToggle = new ActionBarDrawerToggle(MapviewPage.this,mDrawerlayout, R.string.open, R.string.close);

//        mDrawerlayout.addDrawerListener(mToggle);
//        mToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        navigationView = findViewById(R.id.navview);
//        navigationView.setNavigationItemSelectedListener(this);



        if (checkGoogleServices()) {
            init();
        }

        getLocationPermission();
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        Toast.makeText(MapviewPage.this, "Connected", Toast.LENGTH_SHORT).show();

    }

    public boolean checkGoogleServices() {
        Log.d(TAG, "checkGoogleServices: Checking Google Services Version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapviewPage.this);

        if (available == ConnectionResult.SUCCESS) {
            //Map Request
            Log.d(TAG, "checkGoogleServices: Google Play Services Available");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Error Occured
            Log.d(TAG, "checkGoogleServices: An error occured. We'll fix it for you");
            Dialog errordialog = GoogleApiAvailability.getInstance().getErrorDialog(MapviewPage.this, available, ERROR_DIALOG_REQUEST);
            errordialog.show();
        } else {
            Toast.makeText(MapviewPage.this, "Unavailable Map Requests. Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
        return false;

    }


    //Application Map on Callback

    private void getLocationPermission() {
        Log.d(TAG, "Getting Location Permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;

                    //Map Initialization
                    initMap();
                }
            }
        }
    }

    private void initMap() {
        Log.d(TAG, "Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mGoogleMap = googleMap;

                if (mLocationPermissionGranted) {
                    getDeviceLocation();
                    if (ActivityCompat.checkSelfPermission(MapviewPage.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                            (MapviewPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }
        });
    }

    private void getDeviceLocation(){
        Log.d(TAG, "Getting current location");

        mfusedlocationproviderclient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){
                Task location = mfusedlocationproviderclient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG,"getDeviceLocation: Location Found");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), DEFAULT_ZOOM);
                        }
                        else {
                            Log.d(TAG,"No Location Found. Please allow location services.");
                            Toast.makeText(MapviewPage.this, "Location not found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        catch (SecurityException e){
            Log.d(TAG,"getDeviceLocation: Security Exception :" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom) {
        Log.d(TAG,"Moving Location to lng : " + latlng.latitude + ", lng : " + latlng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        if(id == R.id.addmember) {
//            Intent toFamReg = new Intent(MapviewPage.this, FamilyRegistration.class);
//            startActivity(toFamReg);
//            finish();
//            mDrawerlayout.closeDrawers();
//        }
//        if(id == R.id.locupdates) {
//            Intent toLocationUp = new Intent(UserProfile.this, LocationUpdates.class);
//            startActivity(toLocationUp);
//            finish();
//            mDrawerlayout.closeDrawers();
//        }
//        if(id == R.id.setsafezones) {
//            Intent toSafeZones = new Intent(UserProfile.this, SafeZonesList.class);
//            startActivity(toSafeZones);
//            finish();
//            mDrawerlayout.closeDrawers();
//        }
//        if(id == R.id.hotlines) {
//            Intent toHotlines = new Intent(UserProfile.this, NearbyPlacesHotlines.class);
//            startActivity(toHotlines);
//            finish();
//            mDrawerlayout.closeDrawers();
//        }
//        if(id == R.id.acctmanage) {
//            Intent toAccSet = new Intent(UserProfile.this, AccountSettings.class);
//            startActivity(toAccSet);
//            finish();
//            mDrawerlayout.closeDrawers();
//        }
//        if(id ==R.id.logout) {
//            Intent toLogin = new Intent(UserProfile.this, UserLogin.class);
//            startActivity(toLogin);
//            finish();
//            mDrawerlayout.closeDrawers();
//        }
//
//        return false;
//    }

}


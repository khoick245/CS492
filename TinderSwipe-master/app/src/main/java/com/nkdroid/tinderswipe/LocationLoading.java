package com.nkdroid.tinderswipe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
public class LocationLoading extends AppCompatActivity {
    public int i =0;     //Used to prevent the the next activity to be loaded over and over
    public LocationManager locationManager;     //Used to help retrieve our location via GPS or network provider
    public LocationListener locationListener;    //Used to see if our location is changing in the way we set it up
    public static LatLng latLng;

    //SharedPreferences app_preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_loading);

//        app_preferences = PreferenceManager.getDefaultSharedPreferences(LocationLoading.this);
//        SharedPreferences.Editor editor = app_preferences.edit();
//        editor.putInt("Radius", 2);
//        editor.commit();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //Calls the ability to retrieve our location service

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {  //detects if our locations has been detected and/or changed
                latLng = new LatLng(location.getLatitude(),location.getLongitude());    //gets the users current latitude and longitude
                if(i==0) {      //the 'i' we set up.
                    Intent intent = new Intent("com.nkdroid.tinderswipe.MainActivity");     //used to start our next activity
                    startActivity(intent);  //used to actually start the activity
                    i++;    //increments 'i' so that the activity wont start up over and over
                }

                finish();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) { //auto-generated, unused


            }

            @Override
            public void onProviderEnabled(String s) {//auto-generated, unused

            }

            @Override
            public void onProviderDisabled(String s) {//auto-generated, unused

            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); // used to tell the location manager how often to update
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener); // used to tell the location manager how often to update
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //auto-generated, unused
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //  if (id == R.id.action_settings) {
        return true;
    }

    //return super.onOptionsItemSelected(item);

}
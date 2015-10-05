package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.EOFException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to add a fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        }else if(id == R.id.map_location){
            openPreferredLocationInMap();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openPreferredLocationInMap(){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        String zipcode = sharedPrefs.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));

        try{
            List<Address> addresses = geocoder.getFromLocationName(zipcode, 1);
            if(addresses!= null && !addresses.isEmpty()){
                Address address = addresses.get(0);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:"+address.getLatitude()+","+address.getLongitude()));

                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }else{
                    Log.d("LOCATION_MAP", "Couldn't call " + address + ", no receiving apps installed!");
                }
            }
        }catch (Exception e){
            Log.e("Error","Error with map", e);
        }
    }

}

package com.lic.myearth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GoogleMap extends AppCompatActivity {
    SupportMapFragment supportMapFragment;

    Button btLocation;
    String Username = "username";
    String Email = "email";
    String Password = "password";
    String Designation = "designation";
    String Gender = "radioGender";
    private static final String HI ="http://192.168.137.138/earthrecycler/googlelocationinsert.php" ;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText textView1, textView2, textView3, textView4, textView5;
    TextView textViewId1, textViewUsername, textViewEmail1, textViewDesignation1;
    EditText textViewId11, textViewUsername11, textViewEmail11, textViewDesignation11, textViewTruckno1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        btLocation = findViewById(R.id.bt_location);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }


        textViewId1 = (TextView) findViewById(R.id.textViewId);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail1 = (TextView) findViewById(R.id.textViewEmail);

        textViewDesignation1 = (TextView) findViewById(R.id.textViewDesignation);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewId1.setText(String.valueOf(user.getUser_id()));
        textViewUsername.setText(user.getUsername());
        textViewEmail1.setText(user.getEmail());

        textViewDesignation1.setText(user.getDesignation());

        textView1 = (EditText) findViewById(R.id.text_view1);
        textView2 = (EditText) findViewById(R.id.text_view2);
        textView3 = (EditText) findViewById(R.id.text_view3);
        textView4 = (EditText) findViewById(R.id.text_view4);
        textView5 = (EditText) findViewById(R.id.text_view5);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(GoogleMap.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(GoogleMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();



                if (location != null){
                    try {
                        Geocoder geocoder = new Geocoder(GoogleMap.this, Locale.getDefault());

                        textViewUsername11 = (EditText) findViewById(R.id.textViewUsername);
                        textViewId11 = (EditText) findViewById(R.id.textViewId);
                        textViewEmail11 = (EditText) findViewById(R.id.textViewEmail);
                        textViewDesignation11 = (EditText) findViewById(R.id.textViewDesignation);

                        final List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                        textView1.setText(Html.fromHtml("" + addresses.get(0).getLatitude()));
                        textView2.setText(Html.fromHtml("" + addresses.get(0).getLongitude()));
                        textView3.setText(Html.fromHtml("" + addresses.get(0).getCountryName()));
                        textView4.setText(Html.fromHtml("" + addresses.get(0).getLocality()));
                        textView5.setText(Html.fromHtml("" + addresses.get(0).getAddressLine(0)));
                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(final Location location) {
                                if (location != null){
                                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                            MarkerOptions options = new MarkerOptions().position(latLng).title(addresses.get(0).getAddressLine(0));
                                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                                            googleMap.addMarker(options);
                                        }
                                    });
                                    final String text_view1=textView1.getText().toString().trim();
                                    final String text_view2=textView2.getText().toString().trim();
                                    final String text_view3=textView3.getText().toString().trim();
                                    final String text_view4=textView4.getText().toString().trim();
                                    final String text_view5=textView5.getText().toString().trim();
                                    final String textViewUsername=textViewUsername11.getText().toString().trim();
                                    final String textViewId=textViewId11.getText().toString().trim();
                                    final String textViewEmail=textViewEmail11.getText().toString().trim();
                                    final String textViewDesignation=textViewDesignation11.getText().toString().trim();
                                    StringRequest stringRequest=new StringRequest(Request.Method.POST, HI, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {



                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }){
                                        @Override
                                        protected Map<String, String> getParams()  {
                                            Map<String,String> parms=new HashMap<String, String>();
                                            parms.put("latitude",text_view1);
                                            parms.put("longitude",text_view2);
                                            parms.put("countryname",text_view3);
                                            parms.put("locality",text_view4);
                                            parms.put("address",text_view5);
                                            parms.put("loginuser",textViewUsername);
                                            parms.put("loginuserid",textViewId);
                                            parms.put("loginuseremail",textViewEmail);
                                            parms.put("loginuserdesignation",textViewDesignation);
                                            return parms;
                                        }
                                    };
                                    RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                                    requestQueue.add(stringRequest);
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }}
        });
    }



}

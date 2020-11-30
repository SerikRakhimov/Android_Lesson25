package com.example.googlemap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                REQUEST_CODE
        );

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(
                new OnMapReadyCallback() {

                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.location);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.icon(bitmapDescriptor);

                                googleMap.addMarker(markerOptions);
                            }
                        });

                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                double lat = marker.getPosition().latitude;
                                double lon = marker.getPosition().longitude;

                                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                                String provider = locationManager.getBestProvider(new Criteria(), true);
                                Location locations = locationManager.getLastKnownLocation(provider);
                                List<String> providerList = locationManager.getAllProviders();
                                if (locations != null && providerList != null && providerList.size() > 0) {

                                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                                        if (addresses != null && addresses.size() > 0) {
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                return false;
                            }
                        });

                        FloatingActionButton zoom_plus = findViewById(R.id.zoom_plus);
                        zoom_plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Float zoom = googleMap.getCameraPosition().zoom + 1;

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(googleMap.getCameraPosition().target)
                                        .zoom(zoom)
                                        .bearing(googleMap.getCameraPosition().bearing)
                                        .tilt(googleMap.getCameraPosition().tilt)
                                        .build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.animateCamera(cameraUpdate);
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Zoom = " + Float.toString(zoom), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        FloatingActionButton zoom_minus = findViewById(R.id.zoom_minus);
                        zoom_minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Float zoom = googleMap.getCameraPosition().zoom - 1;
                                if (zoom <= 2f) {
                                    zoom = 2f;
                                }

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(googleMap.getCameraPosition().target)
                                        .zoom(zoom)
                                        .bearing(googleMap.getCameraPosition().bearing)
                                        .tilt(googleMap.getCameraPosition().tilt)
                                        .build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.animateCamera(cameraUpdate);
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Zoom = " + Float.toString(zoom), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        FloatingActionButton bearing_plus = findViewById(R.id.bearing_plus);
                        bearing_plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Float bearing = googleMap.getCameraPosition().bearing + 30;
                                if (bearing > 360f) {
                                    bearing = 360f;
                                }

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(googleMap.getCameraPosition().target)
                                        .zoom(googleMap.getCameraPosition().zoom)
                                        .bearing(bearing)
                                        .tilt(googleMap.getCameraPosition().tilt)
                                        .build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.animateCamera(cameraUpdate);

                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Bearing = " + Float.toString(bearing), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        FloatingActionButton bearing_minus = findViewById(R.id.bearing_minus);
                        bearing_minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Float bearing = googleMap.getCameraPosition().bearing - 30;
                                if (bearing < 0f) {
                                    bearing = 0f;
                                }

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(googleMap.getCameraPosition().target)
                                        .zoom(googleMap.getCameraPosition().zoom)
                                        .bearing(bearing)
                                        .tilt(googleMap.getCameraPosition().tilt)
                                        .build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.animateCamera(cameraUpdate);
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Bearing = " + Float.toString(bearing), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        FloatingActionButton tilt_plus = findViewById(R.id.tilt_plus);
                        tilt_plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Float tilt = googleMap.getCameraPosition().tilt + 5;
                                if (tilt > 45f) {
                                    tilt = 45f;
                                }

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(googleMap.getCameraPosition().target)
                                        .zoom(googleMap.getCameraPosition().zoom)
                                        .bearing(googleMap.getCameraPosition().bearing)
                                        .tilt(tilt)
                                        .build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.animateCamera(cameraUpdate);

                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Tilt = " + Float.toString(tilt), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        FloatingActionButton tilt_minus = findViewById(R.id.tilt_minus);
                        tilt_minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Float tilt = googleMap.getCameraPosition().tilt - 5;
                                if (tilt < 0f) {
                                    tilt = 0f;
                                }

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(googleMap.getCameraPosition().target)
                                        .zoom(googleMap.getCameraPosition().zoom)
                                        .bearing(googleMap.getCameraPosition().bearing)
                                        .tilt(tilt)
                                        .build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.animateCamera(cameraUpdate);

                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Tilt = " + Float.toString(tilt), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                    }
                });
    }

}
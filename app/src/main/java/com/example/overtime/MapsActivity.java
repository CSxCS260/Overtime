package com.example.overtime;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.overtime.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ActivityMapsBinding binding;
    PlaceAutocompleteFragment placeAutoComplete;
    String MAPS_API_KEY = "";
    private LatLng location;
    private String locationName;
    private FusedLocationProviderClient fusedLocationClient;
    List<Address> listGeoCoder;
    private LocationRequest locationRequest;
    private FloatingActionButton mapFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        locationRequest = new LocationRequest();
        setContentView(binding.getRoot());

        mapFab = findViewById(R.id.mapFab);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), MAPS_API_KEY);
        }

//        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i("Maps", "Place: " + place.getName() + ", " + place.getId());
                location = place.getLatLng();
                locationName = place.getName();
                geoLocate();

            }
        });

        if (isLocationPermissionGranted()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getLastKnownLocation();
        }
        else {
            requestLocationPermission();
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            getLastKnownLocation();
        }

        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doneMap = new Intent(MapsActivity.this, AddAlarm.class);
                doneMap.putExtra("locationLat", location.latitude);
                doneMap.putExtra("locationLong", location.longitude);
                doneMap.putExtra("locationName", locationName);
                setResult(RESULT_OK, doneMap);
                finish();
            }
        });

    }
        @SuppressLint("MissingPermission")
        private void getLastKnownLocation () {
            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
//                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        LatLng homeLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        map.animateCamera(CameraUpdateFactory.newLatLng(homeLatLng));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng,15));

                    }

                }
            });
        }


        public void geoLocate(){
            map.animateCamera(CameraUpdateFactory.newLatLng(location));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));
            map.clear();
            map.addMarker(new MarkerOptions()
                    .position(location)
                    .snippet(""));
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);
        }
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(@NonNull Location location) {
//                    Toast.makeText(getApplicationContext(), "Got to success", Toast.LENGTH_SHORT).show();
//                    LatLng lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(lastLatLng).title("You are here"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLatLng));
//                }
//            });
//        }
//        Add a marker in Sydney and move the camera
//        mMap.addMarker(new MarkerOptions().position(location).title("Marker in " + locationName));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                map.clear();
                map.addMarker(new MarkerOptions()
                        .position(point)
                        .snippet(""));
                location = point;
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                    Address address = addresses.get(0);
                    location = new LatLng(point.latitude, point.longitude);
                    locationName = address.getAddressLine(0);

//                    geoLocate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

}
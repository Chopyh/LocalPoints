package com.mulweb.localpoints;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;

import sampledata.TestConnection;
import com.mulweb.localpoints.auth.google.AuthActivity;
import com.mulweb.localpoints.fragment.main.ForumFragment;
import com.mulweb.localpoints.fragment.main.ListFragment;
import com.mulweb.localpoints.fragment.main.MapFragment;
import com.mulweb.localpoints.fragment.main.SettingsFragment;
import com.mulweb.localpoints.fragment.main.LocationObject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Apply window insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set default locale to Spanish (Spain)
        Locale locale = new Locale("es", "ES");
        Locale.setDefault(locale);
        PlacesClient placesClient;

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), ("AIzaSyBTv84Asl4WhU94-TxjxDowuGmvWR6wNto"));
        }
        placesClient = Places.createClient(this);

        // Configure the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        // Initialize bottom navigation view
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // If no user is logged in, redirect to AuthActivity
        if (currentUser == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }

        // Check for internet connection
        if (TestConnection.isNetworkAvailable(this)) {
            loadFragment(new MapFragment());
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }

        // Load the map fragment
        loadFragment(new MapFragment());
        nav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            if (item.getItemId() == R.id.nav_item_map) {
                fragment = new MapFragment();
            } else if (item.getItemId() == R.id.nav_item_list) {
                fragment = new ListFragment();
            } else if (item.getItemId() == R.id.nav_item_forum) {
                fragment = new ForumFragment();
            } else if (item.getItemId() == R.id.nav_item_settings) {
                fragment = new SettingsFragment();
            }

            return loadFragment(fragment);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap nMap = googleMap;

        // Set the initial location to Logroño
        LatLng logroño = new LatLng(42.4627, -2.4445);
        // Move the camera to the initial location with a zoom level of 13
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(logroño, 13));
        // Add a marker at the initial location with the title "Logroño"
        nMap.addMarker(new MarkerOptions().position(logroño).title("Logroño"));

        // Set a listener for when the user clicks on the map
        nMap.setOnMapClickListener(latLng -> {
            // Clear existing markers
            nMap.clear();

            double lat = latLng.latitude; // Get latitude
            double lng = latLng.longitude; // Get longitude

            // Create a LocationObject with the selected coordinates
            LocationObject locationObject = new LocationObject(lat, lng);
            // Add a marker at the selected location
            nMap.addMarker(new MarkerOptions().position(latLng).title("Selected location"));

            // Fetch place details using Places API
            fetchPlaceDetails(locationObject);
        });
    }

private String fetchPlaceDetails(LocationObject locationObject) {
    List<Place.Field> placeFields = List.of(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER, Place.Field.RATING);

    // Create a request to fetch place details
    FetchPlaceRequest request = FetchPlaceRequest.newInstance(locationObject.toString(), placeFields);

    PlacesClient placesClient = Places.createClient(this);
    placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
        Place place = response.getPlace();
        String placeName = place.getName(); // Get place name
        String placeAddress = place.getAddress(); // Get place address
        String placePhone = place.getPhoneNumber(); // Get place phone number
        double placeRating = place.getRating(); // Get place rating

        // Log and display place details
        String placeDetails = "Nombre: " + placeName + ", Dirección: " + placeAddress + ", Teléfono: " + placePhone + ", Calificación: " + placeRating;
        Log.d("placeDetails", placeDetails);
        Toast.makeText(this, placeDetails, Toast.LENGTH_LONG).show();
    }).addOnFailureListener((exception) -> {
        Log.e("placeDetails", "Place not found: " + exception.getMessage());
    });

    return locationObject.toString();
}

    // Load the specified fragment
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
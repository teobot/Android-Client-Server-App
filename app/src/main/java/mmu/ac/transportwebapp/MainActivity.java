package mmu.ac.transportwebapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Declaring Instance variables
    private double lat = 0.0;
    private double lng = 0.0;
    private boolean map_view = true;
    // Declaring my views and displays
    private TextView LatAndLngDisplay;
    private LinearLayout text_display_output;
    private LinearLayout map_view_display;
    private Button change_map_button;
    // Declaring the mapView
    private MapView mapView;
    private MapboxMap map;
    // Declaring the current location variable
    Location homeLocation = new Location("Home");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Store the mapbox key here, Strings.xml sometimes doesn't read the key, meaning mapbox fails to load
        Mapbox.getInstance(this, "pk.eyJ1IjoiYjBuZmlyZSIsImEiOiJjazhnajJrcXQwMXJxM21wZWM2Nzg1cDB0In0.Ok8ZzVwCVnY43ubuooEfVw");
        setContentView(R.layout.activity_main);

        // Declaring my views and displays by supplying the view by id
        LatAndLngDisplay = findViewById(R.id.latLngDisplay);
        text_display_output = findViewById(R.id.text_based_display);
        change_map_button = findViewById(R.id.change_display_button);
        map_view_display = findViewById(R.id.map_container);

        // Create a array of the permissions this app requires, so I can check if the user has given consented before using
        String[] requiredPermissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        // For each of the permissions check if granted and if not then close the app
        boolean ok = true;
        for (int i = 0; i < requiredPermissions.length; i++) {
            int result = ActivityCompat.checkSelfPermission(this, requiredPermissions[i]);
            if (result != PackageManager.PERMISSION_GRANTED) {
                ok = false;
            }
        }
        if (!ok) {
            ActivityCompat.requestPermissions(this, requiredPermissions, 1);
            System.exit(0);
        }

        // Create a new location manager to manage the GPS location service
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Create a new location listener and update the lat and lng variables if the GPS location has changed.
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                LatAndLngDisplay.setText("LAT:" + lat + ", LNG:" + lng);
                homeLocation.setLatitude(lat);
                homeLocation.setLongitude(lng);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        });
        // Here I initiate the mapView, And trigger the
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    /**
     * Set the type of mapbox display and get the latest
     * stations results based on the GPS location
     * @param mapboxMap
     */
    @SuppressWarnings("deprecation")
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS);
        stations_refresh_onClick(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * This changes the display from the mapbox to the textbased display
     * @param v
     */
    public void change_display(View v) {
        TextView buttonText = (TextView) v;
        /**
         * If the map_view display is enabled and the button is clicked
         * the user wants to change to a text based display,
         * So I hide the display and change the button text to reflect,
         * Otherwise the text based display is shown
         */
        if (map_view) {
            // If the user is viewing the map and wants to view the text based display
            map_view_display.setVisibility(View.INVISIBLE);
            text_display_output.setVisibility(View.VISIBLE);
            buttonText.setText("VIEW BY MAP");
            map_view = false;
        } else {
            // If the user is viewing the text based display and wants to view the map
            map_view_display.setVisibility(View.VISIBLE);
            text_display_output.setVisibility(View.INVISIBLE);
            buttonText.setText("VIEW BY TEXT");
            map_view = true;
        }
    }

    /**
     * Here I call the AsyncTask to execute the getStationsBylocation class which displays the stations
     * @param v
     */
    public void stations_refresh_onClick(View v) {
        try {
            new getStationsByLocationAsyncTask().execute(lat, lng);
        } catch (Exception e) {

        }
    }

    /**
     * Here I display the arraylist of Stations
     * I create a TableRow to be inserted into the TableLayout inside the scrollview
     * I also create textViews to be inserted inside the TableRows to display the name and distance away
     * I also add the map marker into the mapbox so that when the user views the mapbox then camera will follow and correctly display the markers within the bounds
     * @param stations
     */
    public void display_stations(ArrayList < Station > stations) {
        // Remove all the views from the text based display
        text_display_output.removeAllViews();
        // Clear the map from any markers
        map.clear();
        /**
         * Start the creation of a bounds builder, This creates the bounds for the camera to display,
         * I insert all the lng and lats to the builder so It can fit all the markers in the mapbox view
         */
        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        //Add the current GPS location marker to the mapbox so the user can view where they are in relation to the other markers
        map.addMarker(
                new MarkerOptions()
                    .position(new LatLng(lat,lng))
                    .title("Current Location")
        );
        /**
         * Here is the for each loop for the stations that have been returned from the asnyc task
         */
        for (Station station: stations) {
            // Create two new tableRows and set the params to fit the width and height
            TableRow displayStationRow = new TableRow(this);
            displayStationRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TableRow displayDistanceRow = new TableRow(this);
            displayDistanceRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            // Insert the lat and lng to be considered for the camera bounds
            latLngBounds.include(new LatLng(station.getLatitude(), station.getLongitude()));

            /**
             * Create a text View for the station name
             * Set the typeface to bold
             * set the params to wrap the content
             * set the text size and colour
             */
            TextView display_station_name = new TextView(this);
            display_station_name.setTypeface(null, Typeface.BOLD);
            display_station_name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1));
            display_station_name.setText(station.getStationName());
            display_station_name.setTextSize(18.0f);
            display_station_name.setTextColor(Color.BLACK);
            // ...

            /**
             * Create a text view for the distance too the station field,
             * set the params to wrap the content
             * calculate the distance between the home location of the user and itself,
             * If the distance is less then 1km then display in meters
             * Also set the text size
             */
            TextView distance_too = new TextView(this);
            distance_too.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1));
            Location locationOfStation = new Location("StationLocation");
            locationOfStation.setLatitude(station.getLatitude());
            locationOfStation.setLongitude(station.getLongitude());
            double distance_in_km = Math.round(homeLocation.distanceTo(locationOfStation)) / 1000.0;
            if (distance_in_km < 1) {
                distance_too.setText(distance_in_km * 1000 + "M away");
            } else {
                distance_too.setText(distance_in_km + "km away");
            }
            distance_too.setTextSize(12.0f);
            // ...

            // Add the station location marker to the map, with the title of the station and the distance away
            map.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(station.getLatitude(), station.getLongitude()))
                            .title(station.getStationName())
                            .snippet(distance_too.getText().toString())
            );

            // Add the textViews to the correct tableRow
            displayStationRow.addView(display_station_name);
            displayDistanceRow.addView(distance_too);

            //Add a slight gap between the rows
            displayDistanceRow.setPadding(0, 0, 0, 50);

            // Add the TableRows to the tableLayout to be displayed
            text_display_output.addView(displayStationRow);
            text_display_output.addView(displayDistanceRow);
        }

        try {
            // slowly pan the camera to display all the current markers using the boundery builder
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 250), 5000);
        } catch (Exception e) {

        }
    }

    /**
     * Here is the async function that triggers on button click,
     * This task needs lat and lng so pass them through as multiple args,
     * The doInBackground runs on the background thread and can't update the UI so when finished pass the
     * arraylist of the stations to onPostExecute to display the stations info
     */
    public class getStationsByLocationAsyncTask extends AsyncTask < Double, Integer, ArrayList < Station >> {
        @Override
        protected ArrayList < Station > doInBackground(Double...args) {
            return new TransportWebService().getStationsFromLocation(args[0], args[1]);
        }
        @Override
        protected void onPostExecute(ArrayList < Station > Station) {
            display_stations(Station);
        }
    }


}
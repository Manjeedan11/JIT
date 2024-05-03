package com.affc.ait;

import android.content.Intent;

import static com.azure.android.maps.control.options.CameraOptions.center;
import static com.azure.android.maps.control.options.CameraOptions.zoom;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeColor;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeDashArray;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeWidth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Branch;
import com.affc.ait.models.Coordinate;
import com.affc.ait.utils.GeoLocationParser;
import com.azure.android.maps.control.AzureMaps;
import com.azure.android.maps.control.MapControl;
import com.affc.ait.models.Course;
import com.azure.android.maps.control.layer.LineLayer;
import com.azure.android.maps.control.layer.SymbolLayer;
import com.azure.android.maps.control.source.DataSource;
import com.google.android.gms.maps.model.LatLng;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

public class SelectBranch extends AppCompatActivity {

    static {
        AzureMaps.setSubscriptionKey("YuAD1rFoXVTjQa_-EmN-0zcFygvuCAsrncE4LVdOd0k");
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;


    MapControl mapControl;
    List<Coordinate> coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_branch);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        Intent intent = getIntent();
        int courseID = intent.getIntExtra("course_ID", -1);
        List<Branch> branches = databaseHandler.getBranchesForACourse(courseID);
        coordinates = new ArrayList<>();

        mapControl = findViewById(R.id.mapcontrol);

        mapControl.onCreate(savedInstanceState);
        Coordinate myLocation = getLocation();
        //Wait until the map resources are ready.
        mapControl.onReady(map -> {
            //Add your post map load code here.
            map.setCamera(center(Point.fromLngLat(myLocation.getLongitude(), myLocation.getLatitude())), zoom(17));

            for (Coordinate coordinate : coordinates) {
                DataSource source = new DataSource();
                map.sources.add(source);

                List<Point> temp = new ArrayList<>();
                temp.add(Point.fromLngLat(myLocation.getLongitude(), myLocation.getLatitude()));
                temp.add(Point.fromLngLat(coordinate.getLongitude(), coordinate.getLatitude()));

                //Create a point feature and add it to the data source.
                source.add(Feature.fromGeometry(LineString.fromLngLats(temp)));

                //Create a symbol layer and add it to the map.
                map.layers.add(new LineLayer(source,
                        strokeColor("red"),
                        strokeWidth(4f),
                        strokeDashArray(new Float[]{3f, 3f})));
            }

        });
        renderBranchesForCourses(branches);
    }

    private Coordinate getLocation() {
        // Check if the user has granted location permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get the location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);

                    return new Coordinate(latitude, longitude);
                } else {
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Request location permissions
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        return null;
    }

    // Method to handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the location
                getLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void renderBranchesForCourses(List<Branch> branches) {
        LinearLayout branchLayout = findViewById(R.id.branchLayout);

        for (Branch branch : branches) {
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(12, 12, 12, 12);
            cardView.setLayoutParams(params);

            cardView.setRadius(15);
            cardView.setCardElevation(6);

            TextView textViewBranchName = new TextView(this);
            textViewBranchName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textViewBranchName.setText(branch.getBranch_name());
            textViewBranchName.setTextSize(16);
            textViewBranchName.setPadding(16, 16, 16, 16);

            cardView.addView(textViewBranchName);

            branchLayout.addView(cardView);
            new AsyncTask<String, Void, Coordinate>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                protected Coordinate doInBackground(String... locations) {
                    GeoLocationParser geoLocationParser = new GeoLocationParser();
                    double[] c = geoLocationParser.parseLocation(locations[0]);
                    Log.e("Location", c[0] + " " + c[1]);
                    coordinates.add(new Coordinate(c[0], c[1]));
                    return new Coordinate(c[0], c[1]);
                }

                @Override
                protected void onPostExecute(Coordinate coordinate) {
                    coordinates.add(coordinate);
                }
            }.execute(branch.getLocation());


        }
    }
}

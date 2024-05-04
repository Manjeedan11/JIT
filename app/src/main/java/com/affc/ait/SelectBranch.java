package com.affc.ait;

import android.content.Intent;

import static com.azure.android.maps.control.options.CameraOptions.center;
import static com.azure.android.maps.control.options.CameraOptions.zoom;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeColor;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeDashArray;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeWidth;
import static com.azure.android.maps.control.options.StyleOptions.style;
import static com.azure.android.maps.control.options.SymbolLayerOptions.iconImage;
import static com.azure.android.maps.control.options.SymbolLayerOptions.textColor;
import static com.azure.android.maps.control.options.SymbolLayerOptions.textField;
import static com.azure.android.maps.control.options.SymbolLayerOptions.textSize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.azure.android.maps.control.options.MapStyle;
import com.azure.android.maps.control.source.DataSource;
import com.google.android.gms.maps.model.LatLng;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        Log.e("CID", courseID + " ");
        List<Branch> branches = databaseHandler.getBranchesForACourse(courseID);
        for (Branch branch : branches) {
            Log.e("Branches", branch.getBranch_name());
            Log.e("Branches", branch.getLocation());
        }
        coordinates = new ArrayList<>();

        mapControl = findViewById(R.id.mapcontrol);
        renderBranchesForCourses(branches);
        mapControl.onCreate(savedInstanceState);
        Coordinate myLocation = getLocation();
        //Wait until the map resources are ready.
        mapControl.onReady(map -> {
            //Add your post map load code here.
            map.setCamera(center(Point.fromLngLat(myLocation.getLongitude(), myLocation.getLatitude())), zoom(17));
            map.setStyle(style(MapStyle.SATELLITE));

            for (Branch branch: branches) {
                DataSource source = new DataSource();
                map.sources.add(source);
                //parse location with parser
                GeoLocationParser parser = new GeoLocationParser();
                Coordinate coordinate;
                try {
                    double[] c = parser.parseLocation(branch.getLocation());
                    coordinate = new Coordinate(c[0], c[1]);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                List<Point> temp = new ArrayList<>();
                temp.add(Point.fromLngLat(myLocation.getLongitude(), myLocation.getLatitude()));
                temp.add(Point.fromLngLat(coordinate.getLongitude(), coordinate.getLatitude()));

                //Create a point feature and add it to the data source.
                source.add(Feature.fromGeometry(LineString.fromLngLats(temp)));

                // Create a data source and add it to the map.
                DataSource dataSource = new DataSource();
                map.sources.add(dataSource);

// Create a point at the geographic coordinates where you want to place the marker.
                Point point = Point.fromLngLat(coordinate.getLongitude(), coordinate.getLatitude());

// Create a feature from the point and add it to the data source.
                Feature feature = Feature.fromGeometry(point);
                dataSource.add(feature);

// Create a symbol layer using the data source and add it to the map.
                SymbolLayer symbolLayer = new SymbolLayer(dataSource,
                        textField(branch.getBranch_name()), // Set the marker text.
                        textSize(12f), // Set the text size.
                        textColor(Color.RED) // Set the text color.
                );
                map.layers.add(symbolLayer);


                //Create a symbol layer and add it to the map.
                map.layers.add(new LineLayer(source,
                        strokeColor("red"),
                        strokeWidth(4f),
                        strokeDashArray(new Float[]{3f, 3f})));
            }

        });

        ImageButton button = findViewById(R.id.hamburger);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(SelectBranch.this, button);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.nav_menu_silder, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        String title = (String) menuItem.getTitle();
                        if (title.equals("Login")) {
                            Intent intent = new Intent(SelectBranch.this, Login.class);
                            startActivity(intent);
                        } else if (title.equals("Register")) {
                            Intent intent = new Intent(SelectBranch.this, Register.class);
                            startActivity(intent);
                        } else if (title.equals("Courses")) {
                            Intent intent = new Intent(SelectBranch.this, Home.class);
                            startActivity(intent);
                        } else if (title.equals("Logout")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            Log.e("email", "onMenuItemClick: " + sharedPreferences.getString("email", "") + sharedPreferences.getInt("studentID", -1));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //clear preferences
                            editor.clear();
                            editor.apply();
                            finish();
                            Intent intent = new Intent(SelectBranch.this, Login.class);
                            startActivity(intent);
                        } else if (title.equals("Admin")) {
                            Intent intent = new Intent(SelectBranch.this, AdminLogin.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });


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
                    Toast.makeText(this, "Unable to get current location. defaulting", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Request location permissions
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        return new Coordinate(6.9063, 79.8708); //return default
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
                Log.e("denied", "onRequestPermissionsResult: " + grantResults[0]);
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

            cardView.setOnClickListener(v -> {
               Intent intent = getIntent();
                Intent starter = new Intent(SelectBranch.this, ConfirmCourse.class);
                starter.putExtra("course_ID", intent.getIntExtra("course_ID", -1));
                starter.putExtra("branch_ID", branch.getBranch_id());
                startActivity(starter);
            });

            cardView.addView(textViewBranchName);

            branchLayout.addView(cardView);


        }
    }
}

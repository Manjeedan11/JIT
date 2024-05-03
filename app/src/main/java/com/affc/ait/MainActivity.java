package com.affc.ait;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here
                int id = item.getItemId();

                if(id == R.id.loginitem){
                    // Load Login layout
                }
                else if (id == R.id.registeritem) {
                    // Load Register layout
                    setContentView(R.layout.activity_register);
                }
                else if (id == R.id.coursesitem) {
                    // Load Courses layout
                    setContentView(R.layout.activity_home);
                }
                else if (id == R.id.logoutitem) {
                    // Handle logout
                    handleLogout();
                    return true; // return here as we don't want to load a layout
                }
                else if (id == R.id.adminitem){
                    // Load Admin layout
                    setContentView(R.layout.activity_admin_login);
                }

                // Close the drawer after item is clicked
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }


    private void handleLogout() {
        // Implement logout functionality here
        // For example, clear session, navigate to login screen, etc.
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }


}

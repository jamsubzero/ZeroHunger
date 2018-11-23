package com.example.jam.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jam.myapplication.todb.Sender;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.Provider;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private int SELECTED_NAV = R.id.nav_map; //  map by default

    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //http://eresponse.tk/ZeroHunger/insertNeed.php
    String url = "http://eresponse.tk/ZeroHunger/insertNeed.php";

    MyLocation myLocation;

    FloatingActionButton fab ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(SELECTED_NAV == R.id.nav_needs) {
                    showAddNeedsDialog();
                }else if(SELECTED_NAV == R.id.nav_have){
                    //TODO showAddHavesDialog()
                    showAddHavesDialog();
                }


            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    myLocation = new MyLocation(MainActivity.this);

        //---- goto need by default
        goto_map();

    }

    @Override
    public void onPause() {
        super.onPause();
        myLocation.cancelTimer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {

            goto_map();

        } else if (id == R.id.nav_needs) {

            goto_need();

        } else if (id == R.id.nav_have) {

            goto_have();

        } else if (id == R.id.nav_reports) {

        } else if (id == R.id.nav_reportNow) {

        } else if (id == R.id.nav_tipid) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showAddNeedsDialog(){

        // Snackbar.make(view, "Replalckkkke now with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("What do you need?");
        builder1.setCancelable(true);
        builder1.setView(R.layout.add_need_dialog_layout);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_need_dialog_layout, null);
        builder1.setView(dialogView);
        Location lo;
        builder1.setPositiveButton(
                "SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                            @Override
                            public void gotLocation(Location location){
                                EditText item = dialogView.findViewById(R.id.item);
                                EditText desc = dialogView.findViewById(R.id.desc);
                                String sUserID = "jam";
                                String sItem_name = item.getText().toString();
                                String sDesc = desc.getText().toString();

                                //
                                String sLati = String.valueOf(location.getLatitude());
                                String  sLongi = String.valueOf(location.getLongitude());
                                //Got the location!
                                String sNeed_have = "0"; // 0 for need




                                // save(sUserID, sItem_name, sDesc, sLati, sLongi, sNeed_have);
                                Sender s=new Sender(MainActivity.this,url,sUserID, item, desc, sLati, sLongi, sNeed_have);
                                s.execute();
                            }
                        };

                        myLocation.getLocation(locationResult);
                        //
                        //=============================END FOR LOCATION




//                        EditText name = dialogView.findViewById(R.id.desc);
//                        Toast.makeText(MainActivity.this, name.getText(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showAddHavesDialog(){

        // Snackbar.make(view, "Replalckkkke now with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("What do you have?");
        builder1.setCancelable(true);
        builder1.setView(R.layout.add_need_dialog_layout);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_need_dialog_layout, null);
        builder1.setView(dialogView);
        Location lo;
        builder1.setPositiveButton(
                "SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                            @Override
                            public void gotLocation(Location location){
                                EditText item = dialogView.findViewById(R.id.item);
                                EditText desc = dialogView.findViewById(R.id.desc);
                                String sUserID = "jam";
                                String sItem_name = item.getText().toString();
                                String sDesc = desc.getText().toString();

                                //
                                String sLati = String.valueOf(location.getLatitude());
                                String  sLongi = String.valueOf(location.getLongitude());
                                //Got the location!
                                String sNeed_have = "1"; // 1 for have




                                // save(sUserID, sItem_name, sDesc, sLati, sLongi, sNeed_have);
                                Sender s=new Sender(MainActivity.this,url,sUserID, item, desc, sLati, sLongi, sNeed_have);
                                s.execute();
                            }
                        };

                        myLocation.getLocation(locationResult);
                        //
                        //=============================END FOR LOCATION




//                        EditText name = dialogView.findViewById(R.id.desc);
//                        Toast.makeText(MainActivity.this, name.getText(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }




    private void goto_map() {
        SELECTED_NAV = R.id.nav_map;
        MapFragment mapFragment = new MapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, mapFragment).commit();
        fab.setVisibility(View.INVISIBLE);
    }

    private void goto_need(){
        SELECTED_NAV = R.id.nav_needs;
        NeedFragment needFragment = new NeedFragment();
        //----
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, needFragment).commit();
        fab.setVisibility(View.VISIBLE);
    }

    private void goto_have(){
        SELECTED_NAV = R.id.nav_have;
        HaveFragment haveFragment = new HaveFragment();
        //----
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, haveFragment).commit();
        fab.setVisibility(View.VISIBLE);
    }
    //====================





} //== END OF CLASS

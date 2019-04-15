package com.jamworkspro.explorelocationgms;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

public class JMLocationGMS
{
  //Member Properties
  FusedLocationProviderClient m_FusedLocationClient;
  boolean m_bRequestingLocationUpdates;
  LocationCallback m_LocationCallback;
  LocationRequest m_LocationRequest;
  Task<Location> m_StartLocation;
  Context m_Context;

  JMLocationGMS(Context context, LocationCallback locationCallback)
  {
    //Initialize the activity objects
    m_LocationCallback = locationCallback;
    m_Context = context;

    m_LocationRequest = new LocationRequest();
    m_LocationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
    m_LocationRequest.setInterval(10);
    m_LocationRequest.setFastestInterval(10);
    m_bRequestingLocationUpdates = true;
    m_FusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

    int p = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    int q = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    int p1 = PackageManager.PERMISSION_GRANTED;

    if (p != p1 && q != p1)
    {
      //Result is passed to the onRequestPermissionsResult override from the calling Activity
      ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
    }

    m_StartLocation =  m_FusedLocationClient.getLastLocation();
    m_StartLocation.addOnSuccessListener((Activity) context, new OnSuccessListener<Location>()
    {
      @Override
      public void onSuccess(Location location)
      {
        // Got last known location. In some rare situations this can be null.
        if (location != null)
        {
          startLocationUpdates();
        }
      }
    });
  }

  private void startLocationUpdates()
  {
    if(ActivityCompat.checkSelfPermission(m_Context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
       ActivityCompat.checkSelfPermission(m_Context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    {
      return;
    }
    m_FusedLocationClient.requestLocationUpdates(m_LocationRequest, m_LocationCallback, null /* Looper */);
  }
}

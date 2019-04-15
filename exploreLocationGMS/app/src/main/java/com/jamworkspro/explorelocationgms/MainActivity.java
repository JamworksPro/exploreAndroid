package com.jamworkspro.explorelocationgms;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
  TextView m_txtDistance;
  TextView m_txtTime;
  TextView m_txtSpeed;
  TextView m_txtBearing;
  TextView m_txtLatitude;
  TextView m_txtLongitude;
  TextView m_txtAltitude;
  TextView m_txtDateTime;

  Location m_CurrentLocation;
  Location m_PreviousLocation;

  JMLocationGMS m_LocationGMS;
  LocationCallback m_LocationCallback;

  private void doUpdateLocation(Location location)
  {
    m_PreviousLocation = m_CurrentLocation;
    m_CurrentLocation = location;

    double s = m_CurrentLocation.getSpeed();
    double b = m_CurrentLocation.getBearing();
    double d = m_CurrentLocation.distanceTo(m_PreviousLocation);
    double t = m_CurrentLocation.getElapsedRealtimeNanos() - m_PreviousLocation.getElapsedRealtimeNanos();
    double o = m_CurrentLocation.getLongitude();
    double a = m_CurrentLocation.getLatitude();
    double l = m_CurrentLocation.getAltitude();
    long m = m_CurrentLocation.getTime();

    //Includes conversions for Km/hr to MPH and meters to feet
    m_txtSpeed.setText(String.format("%.1f mph", s*2.23694f));
    m_txtBearing.setText(Float.toString((float) b));
    m_txtDistance.setText(Float.toString((float) d*3.28084f));
    m_txtTime.setText(Float.toString((float) (t/1000000000f)));
    m_txtLongitude.setText(Float.toString((float) o));
    m_txtLatitude.setText(Float.toString((float) a));
    m_txtAltitude.setText(Float.toString((float) l*3.28084f));

    SimpleDateFormat dtf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date(m);
    m_txtDateTime.setText(dtf.format(date));
  }

  @Override protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    m_txtDistance = findViewById(R.id.txtDistance);
    m_txtTime = findViewById(R.id.txtTime);
    m_txtSpeed = findViewById(R.id.txtSpeed);
    m_txtBearing = findViewById(R.id.txtBearing);
    m_txtLongitude = findViewById(R.id.txtLongitude);
    m_txtLatitude = findViewById(R.id.txtLatitude);
    m_txtAltitude = findViewById(R.id.txtAltitude);
    m_txtDateTime = findViewById(R.id.txtDateTime);

    m_LocationCallback = new LocationCallback()
    {
      @Override
      public void onLocationResult(LocationResult locationResult)
      {
        if(locationResult == null)
          return;

        for (Location location : locationResult.getLocations())
        {
          m_CurrentLocation = location;
          doUpdateLocation(location);
        }
      };
    };

    m_LocationGMS = new JMLocationGMS(this, m_LocationCallback);
  }
}

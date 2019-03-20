package com.jamworkspro.exploresenors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;

public class MainActivity extends AppCompatActivity
{
  JMGraph         mJMGraph;
  JMSensors       mJMSensors;
  JMAccelerometer mJMAccelerometer;

  JMDomainRangeSet mDomainRangeSetXaxis;
  JMDomainRangeSet mDomainRangeSetYaxis;
  JMDomainRangeSet mDomainRangeSetZaxis;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mJMGraph = findViewById(R.id.JMGraph);
    mJMGraph.SetAutoScale(false);
    mJMGraph.SetLimitsManually(0, 900, -12.0f, 12.0f);

    mJMSensors = new JMSensors(this);
    mJMAccelerometer = mJMSensors.getAccelerometer();
    mJMAccelerometer.setSampleSize(900);

    mJMAccelerometer.setSensorDataListener(new JMAccelerometer.JMISensorDataListener()
    {
      int iIdxX = -1;
      int iIdxY = -1;
      int iIdxZ = -1;
      boolean bFirstTime = true;

      //Fetch the full dataset and pass it to the JMGraph for presentation
      @Override
      public void onDataSetFull()
      {
        if(bFirstTime)//First and Only time this should be true
        {
          mDomainRangeSetXaxis = mJMAccelerometer.getmXAxis();
          mDomainRangeSetXaxis.setDataPointColor(RED);
          iIdxX = mJMGraph.AddDomainRangeSet(mDomainRangeSetXaxis, true);

          mDomainRangeSetYaxis = mJMAccelerometer.getmYAxis();
          mDomainRangeSetYaxis.setDataPointColor(BLACK);
          iIdxY = mJMGraph.AddDomainRangeSet(mDomainRangeSetYaxis, true);

          mDomainRangeSetZaxis = mJMAccelerometer.getmZAxis();
          mDomainRangeSetZaxis.setDataPointColor(BLUE);
          iIdxZ = mJMGraph.AddDomainRangeSet(mDomainRangeSetZaxis, true);

          bFirstTime = false;
        }
        else
        {
          mJMAccelerometer.getmXAxis().setDataPointColor(RED);
          mJMGraph.ReplaceDomainRangeSet(iIdxX, mJMAccelerometer.getmXAxis());

          mJMAccelerometer.getmYAxis().setDataPointColor(BLACK);
          mJMGraph.ReplaceDomainRangeSet(iIdxY, mJMAccelerometer.getmYAxis());

          mJMAccelerometer.getmZAxis().setDataPointColor(BLUE);
          mJMGraph.ReplaceDomainRangeSet(iIdxZ, mJMAccelerometer.getmZAxis());
        }
        mJMGraph.invalidate();
      }
    });
  }
}


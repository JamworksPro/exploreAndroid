package com.jamworkspro.exploresenors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class JMAccelerometer implements SensorEventListener
{
  private JMDomainRangeSet mXAxis = new JMDomainRangeSet();
  private JMDomainRangeSet mYAxis = new JMDomainRangeSet();
  private JMDomainRangeSet mZAxis = new JMDomainRangeSet();
  private int mSampleSize=0;

  JMAccelerometer(Sensor sensor)
  {
    this.jmiSensorDataListener = null;
  }

  //This is the listener that the client instantiates to be notified when a data buffer is full
  private JMISensorDataListener jmiSensorDataListener;

  public interface JMISensorDataListener
  {
    public void onDataSetFull();
  }

  void setSensorDataListener(JMISensorDataListener listener)
  {
    this.jmiSensorDataListener = listener;
  }

  void setSampleSize(int sampleSize)
  {
    mSampleSize = sampleSize;
  }

  JMDomainRangeSet getmXAxis(){return mXAxis;}
  JMDomainRangeSet getmYAxis(){return mYAxis;}
  JMDomainRangeSet getmZAxis(){return mZAxis;}

  //This is the listener for Sensor Events
  @Override
  public void onSensorChanged(SensorEvent event)
  {
    if (mXAxis.getPointSetSize() < mSampleSize)
    {
      mXAxis.addPoint(mXAxis.getPointSetSize(), event.values[0]);
      mYAxis.addPoint(mYAxis.getPointSetSize(), event.values[1]);
      mZAxis.addPoint(mZAxis.getPointSetSize(), event.values[2]);
      //Date date=new Date(event.timestamp);
      //String str = date.toString();
    }
    else
    {
      jmiSensorDataListener.onDataSetFull();

      //Allocate new objects so as to not cause interference with
      //whatever processing the client performs on the full data sets
      mXAxis = new JMDomainRangeSet();
      mYAxis = new JMDomainRangeSet();
      mZAxis = new JMDomainRangeSet();
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy)
  {

  }
}

class JMSensors
{
  //Properties
  private JMAccelerometer mJMAccelerometer;

  //Constructors
  JMSensors(Context context)
  {
    SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    try
    {
      Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      mJMAccelerometer = new JMAccelerometer(sensor);
      sm.registerListener(mJMAccelerometer, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    catch(Exception e)
    {
      System.out.println(e.getMessage());
    }
  }

  //Interface Methods
  JMAccelerometer getAccelerometer()
  {
    return mJMAccelerometer;
  }
}

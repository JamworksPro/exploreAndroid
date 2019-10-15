package com.jamworkspro.explorejnicallbacks;

import android.content.Context;
import android.widget.Toast;

public class EventHandler
{
  static public void OnEvent(int iEventId)
  {
    Context context = MainActivity.GetContext();
    CharSequence text = "Hello toast!" + "  id = " + iEventId;
    int duration = Toast.LENGTH_SHORT;

    Toast toast = Toast.makeText(context, text, duration);
    toast.show();
  }
}

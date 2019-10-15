package com.jamworkspro.explorejnicallbacks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
  //EventHandler.java needs this info
  static Context g_ctxApplication;
  public static Context GetContext()
  {
    return g_ctxApplication;
  }

  // Used to load the 'native-lib' library on application startup.
  static
  {
    System.loadLibrary("native-lib");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    g_ctxApplication = getApplicationContext();
    setContentView(R.layout.activity_main);

    // Example of a call to a native method
    TextView tv = findViewById(R.id.sample_text);
    tv.setText(stringFromJNI());

    Button btn = findViewById(R.id.button);
    btn.setOnClickListener(new View.OnClickListener()
    {
      @Override public void onClick(View view)
      {
        JNIGenerateEvent(7); //On Some User Action have the JNI call back to our EventHander.OnEvent(int iEventId)
      }
    });
  }

  /**
   * A native method that is implemented by the 'native-lib' native library,
   * which is packaged with this application.
   */
  public native String stringFromJNI();
  public native void JNIGenerateEvent(int iErrorId);
}

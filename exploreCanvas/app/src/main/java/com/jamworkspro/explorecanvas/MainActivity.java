package com.jamworkspro.explorecanvas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

class CanvasView extends View
{
  Canvas m_Canvas;
  public CanvasView(Context context)
  {
    super(context);
  }

  public CanvasView(Context context, @Nullable AttributeSet attrs)
  {
    super(context, attrs);
  }

  @Override protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    m_Canvas = canvas;
    int w = getWidth();
    int h = getHeight();
    int i;
    Paint p = new Paint();
    p.setColor(BLACK);
    p.setStrokeWidth(1f);

    for(i=0;i<w;i++)
    {
      float f = i;
      float e = (float)500;
      canvas.drawPoint(f, e, p);
    }
  }
}

public class MainActivity extends AppCompatActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}

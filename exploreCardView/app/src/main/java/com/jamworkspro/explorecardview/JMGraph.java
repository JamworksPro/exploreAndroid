package com.jamworkspro.explorecardview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;

class JMDomainRangeSet
{
  //Properties
  private boolean m_bActive = true;
  private long m_DataPointColor;

  class POINT
  {
    double domain;
    double range;
  }
  private List<POINT> m_PointSet = new ArrayList<>();

  private POINT m_fMAXDomain = new POINT();
  private POINT m_fMINDomain = new POINT();
  private POINT m_fMAXRange = new POINT();
  private POINT m_fMINRange = new POINT();

  //Constructor
  JMDomainRangeSet()
  {
    m_fMINRange.range = 0.0;
    m_fMINRange.domain = 0.0;
    m_fMAXRange.range = 0.0;
    m_fMAXRange.domain = 0.0;

    m_fMINDomain.range = 0.0;
    m_fMINDomain.domain = 0.0;
    m_fMAXDomain.range = 0.0;
    m_fMAXDomain.domain = 0.0;

    m_DataPointColor = BLACK;
  }

  //Interface Methods
  boolean addPoint(double domain, double range)
  {
    POINT p = new POINT();
    p.domain = domain;
    p.range = range;

    if (Double.isNaN(range))
      return false;

    if (getPointSetSize() == 0)
    {
      m_fMAXDomain.domain = m_fMINDomain.domain = domain;
      m_fMAXDomain.range = m_fMINDomain.range = range;
      m_fMAXRange.domain = m_fMINRange.domain = domain;
      m_fMAXRange.range = m_fMINRange.range = range;
    }

    m_PointSet.add(p);

    if (domain > m_fMAXDomain.domain)
    {
      m_fMAXDomain.domain = domain;
      m_fMAXDomain.range = range;
    }

    if (domain < m_fMINDomain.domain)
    {
      m_fMINDomain.domain = domain;
      m_fMINDomain.range = range;
    }

    if (range > m_fMAXRange.range)
    {
      m_fMAXRange.range = range;
      m_fMAXRange.domain = domain;
    }

    if (range < m_fMINRange.range)
    {
      m_fMINRange.range = range;
      m_fMINRange.domain = domain;
    }

    return true;
  }

  void setActiveState(boolean bActive) {m_bActive = bActive;}

  boolean getActiveState() {return m_bActive;}

  long getPointSetSize()
  {
    return m_PointSet.size();
  }

  double getDomain(int i)
  {
    return m_PointSet.get(i).domain;
  }

  double getRange(int i)
  {
    return m_PointSet.get(i).range;
  }

  POINT getMaxDomain()
  {
    return m_fMAXDomain;
  }

  POINT getMinDomain()
  {
    return m_fMINDomain;
  }

  POINT getMaxRange()
  {
    return m_fMAXRange;
  }

  POINT getMinRange()
  {
    return m_fMINRange;
  }

  public void clear() {m_PointSet.clear();}

  public List<POINT> getPointSet()
  {
    return m_PointSet;
  }

  void setDataPointColor(long color) {m_DataPointColor = color;}

  long getDataPointColor() {return m_DataPointColor;}
};

public class JMGraph extends View
{
  //Properties
  private List<JMDomainRangeSet> m_listOfDomainRangeSets = new ArrayList<>();

  public static final int MODE_PLOT = 0;
  public static final int MODE_HIST = 1;
  private long m_PlotMode;
  private Paint m_Paint;
  private Canvas m_Canvas;
  private long m_CanvasWidth;
  private long m_CanvasHeight;

  //Coordinate Limits
  private boolean m_bAutoScale = true;
  private double m_XLower = -1.0f;
  private double m_XUpper = 1.0f;
  private double m_YLower = -1.0f;
  private double m_YUpper = 1.0f;

  //Factors that determine the physical location of the Graph's Axis'
  private double m_XScale;
  private double m_YScale;

  //Absolute value of the Axis' length
  private double m_XSpan;
  private double m_YSpan;

  private Point m_ptXAxisUpper = new Point(0, 0);
  private Point m_ptXAxisLower = new Point(0, 0);
  private Point m_ptYAxisUpper = new Point(0, 0);
  private Point m_ptYAxisLower = new Point(0, 0);

  //Constructors
  public JMGraph(Context context, AttributeSet attrs)
  {
    super(context, attrs);

    m_Paint = new Paint();
    m_PlotMode = MODE_PLOT;
    m_Paint.setStrokeWidth(3);
    m_Paint.setColor(BLACK);
  }

  public JMGraph(Context context)
  {
    super(context);
  }

  //Interface Methods
  public void SetPlotMode(long iPlotMode)
  {
    m_PlotMode = iPlotMode;
  }

  public JMDomainRangeSet GetDomainRangeSet(long iIdxOfDomainRangeSet)
  {
    return m_listOfDomainRangeSets.get((int) iIdxOfDomainRangeSet);
  }

  public void AddPointToDomainRangeSet(int iIdxOfDomainRangeSet, double fDomain, double fRange)
  {
    m_listOfDomainRangeSets.get(iIdxOfDomainRangeSet).addPoint(fDomain, fRange);
  }

  public void SetDomainRangeSetActiveState(int iIdxOfDomainRangeSet, boolean bActive)
  {
    m_listOfDomainRangeSets.get(iIdxOfDomainRangeSet).setActiveState(bActive);
    invalidate();
  }

  public boolean GetDomainRangeSetActiveState(int iIdxOfDomainRangeSet)
  {
    return m_listOfDomainRangeSets.get(iIdxOfDomainRangeSet).getActiveState();
  }

  public void SetLimitsBasedOnDomainRangeSet(int iIdxOfDomainRangeSet)
  {
    m_XLower = m_listOfDomainRangeSets.get(iIdxOfDomainRangeSet).getMinDomain().domain;
    m_XUpper = m_listOfDomainRangeSets.get(iIdxOfDomainRangeSet).getMaxDomain().domain;
    m_YLower = m_listOfDomainRangeSets.get(iIdxOfDomainRangeSet).getMinRange().range;
    m_YUpper = m_listOfDomainRangeSets.get(iIdxOfDomainRangeSet).getMaxRange().range;
  }

  public void SetLimitsManually(double fXLower, double fXUpper, double fYLower, double fYUpper)
  {
    m_XLower = fXLower;
    m_XUpper = fXUpper;
    m_YLower = fYLower;
    m_YUpper = fYUpper;
  }

  public void SetAutoScale(boolean bAutoScale)
  {
    m_bAutoScale = bAutoScale;
  }

  public int CreateDomainRangeSet(boolean bActive)
  {
    JMDomainRangeSet ds = new JMDomainRangeSet();
    ds.setActiveState(bActive);
    m_listOfDomainRangeSets.add(ds);

    return m_listOfDomainRangeSets.size() - 1;
  }

  public int AddDomainRangeSet(JMDomainRangeSet DomainRangeSet, boolean bActive)
  {
    DomainRangeSet.setActiveState(bActive);
    m_listOfDomainRangeSets.add(DomainRangeSet);

    return m_listOfDomainRangeSets.size() - 1;
  }

  public void ReplaceDomainRangeSet(long iIdxOfDomainRangeSet, JMDomainRangeSet jmDomainRangeSet)
  {
    JMDomainRangeSet drs = m_listOfDomainRangeSets.get((int) iIdxOfDomainRangeSet);
    drs = null;
    m_listOfDomainRangeSets.set((int) iIdxOfDomainRangeSet, jmDomainRangeSet);
  }


  //Methods
  private void LabelX(String strLimit, Paint.Align iAlign)
  {
    m_Paint.setTextAlign(iAlign);

    Point ptTextStart = new Point();

    switch (iAlign)
    {
      case LEFT:
        ptTextStart.x = 0;
        strLimit = Double.toString(m_XLower);
        break;

      case RIGHT:
        ptTextStart.x = m_Canvas.getWidth();
        strLimit = Double.toString(m_XUpper);
        break;
    }
    ptTextStart.y = (int) (m_CanvasHeight * m_YScale);

    //Get the Text size metrics
    double[] f = {0.0f};
    Rect r = new Rect();
    m_Paint.getTextBounds(strLimit, 0, strLimit.length(), r);

    int iTextWidth = r.right - r.left;
    int iTextHeight = r.bottom - r.top;

    int iHeightUpper = (int) (m_CanvasHeight * m_YScale);
    int iHeightLower = (int) (m_CanvasHeight - iHeightUpper);

    if (iHeightUpper < iTextHeight)
      ptTextStart.y = (int) (m_CanvasHeight * m_YScale + iTextHeight);

    m_Canvas.drawText(strLimit, ptTextStart.x, ptTextStart.y - 10, m_Paint);
  }

  private void Draw_XAxis()
  {
    if (m_YUpper <= 0)
      //range is < 0 hence the X Axis is positioned at the TOP
      m_YScale = 0.0f;
    else if (m_YSpan > m_YUpper)
      //range = 0 intercepts the X Axis
      m_YScale = m_YUpper / m_YSpan;
    else
      //range is = 0 hence the X Axis is positioned at the BOTTOM
      m_YScale = 1.0f;

    m_ptXAxisLower.x = 0;
    m_ptXAxisLower.y = (int) ((m_CanvasHeight - 1) * m_YScale);
    m_ptXAxisUpper.x = (int) m_CanvasWidth;
    m_ptXAxisUpper.y = (int) ((m_CanvasHeight - 1) * m_YScale);
    m_Canvas.drawLine(m_ptXAxisLower.x, m_ptXAxisLower.y, m_ptXAxisUpper.x, m_ptXAxisUpper.y, m_Paint);

    //Label the X Axis
    LabelX(String.format("%f", m_XLower), Paint.Align.LEFT);
    LabelX(String.format("%f", m_XUpper), Paint.Align.RIGHT);
  }

  private void LabelY(String strLimit, boolean bIsUpperLimit)
  {
    //Get the Text size metrics
    double[] f = {0.0f};
    Rect r = new Rect();
    m_Paint.getTextBounds(strLimit, 0, strLimit.length(), r);

    int iTextWidth = r.right - r.left;
    int iTextHeight = r.bottom - r.top;

    Point ptTextStart = new Point();

    ptTextStart.x = (int) (m_CanvasWidth * m_XScale);
    if (bIsUpperLimit)
      ptTextStart.y = iTextHeight * 2;
    else
      ptTextStart.y = (int) (m_CanvasHeight - iTextHeight);

    long iWidthLeft = (int) (m_CanvasWidth * m_XScale);
    long iWidthRight = (int) (m_CanvasWidth - iWidthLeft);

    if (iWidthRight > iTextWidth)
      m_Paint.setTextAlign(Paint.Align.LEFT);
    else
      m_Paint.setTextAlign(Paint.Align.RIGHT);

    if (!bIsUpperLimit)
      ptTextStart.y = ptTextStart.y - 20;
    m_Canvas.drawText(strLimit, ptTextStart.x, ptTextStart.y, m_Paint);
  }

  private void Draw_YAxis()
  {
    //Draw the Y Axis
    if (m_XUpper <= 0)
      //domain is less than 0 hence the Y Axis is positioned at the RIGHT
      m_XScale = 1.0f;
    else if (m_XSpan > m_XUpper)
      //domain = 0 intercepts the Y Axis
      m_XScale = 1.0f - m_XUpper / m_XSpan;
    else
      //domain = 0 hence the Y Axis is positioned at the LEFT
      m_XScale = 0.0f;

    m_ptYAxisLower.x = (int) ((m_CanvasWidth - 1) * m_XScale);
    m_ptYAxisLower.y = (int) m_CanvasHeight;
    m_ptYAxisUpper.x = (int) ((m_CanvasWidth - 1) * m_XScale);
    m_ptYAxisUpper.y = 0;
    m_Canvas.drawLine(m_ptYAxisLower.x, m_ptYAxisLower.y, m_ptYAxisUpper.x, m_ptYAxisUpper.y, m_Paint);

    //Label the Y Axis
    LabelY(String.format("%f", m_YLower), false);
    LabelY(String.format("%f", m_YUpper), true);
  }

  private void DoPlot()
  {
    long s = m_listOfDomainRangeSets.size();
    boolean bLimitsNotIntialized = true;
    for (int j = 0; j < s; j++)
    {
      if (m_listOfDomainRangeSets.get(j).getActiveState())
      {
        //Define graph coordinate limits.  Though the graph can plot many different datasets, the
        //datasets must all be plotted on the same scale.  This portion of the for loop sets the
        //initial coordinate limits and evaluates the limits for each dataset to be plotted adjusting
        //the limits to ensure that each dataset is fully plotted.
        if (m_bAutoScale)
        {
          if (bLimitsNotIntialized)
          {
            m_XUpper = m_listOfDomainRangeSets.get(j).getMaxDomain().domain;
            m_XLower = m_listOfDomainRangeSets.get(j).getMinDomain().domain;
            m_YUpper = m_listOfDomainRangeSets.get(j).getMaxRange().range;
            m_YLower = m_listOfDomainRangeSets.get(j).getMinRange().range;
            bLimitsNotIntialized = false;
          }

          if (m_listOfDomainRangeSets.get(j).getMaxDomain().domain > m_XUpper)
            m_XUpper = m_listOfDomainRangeSets.get(j).getMaxDomain().domain;

          if (m_listOfDomainRangeSets.get(j).getMinDomain().domain < m_XLower)
            m_XLower = m_listOfDomainRangeSets.get(j).getMinDomain().domain;

          if (m_listOfDomainRangeSets.get(j).getMaxRange().range > m_YUpper)
            m_YUpper = m_listOfDomainRangeSets.get(j).getMaxRange().range;

          if (m_listOfDomainRangeSets.get(j).getMinRange().range < m_YLower)
            m_YLower = m_listOfDomainRangeSets.get(j).getMinRange().range;
        }
      }
    }

    m_XSpan = m_XUpper - m_XLower;
    m_YSpan = m_YUpper - m_YLower;

    for (int j = 0; j < s; j++)
    {
      double PxData, PyData;
      double Px, Py;

      //Plot the active datasets
      JMDomainRangeSet drs = m_listOfDomainRangeSets.get(j);
      if (drs.getActiveState())
      {
        m_Paint.setColor((int) drs.getDataPointColor());
        long iDataSetSize = drs.getPointSetSize();
        for (int i = 0; i < iDataSetSize; i++)
        {
          PxData = drs.getDomain(i);
          PyData = drs.getRange(i);
          Px = ((PxData - m_XLower) / m_XSpan) * m_CanvasWidth;
          Py = ((m_YUpper - PyData) / m_YSpan) * m_CanvasHeight;
          switch ((int) m_PlotMode)
          {
            case MODE_PLOT:
              m_Canvas.drawPoint((float)Px, (float)Py, m_Paint);
              break;

            case MODE_HIST:
              m_Canvas.drawLine((float)Px, 0 + m_CanvasHeight, (float)Px, (float)Py, m_Paint);
              break;
          }
        }
      }
    }
    m_Paint.setTextSize(m_Canvas.getHeight() * .05f);
    m_Paint.setColor(BLACK);
    Draw_XAxis();
    Draw_YAxis();
  }

  //Events
  @Override protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);

    m_Canvas = canvas;
    m_CanvasHeight = m_Canvas.getHeight();
    m_CanvasWidth = m_Canvas.getWidth();

    DoPlot();
  }
}


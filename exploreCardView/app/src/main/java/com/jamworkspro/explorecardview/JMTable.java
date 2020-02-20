package com.jamworkspro.explorecardview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

public class JMTable extends TableLayout
{
  //Properties
  List<TableRow>  m_ListOfRows;

  //Constructors
  public JMTable(Context context)
  {
    super(context);
  }

  public JMTable(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    m_ListOfRows = new ArrayList<TableRow>();
  }

  public TableRow AddTableRow()
  {
    TableRow tableRow = new TableRow(this.getContext(), null);

    m_ListOfRows.add(tableRow);
    addView(tableRow);
    return tableRow;
  }

  public void AddRowColumn(TableRow tableRow, View jmEdit)
  {
    tableRow.addView(jmEdit);
  }

  public int GetTableRowCount()
  {
    return m_ListOfRows.size();
  }
}

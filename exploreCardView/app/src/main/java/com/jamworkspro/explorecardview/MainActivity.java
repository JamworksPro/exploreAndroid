package com.jamworkspro.explorecardview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.CYAN;
import static android.graphics.Color.WHITE;
import static android.view.Gravity.RIGHT;

abstract class JWPtextChangedListener<T> implements TextWatcher
{
  private T target;

  public JWPtextChangedListener(T target)
  {
    this.target = target;
  }

  @Override
  public void afterTextChanged(Editable s)
  {
    this.onTextChanged(target, s);
  }

  public abstract void onTextChanged(T target, Editable s);
  public abstract void beforeTextChanged(CharSequence s, int start, int count, int after);
}

public class MainActivity extends AppCompatActivity
{
  public static boolean isNumeric(String str)
  {
    return str.matches("^[-+]?(\\d*\\.)?\\d+$");  //match a number with optional '-' and decimal.
  }

  CardView.OnClickListener cvOCL = new CardView.OnClickListener()
  {
    @Override public void onClick(View cv)
    {
      Class<?> c = cv.getClass();
      int id = cv.getId();
    }
  };

  GridLayout grdLytCards;
  JMKeyboardView m_KeyboardView;
  @Override protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    grdLytCards = findViewById(R.id.grdLytCards);
    grdLytCards.setUseDefaultMargins(true);

    m_KeyboardView = findViewById(R.id.JMKeyboardView);

    CardView card[];
    CardView.OnClickListener cvOCL = new CardView.OnClickListener()
    {
      @Override public void onClick(View cv)
      {
        Class<?> c = cv.getClass();
        int id = cv.getId();
      }
    };

    com.jamworkspro.explorecardview.JMEdit[] m_edtVariableValue = new JMEdit[256];;
    int m_iSelectedFunction=0;
    GridLayout grdLytCards;
    GridLayout.LayoutParams lytParms = new GridLayout.LayoutParams();

    android.widget.GridLayout.LayoutParams lp = new android.widget.GridLayout.LayoutParams();
    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

    card = new CardView[256];
    grdLytCards = findViewById(R.id.grdLytCards);

    for (int i = 0; i < 3; i++)
    {
      card[i] = new CardView(this);
      LayoutInflater linf = this.getLayoutInflater();
      View v = linf.inflate(R.layout.layout_functiondef, card[i], true);

      TextView l = v.findViewById(R.id.lblResult);
      l.setRawInputType(InputType.TYPE_NULL);
      TextView tv = v.findViewById(R.id.txtFunctionDef);
      tv.setRawInputType(InputType.TYPE_NULL);
      TextView tv1 = v.findViewById(R.id.txtResult);
      tv.setRawInputType(InputType.TYPE_NULL);

      //tv.setText("x^2+x+c");
      //tv1.setText("8.037");

      JMTable jmt = v.findViewById(R.id.tblVariables);
      for (int m = 0; m < 8; m++)
      {
        jmt.setColumnStretchable(1, true);
        TableRow row = new TableRow(this);
        row.setLayoutParams(lp);

        TextView strVariableName = new EditText(this);
        strVariableName.setInputType(InputType.TYPE_NULL);
        strVariableName.setBackgroundColor(CYAN);
        String strVarName = "HOla";
        strVariableName.setText(strVarName);
        strVariableName.setTextSize(12);
        strVariableName.setTextColor(BLACK);
        strVariableName.setPadding(0, 0, 5, 0);
        strVariableName.setEnabled(false);

        m_edtVariableValue[m] = new JMEdit(this, null);
        m_edtVariableValue[m].SetKeyboardView(m_KeyboardView);
        m_edtVariableValue[m].setId(m);
        m_edtVariableValue[m].setBackgroundColor(WHITE);
        m_edtVariableValue[m].setPadding(0, 0, 5, 0);
        m_edtVariableValue[m].setGravity(RIGHT);
        m_edtVariableValue[m].setSingleLine();
        m_edtVariableValue[m].setTextColor(BLACK);

        double dValue = 123.456;
        m_edtVariableValue[m].setText(Double.toString(dValue));
        m_edtVariableValue[m].setTextSize(12);
        m_edtVariableValue[m].setSelection(0, 1);

        m_edtVariableValue[m].setSelection(0,1);
        m_edtVariableValue[m].setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
          @Override
          public void onFocusChange(View v, boolean hasFocus)
          {
            if(hasFocus)
              m_KeyboardView.SetEditText((JMEdit)v);
          }
        });

        m_edtVariableValue[m].addTextChangedListener
        (
        new JWPtextChangedListener<JMEdit>(m_edtVariableValue[m])
        {
          @Override
          public void onTextChanged(JMEdit target, Editable s)
          {
            //Event Action
            String strDoubleValue = s.toString();

            if(isNumeric(strDoubleValue.substring(0, strDoubleValue.length()-1)))
            {
              double dValue = Double.parseDouble(strDoubleValue);
              //FunctiontoCallinMainActivity.SetVariableValue(m_iSelectedFunction, target.getId(), dValue);

              //double d = FunctiontoCallinMainActivity.Evaluate(m_iSelectedFunction);
              //SaveFunction(0);
              //m_txtResult.setText(String.valueOf(d));
            }
          }

          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after)
          {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count)
          {

          }
        }
        );
        jmt.AddRowColumn(row, strVariableName);
        jmt.AddRowColumn(row, m_edtVariableValue[m]);
        jmt.addView(row, m);
      }

      card[i].setLayoutParams(lp);
      card[i].setId(i);
      card[i].setOnClickListener(cvOCL);
      // Set CardView corner radius
      card[i].setRadius(40);
      // Set cardView content padding
      card[i].setContentPadding(10, 10, 10, 10);

      // Set a background color for CardView
      card[i].setCardBackgroundColor(Color.LTGRAY);
      // Set the CardView maximum elevation
      card[i].setMaxCardElevation(30);
      // Set CardView elevation
      card[i].setCardElevation(18);

      // Finally, add the CardView in root layout
      grdLytCards.addView(card[i]);
    }
  }
}
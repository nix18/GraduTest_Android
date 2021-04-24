package com.myapp.gradutest_android.utils.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class ClockInChartXFormatter  implements IAxisValueFormatter {

    private String[] mValues;

    public ClockInChartXFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(value != 0) value = value-1;
        return mValues[(int) (value) % mValues.length];
    }
}


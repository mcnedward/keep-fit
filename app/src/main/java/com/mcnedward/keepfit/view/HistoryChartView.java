package com.mcnedward.keepfit.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.enums.Unit;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Edward on 3/7/2016.
 */
public class HistoryChartView extends ChartView {
    private static final String TAG = "HistoryChartView";

    public HistoryChartView(List<Goal> goals, Context context) {
        super(goals, context);
    }

    public HistoryChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void editGoal(Goal goal) {
        updatePlot();
    }

    public void setGoalDates(List<Goal> goals) {
        this.goals = goals;
        updatePlot();
    }

    public void switchUnit(Unit unit) {
        this.unit = unit;
        updatePlot();
    }

    public void notifyDateChanged(int dateRange) {
        String dateRangeStamp = Dates.getDateFromRange(dateRange, Dates.PRETTY_DATE);
        String currentDateStamp = Dates.getCalendarPrettyDate();
        dateMessage = String.format("No activity found from %s to %s", dateRangeStamp, currentDateStamp);
    }

    public void refresh() {
        plot.clear();
    }

}

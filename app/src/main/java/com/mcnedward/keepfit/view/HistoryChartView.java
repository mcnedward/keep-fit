package com.mcnedward.keepfit.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.GoalDate;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/7/2016.
 */
public class HistoryChartView extends LinearLayout {

    private Context context;
    private List<Goal> goals;
    private XYPlot xyPlot;
    private static final String X_LABEL = "Date";
    private static final String Y_LABEL = "Steps";
    private SimpleXYSeries historySeries;

    public HistoryChartView(List<Goal> goals, Context context) {
        super(context);
        this.goals = goals;
        this.context = context;
        if (!isInEditMode()) {
            inflate(context, R.layout.view_history_chart, this);
            initializePlot();
        }
    }

    public HistoryChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (!isInEditMode()) {
            inflate(context, R.layout.view_history_chart, this);
            initializePlot();
        }
    }

    private void initializePlot() {
        xyPlot = (XYPlot) findViewById(R.id.history_plot);
        xyPlot.setDomainLabel(X_LABEL);
        xyPlot.setRangeLabel(Y_LABEL);
        xyPlot.setBorderPaint(null);
//        xyPlot.setRangeBoundaries(LOWER_BOUNDARY, UPPER_BOUNDARY, BoundaryMode.FIXED);
//        xyPlot.setRangeStep(XYStepMode.SUBDIVIDE, Math.abs(LOWER_BOUNDARY - UPPER_BOUNDARY));
//        xyPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);

        if (goals != null && !goals.isEmpty()) {
            updatePlot();
        }
    }

    private void updatePlot() {
        refresh();

        List<Integer> stepAmounts = new ArrayList<>();
        int lowerBoundary = 0, upperBoundary = 0;
        for (Goal goal : goals) {
            stepAmounts.add(goal.getStepAmount());
            int stepAmount = goal.getStepAmount();
            if (lowerBoundary == 0 || stepAmount < lowerBoundary)
                lowerBoundary = stepAmount;
            if (upperBoundary == 0 || stepAmount > upperBoundary)
                upperBoundary = stepAmount;
        }

        historySeries = new SimpleXYSeries(stepAmounts, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Steps");
        xyPlot.addSeries(historySeries, new LineAndPointFormatter(Color.GREEN, Color.GREEN, Color.TRANSPARENT, null));

        xyPlot.setRangeTopMin(lowerBoundary / 2);
        xyPlot.setRangeTopMax(upperBoundary);
        xyPlot.redraw();
    }

    private void setDomainValues() {
        if (goals != null) {
            xyPlot.setDomainValueFormat(new NumberFormat() {
                @Override
                public StringBuffer format(double value, StringBuffer buffer, FieldPosition field) {
                    for (Goal goal : goals)
                        buffer.append(goal.getCreatedOn() + " ");
                    return buffer;
                }

                @Override
                public StringBuffer format(long value, StringBuffer buffer, FieldPosition field) {
                    throw new UnsupportedOperationException("Not yet implemented.");
                }

                @Override
                public Number parse(String string, ParsePosition position) {
                    throw new UnsupportedOperationException("Not yet implemented.");
                }
            });
        }
    }

    public void refresh() {
        xyPlot.clear();
    }

    public void setGoalDates(List<Goal> goals) {
        this.goals = goals;
        updatePlot();
    }

}

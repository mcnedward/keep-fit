package com.mcnedward.keepfit.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
public abstract class ChartView extends LinearLayout {
    private static final String TAG = "ChartView";

    protected Context context;
    protected TextView txtNoDates;
    protected List<Goal> goals;
    protected XYPlot plot;
    protected SimpleXYSeries historySeries;
    protected List<Double> stepAmounts;
    protected List<Integer> dates;
    protected double upperBound;
    protected int maxDomainStep = 10;
    protected Unit unit = Unit.METER;
    protected String dateMessage;

    public ChartView(List<Goal> goals, Context context) {
        super(context);
        this.goals = goals;
        this.context = context;
        initialize();
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        goals = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        if (!isInEditMode()) {
            inflate(context, R.layout.view_chart, this);
            initializePlot();
        }
    }

    private void initializePlot() {
        plot = (XYPlot) findViewById(R.id.plot_chart);
        txtNoDates = (TextView) findViewById(R.id.txt_no_dates);
        plot.getLayoutManager().remove(plot.getLegendWidget());
        plot.setBorderPaint(null);

        plot.setRangeBottomMax(0);
        plot.getGraphWidget().getRangeLabelPaint().setTextSize(20);
        plot.getGraphWidget().getDomainLabelPaint().setTextSize(20);
        plot.setRangeValueFormat(new DecimalFormat("0"));

        if (goals != null && !goals.isEmpty()) {
            updatePlot();
        }
    }

    protected void updatePlot() {
        refresh();

        double rangeIncrement = handleData();

        historySeries = new SimpleXYSeries(dates, stepAmounts, null);
        plot.addSeries(historySeries, new LineAndPointFormatter(
                ContextCompat.getColor(context, R.color.LimeGreen),
                ContextCompat.getColor(context, R.color.ForestGreen),
                Color.TRANSPARENT, null));

        updatePlotWidget(rangeIncrement);

        if (dates.size() <= 1) {
            txtNoDates.setText(dateMessage);
            txtNoDates.setVisibility(VISIBLE);
        } else
            txtNoDates.setVisibility(GONE);

        plot.redraw();
    }

    private void updatePlotWidget(final double rangeIncrement) {
        if (goals != null && !goals.isEmpty()) {
            plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, rangeIncrement);
            plot.setRangeTopMin(upperBound + rangeIncrement);

            int goalCount = goals.size();
            plot.setDomainStep(XYStepMode.SUBDIVIDE, goalCount <= 10 ? goalCount : maxDomainStep);
            plot.setDomainStepValue(goalCount <= 10 ? goalCount : maxDomainStep);

            plot.setDomainValueFormat(new Format() {
                private SimpleDateFormat fromDateFormat = new SimpleDateFormat(Dates.NUMBER_DATE);
                private SimpleDateFormat toDateFormat = new SimpleDateFormat("dd/MM");

                private int count = 0;
                private int fieldsShown = 0;

                @Override
                public StringBuffer format(Object value, StringBuffer buffer, FieldPosition field) {
                    if (dates.size() == 0)
                        return buffer;
                    int position = count++;
                    int dateCount = dates.size();
                    if (dateCount > maxDomainStep) {
                        int fieldsToDisplay = dateCount / 2;
                        if (position % 2 == 0) {
                            int index = fieldsToDisplay * fieldsShown++;
                            if (index > dateCount - 1) index = dateCount - 1;
                            return handleBuffer(dates.get(index), buffer, field);
                        } else
                            return buffer;
                    }
                    return handleBuffer(dates.get(position), buffer, field);
                }

                private StringBuffer handleBuffer(int dateValue, StringBuffer buffer, FieldPosition position) {
                    Date date = null;
                    try {
                        date = fromDateFormat.parse(String.valueOf(dateValue));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return toDateFormat.format(date, buffer, position);
                }

                @Override
                public Object parseObject(String string, ParsePosition position) {
                    return null;
                }
            });
            plot.setRangeValueFormat(new Format() {
                private DecimalFormat format = new DecimalFormat("#.###");

                @Override
                public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
                    return format.format(object, buffer, field);
                }

                @Override
                public Object parseObject(String string, ParsePosition position) {
                    return null;
                }
            });
        }
    }

    private double handleData() {
        stepAmounts = new ArrayList<>();
        dates = new ArrayList<>();
        upperBound = 0;
        double average = 0;
        for (Goal goal : goals) {
            double stepAmount = goal.getStepAmount();
            stepAmount = Unit.convert(goal.getUnit(), unit, stepAmount);
            stepAmounts.add(stepAmount);
            average += stepAmount;

            dates.add(Integer.valueOf(goal.getCreatedOnDateNumber()));
            if (upperBound == 0 || upperBound < goal.getStepAmount())
                upperBound = stepAmount;
        }

        average /= goals.size();
        return average;
    }

    public void notifyUpdateFinished() {
        plot.redraw();
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

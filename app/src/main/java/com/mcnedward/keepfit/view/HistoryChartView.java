package com.mcnedward.keepfit.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.PositionMetrics;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Edward on 3/7/2016.
 */
public class HistoryChartView extends LinearLayout {

    private Context context;
    private List<Goal> goals;
    private XYPlot plot;
    private SimpleXYSeries historySeries;

    private List<Double> stepAmounts;
    private List<Integer> dates;
    private double upperBound;
    private Unit unit = Unit.METER;

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
        goals = new ArrayList<>();
        if (!isInEditMode()) {
            inflate(context, R.layout.view_history_chart, this);
            initializePlot();
        }
    }

    private void initializePlot() {
        plot = (XYPlot) findViewById(R.id.history_plot);
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

    private void updatePlot() {
        refresh();

        double rangeIncrement = handleData();

        historySeries = new SimpleXYSeries(dates, stepAmounts, null);
        plot.addSeries(historySeries, new LineAndPointFormatter(
                ContextCompat.getColor(context, R.color.LimeGreen),
                ContextCompat.getColor(context, R.color.ForestGreen),
                Color.TRANSPARENT, null));

        updatePlotWidget(rangeIncrement);

        plot.redraw();
    }

    private void updatePlotWidget(double rangeIncrement) {
        if (goals != null) {
            plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, rangeIncrement);
            plot.setRangeTopMin(upperBound + rangeIncrement);
            plot.setRangeValueFormat(new Format() {
                private DecimalFormat format = new DecimalFormat("#.#####");
                @Override
                public StringBuffer format(Object object, StringBuffer buffer, FieldPosition position) {
                    double value = (Double) object;
                    return format.format(value, buffer, position);
                }

                @Override
                public Object parseObject(String string, ParsePosition position) {
                    return null;
                }
            });

            plot.setDomainStep(XYStepMode.SUBDIVIDE, goals.size());
            plot.setDomainValueFormat(new Format() {

                private SimpleDateFormat fromDateFormat = new SimpleDateFormat(Extension.DATABASE_DATE);
                private SimpleDateFormat toDateFormat = new SimpleDateFormat("dd/MM");

                @Override
                public StringBuffer format(Object value, StringBuffer buffer, FieldPosition position) {
                    int dateValue = ((Double) value).intValue();
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

            dates.add(Integer.valueOf(goal.getCreatedOn()));
            if (upperBound == 0 || upperBound < goal.getStepAmount())
                upperBound = stepAmount;
        }

        average /= goals.size();
        return average;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
        updatePlot();
    }

    public void editGoal(Goal goal) {
        int index = -1;
        for (int x = 0; x < goals.size(); x++) {
            if (goals.get(x).getId().equals(goal.getId())) {
                index = x;
                break;
            }
        }
        if (index != -1) {
            goals.set(index, goal);
            updatePlot();
        }
    }

    public void deleteGoal(Goal goal) {
        goals.remove(goal);
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

    public void refresh() {
        plot.clear();
    }

}

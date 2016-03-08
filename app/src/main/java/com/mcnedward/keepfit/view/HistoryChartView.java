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
public class HistoryChartView extends LinearLayout {

    private Context context;
    private List<Goal> goals;
    private XYPlot plot;
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
        plot = (XYPlot) findViewById(R.id.history_plot);
        plot.getLayoutManager().remove(plot.getLegendWidget());
        plot.setBorderPaint(null);

        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 10);
        plot.getGraphWidget().getRangeLabelPaint().setTextSize(20);
        plot.getGraphWidget().getDomainLabelPaint().setTextSize(20);

        if (goals != null && !goals.isEmpty()) {
            updatePlot();
        }
    }

    private void updatePlot() {
        List<Integer> stepAmounts = new ArrayList<>();
        List<Integer> dates = new ArrayList<>();
        int lowerBoundary = 0, upperBoundary = 0;
        for (Goal goal : goals) {
            stepAmounts.add(goal.getStepAmount());
            dates.add(Integer.valueOf(goal.getCreatedOn()));
            int stepAmount = goal.getStepAmount();
            if (lowerBoundary == 0 || stepAmount < lowerBoundary)
                lowerBoundary = stepAmount;
            if (upperBoundary == 0 || stepAmount > upperBoundary)
                upperBoundary = stepAmount;
        }

        refresh();

        historySeries = new SimpleXYSeries(dates, stepAmounts, null);
        plot.addSeries(historySeries, new LineAndPointFormatter(
                ContextCompat.getColor(context, R.color.LimeGreen),
                ContextCompat.getColor(context, R.color.ForestGreen),
                Color.TRANSPARENT, null));

        updatePlotWidget();

        plot.redraw();
    }

    private void updatePlotWidget() {
        if (goals != null) {
            plot.setRangeValueFormat(new DecimalFormat("0"));

            plot.setDomainStep(XYStepMode.SUBDIVIDE, goals.size());
            plot.setDomainValueFormat(new Format() {

                private SimpleDateFormat fromDateFormat = new SimpleDateFormat(Extension.DATABASE_DATE);
                private SimpleDateFormat toDateFormat = new SimpleDateFormat("dd/MM");

                @Override
                public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition position) {
                    int dateValue = ((Double) obj).intValue();
                    Date date = null;
                    try {
                        date = fromDateFormat.parse(String.valueOf(dateValue));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return toDateFormat.format(date, stringBuffer, position);
                }

                @Override
                public Object parseObject(String source, ParsePosition position) {
                    return null;
                }
            });
        }
    }

    public void refresh() {
        plot.clear();
    }

    public void setGoalDates(List<Goal> goals) {
        this.goals = goals;
        updatePlot();
    }

}

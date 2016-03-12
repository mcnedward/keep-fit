package com.mcnedward.keepfit.activity.fragment;

import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.view.StatisticView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public class StatisticsFragment extends AdvancedFragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Goal>> {
    private static final String TAG = "StatisticsFragment";

    private StatisticView viewAverage;
    private StatisticView viewMinimum;
    private StatisticView viewMaximum;
    private StatisticView viewPercentage;
    private TextView txtDates;
    private Unit unit = Unit.METER;
    private List<Goal> goals;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_statistics;
    }

    @Override
    protected void setup(View view) {
        goals = new ArrayList<>();
        txtDates = (TextView) view.findViewById(R.id.statistics_date);
        viewAverage = (StatisticView) view.findViewById(R.id.statistics_average);
        viewMinimum = (StatisticView) view.findViewById(R.id.statistics_minimum);
        viewMaximum = (StatisticView) view.findViewById(R.id.statistics_maximum);
        viewPercentage = (StatisticView) view.findViewById(R.id.statistics_percentage);

        updateDateText(seekBar.getProgress());
    }

    @Override
    public void onUnitChanged(Unit unit) {
        this.unit = unit;
        handleData();
    }

    @Override
    public void onDateProgressChanged(int progress) {
        updateDateText(progress);
    }

    private void updateDateText(int progress) {
        String dateRangeStamp = Dates.getDateFromRange(progress, Dates.PRETTY_DATE);
        String currentDateStamp = Dates.getCalendarPrettyDate();
        txtDates.setText(String.format("%s to %s", dateRangeStamp, currentDateStamp));
    }

    @Override
    public void onLoadFinished(Loader<List<Goal>> loader, List<Goal> data) {
        goals = data;
        handleData();
    }

    private void handleData() {
        double averageStepAmount = 0;
        double averageGoalAmount = 0;
        int goalsCompleted = 0;
        int percentage = 0;
        if (!goals.isEmpty()) {
            for (Goal goal : goals) {
                double stepAmount = Unit.convert(goal.getUnit(), unit, goal.getStepAmount());
                averageStepAmount += stepAmount;

                double stepGoal = Unit.convert(goal.getUnit(), unit, goal.getStepGoal());
                averageGoalAmount += stepGoal;

                if (goal.isGoalReached())
                    goalsCompleted++;
            }
            averageStepAmount /= goals.size();
            averageGoalAmount /= goals.size();
            if (goals.size() > 0)
                percentage = (goalsCompleted / goals.size()) * 100;
        }
        viewAverage.updateNumbers(averageStepAmount, averageGoalAmount, unit);
        viewMinimum.updateNumbers(averageStepAmount, averageGoalAmount, unit);
        viewMaximum.updateNumbers(averageStepAmount, averageGoalAmount, unit);
        viewPercentage.setAsPercentage(percentage);
    }

    @Override
    public void onLoaderReset(Loader<List<Goal>> loader) {
    }

    private void updateAverage() {

    }

    private void updateMinimum() {

    }

    private void updateMaximum() {

    }

}

package com.mcnedward.keepfit.activity.fragment;

import android.support.v4.content.Loader;
import android.view.View;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.view.HistoryChartView;

import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/23/2016.
 */
public class HistoryFragment extends AdvancedFragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Goal>> {
    private static final String TAG = "HistoryActivity";

    protected HistoryChartView historyChartView;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void setup(View view) {
        historyChartView = (HistoryChartView) view.findViewById(R.id.chart_history);
    }

    @Override
    public void onUnitChanged(Unit unit) {
        historyChartView.switchUnit(unit);
    }

    @Override
    public void onDateProgressChanged(int progress) {
        historyChartView.notifyDateChanged(progress);
    }

    @Override
    public void onLoadFinished(Loader<List<Goal>> loader, List<Goal> data) {
        historyChartView.setGoalDates(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Goal>> loader) {
        historyChartView.refresh();
    }

}

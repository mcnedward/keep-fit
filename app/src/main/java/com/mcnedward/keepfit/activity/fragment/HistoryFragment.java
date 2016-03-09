package com.mcnedward.keepfit.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.loader.GoalDateDataLoader;
import com.mcnedward.keepfit.utils.adapter.UnitAdapter;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.view.HistoryChartView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/23/2016.
 */
public class HistoryFragment extends BaseFragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Goal>> {
    private static final String TAG = "HistoryActivity";
    private final int LOADER_ID = new Random().nextInt(1000);

    private Context context;
    private GoalRepository repository;
    private HistoryChartView historyChartView;
    private Spinner spinUnit;
    private UnitAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initialize(view);
        return view;
    }

    @Override
    protected void initialize(View view) {
        context = view.getContext();
        repository = new GoalRepository(context);
        historyChartView = (HistoryChartView) view.findViewById(R.id.history_chart);

        registerReceivers();
        initializeSpinner(view);
        initializeLoader();
    }

    @Override
    protected void addGoalActionReceived(Goal goal) {
        historyChartView.addGoal(goal);
    }

    @Override
    protected void deleteGoalActionReceived(Goal goal) {
    }

    @Override
    protected void updateGoalOfDayActionReceived(Goal goal) {
        historyChartView.editGoal(goal);
    }

    @Override
    protected void updateGoalAmountActionReceived(Goal goal) {
        historyChartView.editGoal(goal);
    }

    @Override
    protected void editModeSwitchActionReceived(boolean isEditMode, String date) {

    }

    @Override
    protected void calendarChangeActionReceived(String date) {

    }

    private void initializeSpinner(View view) {
        spinUnit = (Spinner) view.findViewById(R.id.spinner_history_unit);
        adapter = new UnitAdapter(context, android.R.layout.simple_spinner_item, Arrays.asList(Unit.values()));
        spinUnit.setAdapter(adapter);
        spinUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switchUnit(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void switchUnit(Unit unit) {
        historyChartView.switchUnit(unit);
    }

    private void initializeLoader() {
        Log.d(TAG, "### Calling initLoader! ###");
        if (getActivity().getSupportLoaderManager().getLoader(LOADER_ID) == null)
            Log.d(TAG, "### Initializing a new Loader... ###");
        else
            Log.d(TAG, "### Reconnecting with existing Loader (id " + LOADER_ID + ")... ###");
        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    @Override
    public Loader<List<Goal>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "CREATING LOADER " + id);
        return new GoalDateDataLoader(context);
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

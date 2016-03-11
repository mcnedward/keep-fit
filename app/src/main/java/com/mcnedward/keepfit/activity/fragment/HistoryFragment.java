package com.mcnedward.keepfit.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.loader.GoalDateDataLoader;
import com.mcnedward.keepfit.utils.adapter.EnumAdapter;
import com.mcnedward.keepfit.utils.enums.Date;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.view.HistoryChartView;

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
    private GoalDateDataLoader loader;
    private HistoryChartView historyChartView;
    private Spinner spinUnit;
    private EnumAdapter adapter;
    private Spinner spinDate;
    private ArrayAdapter dateAdapter;
    private SeekBar seekBar;
    private TextView txtHistoryNoDates;

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

        initializeSpinner(view);
        initializeProgressDate(view);
        initializeLoader();
    }

    private void initializeSpinner(View view) {
        spinUnit = (Spinner) view.findViewById(R.id.spinner_history_unit);
        adapter = new EnumAdapter(context, android.R.layout.simple_spinner_item, Unit.getEnums());
        spinUnit.setAdapter(adapter);
        spinUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switchUnit((Unit) adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDate = (Spinner) view.findViewById(R.id.spinner_history_date);
        dateAdapter = new EnumAdapter(context, android.R.layout.simple_spinner_item, Date.getEnums());
        spinDate.setAdapter(dateAdapter);
        spinDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Date date = (Date) dateAdapter.getItem(position);
                seekBar.setMax(date.getMaxDays());
                seekBar.setProgress(date.getMaxDays());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeProgressDate(View view) {
        seekBar = (SeekBar) view.findViewById(R.id.progress_history_date);
        final Date date = (Date) spinDate.getSelectedItem();
        seekBar.setMax(date.getMaxDays());
        seekBar.setProgress(date.getMaxDays());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                loader.loadFromRange(progress);
                historyChartView.notifyDateChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
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
        loader = new GoalDateDataLoader(context);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Goal>> loader, List<Goal> data) {
        historyChartView.setGoalDates(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Goal>> loader) {
        historyChartView.refresh();
    }

    @Override
    protected void addGoalActionReceived(Goal goal) {
        loader.forceLoad();
    }

    @Override
    protected void deleteGoalActionReceived(Goal goal) {
        loader.forceLoad();
    }

    @Override
    protected void updateGoalOfDayActionReceived(Goal goal) {
        loader.forceLoad();
    }

    @Override
    protected void updateGoalAmountActionReceived(Goal goal) {
        loader.forceLoad();
    }

    @Override
    protected void editModeSwitchActionReceived(boolean isEditMode, String date) {

    }

    @Override
    protected void calendarChangeActionReceived(String date) {

    }
}

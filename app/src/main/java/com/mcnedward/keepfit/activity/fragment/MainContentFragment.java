package com.mcnedward.keepfit.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.GoalListAdapter;
import com.mcnedward.keepfit.repository.loader.GoalDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 1/31/2016.
 */
public class MainContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Goal>> {
    private final static String TAG = "MainContentFragment";
    public static final int LOADER_ID = new Random().nextInt(1000);

    private static Context context;

    private View stepCounter;
    private static GoalListAdapter adapter;

    private static Goal goalOfDay;
    private static TextView goalOfDayName;
    private static TextView goalOfDayStepAmount;
    private static TextView goalOfDayStepGoal;
    private static ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = null;// inflater.inflate(R.layout.main_content_fragment, container);
        initialize(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static void addGoal(Goal goal) {
        adapter.addGoal(goal);
        updateGoalOfDay(goal);
    }

    public static void editGoal(Goal goal) {
        adapter.editGoal(goal);
    }

    public static void updateStepCounter(Goal goal) {
        adapter.editGoal(goal);
        updateGoalOfDay(goal);
    }

    private void initialize(View view) {
        context = view.getContext();

        stepCounter = view.findViewById(R.id.step_counter);
        Extension.setRippleBackground(stepCounter, context);
        final Activity activity = getActivity();
        stepCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goalOfDay != null)
                    Extension.startStepCounterPopup(goalOfDay, activity);
            }
        });

        goalOfDayName = (TextView) view.findViewById(R.id.goal_of_day_name);
        goalOfDayStepAmount = (TextView) view.findViewById(R.id.goal_of_day_step_amount);
        goalOfDayStepGoal = (TextView) view.findViewById(R.id.goal_of_day_step_goal);

        initializeLoader();

        progressBar = (ProgressBar) view.findViewById(R.id.step_progress_bar);
        progressBar.setProgress(0);
    }

    private static void updateGoalOfDay(Goal goal) {
        goalOfDay = goal;
        goalOfDayName.setText(goalOfDay.getName());
        goalOfDayStepAmount.setText(String.valueOf(goalOfDay.getStepAmount()));
        goalOfDayStepGoal.setText(String.valueOf(goalOfDay.getStepGoal()));
        progressBar.setMax(goalOfDay.getStepGoal());
        progressBar.setProgress(goalOfDay.getStepAmount());
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
        Log.d(TAG, "CREATING LOADER");
        return new GoalDataLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<List<Goal>> loader, List<Goal> data) {
        adapter.setGroups(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Goal>> loader) {
        adapter.setGroups(new ArrayList<Goal>());
    }
}

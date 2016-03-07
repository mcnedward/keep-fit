package com.mcnedward.keepfit.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.GoalDate;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.loader.GoalDateDataLoader;
import com.mcnedward.keepfit.utils.GoalDateAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/23/2016.
 */
public class HistoryFragment extends BaseFragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<GoalDate>> {
    private static final String TAG = "HistoryActivity";
    private final int LOADER_ID = new Random().nextInt(1000);

    private Context context;
    private GoalRepository repository;
    private GoalDateAdapter expListAdapter;

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

        ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.history_expandable_list);
        expListAdapter = new GoalDateAdapter(context);
        expListView.setAdapter(expListAdapter);

        initializeLoader();
    }

    @Override
    protected void registerReceivers() {

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
    public Loader<List<GoalDate>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "CREATING LOADER " + id);
        return new GoalDateDataLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<List<GoalDate>> loader, List<GoalDate> data) {
        List<String> dates = new ArrayList<>();
        List<List<Goal>> goals = new ArrayList<>();
        for (GoalDate gco : data) {
            dates.add(gco.getDate());
            goals.add(gco.getGoals());
        }
        expListAdapter.setUp(dates, goals);
    }

    @Override
    public void onLoaderReset(Loader<List<GoalDate>> loader) {
        expListAdapter.setUp(new ArrayList<String>(), new ArrayList<List<Goal>>());
    }
}

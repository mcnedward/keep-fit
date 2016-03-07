package com.mcnedward.keepfit.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.loader.GoalDataLoader;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.utils.GoalListAdapter;
import com.mcnedward.keepfit.utils.enums.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/23/2016.
 */
public abstract class BaseGoalListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Goal>> {
    private static final String TAG = "BaseGoalListFragment";
    private final int LOADER_ID = new Random().nextInt(1000);

    protected static Context context;
    protected IGoalRepository goalRepository;
    private GoalListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        initialize(view);
        return view;
    }

    protected abstract int getLayoutResource();

    protected abstract void itemClickAction(Goal goal);

    protected abstract void itemLongClickAction(Goal goal);

    private void initialize(View view) {
        context = view.getContext();
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter("addGoal"));

        goalRepository = new GoalRepository(context);

        initializeGoalList(view);
        initializeLoader();
    }

    private void initializeGoalList(View view) {
        adapter = new GoalListAdapter(getContext());
        ListView goalList = (ListView) view.findViewById(R.id.goal_list);

        goalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemClickAction(adapter.getItem(i));
            }
        });
        goalList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemLongClickAction(adapter.getItem(i));
                return true;
            }
        });
        goalList.setAdapter(adapter);
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

    public class FragmentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Action action = Action.getById(intent.getIntExtra("action", 0));
            switch (action) {
                case ADD_GOAL_ACTIVITY:
                    Goal goal = (Goal) intent.getSerializableExtra("goal");
                    adapter.addGoal(goal);
                    break;
            }
        }
    }
}

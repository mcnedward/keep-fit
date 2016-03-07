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
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.repository.loader.GoalDataLoader;
import com.mcnedward.keepfit.utils.GoalListAdapter;
import com.mcnedward.keepfit.utils.enums.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/22/2016.
 */
public class GoalsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Goal>> {
    private static final String TAG = "BaseGoalListFragment";
    private final int LOADER_ID = new Random().nextInt(1000);

    private Context context;
    private IGoalRepository goalRepository;
    private GoalListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        initialize(view);
        return view;
    }

    @Override
    protected void initialize(View view) {
        context = view.getContext();

        goalRepository = new GoalRepository(context);

        initializeGoalList(view);
        initializeLoader();
        registerReceivers();
    }

    @Override
    protected void registerReceivers() {
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.ADD_GOAL.title));
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.UPDATE_GOAL_OF_DAY.title));
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.UPDATE_GOAL_AMOUNT.title));

    }

    private void initializeGoalList(View view) {
        adapter = new GoalListAdapter(getContext());
        ListView goalList = (ListView) view.findViewById(R.id.goal_list);

        goalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Goal goal = adapter.getItem(i);
                Toast.makeText(getContext(), goal.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        goalList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Goal goal = adapter.getItem(i);
                Toast.makeText(getContext(), goal.toString(), Toast.LENGTH_SHORT).show();
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
        GoalDataLoader loader = new GoalDataLoader(context);
        adapter.setLoader(loader);
        return loader;
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
            Goal goal = (Goal) intent.getSerializableExtra("goal");
            switch (action) {
                case ADD_GOAL:
                    adapter.addGoal(goal);
                    break;
                case UPDATE_GOAL_OF_DAY:
                    adapter.editGoal(goal);
                    break;
                case UPDATE_GOAL_AMOUNT:
                    adapter.editGoal(goal);
                    break;
            }
        }
    }

}

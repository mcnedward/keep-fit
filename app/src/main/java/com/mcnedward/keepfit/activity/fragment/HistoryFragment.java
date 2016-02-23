package com.mcnedward.keepfit.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.GoalsCreatedOn;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.GoalHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public class HistoryFragment extends BaseFragment {
    private static final String TAG = "HistoryActivity";

    private Context context;
    private GoalRepository repository;
    private GoalHistoryAdapter expListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        context = view.getContext();
        repository = new GoalRepository(context);

        ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.history_expandable_list);
        expListAdapter = new GoalHistoryAdapter(context);
        // TODO Put this in AsyncTaskLoader!
        List<GoalsCreatedOn> goalsCreatedOn = repository.getGoalDates();
        List<String> history = new ArrayList<>();
        List<List<Goal>> goals = new ArrayList<>();
        for (GoalsCreatedOn gco : goalsCreatedOn) {
            history.add(gco.getCreatedOn());
            goals.add(gco.getGoals());
        }
        expListAdapter.setUp(history, goals);
        expListView.setAdapter(expListAdapter);
    }

}

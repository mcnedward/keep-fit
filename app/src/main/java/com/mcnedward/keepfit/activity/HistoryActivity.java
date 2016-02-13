package com.mcnedward.keepfit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.GoalsCreatedOn;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.GoalHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/13/2016.
 */
public class HistoryActivity extends AppCompatActivity {
    private final static String TAG = "HistoryActivity";

    private GoalRepository repository;
    private GoalHistoryAdapter expListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        repository = new GoalRepository(this);
        initialize();
    }

    private void initialize() {
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.history_expandable_list);
        expListAdapter = new GoalHistoryAdapter(this);
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

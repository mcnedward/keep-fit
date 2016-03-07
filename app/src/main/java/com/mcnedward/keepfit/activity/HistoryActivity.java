package com.mcnedward.keepfit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.GoalDateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/13/2016.
 */
public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";

    private GoalRepository repository;
    private GoalDateAdapter expListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_history);
        repository = new GoalRepository(this);
        initialize();
    }

    private void initialize() {
//        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.history_expandable_list);
//        expListAdapter = new GoalDateAdapter(this);
        // TODO Put this in AsyncTaskLoader!
        List<String> history = new ArrayList<>();
        List<List<Goal>> goals = new ArrayList<>();
//        for (GoalDates gco : goalsCreatedOn) {
//            history.add(gco.getDate());
//            goals.add(gco.getGoals());
//        }
        expListAdapter.setUp(history, goals);
//        expListView.setAdapter(expListAdapter);
    }

}

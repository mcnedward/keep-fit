package com.mcnedward.keepfit.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.GoalListAdapter;
import com.mcnedward.keepfit.utils.KeepFitDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 1/31/2016.
 */
public class MainContentFragment extends Fragment {

    private static Context context;

    private KeepFitDatabase database;

    private ListView goalList;
    private View stepCounter;

    private static GoalListAdapter adapter;
    private static Goal goalOfDay;
    private static TextView goalOfDayName;
    private static TextView goalOfDayStepAmount;
    private static TextView goalOfDayStepGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.main_content_fragment, container);
        initialize(view);
        return view;
    }

    public static void addGoal(Goal goal) {
        adapter.addGoal(goal);
        updateGoalOfDay(goal);
    }

    public static void editGoal(Goal goal) {
        adapter.editGoal(goal);
    }

    private void initialize(View view) {
        context = view.getContext();
        database = new KeepFitDatabase(context);

        stepCounter = view.findViewById(R.id.step_counter);
        Extension.setRippleBackground(stepCounter, context);
        final Activity activity = getActivity();
        stepCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Extension.startAlbumPopup(goalOfDay, activity);
            }
        });

        goalOfDayName = (TextView) view.findViewById(R.id.goal_of_day_name);
        goalOfDayStepAmount = (TextView) view.findViewById(R.id.goal_of_day_step_amount);
        goalOfDayStepGoal = (TextView) view.findViewById(R.id.goal_of_day_step_goal);

        goalList = (ListView) view.findViewById(R.id.goal_list);
        adapter = new GoalListAdapter(getContext());
        goalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Long tap to set this as the active goal!", Toast.LENGTH_SHORT).show();
            }
        });
        goalList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                updateGoalOfDay(adapter.getItem(i));
                return true;
            }
        });

        fillGoalList();
        goalList.setAdapter(adapter);
    }

    public static void updateStepCounter(int steps) {
        goalOfDay.setStepAmount(steps);
        goalOfDayStepAmount.setText(String.valueOf(steps));
    }

    private static void updateGoalOfDay(Goal goal) {
        goalOfDay = goal;
        goalOfDayName.setText(goalOfDay.getName());
        goalOfDayStepAmount.setText(String.valueOf(goalOfDay.getStepAmount()));
        goalOfDayStepGoal.setText(String.valueOf(goalOfDay.getStepGoal()));
    }

    private void fillGoalList() {
        List<Goal> goals = database.getAllGoals();
        if (goals.isEmpty()) return;
        updateGoalOfDay(goals.get(0));
        adapter.addGoals(goals);
    }

}

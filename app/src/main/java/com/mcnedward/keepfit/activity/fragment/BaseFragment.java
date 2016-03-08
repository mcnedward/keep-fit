package com.mcnedward.keepfit.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.enums.Action;

/**
 * Created by Edward on 2/23/2016.
 */
public abstract class BaseFragment extends Fragment {

    public enum FragmentCode {
        GOAL_OF_THE_DAY(1, "Goal Of The Day"),
        GOAL(2, "Goals"),
        HISTORY(3, "History");
        int id;
        String title;
        FragmentCode(int id, String title) {
            this.id = id;
            this.title = title;
        }
        public int id() {
            return id;
        }
        public String title() {
            return title;
        }
    }

    public static BaseFragment newInstance(FragmentCode code) {
        switch (code) {
            case GOAL_OF_THE_DAY:
                return new GoalOfDayFragment();
            case GOAL:
                return new GoalsFragment();
            case HISTORY:
                return new HistoryFragment();
        }
        return null;
    }

    protected abstract void initialize(View view);
    protected abstract void addGoalActionReceived(Goal goal);
    protected abstract void deleteGoalActionReceived(Goal goal);
    protected abstract void updateGoalOfDayActionReceived(Goal goal);
    protected abstract void updateGoalAmountActionReceived(Goal goal);
    protected abstract void editModeSwitchActionReceived(boolean isEditMode, String date);
    protected abstract void calendarChangeActionReceived(String date);

    protected void registerReceivers() {
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.ADD_GOAL.title));
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.DELETE_GOAL.title));
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.UPDATE_GOAL_OF_DAY.title));
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.UPDATE_GOAL_AMOUNT.title));
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.EDIT_MODE_SWITCH.title));
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter(Action.CALENDER_CHANGE.title));
    }

    public class FragmentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Action action = Action.getById(intent.getIntExtra("action", 0));
            Goal goal = (Goal) intent.getSerializableExtra("goal");
            switch (action) {
                case ADD_GOAL: {
                    addGoalActionReceived(goal);
                    break;
                }
                case DELETE_GOAL: {
                    deleteGoalActionReceived(goal);
                }
                case UPDATE_GOAL_OF_DAY: {
                    updateGoalOfDayActionReceived(goal);
                    break;
                }
                case UPDATE_GOAL_AMOUNT: {
                    updateGoalAmountActionReceived(goal);
                    break;
                }
                case EDIT_MODE_SWITCH: {
                    boolean isEditMode = intent.getBooleanExtra("isEditMode", false);
                    String date = intent.getStringExtra("date");
                    editModeSwitchActionReceived(isEditMode, date);
                    break;
                }
                case CALENDER_CHANGE: {
                    String date = intent.getStringExtra("date");
                    calendarChangeActionReceived(date);
                    break;
                }
            }
        }
    }
}

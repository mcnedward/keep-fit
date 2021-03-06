package com.mcnedward.keepfit.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mcnedward.keepfit.activity.MainActivity;
import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.enums.Action;

/**
 * Created by Edward on 2/23/2016.
 */
public abstract class BaseFragment extends Fragment {

    private FragmentReceiver receiver;
    private boolean isReceiverRegistered = false;
    private static boolean pauseReceiver = true;

    public static BaseFragment newInstance(FragmentCode code) {
        switch (code.getCodeByTitle()) {
            case GOAL_OF_THE_DAY:
                return new GoalOfDayFragment();
            case GOALS:
                return new GoalsFragment();
            case HISTORY:
                return new HistoryFragment();
            case STATISTICS:
                return new StatisticsFragment();
        }
        return null;
    }

    @Override
    public void onResume() {
        if (!isReceiverRegistered) {
            isReceiverRegistered = true;
            registerReceivers();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isReceiverRegistered && !MainActivity.IS_IN_SETTINGS && pauseReceiver) {
            isReceiverRegistered = false;
            pauseReceiver = false;
            unRegisterReceivers();
        }
        super.onPause();
    }

    protected abstract void initialize(View view);

    protected abstract void addGoalActionReceived(Goal goal);

    protected abstract void deleteGoalActionReceived(Goal goal);

    protected abstract void updateGoalOfDayActionReceived(Goal goal);

    protected void updateGoalAmountActionReceived(Goal goal) {

    }

    protected void testModeSwitchActionReceived(boolean isTestMode, String date) {

    }

    protected void editModeSwitchActionReceived(boolean isEditMode) {

    }

    protected void calendarChangeActionReceived(String date) {

    }

    protected void notifyAlgorithmRunning(boolean running) {

    }

    private void unRegisterReceivers() {
        getActivity().unregisterReceiver(receiver);
    }

    private void registerReceivers() {
        receiver = new FragmentReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter(Action.ADD_GOAL.title));
        getActivity().registerReceiver(receiver, new IntentFilter(Action.DELETE_GOAL.title));
        getActivity().registerReceiver(receiver, new IntentFilter(Action.UPDATE_GOAL_OF_DAY.title));
        getActivity().registerReceiver(receiver, new IntentFilter(Action.UPDATE_GOAL_AMOUNT.title));
        getActivity().registerReceiver(receiver, new IntentFilter(Action.TEST_MODE_SWITCH.title));
        getActivity().registerReceiver(receiver, new IntentFilter(Action.EDIT_MODE_SWITCH.title));
        getActivity().registerReceiver(receiver, new IntentFilter(Action.CALENDER_CHANGE.title));
        getActivity().registerReceiver(receiver, new IntentFilter(Action.ALGORITHM_RUNNING.title));
    }

    public void setPauseReceiver(boolean pauseReceiver) {
        this.pauseReceiver = pauseReceiver;
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
                case TEST_MODE_SWITCH: {
                    boolean isTestMode = intent.getBooleanExtra("isTestMode", false);
                    testModeSwitchActionReceived(isTestMode, Dates.getCalendarPrettyDate(MainActivity.CALENDAR.getTime()));
                    break;
                }
                case EDIT_MODE_SWITCH: {
                    boolean isEditMode = intent.getBooleanExtra("isEditMode", false);
                    editModeSwitchActionReceived(isEditMode);
                    break;
                }
                case CALENDER_CHANGE: {
                    String date = intent.getStringExtra("date");
                    calendarChangeActionReceived(date);
                    break;
                }
                case ALGORITHM_RUNNING: {
                    boolean running = intent.getBooleanExtra("running", false);
                    notifyAlgorithmRunning(running);
                    break;
                }
            }
        }
    }
}

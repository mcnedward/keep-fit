package com.mcnedward.keepfit.activity.fragment;

import android.support.v4.app.Fragment;

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
}

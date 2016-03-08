package com.mcnedward.keepfit.utils.enums;

/**
 * Created by Edward on 3/7/2016.
 */
public enum Action {
    ADD_GOAL(1, "addGoal"),
    DELETE_GOAL(2, "deleteGoal"),
    UPDATE_GOAL_OF_DAY(3, "updateGoalOfDay"),
    UPDATE_GOAL_AMOUNT(4, "updateGoalAmount"),
    EDIT_MODE_SWITCH(5, "editModeSwitch"),
    CALENDER_CHANGE(6, "calendarChange");

    public int id;
    public String title;

    Action(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Action getById(int id) {
        for (Action action : Action.values())
            if (id == action.id)
                return action;
        return null;
    }
}

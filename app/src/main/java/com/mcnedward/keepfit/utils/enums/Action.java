package com.mcnedward.keepfit.utils.enums;

/**
 * Created by Edward on 3/7/2016.
 */
public enum Action {
    ADD_GOAL(1, "addGoal"),
    DELETE_GOAL(2, "deleteGoal"),
    UPDATE_GOAL_OF_DAY(3, "updateGoalOfDay"),
    UPDATE_GOAL_AMOUNT(4, "updateGoalAmount"),
    TEST_MODE_SWITCH(5, "testModeSwitch"),
    EDIT_MODE_SWITCH(6, "editModeSwitch"),
    CALENDER_CHANGE(7, "calendarChange"),
    TAB_ORDER_CHANGE(8, "tabOrderChange"),
    ADD_GOAL_POPUP(9, "addGoalPopup"),
    GOAL_REACHED(10, "goalReached"),
    ALGORITHM_CHANGE(11, "algorithmChange");

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

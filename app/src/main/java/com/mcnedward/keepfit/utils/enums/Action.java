package com.mcnedward.keepfit.utils.enums;

/**
 * Created by Edward on 3/7/2016.
 */
public enum Action {
    ADD_GOAL(1, "addGoal"),
    UPDATE_GOAL_OF_DAY(2, "updateGoalOfDay"),
    UPDATE_GOAL_AMOUNT(3, "updateGoalAmount");

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

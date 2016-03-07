package com.mcnedward.keepfit.utils.enums;

/**
 * Created by Edward on 3/7/2016.
 */
public enum Action {
    ADD_GOAL_ACTIVITY(1),
    UPDATE_GOAL_OF_DAY(2);

    int id;

    Action(int id) {
        this.id = id;
    }

    public static Action getById(int id) {
        for (Action action : Action.values())
            if (id == action.id)
                return action;
        return null;
    }
}

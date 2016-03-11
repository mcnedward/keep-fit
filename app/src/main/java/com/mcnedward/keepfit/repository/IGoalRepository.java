package com.mcnedward.keepfit.repository;

import com.mcnedward.keepfit.model.Goal;

import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public interface IGoalRepository extends IRepository<Goal> {

    Goal getGoalByName(String goalName);
    Goal getGoalOfDay();
    void setGoalOfDay(Goal goal);
    List<Goal> getGoalHistory();
    List<Goal> getGoalsForDay();
    List<Goal> getGoalsInRange(int dateRange);

}

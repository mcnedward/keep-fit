package com.mcnedward.keepfit.repository;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.History;

/**
 * Created by Edward on 2/23/2016.
 */
public interface IHistoryRepository extends IRepository<History> {

    History getHistoryForCurrentDate();
    History getHistoryForDate(String date);
    History getHistoryForGoal(Goal goal);
    boolean updateGoalOfDay(Goal goal);

}

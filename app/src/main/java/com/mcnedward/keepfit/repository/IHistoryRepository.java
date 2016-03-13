package com.mcnedward.keepfit.repository;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.History;

import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public interface IHistoryRepository extends IRepository<History> {

    History getHistoryForCurrentDate();

    List<Long> getGoalOfDayIds();

    List<Long> getGoalOfDayIdsInRange(int dateRange);
}

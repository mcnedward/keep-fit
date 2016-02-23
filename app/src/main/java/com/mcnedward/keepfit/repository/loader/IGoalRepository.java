package com.mcnedward.keepfit.repository.loader;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.GoalsCreatedOn;
import com.mcnedward.keepfit.repository.IRepository;

import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public interface IGoalRepository extends IRepository<Goal> {

    Goal getGoalByName(String goalName);
    List<GoalsCreatedOn> getGoalDates();

}

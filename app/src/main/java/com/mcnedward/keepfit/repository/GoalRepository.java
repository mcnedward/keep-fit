package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.History;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/8/2016.
 */
public class GoalRepository extends Repository<Goal> implements IGoalRepository {
    private static final String TAG = "GoalRepository";

    private IHistoryRepository historyRepository;

    public GoalRepository(Context context) {
        super(context);
        historyRepository = new HistoryRepository(context);
    }

    public GoalRepository(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public Goal save(Goal goal) throws EntityAlreadyExistsException {
        goal.setIsGoalOfDay(true);
        // Check for history for this day
        History history = historyRepository.getHistoryForCurrentDate();
        Goal currentGoalOfDay = get(history.getGoalOfDayId());
        if (currentGoalOfDay != null) {
            currentGoalOfDay.setIsGoalOfDay(false);
            try {
                super.update(currentGoalOfDay);
            } catch (EntityDoesNotExistException e) {
                e.printStackTrace();
            }
        }
        goal.setHistoryId(history.getId());
        goal.setCreatedOn(history.getCreatedOn());
        goal.setStepAmount(history.getTotalStepsForDay());
        goal = super.save(goal);
        history.setGoalOfDayId(goal.getId());
        try {
            historyRepository.update(history);
        } catch (EntityDoesNotExistException e) {
            e.printStackTrace();
        }
        return goal;
    }

    @Override
    public boolean update(Goal goal) throws EntityDoesNotExistException {
        History history = historyRepository.getHistoryForCurrentDate();
        Goal currentGoalOfDay = get(history.getGoalOfDayId());
        if (currentGoalOfDay != null && currentGoalOfDay.getId() != goal.getId()) {
            currentGoalOfDay.setIsGoalOfDay(false);
            super.update(currentGoalOfDay);
        }

        // Convert the step amount. First ensure that the Unit is in Meters, then change to Steps
        Unit goalUnit = goal.getUnit();
        double amount = goal.getStepAmount();
        if (!goalUnit.equals(Unit.METER)) {
            amount = Unit.convert(goalUnit, Unit.METER, amount);
        }
        double stepAmount = Unit.convert(Unit.METER, Unit.STEP, amount);

        history.setTotalStepsForDay(stepAmount);
        history.setGoalOfDayId(goal.getId());
        historyRepository.update(history);
        return super.update(goal);
    }

    @Override
    public Goal getGoalByName(String goalName) {
        return read(DatabaseHelper.G_GOAL + " = ?", new String[]{goalName}, null, null, null).get(0);
    }

    @Override
    public Goal getGoalOfDay() {
        History history = historyRepository.getHistoryForCurrentDate();
        return get(history.getGoalOfDayId());
    }

    @Override
    public void setGoalOfDay(Goal goal) {
        goal.setIsGoalOfDay(true);
        Goal currentGoalOfDay = getGoalOfDay();
        currentGoalOfDay.setIsGoalOfDay(false);
        History history = historyRepository.getHistoryForCurrentDate();
        history.setGoalOfDayId(goal.getId());
        try {
            historyRepository.update(history);
            super.update(currentGoalOfDay);
            super.update(goal);
        } catch (EntityDoesNotExistException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Goal> getGoalHistory() {
        List<Long> goalIds = historyRepository.getGoalOfDayIds();
        return getGoalsFromIdList(goalIds);
    }

    @Override
    public List<Goal> getGoalsForDay() {
        History history = historyRepository.getHistoryForCurrentDate();
        List<Goal> goals = read(DatabaseHelper.G_HISTORY_ID + " = ?", new String[]{String.valueOf(history.getId())}, null, null, DatabaseHelper.G_IS_GOAL_OF_DAY + " DESC");
        return goals;
    }

    @Override
    public List<Goal> getGoalsInRange(int dateRange) {
        List<Long> goalIds = historyRepository.getGoalOfDayIdsInRange(dateRange);
        return getGoalsFromIdList(goalIds);
    }

    private List<Goal> getGoalsFromIdList(List<Long> goalIds) {
        StringBuilder query = new StringBuilder(DatabaseHelper.ID + " IN (");
        String[] goalIdArray = new String[goalIds.size()];
        for (int i = 0; i < goalIds.size(); i++) {
            long id = goalIds.get(i);
            goalIdArray[i] = String.valueOf(id);
            if (i != goalIds.size() - 1)
                query.append("?, ");
            else
                query.append("?");
        }
        query.append(")");
        return read(query.toString(), goalIdArray, null, null, null);
    }

    @Override
    public void populateTestData() {
        List<String> timestamps = new ArrayList<>();
        timestamps.add("2016-03-07");
        timestamps.add("2016-03-06");
        timestamps.add("2016-03-05");
        timestamps.add("2016-03-04");
        timestamps.add("2016-03-03");
        timestamps.add("2016-03-02");
        timestamps.add("2016-03-01");
        timestamps.add("2016-02-29");
        timestamps.add("2016-02-28");
        timestamps.add("2016-02-27");
        timestamps.add("2016-02-26");
        timestamps.add("2016-02-25");
        timestamps.add("2016-02-24");
        timestamps.add("2016-02-23");
        timestamps.add("2016-02-22");
        timestamps.add("2016-02-21");
        timestamps.add("2016-02-20");
        timestamps.add("2016-01-29");
        timestamps.add("2016-01-28");
        timestamps.add("2016-01-27");
        timestamps.add("2016-01-25");
        timestamps.add("2016-01-23");
        timestamps.add("2016-01-22");
        timestamps.add("2016-01-21");
        timestamps.add("2016-01-20");
        timestamps.add("2016-01-19");
        timestamps.add("2016-01-18");
        Random rand = new Random();
        for (String timestamp : timestamps) {
            int stepGoal = rand.nextInt(1000);
            if (stepGoal <= 2) stepGoal = 3;
            int stepAmount = rand.nextInt(stepGoal / 2);
            Goal goal = new Goal();
            goal.setName("Goal " + stepGoal);
            goal.setStepGoal(stepGoal);
            goal.setStepAmount(stepAmount);
            goal.setIsGoalOfDay(true);
            goal.setUnit(Unit.METER);
            goal.setCreatedOn(timestamp);
            goal.setStepAmount(stepAmount);

            History history = new History(timestamp);
            history.setTotalStepsForDay(goal.getStepAmount());

            try {
                history = historyRepository.save(history);
                goal.setHistoryId(history.getId());
                goal = super.save(goal);
                history.setGoalOfDayId(goal.getId());
                historyRepository.update(history);
            } catch (EntityAlreadyExistsException e) {
                e.printStackTrace();
            } catch (EntityDoesNotExistException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String[] getAllColumns() {
        return new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.G_GOAL,
                DatabaseHelper.G_STEP_GOAL,
                DatabaseHelper.G_IS_GOAL_OF_DAY,
                DatabaseHelper.G_UNIT,
                DatabaseHelper.G_HISTORY_ID
        };
    }

    /**
     * Generates a Goal object from the database cursor.
     * It is important to place the StepGoal BEFORE the StepAmount, since calculations done in setStepAmount() are reliant on StepGoal being already set.
     *
     * @param cursor The cursor to use for generating the Goal
     * @return The generated Goal
     */
    @Override
    public Goal generateObjectFromCursor(Cursor cursor) {
        Goal goal = new Goal();
        goal.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_GOAL)));
        goal.setStepGoal(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_GOAL)));
        goal.setIsGoalOfDay(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_IS_GOAL_OF_DAY)) == 1);
        goal.setUnit(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_UNIT)));
        goal.setHistoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_HISTORY_ID)));
        History history = historyRepository.get(goal.getHistoryId());
        goal.setStepAmount(history.getTotalStepsForDay());
        goal.setCreatedOn(history.getCreatedOn());
        return goal;
    }

    /**
     * Generates the values for a Goal entity to be saved in the database.
     *
     * @param entity The Goal entity to use for database values
     * @return The ContentValues to use in the database
     */
    @Override
    public ContentValues generateContentValuesFromEntity(Goal entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.G_GOAL, entity.getName());
        values.put(DatabaseHelper.G_STEP_GOAL, entity.getStepGoal());
        values.put(DatabaseHelper.G_IS_GOAL_OF_DAY, entity.isGoalOfDay());
        values.put(DatabaseHelper.G_UNIT, entity.getUnitId());
        values.put(DatabaseHelper.G_HISTORY_ID, entity.getHistoryId());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.GOALS_TABLE;
    }
}

package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.GoalsCreatedOn;
import com.mcnedward.keepfit.model.History;
import com.mcnedward.keepfit.repository.loader.IGoalRepository;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/8/2016.
 */
public class GoalRepository extends Repository<Goal> implements IGoalRepository {
    private final static String TAG = "GoalRepository";

    HistoryRepository historyRepository;

    public GoalRepository(Context context) {
        super(context);
        historyRepository = new HistoryRepository(context, this);
    }

    @Override
    public Goal save(Goal goal) throws EntityAlreadyExistsException {
        String timestamp = Extension.getTimestamp();
        String datestamp = Extension.getDateStamp();
        goal.setCreatedOn(timestamp);
        goal.setUpdatedOn(timestamp);
        goal = super.save(goal);
        History history = historyRepository.getHistoryForDate(datestamp);
        if (history == null) {
            history = new History(datestamp, goal);
            historyRepository.save(history);
        }
        history.setGoal(goal);
        try {
            historyRepository.update(history);
        } catch (EntityDoesNotExistException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return goal;
    }

    @Override
    public boolean update(Goal goal) throws EntityDoesNotExistException {
        goal.setUpdatedOn(Extension.getTimestamp());
        return super.update(goal);
    }

    public Goal getGoalByName(String goalName) {
        return read(DatabaseHelper.G_GOAL + " = ?", new String[] {goalName}, null, null, null).get(0);
    }

    public List<GoalsCreatedOn> getGoalDates() {
        List<GoalsCreatedOn> goalsCreatedOn = new ArrayList<>();
        // Get the distinct goal dates
        List<Goal> goalDates = readDistinct(null, null, DatabaseHelper.G_CREATED_ON, null, null, null);
        List<String> dates = new ArrayList<>();
        for (Goal goal : goalDates)
            dates.add(goal.getCreatedOn());
        // Sort all of the goals by date
        List<Goal> goals = retrieve();
        for (String date : dates) {
            GoalsCreatedOn gco = new GoalsCreatedOn(date);
            for (Goal goal : goals) {
                if (goal.getCreatedOn().equals(date)) {
                    gco.add(goal);
                }
            }
            goalsCreatedOn.add(gco);
        }
        return goalsCreatedOn;
    }

    public void fillOldDateTestData() {
        List<String> timestamps = new ArrayList<>();
        timestamps.add("12-02-2016-07-46-31");
        timestamps.add("11-02-2016-07-46-31");
        timestamps.add("10-02-2016-07-46-31");
        timestamps.add("09-02-2016-07-46-31");
        timestamps.add("08-02-2016-07-46-31");
        Random rand = new Random();
        for (String timestamp : timestamps)
            for (int i = 0; i < 10; i++) {
                int stepGoal = rand.nextInt(1000);
                int stepAmount = rand.nextInt(stepGoal);
                Goal goal = new Goal();
                goal.setName("Goal " + i);
                goal.setStepAmount(stepAmount);
                goal.setStepGoal(stepGoal);
                goal.setCreatedOn(timestamp);
                try {
                    super.save(goal);
                } catch (EntityAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public String[] getAllColumns() {
        return new String[] {DatabaseHelper.ID, DatabaseHelper.G_GOAL, DatabaseHelper.G_STEP_AMOUNT, DatabaseHelper.G_STEP_GOAL, DatabaseHelper.G_IS_GOAL_OF_DAY, DatabaseHelper.G_CREATED_ON, DatabaseHelper.G_UPDATED_ON};
    }

    @Override
    public Goal generateObjectFromCursor(Cursor cursor) {
        Goal goal = new Goal();
        goal.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_GOAL)));
        goal.setStepAmount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_AMOUNT)));
        goal.setStepGoal(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_GOAL)));
        goal.setCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_CREATED_ON)));
        goal.setUpdatedOn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_UPDATED_ON)));
        return goal;
    }

    @Override
    public ContentValues generateContentValuesFromEntity(Goal entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.G_GOAL, entity.getName());
        values.put(DatabaseHelper.G_STEP_AMOUNT, entity.getStepAmount());
        values.put(DatabaseHelper.G_STEP_GOAL, entity.getStepGoal());
        values.put(DatabaseHelper.G_CREATED_ON, entity.getCreatedOn());
        values.put(DatabaseHelper.G_UPDATED_ON, entity.getUpdatedOn());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.GOAL_TABLE;
    }
}
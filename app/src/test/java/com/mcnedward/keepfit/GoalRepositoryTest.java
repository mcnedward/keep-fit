package com.mcnedward.keepfit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContext;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.DatabaseHelper;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Created by Edward on 3/11/2016.
 */
public class GoalRepositoryTest extends AndroidTestCase {

    private IGoalRepository repository;

    public void setUp() throws Exception {
        super.setUp();

        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        assertNotNull(context);
        DatabaseHelper helper = new DatabaseHelper(context);
        assertNotNull(helper);
        assertNotNull(helper.getDatabaseName());
        assertNotNull(helper.getWritableDatabase());
        repository = new GoalRepository(helper);
        assertNotNull(repository);
    }

    public void tearDown() throws Exception {
        repository.close();
        super.tearDown();
    }

    public void testAddEntry() {
        Goal goal = new Goal("Test Goal", 50);
        Goal savedGoal = null;
        try {
            savedGoal = repository.save(goal);
        } catch (EntityAlreadyExistsException e) {
            fail();
        }
        Assert.assertThat(savedGoal, is(notNullValue()));
    }
}

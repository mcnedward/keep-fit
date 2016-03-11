package com.mcnedward.keepfit;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.mcnedward.keepfit.activity.MainActivity;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.DatabaseHelper;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;

import junit.framework.Assert;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Edward on 3/11/2016.
 */
public class DatabaseHelperTest extends InstrumentationTestCase {
    private DatabaseHelper helper;
    private IGoalRepository repository;

    public DatabaseHelperTest() {
    }

    @Before
    public void setUp() throws Exception {
        helper = new DatabaseHelper(getInstrumentation().getTargetContext());
        repository = new GoalRepository(helper);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldSaveGoal() throws Exception {
        Goal goal = new Goal("Test Goal", 50);
        Goal savedGoal = null;
        savedGoal = repository.save(goal);
        Assert.assertNotNull(savedGoal);
    }
}

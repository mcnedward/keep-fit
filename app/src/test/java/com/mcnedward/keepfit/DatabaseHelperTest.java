package com.mcnedward.keepfit;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import static android.support.test.InstrumentationRegistry.getTargetContext;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.DatabaseHelper;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Created by Edward on 3/11/2016.
 */
public class DatabaseHelperTest extends AndroidTestCase {

    private DatabaseHelper helper;
    private IGoalRepository repository;

    public DatabaseHelperTest() {
        super();
    }

    @Before
    public void setUp() throws Exception {
        helper = new DatabaseHelper(getContext());
        repository = new GoalRepository(helper);
    }

    @After
    public void tearDown() throws Exception {
        helper.close();
    }

    @Test
    public void shouldSaveGoal() throws Exception {
        Goal goal = new Goal("Test Goal", 50);
        Goal savedGoal = null;
        savedGoal = repository.save(goal);
        Assert.assertThat(savedGoal, is(notNullValue()));
    }

}

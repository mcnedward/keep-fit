package com.mcnedward.keepfit.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.fragment.BaseFragment;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.Extension;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static boolean IS_EDIT_MODE;
    public static Calendar CALENDAR;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private MenuItem calendarItem;

    private IGoalRepository goalRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialize();
    }

    private void initialize() {
        // Set up the ViewPager with the sections adapter.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        goalRepository = new GoalRepository(this);
    }

    private void initializeCalendarButton(MenuItem item) {
        CALENDAR = Calendar.getInstance();
        final Activity activity = this;
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        CALENDAR.set(Calendar.YEAR, year);
                        CALENDAR.set(Calendar.MONTH, monthOfYear);
                        CALENDAR.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Extension.broadcastCalendarChange(Dates.getCalendarPrettyDate(CALENDAR.getTime()), activity);
                    }
                };
                new DatePickerDialog(MainActivity.this, date, CALENDAR
                        .get(Calendar.YEAR), CALENDAR.get(Calendar.MONTH),
                        CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
                return true;
            }
        });
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements
            ViewPager.OnPageChangeListener {

        final private int PAGE_COUNT = BaseFragment.FragmentCode.values().length;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BaseFragment.newInstance(BaseFragment.FragmentCode.values()[position]);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return BaseFragment.FragmentCode.values()[position].title();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        calendarItem = menu.findItem(R.id.action_calendar);
        initializeCalendarButton(calendarItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            IS_EDIT_MODE = !item.isChecked();
            Extension.broadcastEditModeSwitch(IS_EDIT_MODE, Dates.getCalendarPrettyDate(CALENDAR.getTime()), this);
            if (item.isChecked()) {
                item.setChecked(false);
                calendarItem.setVisible(false);
            } else {
                item.setChecked(true);
                calendarItem.setVisible(true);
            }
            return true;
        }
        if (id == R.id.action_settings) {
            Extension.startSettingsActivity(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

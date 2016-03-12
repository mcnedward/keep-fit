package com.mcnedward.keepfit.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.fragment.BaseFragment;
import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.repository.FragmentCodeRepository;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IFragmentCodeRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static boolean IS_TEST_MODE;
    public static boolean IS_EDIT_MODE;
    public static Calendar CALENDAR;
    public static boolean IS_IN_SETTINGS = false;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private MenuItem calendarItem;
    private List<FragmentCode> fragmentCodes;

    private IGoalRepository goalRepository;
    private IFragmentCodeRepository fragmentCodeRepository;
    private ActionReceiver receiver;
    private boolean isReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        receiver = new ActionReceiver();

        goalRepository = new GoalRepository(this);
        fragmentCodeRepository = new FragmentCodeRepository(this);

        fragmentCodes = fragmentCodeRepository.getFragmentCodesSorted();

        initialize();
    }

    @Override
    public void onResume() {
        if (!isReceiverRegistered) {
            isReceiverRegistered = true;
            registerReceiver(receiver, new IntentFilter(Action.TEST_MODE_SWITCH.title));
            registerReceiver(receiver, new IntentFilter(Action.EDIT_MODE_SWITCH.title));
            registerReceiver(receiver, new IntentFilter(Action.TAB_ORDER_CHANGE.title));
        }
        if (IS_IN_SETTINGS) {
            IS_IN_SETTINGS = false;
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isReceiverRegistered && !IS_IN_SETTINGS) {
            isReceiverRegistered = false;
            unregisterReceiver(receiver);
        }
        super.onPause();
    }

    private void initialize() {
        setupSectionsPagerAdapter();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupSectionsPagerAdapter() {
        // Set up the ViewPager with the sections adapter.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        sectionsPagerAdapter.notifyDataSetChanged();
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

        private int pageCount;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            pageCount = fragmentCodes.size();
        }

        @Override
        public Fragment getItem(int position) {
            return BaseFragment.newInstance(fragmentCodes.get(position));
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentCodes.get(position).getTitle();
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
        if (id == R.id.action_settings) {
            IS_IN_SETTINGS = true;
            Extension.startSettingsActivity(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTestMode(boolean isTestMode) {
        IS_TEST_MODE = isTestMode;
        if (isTestMode)
            calendarItem.setVisible(true);
        else
            calendarItem.setVisible(false);
    }

    public class ActionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Action action = Action.getById(intent.getIntExtra("action", 0));
            switch (action) {
                case TEST_MODE_SWITCH: {
                    boolean isTestMode = intent.getBooleanExtra("isTestMode", false);
                    toggleTestMode(isTestMode);
                    break;
                }
                case EDIT_MODE_SWITCH: {
                    IS_EDIT_MODE = intent.getBooleanExtra("isEditMode", false);
                    break;
                }
            }
        }
    }
}

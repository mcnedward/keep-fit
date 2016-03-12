package com.mcnedward.keepfit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Statistic;
import com.mcnedward.keepfit.repository.IStatisticRepository;
import com.mcnedward.keepfit.repository.StatisticRepository;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;
import com.mcnedward.keepfit.view.SettingStatisticsView;

import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingsStatisticsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsStatisticsActivity";

    private SettingStatisticsView view;
    private IStatisticRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new SettingStatisticsView(this);
        repository = new StatisticRepository(this);
        setContentView(view);
        setTitle(R.string.statistics_view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (view.settingsChanged())
                    updateStatistics();
                else
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (view.settingsChanged())
            updateStatistics();
        else
            super.onBackPressed();
    }

    private void updateStatistics() {
        List<Statistic> statistics = view.getStatistics();
        for (Statistic statistic : statistics) {
            try {
                repository.update(statistic);
            } catch (EntityDoesNotExistException e) {
                e.printStackTrace();
            }
        }
        Extension.restartApplication(this);
        finish();
    }

}

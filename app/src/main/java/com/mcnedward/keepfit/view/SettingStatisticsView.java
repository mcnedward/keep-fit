package com.mcnedward.keepfit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Statistic;
import com.mcnedward.keepfit.repository.IStatisticRepository;
import com.mcnedward.keepfit.repository.StatisticRepository;
import com.mcnedward.keepfit.utils.listener.SettingChangedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingStatisticsView extends LinearLayout implements SettingChangedListener {
    private static final String TAG = "SettingStatisticsView";

    private Context context;
    private IStatisticRepository repository;
    private List<StatisticsSelectionView> views;
    private boolean settingsChanged;

    public SettingStatisticsView(Context context) {
        super(context);
        initialize(context);
    }

    public SettingStatisticsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(final Context context) {
        inflate(context, R.layout.view_setting_statistics, this);
        this.context = context;
        repository = new StatisticRepository(context);
        views = new ArrayList<>();
        List<Statistic> statistics = repository.retrieve();

        StatisticsSelectionView averageView = (StatisticsSelectionView) findViewById(R.id.setting_statistics_average);
        StatisticsSelectionView minimumView = (StatisticsSelectionView) findViewById(R.id.setting_statistics_minimum);
        StatisticsSelectionView maximumView = (StatisticsSelectionView) findViewById(R.id.setting_statistics_maximum);
        StatisticsSelectionView percentageView = (StatisticsSelectionView) findViewById(R.id.setting_statistics_percentage);

        for (Statistic s : statistics) {
            switch (s.getStatByTitle()) {
                case AVERAGE:
                    averageView.setStatistic(s);
                    break;
                case MINIMUM:
                    minimumView.setStatistic(s);
                    break;
                case MAXIMUM:
                    maximumView.setStatistic(s);
                    break;
                case PERCENTAGE:
                    percentageView.setStatistic(s);
                    break;
            }
        }

        views.add(averageView);
        views.add(minimumView);
        views.add(maximumView);
        views.add(percentageView);

        for (StatisticsSelectionView v : views)
            v.setSettingChangedListener(this);
    }

    public List<Statistic> getStatistics() {
        List<Statistic> statistics = new ArrayList<>();
        for (StatisticsSelectionView v : views)
            statistics.add(v.getStatistic());
        return statistics;
    }

    @Override
    public void notifySettingChanged() {
        settingsChanged = true;
    }

    public boolean settingsChanged() {
        return settingsChanged;
    }
}

package com.mcnedward.keepfit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Statistic;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.listener.SettingChangedListener;

/**
 * Created by Edward on 3/8/2016.
 */
public class StatisticsSelectionView extends LinearLayout {
    private static final String TAG = "StatisticsSelectionView";

    private Context context;
    private Statistic statistic;
    private CheckBox checkShow;
    private CheckBox checkOpen;
    private SettingChangedListener listener;

    public StatisticsSelectionView(Context context) {
        super(context);
        initialize(context);
    }

    public StatisticsSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StatisticsSelectionView,
                0, 0);

        String title = "";
        try {
            title = a.getString(R.styleable.StatisticsSelectionView_statistics_selection_title);
        } finally {
            a.recycle();
        }
        ((TextView) findViewById(R.id.statistics_selection_title)).setText(title);
    }

    private void initialize(final Context context) {
        inflate(context, R.layout.view_statistics_selection, this);
        this.context = context;
        View view = findViewById(R.id.statistics_container);
        Extension.setRippleBackground(view, context);

        checkShow = (CheckBox) findViewById(R.id.check_statistics_show);
        checkOpen = (CheckBox) findViewById(R.id.check_statistics_open);

        setupContainer(R.id.statistics_show_container, checkShow, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ignoreCheckChange) return;
                statistic.setShow(isChecked);
                listener.notifySettingChanged();
            }
        });
        setupContainer(R.id.statistics_open_container, checkOpen, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ignoreCheckChange) return;
                statistic.setOpen(isChecked);
                listener.notifySettingChanged();
            }
        });
    }

    private void setupContainer(int containerId, final CheckBox checkBox, final CompoundButton.OnCheckedChangeListener listener) {
        View view = findViewById(containerId);
        Extension.setRippleBackground(view, context);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
        checkBox.setOnCheckedChangeListener(listener);
    }

    private boolean ignoreCheckChange = false;
    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
        ignoreCheckChange = true;
        checkShow.setChecked(statistic.isShow());
        checkOpen.setChecked(statistic.isOpen());
        ignoreCheckChange = false;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setSettingChangedListener(SettingChangedListener listener) {
        this.listener = listener;
    }
}

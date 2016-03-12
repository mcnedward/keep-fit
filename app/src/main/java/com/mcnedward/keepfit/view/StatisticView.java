package com.mcnedward.keepfit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.enums.Unit;

/**
 * Created by Edward on 3/7/2016.
 */
public class StatisticView extends LinearLayout {
    private static final String TAG = "StatisticView";

    private Context context;
    private TextView txtSteps;
    private TextView txtGoalAmount;
    private View txtSlash;
    private ProgressBar progressBar;
    private ImageView viewArrow;
    private boolean showContent = false;
    private boolean isPercentage;

    public StatisticView(Context context, String goalSteps, String goalAmount) {
        super(context);
        initialize(context, "", goalSteps, goalAmount);
    }

    public StatisticView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StatisticView,
                0, 0);

        String title = "";
        try {
            title = a.getString(R.styleable.StatisticView_statistics_title);
        } finally {
            a.recycle();
        }
        initialize(context, title, "", "");
    }

    private void initialize(Context context, String title, String goalSteps, String goalAmount) {
        inflate(context, R.layout.view_statistic, this);
        this.context = context;

        ((TextView) findViewById(R.id.statistics_title)).setText(title);
        txtSteps = (TextView) findViewById(R.id.statistics_goal_steps);
        txtSteps.setText(goalSteps);
        txtGoalAmount = (TextView) findViewById(R.id.statistics_goal_amount);
        txtGoalAmount.setText(goalAmount);
        txtSlash = findViewById(R.id.statistics_slash);
        viewArrow = (ImageView) findViewById(R.id.statistics_arrow);

        progressBar = (ProgressBar) findViewById(R.id.statistics_progress_bar);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent();
            }
        });
    }

    public void setAsPercentage(int percentage) {
        txtSlash.setVisibility(GONE);
        txtGoalAmount.setText(String.valueOf(percentage) + "%");
        progressBar.setMax(100);
        progressBar.setProgress(percentage);
        isPercentage = true;
    }

    public void updateNumbers(double stepAmount, double goalAmount, Unit unit) {
        String averageStep = Unit.format(stepAmount);
        String averageGoal = Unit.format(goalAmount);
        txtSteps.setText(averageStep + " " + unit.abbreviation);
        txtGoalAmount.setText(averageGoal + " " + unit.abbreviation);

        bumpProgress(stepAmount, goalAmount);
        progressBar.setMax((int) goalAmount);
        progressBar.setProgress((int) stepAmount);
    }

    private void bumpProgress(double stepAmount, double goalAmount) {
        if (goalAmount != 0 && stepAmount != 0 && goalAmount < 1 && stepAmount < 1) {
            goalAmount *= 100;
            stepAmount *= 100;
            bumpProgress(stepAmount, goalAmount);
        }
    }

    public void toggleContent() {
        toggleContent(showContent);
    }

    public void toggleContent(boolean show) {
        if (show) {
            txtSteps.setVisibility(VISIBLE);
            txtGoalAmount.setVisibility(VISIBLE);
            progressBar.setVisibility(VISIBLE);
            if (!isPercentage) txtSlash.setVisibility(VISIBLE);
            viewArrow.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.arrow_up_float));
        } else {
            txtSteps.setVisibility(GONE);
            txtGoalAmount.setVisibility(GONE);
            progressBar.setVisibility(GONE);
            txtSlash.setVisibility(GONE);
            viewArrow.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.arrow_down_float));
        }
        showContent = !showContent;
    }

}

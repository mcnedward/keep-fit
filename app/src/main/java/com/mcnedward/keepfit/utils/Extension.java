package com.mcnedward.keepfit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.EditGoalActivity;
import com.mcnedward.keepfit.activity.HistoryActivity;
import com.mcnedward.keepfit.activity.StepCounterPopup;
import com.mcnedward.keepfit.model.Goal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Edward on 1/31/2016.
 */
public class Extension {
    private final static String TAG = "Extension";

    /**
     * Creates a new RippleDrawable for a ripple effect on a View.
     *
     * @param rippleColor     The color of the ripple.
     * @param backgroundColor The color of the background for the ripple. If this is 0, then there will be no background and the ripple effect will be circular.
     * @param context         The context.
     * @return A RippleDrawable.
     */
    public static void setRippleBackground(View view, int rippleColor, int backgroundColor, Context context) {
        view.setBackground(new RippleDrawable(
                new ColorStateList(
                        new int[][]
                                {
                                        new int[]{android.R.attr.state_window_focused},
                                },
                        new int[]
                                {
                                        ContextCompat.getColor(context, rippleColor),
                                }),
                backgroundColor == 0 ? null : new ColorDrawable(ContextCompat.getColor(context, backgroundColor)),
                null));
    }

    /**
     * Creates a new RippleDrawable for a ripple effect on a View. This will create a ripple with the default color of FireBrick for the ripple and GhostWhite for the background.
     *
     * @param context The context.
     * @return A RippleDrawable.
     */
    public static void setRippleBackground(View view, Context context) {
        setRippleBackground(view, R.color.FireBrick, R.color.GhostWhite, context);
    }

    public static void startEditGoalActivity(Goal goal, boolean isEdit, Activity activity) {
        Intent intent = new Intent(activity, EditGoalActivity.class);
        intent.putExtra("goal", goal);
        intent.putExtra("isEdit", isEdit);
        activity.startActivity(intent);
    }

    public static void startHistoryActivity(Activity activity) {
        Intent intent = new Intent(activity, HistoryActivity.class);
        activity.startActivity(intent);
    }

    public static void startStepCounterPopup(final Goal goal, final Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, StepCounterPopup.class);
                intent.putExtra("goal", goal);
                activity.startActivity(intent);
            }
        }, 300);
    }

    public static String getTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        return simpleDateFormat.format(new Date());
    }

}


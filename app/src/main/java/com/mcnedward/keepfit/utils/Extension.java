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
import com.mcnedward.keepfit.activity.AddGoalPopup;
import com.mcnedward.keepfit.activity.EditGoalActivity;
import com.mcnedward.keepfit.activity.HistoryActivity;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.enums.Action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Edward on 1/31/2016.
 */
public class Extension {
    private static final String TAG = "Extension";

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

    /***** Starting activity *****/

    public static void startHistoryActivity(Activity activity) {
        Intent intent = new Intent(activity, HistoryActivity.class);
        activity.startActivity(intent);
    }

    public static void startAddGoalPopup(final Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, AddGoalPopup.class);
                activity.startActivityForResult(intent, ActivityCode.ADD_GOAL_POPUP);
            }
        }, 300);
    }

    /***** Broadcasting *****/

    public static void broadcastAddGoal(Goal goal, Context context) {
        Intent intent = new Intent(Action.ADD_GOAL.title);
        intent.putExtra("goal", goal);
        intent.putExtra("action", Action.ADD_GOAL.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastUpdateGoalOfDay(Goal goal, Context context) {
        Intent intent = new Intent(Action.UPDATE_GOAL_OF_DAY.title);
        intent.putExtra("goal", goal);
        intent.putExtra("action", Action.UPDATE_GOAL_OF_DAY.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastUpdateGoalAmount(Goal goal, Context context) {
        Intent intent = new Intent(Action.UPDATE_GOAL_AMOUNT.title);
        intent.putExtra("goal", goal);
        intent.putExtra("action", Action.UPDATE_GOAL_AMOUNT.id);
        context.sendBroadcast(intent);
    }


    /***** Date Stuff *****/

    public static String getTimestamp() {
        return getTimestamp(new Date());
    }

    public static String getTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String getDateStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }

    public static String getPrettyDate(Date date) {
        String timestamp = getTimestamp(date);
        return getPrettyDate(timestamp);
    }

    public static String getPrettyDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String d = null;
        try {
            Date theDate = simpleDateFormat.parse(date);
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            d = f.format(theDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Calendar getCalendar(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date theDate = simpleDateFormat.parse(date);
            calendar.setTime(theDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

}


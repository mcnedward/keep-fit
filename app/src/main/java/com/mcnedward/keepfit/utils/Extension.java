package com.mcnedward.keepfit.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.AddGoalPopup;
import com.mcnedward.keepfit.activity.MainActivity;
import com.mcnedward.keepfit.activity.SettingsActivity;
import com.mcnedward.keepfit.activity.SettingsResetActivity;
import com.mcnedward.keepfit.activity.SettingsStatisticsActivity;
import com.mcnedward.keepfit.activity.SettingsTabLayoutActivity;
import com.mcnedward.keepfit.algorithm.AlgorithmService;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.enums.Action;
import com.mcnedward.keepfit.utils.enums.Settings;

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

    /*****
     * Starting activity
     *****/

    public static void startAddGoalPopup(final Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, AddGoalPopup.class);
                activity.startActivityForResult(intent, Action.ADD_GOAL_POPUP.id);
            }
        }, 300);
    }

    public static void startSettingsActivity(final Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    public static void startSettingsTabLayoutActivity(final Activity activity) {
        Intent intent = new Intent(activity, SettingsTabLayoutActivity.class);
        activity.startActivity(intent);
    }

    public static void startSettingsStatisticsActivity(final Activity activity) {
        Intent intent = new Intent(activity, SettingsStatisticsActivity.class);
        activity.startActivity(intent);
    }

    public static void startSettingsResetActivity(final Activity activity) {
        Intent intent = new Intent(activity, SettingsResetActivity.class);
        activity.startActivity(intent);
    }

    public static void setAlgorithmRunning(boolean running, final Activity activity) {
        Intent intent = new Intent(activity, AlgorithmService.class);
        intent.putExtra(Settings.RUNNING_ALGORITHM.name(), running);
        activity.startService(intent);
        broadcastAlgorithmRunning(running, activity);
    }

    public static void restartApplication(final Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("exiting", true);
        activity.startActivity(intent);
    }

    /*****
     * Broadcasting
     *****/

    public static void broadcastAddGoal(Goal goal, Context context) {
        Intent intent = new Intent(Action.ADD_GOAL.title);
        intent.putExtra("goal", goal);
        intent.putExtra("action", Action.ADD_GOAL.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastDeleteGoal(Goal goal, Context context) {
        Intent intent = new Intent(Action.DELETE_GOAL.title);
        intent.putExtra("goal", goal);
        intent.putExtra("action", Action.DELETE_GOAL.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastUpdateGoalOfDay(Goal goal, Context context) {
        Intent intent = new Intent(Action.UPDATE_GOAL_OF_DAY.title);
        intent.putExtra("goal", goal);
        intent.putExtra("action", Action.UPDATE_GOAL_OF_DAY.id);
        context.sendBroadcast(intent);

        if (goal.isGoalReached())
            sendNotificationGoalReached(goal, (Activity) context);
    }

    public static void broadcastUpdateGoalAmount(Goal goal, Context context) {
        Intent intent = new Intent(Action.UPDATE_GOAL_AMOUNT.title);
        intent.putExtra("goal", goal);
        intent.putExtra("action", Action.UPDATE_GOAL_AMOUNT.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastTestModeSwitch(boolean isTestMode, Context context) {
        Intent intent = new Intent(Action.TEST_MODE_SWITCH.title);
        intent.putExtra("isTestMode", isTestMode);
        intent.putExtra("action", Action.TEST_MODE_SWITCH.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastEditModeSwitch(boolean isEditMode, Context context) {
        Intent intent = new Intent(Action.EDIT_MODE_SWITCH.title);
        intent.putExtra("isEditMode", isEditMode);
        intent.putExtra("action", Action.EDIT_MODE_SWITCH.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastCalendarChange(String date, Context context) {
        Intent intent = new Intent(Action.CALENDER_CHANGE.title);
        intent.putExtra("date", date);
        intent.putExtra("action", Action.CALENDER_CHANGE.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastAlgorithmChange(boolean started, Context context) {
        Intent intent = new Intent(Action.ALGORITHM_CHANGE.title);
        intent.putExtra("started", started);
        intent.putExtra("action", Action.ALGORITHM_CHANGE.id);
        context.sendBroadcast(intent);
    }

    public static void broadcastAlgorithmRunning(boolean running, Context context) {
        Intent intent = new Intent(Action.ALGORITHM_RUNNING.title);
        intent.putExtra("running", running);
        intent.putExtra("action", Action.ALGORITHM_RUNNING.id);
        context.sendBroadcast(intent);
    }

    /*****
     * Notifications
     *****/

    /**
     * Source: http://developer.android.com/training/notify-user/build-notification.html
     * @param goal
     * @param activity
     */
    public static void sendNotificationGoalReached(Goal goal, final Activity activity) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.drawable.ic_sentiment_very_satisfied_black_24dp)
                        .setContentTitle("Goal Reached!")
                        .setContentText(String.format("You reached the goal %s of %s %s!", goal.getName(), goal.getStepGoal(), goal.getUnit().abbreviation));

        Intent intent = new Intent(activity, MainActivity.class);
        intent.setAction(Action.GOAL_REACHED.title);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        int notificationId = 10;
        // Gets an instance of the NotificationManager service
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        notificationManager.notify(notificationId, builder.build());
    }

}


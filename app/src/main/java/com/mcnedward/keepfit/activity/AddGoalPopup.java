package com.mcnedward.keepfit.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.mcnedward.keepfit.utils.enums.Action;
import com.mcnedward.keepfit.view.AddGoalView;

/**
 * Created by Edward on 1/31/2016.
 */
public class AddGoalPopup extends Activity {
    private static final String TAG = "AddGoalPopup";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new AddGoalView(this));
        initializeWindow();
    }

    private void initializeWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.8);
        int height = (int) (dm.heightPixels * 0.45);

        getWindow().setLayout(width, height);
    }

    public class FragmentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Action action = Action.getById(intent.getIntExtra("action", 0));
            switch (action) {
                case ADD_GOAL:
                    finish();
                    break;
            }
        }
    }


}

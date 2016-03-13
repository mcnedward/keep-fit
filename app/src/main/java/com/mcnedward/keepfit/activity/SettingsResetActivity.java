package com.mcnedward.keepfit.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Statistic;
import com.mcnedward.keepfit.repository.DatabaseHelper;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Settings;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingsResetActivity extends AppCompatActivity {
    private static final String TAG = "SettingsResetActivity";

    private Context context;
    private boolean reset = false;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_reset_statistics);
        setTitle(R.string.reset);
        context = this;
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        editor = settings.edit();

        initializeResetSettings();
        initializeResetData();
        initializeResetAll();
    }

    private void initializeResetSettings() {
        View button = findViewById(R.id.reset_settings_button);
        Extension.setRippleBackground(button, R.color.GhostWhite, R.color.FireBrick, context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(Settings.SHOWN_ALGORITHM_MESSAGE.name(), false);
                editor.putBoolean(Settings.RUNNING_ALGORITHM.name(), false);
                editor.commit();
                Toast.makeText(context, R.string.reset_settings_complete_message, Toast.LENGTH_SHORT).show();
                reset = true;
            }
        });
    }

    private void initializeResetData() {
        View button = findViewById(R.id.reset_data_button);
        Extension.setRippleBackground(button, R.color.GhostWhite, R.color.FireBrick, context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.resetData(context);
                Toast.makeText(context, R.string.reset_settings_complete_message, Toast.LENGTH_SHORT).show();
                reset = true;
            }
        });
    }

    private void initializeResetAll() {
        View button = findViewById(R.id.reset_all_button);
        Extension.setRippleBackground(button, R.color.GhostWhite, R.color.FireBrick, context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.resetDatabase(context);
                editor.putBoolean(Settings.SHOWN_ALGORITHM_MESSAGE.name(), false);
                editor.putBoolean(Settings.RUNNING_ALGORITHM.name(), false);
                editor.commit();
                Toast.makeText(context, R.string.reset_settings_complete_message, Toast.LENGTH_SHORT).show();
                reset = true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (reset)
                    restartApp();
                else
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (reset)
            restartApp();
        else
            super.onBackPressed();
    }

    private void restartApp() {
        Extension.restartApplication(this);
        finish();
    }

}

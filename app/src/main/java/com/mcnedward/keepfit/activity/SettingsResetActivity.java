package com.mcnedward.keepfit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Statistic;
import com.mcnedward.keepfit.repository.DatabaseHelper;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingsResetActivity extends AppCompatActivity {
    private static final String TAG = "SettingsResetActivity";

    private boolean reset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_reset_statistics);
        setTitle(R.string.reset);

        final Context context = this;
        View button = findViewById(R.id.reset_button);
        Extension.setRippleBackground(button, R.color.GhostWhite, R.color.FireBrick, context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.resetDatabase(context);
                findViewById(R.id.reset_complete).setVisibility(View.VISIBLE);
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

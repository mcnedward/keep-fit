package com.mcnedward.keepfit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.repository.FragmentCodeRepository;
import com.mcnedward.keepfit.repository.IFragmentCodeRepository;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;
import com.mcnedward.keepfit.view.SettingTabLayoutView;

import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingsTabLayoutActivity extends AppCompatActivity {
    private static final String TAG = "SettingsTabLayoutActivity";

    private IFragmentCodeRepository repository;
    private SettingTabLayoutView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new SettingTabLayoutView(this);
        repository = new FragmentCodeRepository(this);
        setContentView(view);
        setTitle(R.string.tab_layout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (view.valuesChanged())
                    updateFragmentCodes();
                else
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (view.valuesChanged())
            updateFragmentCodes();
        else
            super.onBackPressed();
    }

    private void updateFragmentCodes() {
        List<FragmentCode> fragmentCodes = view.getFragmentCodes();
        for (FragmentCode code : fragmentCodes) {
            try {
                repository.update(code);
            } catch (EntityDoesNotExistException e) {
                e.printStackTrace();
            }
        }
        Extension.restartApplication(this);
        finish();
    }

}

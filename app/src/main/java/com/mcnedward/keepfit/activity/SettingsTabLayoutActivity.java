package com.mcnedward.keepfit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TableLayout;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.fragment.BaseFragment;
import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.repository.FragmentCodeRepository;
import com.mcnedward.keepfit.repository.IFragmentCodeRepository;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;
import com.mcnedward.keepfit.view.SettingView;
import com.mcnedward.keepfit.view.TabLayoutView;
import com.mcnedward.keepfit.view.TabSelectionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingsTabLayoutActivity extends AppCompatActivity {
    private static final String TAG = "SettingsTabLayoutActivity";

    private IFragmentCodeRepository repository;
    private TabLayoutView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new TabLayoutView(this);
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

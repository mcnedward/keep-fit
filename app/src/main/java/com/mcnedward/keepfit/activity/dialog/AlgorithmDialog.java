package com.mcnedward.keepfit.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Settings;

/**
 * Created by Edward on 3/13/2016.
 */
public class AlgorithmDialog extends DialogFragment {
    public static final String TAG = "AlgorithmDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.algorithm_dialog_message)
                .setPositiveButton(R.string.algorithm_positive_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(Settings.SHOWN_ALGORITHM_MESSAGE.name(), true);
                        editor.commit();
                        Extension.broadcastAlgorithmChange(true, getContext());
                    }
                })
                .setNegativeButton(R.string.algorithm_negative_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }

}

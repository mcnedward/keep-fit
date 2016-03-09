package com.mcnedward.keepfit.utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.utils.enums.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class UnitAdapter extends ArrayAdapter<Unit> {
    private static final String TAG = "UnitAdapter";

    private Context context;
    private List<Unit> units;

    public UnitAdapter(Context context, int resource) {
        super(context, resource);
        units = new ArrayList<>();
        initialize(context);
    }

    public UnitAdapter(Context context, int resource, List<Unit> units) {
        super(context, resource, units);
        this.units = units;
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.item_spinner, parent, false);
        }
        Unit item = units.get(position);
        if (item != null) {
            TextView textView = (TextView) row.findViewById(R.id.simple_item);
            textView.setText(item.title);
        }
        return row;
    }
}

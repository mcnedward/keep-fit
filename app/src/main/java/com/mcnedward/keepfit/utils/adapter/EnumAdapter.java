package com.mcnedward.keepfit.utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.utils.enums.IBaseEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class EnumAdapter extends ArrayAdapter<IBaseEnum> {
    private static final String TAG = "EnumAdapter";

    private Context context;
    LayoutInflater inflater;
    private List<IBaseEnum> enums;

    public EnumAdapter(Context context, int resource) {
        super(context, resource);
        enums = new ArrayList<>();
        initialize(context);
    }

    public EnumAdapter(Context context, int resource, List<IBaseEnum> enums) {
        super(context, resource, enums);
        this.enums = enums;
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
        inflater = ((Activity)context).getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        IBaseEnum item = enums.get(position);
        if (item != null) {
            ((TextView) view).setText(item.getTitle());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_spinner, parent, false);
        }
        IBaseEnum item = enums.get(position);
        if (item != null) {
            TextView textView = (TextView) view.findViewById(R.id.simple_item);
            textView.setText(item.getTitle());
        }
        return view;
    }
}

package com.mcnedward.keepfit.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.loader.GoalDataLoader;
import com.mcnedward.keepfit.view.GoalView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 1/31/2016.
 */
public class GoalListAdapter extends ArrayAdapter<Goal> {
    private static final String TAG = "GoalListAdapter";

    private GoalDataLoader loader;
    private List<Goal> groups;
    protected Context context;
    protected LayoutInflater inflater;

    public GoalListAdapter(Context context) {
        this(new ArrayList<Goal>(), context);
    }

    public GoalListAdapter(List<Goal> groups, Context context) {
        super(context, R.layout.item_goal);
        this.groups = groups;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new GoalView(getItem(position), context);
        }
        GoalView view = (GoalView) convertView;
        view.update(groups.get(position), this);
        return view;
    }

    public void notifyDataSetChanged(boolean triggerReload) {
        if (triggerReload) {
            List<Goal> data = loader.loadInBackground();
            setGroups(data);
        } else
            notifyDataSetChanged();
    }

    public void setLoader(GoalDataLoader loader) {
        this.loader = loader;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Goal getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setGroups(List<Goal> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    public void reset() {
        groups = new ArrayList<>();
    }

}

package com.mcnedward.keepfit.utils.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.Extension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/13/2016.
 */
public class GoalDateAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "GoalDateAdapter";

    private Context context;
    private List<String> dates;
    private List<List<Goal>> goals;

    public GoalDateAdapter(Context context) {
        this.context = context;
        dates = new ArrayList<>();
        goals = new ArrayList<>();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, R.layout.item_simple, null);
        TextView textView = (TextView) convertView.findViewById(R.id.simple_item);
        String history = dates.get(groupPosition);
        textView.setText(Dates.getPrettyDateFromDatabaseDate(history));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, R.layout.simple_adapter_bottom_item, null);
        TextView txtGoalName = (TextView) convertView.findViewById(R.id.goal_name);
        TextView txtStepProgress = (TextView) convertView.findViewById(R.id.step_progress_text);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.step_progress_bar);

        Goal goal = goals.get(groupPosition).get(childPosition);
        txtGoalName.setText(goal.getName());
        String progress = String.format("%s/%s", goal.getStepAmount(), goal.getStepGoal());
        txtStepProgress.setText(progress);
        progressBar.setMax((int) goal.getStepGoal());
        progressBar.setProgress((int) goal.getStepAmount());

        return convertView;
    }

    public void setUp(List<String> dates, List<List<Goal>> goals) {
        this.dates = dates;
        this.goals = goals;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return dates.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return goals.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dates.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return goals.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

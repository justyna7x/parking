package com.example.parked2;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableTextViewAdapter extends BaseExpandableListAdapter {

    Context context;


    String[] faqs = {
            "How to pay for a parking space?",
            "How to check your zone and use the map?",
            "Nie wiem xd"
    };

    String[][]anwser={
            {"1.\tOn the menu above scroll left to find the button “Pay for space”. Tap on it.\n" +
                    "2.\tPlease select the zone you are going to park your car in (if you are unsure which zone are you in, please check the map which you can find below)\n" +
                    "3.\tSelect the amount of time you want to purchase the ticket. Remember to only leave your car in the spaces that are marked with the sign “Pay at the machine”.\n" +
                    "4.\tTap on “Pay” and you can leave your car parked!\n"},
            {"1.\tTo check your zone switch on the location service on your phone and click the button down below in the app “View your zone”\n" +
                    "2.\tIt will show you the street and the zone you are currently located in. To view the map, tap on “View map”.\n" +
                    "3.\tIf you would like to save the last location you have been (parked your car), click the button “Save your location”. It will add a pinpoint to the map, and you do not need to worry about remembering where exactly you have parked your car.\n"},
            {"Hehehe"}
    };

    public ExpandableTextViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return faqs.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return anwser[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return faqs[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return anwser[groupPosition][childPosition];
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String questionFaq = (String)getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.faqs_title, null);
        }
        TextView questionFaq2 = convertView.findViewById(R.id.instructionTitle);
        questionFaq2.setTypeface(null, Typeface.BOLD);
        questionFaq2.setText(questionFaq);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String anwserFaq = (String)getChild(groupPosition, childPosition);
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.faqs_anwser, null);
        }

        TextView anwserFaq2 = convertView.findViewById(R.id.descriptionFaqView);
        anwserFaq2.setText(anwserFaq);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

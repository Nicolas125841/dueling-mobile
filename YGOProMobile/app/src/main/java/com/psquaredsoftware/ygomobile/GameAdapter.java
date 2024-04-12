package com.psquaredsoftware.ygomobile;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GameAdapter extends ArrayAdapter {

    private ArrayList<String> names;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView v = (TextView) super.getView(position, convertView, parent);

        if(v.getText().toString().charAt(v.getText().toString().length() - 1) == 'T')
            v.setBackgroundColor(Color.GRAY);
        else if(v.getText().toString().charAt(v.getText().toString().length() - 1) == 'O')
            v.setBackgroundColor(Color.argb(255, 199, 141, 226));
        else
            v.setBackgroundColor(Color.argb(255, 141, 219, 226));

        v.setText(v.getText().toString().substring(0, v.getText().toString().lastIndexOf('#')));

        return v;

    }

    public GameAdapter(Context context, int resource, Object[] objects) {

        super(context, resource, objects);

    }

}

package com.psquaredsoftware.ygomobile;

import android.graphics.drawable.Drawable;
import android.widget.BaseAdapter;

import android.widget.*;
import java.util.*;

import android.view.*;
import android.content.*;

public class CardImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Drawable> bitmapList;
    private int width;
    private int height;

    public CardImageAdapter(Context context, ArrayList<Drawable> bitmapList, int w, int h) {
        this.context = context;
        this.bitmapList = bitmapList;
        this.width = w;
        this.height = h;
    }

    public int getCount() {
        return this.bitmapList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(this.context);
            imageView.setMinimumHeight(height);
            imageView.setMinimumWidth(width);
            imageView.setMaxHeight(height);
            imageView.setMaxWidth(width);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageDrawable(this.bitmapList.get(position));
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

}

package com.theace.natok2017;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tareq on 1/8/2017.
 */
///this is for ListView elements

    public class CustomAdapter extends ArrayAdapter<String> {

   public static int icons[]  ={R.drawable.mos,R.drawable.mit,R.drawable.cha,R.drawable.opi,R.drawable.tah};

    private final Activity context;
    private final List<String> items;

    public CustomAdapter(Activity context, List<String> items) {


        super(context, R.layout.listview_card, items);
        this.context = context;
        this.items = items;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.listview_card, null, true);

        ImageView thumbnail= (ImageView) rowView.findViewById(R.id.thumbnail_card);
        TextView areaName = (TextView) rowView.findViewById(R.id.title_card);


        thumbnail.setImageResource(icons[position]);
        areaName.setText(items.get(position)); //2 * position + 1

        return rowView;
    }

}
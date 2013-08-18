package com.sagol.umorili;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UmoriliAdapter extends ArrayAdapter<UmoriliDataContent.DataItem> {
    private final Activity activity;
    //
    private final ArrayList<UmoriliDataContent.DataItem> entries;

    // конструктор класса, принимает активность, листвью и массив данных
    public UmoriliAdapter(final Activity a, final int textViewResourceId, final ArrayList<UmoriliDataContent.DataItem> entries) {

        super(a, textViewResourceId, entries);
        this.entries = entries;
        activity = a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_detail_item, parent, false);
            holder = new ViewHolder();
            // инициализируем нашу разметку
            if (v != null) {
                holder.textView = (TextView) v.findViewById(R.id.html_view);
                v.setTag(holder);
            }
        } else {
            holder = (ViewHolder) v.getTag();
        }
        UmoriliDataContent.DataItem dataItem = entries.get(position);
        if (dataItem != null) {
            holder.textView.setText(Html.fromHtml(dataItem.element, null, null));
        }
        return v;
    }

    // для быстроты вынесли в отдельный класс
    private static class ViewHolder {
        public TextView textView;
    }
}

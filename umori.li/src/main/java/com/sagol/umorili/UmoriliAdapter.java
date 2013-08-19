package com.sagol.umorili;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
    private int style;
    private Context ctx;

    // конструктор класса, принимает активность, листвью и массив данных
    public UmoriliAdapter(final Activity a, final int textViewResourceId,
                          final ArrayList<UmoriliDataContent.DataItem> entries) {

        super(a, textViewResourceId, entries);
        this.entries = entries;
        activity = a;
        String font;
        ctx = activity.getApplicationContext();
        if (ctx != null) {
            SharedPreferences mySharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(ctx);
            font = mySharedPreferences.getString("font_list_preference", "1");
        }
        else {
            font = "1";
        }
        int font_size;
        try {
            font_size = Integer.parseInt(font);
        } catch (NumberFormatException e) {
            font_size = 1;
        }

        switch (font_size) {
            case 0:
                style = R.style.smallFont;
                break;
            case 1:
                style = R.style.normalFont;
                break;
            case 2:
                style = R.style.largeFont;
                break;
            default:
                style = R.style.normalFont;
        }
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
            if (ctx != null) {
                holder.textView.setTextAppearance(ctx, style);
            }
            holder.textView.setText(Html.fromHtml(dataItem.element, null, null));
        }
        return v;
    }

    // для быстроты вынесли в отдельный класс
    private static class ViewHolder {
        public TextView textView;
    }
}

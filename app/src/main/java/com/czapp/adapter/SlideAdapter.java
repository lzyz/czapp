package com.czapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.czapp.R;

import java.util.List;

/**
 * Created by admin on 2018/5/3.
 */

public class SlideAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public SlideAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_yc, null);
            holder = new ViewHolder();
            holder.txt = (TextView) convertView.findViewById(R.id.txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt.setText(list.get(position));
        return convertView;
    }

    public class ViewHolder {
        private TextView txt;
    }
}

package com.czapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czapp.R;
import com.czapp.bean.MeBean;

import java.util.List;

/**
 * Created by admin on 2018/5/2.
 */

public class MeAdapter extends ArrayAdapter<MeBean> {
    Context context;
    int resource;
    List<MeBean> objects;
    public MeAdapter(Context context, int resource, List<MeBean> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        LinearLayout topView;
        ImageView leftImageView;
        ImageView rightImageView;
        TextView title;
        if(convertView!=null){
            view=convertView;
        }else {
            view= LayoutInflater.from(context).inflate(resource, null);
        }
        topView=(LinearLayout)view.findViewById(R.id.me_distance);
        leftImageView=(ImageView)view.findViewById(R.id.me_left_icon);
        rightImageView=(ImageView)view.findViewById(R.id.me_right_icon);
        title=(TextView)view.findViewById(R.id.me_text);

        if(objects.get(position).getLeftImgId()!=0){
            leftImageView.setImageResource(objects.get(position).getLeftImgId());
        }

        if(objects.get(position).isIsrightImgShow()){
            rightImageView.setImageResource(objects.get(position).getRightImgId());
            rightImageView.setVisibility(View.VISIBLE);
        }
        else {
            rightImageView.setVisibility(View.GONE);
        }
        if(objects.get(position).isDistanceShow()){
            topView.setVisibility(View.VISIBLE);
        }else {
            topView.setVisibility(View.GONE);
        }
        title.setText(objects.get(position).getTitle());

        return view;
    }

}

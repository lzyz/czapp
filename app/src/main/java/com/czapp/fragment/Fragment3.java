package com.czapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.czapp.Contactperson;
import com.czapp.LocationFilter;
import com.czapp.R;
import com.czapp.activity.ActivityHealthGuide;
import com.czapp.adapter.SetAdapter;
import com.czapp.bean.SetBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by admin on 2018/4/30.
 */

public class Fragment3 extends Fragment{

    private ListView setListView;

    ArrayAdapter<SetBean> adapter;

    private boolean isHangUp = false;//用来判断挂断电话

    private boolean isLink = false;

    private Stack<String> contactsList = new Stack<String>();

    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_3, container, false);
        setListView=(ListView)view.findViewById(R.id.set_listview);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<SetBean> lists = new ArrayList<SetBean>();
        SetBean setBean = new SetBean();

        setBean.setDistanceShow(true);
        setBean.setIsrightImgShow(false);
        setBean.setLeftImgId(R.drawable.find_select);
        setBean.setTitle("我的位置");
        lists.add(setBean);


        setBean = new SetBean();
        setBean.setDistanceShow(true);
        setBean.setIsrightImgShow(false);
        setBean.setLeftImgId(R.drawable.lxr);
        setBean.setTitle("紧急联系人");
        lists.add(setBean);

        setBean = new SetBean();
        setBean.setDistanceShow(true);
        setBean.setIsrightImgShow(false);
        setBean.setLeftImgId(R.drawable.wdlx);
        setBean.setTitle("安全指南");
        lists.add(setBean);

        setBean = new SetBean();
        setBean.setDistanceShow(true);
        setBean.setIsrightImgShow(false);
        setBean.setLeftImgId(R.drawable.ycfx);
        setBean.setTitle("紧急情况");
        lists.add(setBean);

        adapter=new SetAdapter(getActivity(), R.layout.item_set, lists);
        setListView.setAdapter(adapter);
        setListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Intent intent = new Intent(getActivity(),LocationFilter.class);
                    startActivity(intent);
                }else if(position==1){
                    Intent intent = new Intent(getActivity(),Contactperson.class);
                    startActivity(intent);
                }else if(position==2){
                    startActivity(new Intent(getActivity(), ActivityHealthGuide.class));
                }else if(position==3){
                    Intent intent = new Intent(getActivity(),LocationFilter.class);
                    intent.putExtra("flag",true);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"4",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

package com.czapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.czapp.R;
import com.czapp.activity.ActivityDC;
import com.czapp.activity.ActivityPersonalMessage;
import com.czapp.activity.ActivtiyPersonalSet;
import com.czapp.adapter.MeAdapter;
import com.czapp.bean.MeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/4/30.
 */

public class Fragment4 extends Fragment{
    private ListView meListView;
    ArrayAdapter<MeBean> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_4, container, false);
        meListView=(ListView)view.findViewById(R.id.me_listview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<MeBean> lists = new ArrayList<MeBean>();

        MeBean meBean = new MeBean();

        meBean.setDistanceShow(true);
        meBean.setIsrightImgShow(false);
        meBean.setLeftImgId(R.drawable.stda);
        meBean.setTitle("身体档案");
        lists.add(meBean);


        meBean = new MeBean();
        meBean.setDistanceShow(true);
        meBean.setIsrightImgShow(false);
        meBean.setLeftImgId(R.drawable.sjzx);
        meBean.setTitle("数据中心");
        lists.add(meBean);

        meBean = new MeBean();
        meBean.setDistanceShow(true);
        meBean.setIsrightImgShow(false);
        meBean.setLeftImgId(R.drawable.grxx);
        meBean.setTitle("个人信息");
        lists.add(meBean);

        meBean = new MeBean();
        meBean.setDistanceShow(false);
        meBean.setIsrightImgShow(false);
        meBean.setLeftImgId(R.drawable.zdy);
        meBean.setTitle("自定义设置");
        lists.add(meBean);

        meBean = new MeBean();
        meBean.setDistanceShow(true);
        meBean.setIsrightImgShow(false);
        meBean.setLeftImgId(R.drawable.ycrj);
        meBean.setTitle("异常统计");
        lists.add(meBean);

        adapter=new MeAdapter(getActivity(), R.layout.item_me, lists);
        meListView.setAdapter(adapter);
        meListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Toast.makeText(getActivity(),"0",Toast.LENGTH_SHORT).show();
                }else if(position==1){
                    startActivity(new Intent(getActivity(), ActivityDC.class));
//                    Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();
                }else if(position==2){
                    startActivity(new Intent(getActivity(), ActivityPersonalMessage.class));
                }else if(position==3){
                    startActivity(new Intent(getActivity(), ActivtiyPersonalSet.class));
                }else{
                    //startActivity(new Intent(getActivity(), ActivityAbnormal.class));
//                    Toast.makeText(getActivity(),"4",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

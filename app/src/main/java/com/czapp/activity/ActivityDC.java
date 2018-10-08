package com.czapp.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.czapp.R;
import com.czapp.WashingRoomPojo;
import com.czapp.bean.Health;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by admin on 2018/5/3.
 */

public class ActivityDC extends FragmentActivity {

    private String[] sj;
    private double[] xl;
    private double[] xy;
    private double[] tw;
    private LinearLayout llFloorDes, llAreas;
    private RadioGroup rgFloors;
    private PopupWindow popupWindow;
    private TextView tvFloorNum;
    private Button btnUp, btnDown;
    private int totalFloors = 3, currentFloor = 1;
    private TextView tvWRItem1, tvWRItem2, tvWRItem3;
    private ArrayList<WashingRoomPojo> washingroomAreas = new ArrayList<WashingRoomPojo>();

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    List<Health> list= (List<Health>) msg.obj;
                    xl = new double[list.size()];
                    xy = new double[list.size()];
                    tw = new double[list.size()];
                    sj = new String[list.size()];
                    int num = list.size()-1;
                    int i =0;
                    for (Health hb : list) {
                        //获得数据的objectId信息
                        xl[num-i] = hb.getXt();
                        xy[num-i] = hb.getXy();
                        tw[num-i] = hb.getTw();
                        sj[num-i] = hb.getCreatedAt();
                        i++;
//                    Lost.getObjectId();
                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
//                    Lost.getCreatedAt();
                    }
                    for (i = 0; i < tw.length; i++) {
                        WashingRoomPojo wp = new WashingRoomPojo();
                        wp.setWrAreaName(sj[i]);
                        if (tw[i] >= 36 && tw[i] <= 37) {
                            wp.setTtw(String.valueOf(tw[i])).setFtw("-");
                        } else {
                            wp.setFtw(String.valueOf(tw[i])).setTtw("-");
                        }

                        if(xl[i]>=60&&xl[i]<=100){
                            wp.setTxl(String.valueOf(xl[i])).setFxl("-");
                        }else{
                            wp.setFxl(String.valueOf(xl[i])).setTxl("-");
                        }

                        if(xy[i]>=90&&xy[i]<=140){
                            wp.setTxy(String.valueOf(xy[i])).setFxy("-");
                        }else{
                            wp.setFxy(String.valueOf(xy[i])).setTxy("-");
                        }
                        washingroomAreas.add(wp);
                    }
                    showData();
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dc);


        getData();
        setViews();
        initPopwindow();
        setListeners();
    }


    /**
     * 显示数据
     */
    private void showData() {

        for (int i = 0; i < washingroomAreas.size(); i++) {// 动态添加状态
            final WashingRoomPojo pojo = washingroomAreas.get(i);
//            Log.i("测试",pojo.wrAreaName);
            LinearLayout llWashingRoomItem = new LinearLayout(this);
            llWashingRoomItem.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llWashingRoomItem = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.wr_area_item, null);
            TextView tvAreaName = (TextView) llWashingRoomItem
                    .findViewById(R.id.tv_wr_areaname);
            TextView tvManMt = (TextView) llWashingRoomItem
                    .findViewById(R.id.tv_wr_mt_man);
            TextView tvManDc = (TextView) llWashingRoomItem
                    .findViewById(R.id.tv_wr_dc_man);
            TextView tvManXBC = (TextView) llWashingRoomItem
                    .findViewById(R.id.tv_wr_xbc_man);
            TextView tvWomanMt = (TextView) llWashingRoomItem
                    .findViewById(R.id.tv_wr_mt_woman);
            TextView tvWomanDc = (TextView) llWashingRoomItem
                    .findViewById(R.id.tv_wr_dc_woman);
            TextView tvWomanXBC = (TextView) llWashingRoomItem
                    .findViewById(R.id.tv_wr_xbc_woman);

            tvAreaName.setText(pojo.wrAreaName);
            tvManMt.setText("" + pojo.Txl);
            tvManDc.setText("" + pojo.Ttw);
            tvManXBC.setText("" + pojo.Txy);
            tvWomanMt.setText("" + pojo.Fxl);
            tvWomanDc.setText("" + pojo.Ftw);
            tvWomanXBC.setText("" + pojo.Fxy);
            llAreas.addView(llWashingRoomItem);
        }
    }

    /**
     * 获取数据
     */
    private void getData() {

        BmobQuery<Health> query = new BmobQuery<Health>();
        BmobUser user = BmobUser.getCurrentUser(this);
        query.addWhereEqualTo("u", user);
        query.findObjects(this, new FindListener<Health>() {
            @Override
            public void onSuccess(List<Health> object) {
                // TODO Auto-generated method stub
                showToast("查询成功：共" + object.size() + "条数据。");

                Message message = handler.obtainMessage();
                message.what = 0;
                //以消息为载体
                message.obj = object;//这里的list就是查询出list
                //向handler发送消息
                handler.sendMessage(message);

//                xl = new double[object.size()];
//                xy = new double[object.size()];
//                tw = new double[object.size()];
//                sj = new String[object.size()];
//                int i = 0;
//                for (Health hb : object) {
//                    //获得数据的objectId信息
//                    xl[i] = hb.getXt();
//                    xy[i] = hb.getXy();
//                    tw[i] = hb.getTz();
//                    sj[i] = hb.getCreatedAt();
//                    i++;
//                }
//
//                for (i = 0; i < tw.length; i++) {
//                    if (tw[i] >= 36 && tw[i] <= 37) {
//                        washingroomAreas.add(new WashingRoomPojo().setWrAreaName(sj[i]).setTxl("").setTtw(String.valueOf(tw[i])).setTxy("").setFxl("").setFtw("").setFxy(""));
//                    } else {
//                        washingroomAreas.add(new WashingRoomPojo().setWrAreaName(sj[i]).setTxl("").setTtw("").setTxy("").setFxl("").setFtw(String.valueOf(tw[i])).setFxy(""));
//                    }
//                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                showToast("查询失败：" + msg);
            }
        });

//        for(int i = 0;i<tw.length;i++){
//            if(tw[i]>=36&&tw[i]<=37){
//                washingroomAreas.add(new WashingRoomPojo().setWrAreaName(sj[i]).setTxl("").setTtw(String.valueOf(tw[i])).setTxy("").setFxl("").setFtw("").setFxy(""));
//            }else{
//                washingroomAreas.add(new WashingRoomPojo().setWrAreaName(sj[i]).setTxl("").setTtw("").setTxy("").setFxl("").setFtw(String.valueOf(tw[i])).setFxy(""));
//            }
//        }

//    query.findObjects(this, new FindCallback() {
//
//            @Override
//            public void onSuccess(JSONArray jsonArray) {
//                //注意：查询结果是JSONArray
////                        showToast("查询成功：" + jsonArray.length());
//                xl = new double[jsonArray.length()];
//                xy = new double[jsonArray.length()];
//                tw = new double[jsonArray.length()];
//                sj = new String[jsonArray.length()];
////                        Log.i("添加数据：", String.valueOf(jsonArray));
//                try{
//                    for (int i = 0; i < jsonArray.length(); i++) {//循环json数组
//                        JSONObject ob  = (JSONObject) jsonArray.get(i);//得到json对象
//                        tw[i] = ob.getDouble("tw");
//                        xy[i] = ob.getDouble("xy");
//                        xl[i] = ob.getDouble("xt");
//                        sj[i] = ob.getString("updatedAt");
////                        Log.i("体温：", String.valueOf(tw[i]));
//                    }
//                    WashingRoomPojo wp = new WashingRoomPojo();
//
//                    washingroomAreas.add(new WashingRoomPojo().setWrAreaName("2018/6/19").setTxl("").setTtw("").setTxy("").setFxl("").setFtw("").setFxy(""));
//                    for(int i = 0;i<tw.length;i++){
//                        if(tw[i]>=36&&tw[i]<=37){
//                            washingroomAreas.add(new WashingRoomPojo().setWrAreaName(sj[i]).setTxl("").setTtw(String.valueOf(tw[i])).setTxy("").setFxl("").setFtw("").setFxy(""));
//                        }else{
//                            washingroomAreas.add(new WashingRoomPojo().setWrAreaName(sj[i]).setTxl("").setTtw("").setTxy("").setFxl("").setFtw(String.valueOf(tw[i])).setFxy(""));
//                        }
//                    }
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(int i, String s) {
//                showToast("查询失败：" + s);
//            }
//
//        });

//        Log.i("jishu", String.valueOf(washingroomAreas.size()));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("A区")
//                .setWrManDC(8).setWrManMT(6).setWrManXBC(6).setWrWomanDC(8)
//                .setWrWomanMT(6).setWrWomanXBC(6));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("B区")
//                .setWrManDC(8).setWrManMT(5).setWrManXBC(6).setWrWomanDC(9)
//                .setWrWomanMT(6).setWrWomanXBC(6));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("C区")
//                .setWrManDC(8).setWrManMT(6).setWrManXBC(4).setWrWomanDC(8)
//                .setWrWomanMT(6).setWrWomanXBC(3));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("D区")
//                .setWrManDC(8).setWrManMT(6).setWrManXBC(6).setWrWomanDC(8)
//                .setWrWomanMT(6).setWrWomanXBC(6));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("E区")
//                .setWrManDC(8).setWrManMT(5).setWrManXBC(6).setWrWomanDC(9)
//                .setWrWomanMT(6).setWrWomanXBC(6));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("F区")
//                .setWrManDC(8).setWrManMT(6).setWrManXBC(4).setWrWomanDC(8)
//                .setWrWomanMT(6).setWrWomanXBC(3));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("G区")
//                .setWrManDC(8).setWrManMT(6).setWrManXBC(4).setWrWomanDC(8)
//                .setWrWomanMT(6).setWrWomanXBC(3));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("H区")
//                .setWrManDC(8).setWrManMT(6).setWrManXBC(6).setWrWomanDC(8)
//                .setWrWomanMT(6).setWrWomanXBC(6));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("I区")
//                .setWrManDC(8).setWrManMT(5).setWrManXBC(6).setWrWomanDC(9)
//                .setWrWomanMT(6).setWrWomanXBC(6));
//        washingroomAreas.add(new WashingRoomPojo().setWrAreaName("J区")
//                .setWrManDC(8).setWrManMT(6).setWrManXBC(4).setWrWomanDC(8)
//                .setWrWomanMT(6).setWrWomanXBC(3));
    }

    private void setViews() {
        llFloorDes = (LinearLayout) findViewById(R.id.ll_floor);
        tvFloorNum = (TextView) findViewById(R.id.tv_floor_num);
        btnUp = (Button) findViewById(R.id.btn_wr_up);
        btnDown = (Button) findViewById(R.id.btn_wr_down);

        tvWRItem1 = (TextView) findViewById(R.id.tv_wr_item1);
        tvWRItem2 = (TextView) findViewById(R.id.tv_wr_item2);
        tvWRItem3 = (TextView) findViewById(R.id.tv_wr_item3);
        llAreas = (LinearLayout) findViewById(R.id.wr_areas);

        tvFloorNum.setText("心率正常范围60-100");
        initTvItems(tvWRItem1, tvWRItem2, tvWRItem3);

    }

    private void setListeners() {
        llFloorDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrClosePopMenu(view);
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {// 上一层
            @Override
            public void onClick(View v) {
                if (currentFloor < totalFloors) {
//                    RadioButton rb = (RadioButton) group.findViewById(rgFloors
//                            .getCheckedRadioButtonId());
                    rgFloors.check(++currentFloor);
//                    String floor = rb.getText().toString();
//                    tvFloorNum.setText(floor);
                }
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {// 下一层
            @Override
            public void onClick(View arg0) {
                if (currentFloor > 1) {
                    rgFloors.check(--currentFloor);
                }
            }
        });
    }

    /**
     * PopupWindow初始化
     */
    private void initPopwindow() {
        View contentView = getLayoutInflater().inflate(R.layout.floor_pop_menu,
                null, false);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setOutsideTouchable(true); // 设置允许在外点击消失
        popupWindow
                .setBackgroundDrawable(new BitmapDrawable(getResources(), "")); // 点击返回键也能使其消失，不影响背景
        popupWindow.setAnimationStyle(R.style.animation);// 设置PopupWindow弹出和退出时候的动画效果

        rgFloors = (RadioGroup) contentView.findViewById(R.id.rg_floors); // 单选按钮组

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        RadioButton radio = (RadioButton) inflater.inflate(
                R.layout.floor_radiobutton_item, null);
        radio.setText("心率正常范围60-100");
        radio.setId(1);
        rgFloors.addView(radio);
        RadioButton radio1 = (RadioButton) inflater.inflate(
                R.layout.floor_radiobutton_item, null);
        radio1.setText("收缩压正常范围90-140");
        radio1.setId(2);
        rgFloors.addView(radio1);

        RadioButton radio2 = (RadioButton) inflater.inflate(
                R.layout.floor_radiobutton_item, null);
        radio2.setText("体温正常范围36-37");
        radio2.setId(3);
        rgFloors.addView(radio2);

        for (int i = 1; i <= totalFloors; i++) {// 添加单选按钮
            if (i == currentFloor) {
                radio.setChecked(true);
            }
        }
//        for (int i = 1; i <= totalFloors; i++) {// 添加单选按钮
//            RadioButton radio = (RadioButton) inflater.inflate(
//                    R.layout.floor_radiobutton_item, null);
//            radio.setText(" " + i);
//            radio.setId(i);// 让id和楼层相同
//            rgFloors.addView(radio);
//
//            if (i == currentFloor) {
//                radio.setChecked(true);
//            }
//        }

        rgFloors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {// 设置楼层选择监听事件
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                currentFloor = checkedId;// 当前选中楼层
                RadioButton rb = (RadioButton) group.findViewById(rgFloors
                        .getCheckedRadioButtonId());
                String floor = rb.getText().toString();
                tvFloorNum.setText(floor);
            }
        });
    }

    /**
     * 初始化界面分类文字（字体大小不同）
     *
     * @param tv1
     * @param tv2
     * @param tv3
     */
    private void initTvItems(TextView tv1, TextView tv2, TextView tv3) {
        SpannableString itemText1 = new SpannableString("心率(bpm)");
        SpannableString itemText2 = new SpannableString("体温(℃)");
        SpannableString itemText3 = new SpannableString("收缩压(mmHg)");
        itemText1.setSpan(new TextAppearanceSpan(this,
                        R.style.wr_item_txt_front), 0, 2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemText1.setSpan(new TextAppearanceSpan(this,
                        R.style.wr_item_txt_behind), 2, 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemText2.setSpan(new TextAppearanceSpan(this,
                        R.style.wr_item_txt_front), 0, 2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemText2.setSpan(new TextAppearanceSpan(this,
                        R.style.wr_item_txt_behind), 2, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemText3.setSpan(new TextAppearanceSpan(this,
                        R.style.wr_item_txt_front), 0, 3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemText3.setSpan(new TextAppearanceSpan(this,
                        R.style.wr_item_txt_behind), 3, 9,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv1.setText(itemText1, TextView.BufferType.SPANNABLE);
        tv2.setText(itemText2, TextView.BufferType.SPANNABLE);
        tv3.setText(itemText3, TextView.BufferType.SPANNABLE);
    }

    /**
     * 弹出菜单
     *
     * @param view
     */
    public void openOrClosePopMenu(View view) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        } else {
            popupWindow.showAsDropDown(view);
            rgFloors.check(currentFloor);// 每次打开弹窗，重新选中
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

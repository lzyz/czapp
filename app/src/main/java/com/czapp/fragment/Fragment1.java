package com.czapp.fragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.czapp.R;
import com.czapp.bean.Health;

import java.util.Locale;
import java.util.Random;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by admin on 2018/4/30.
 */

public class Fragment1 extends Fragment implements View.OnClickListener{
    private Health healthBean;

    private View view;
    private int a,b,c,d,e;
    private EditText tw;
    private EditText tz;
    private EditText xl;
    private EditText xy;
    private EditText xz;
    private Button jc,bb;
    private TextToSpeech mTextToSpeech=null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_1, container, false);

        healthBean = new Health();
        healthBean.setUser(BmobUser.getCurrentUser(getContext()));

        //实例并初始化TTS对象
        mTextToSpeech=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTextToSpeech.setLanguage(Locale.CHINA);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(getActivity(),"初始化失败!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //事件监听
        tw = (EditText) view.findViewById(R.id.tw);
        tz = (EditText) view.findViewById(R.id.tz);
        xl = (EditText) view.findViewById(R.id.xl);
        xy = (EditText) view.findViewById(R.id.xy);
        xz = (EditText) view.findViewById(R.id.xz);

        jc = (Button) view.findViewById(R.id.jc);
        jc.setOnClickListener(this);

        bb = (Button) view.findViewById(R.id.bb);
        bb.setOnClickListener(this);

        return view;
    }


    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.jc:
                randomSet();
                break;
            case R.id.bb:
                bobao();
                break;
        }
    }

    private void randomSet(){
        Random rand = new Random();
        a = rand.nextInt(5)+37;
        b = rand.nextInt(30)+70;
        c = rand.nextInt(50)+80;
        d = rand.nextInt(70)+110;
        e = rand.nextInt(2)+6;
        String a1 = a+"℃";
        String b1 = b+"kg";
        String c1 = c+"次/分钟";
        String d1 = d+"mmHg";
        String e1 = e+"mmol/L";

        healthBean = new Health();
        healthBean.setTw(Double.valueOf(a));
        healthBean.setTz(Double.valueOf(b));
        healthBean.setXt(Double.valueOf(c));
        healthBean.setXy(Double.valueOf(d));
        healthBean.setXz(Double.valueOf(e));

        BmobUser user = BmobUser.getCurrentUser(this.getContext());
        healthBean.setUser(user);

        healthBean.save(this.getContext(), new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.i("添加数据：","成功");
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("添加数据：","失败");
            }
        });


        tw.setText(a1);
        tz.setText(b1);
        xl.setText(c1);
        xy.setText(d1);
        xz.setText(e1);
    }

    private void bobao(){
        if(a==0){
            mTextToSpeech.speak("请先进行检测!", TextToSpeech.QUEUE_FLUSH, null);
        }else{
            String sp = "体温 "+a+" 摄氏度，"+"体重 "+b+" 公斤，心跳 "+c+" 次每分钟，血压 "+d+" 毫米汞柱，血脂 "+e+" 毫摩尔每升。";
            if(a>=36&&a<=37&&c>=60&&c<=100&&d>=90&&d<=140&&e<=5.2){
                sp += "身体状况良好。";
            }else{
                if(a<36){
                    sp += "体温偏低，";
                }else if(a>37){
                    sp += "体温偏高，";
                }

                if(c<60){
                    sp += "心率偏低，";
                }else if(c>100){
                    sp += "心率偏高，";
                }

                if(d<90){
                    sp += "血压偏低，";
                }else if (d>140){
                    sp += "血压偏低，";
                }

                if(e>5.2){
                    sp += "血脂偏高。";
                }
            }
            mTextToSpeech.speak(sp, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

}

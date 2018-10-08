package com.czapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.czapp.R;


/**
 * Created by admin on 2018/5/3.
 */

public class ActivtiyPersonalSet extends FragmentActivity implements View.OnClickListener{
    private Button btn_set;
    private EditText l_xy;
    private EditText h_xy;
    private EditText l_xl;
    private EditText h_xl;
    private EditText l_xz;
    private EditText h_xz;
    private EditText l_tz;
    private EditText h_tz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_personal_set);

        btn_set = (Button)findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);

        l_xy = (EditText) findViewById(R.id.l_xy);
        h_xy = (EditText) findViewById(R.id.h_xy);

        l_xz = (EditText) findViewById(R.id.l_xz);
        h_xz = (EditText) findViewById(R.id.h_xz);

        l_xl = (EditText) findViewById(R.id.l_xl);
        h_xl = (EditText) findViewById(R.id.h_xl);


        l_tz = (EditText) findViewById(R.id.l_tz);
        h_tz = (EditText) findViewById(R.id.h_tz);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_set:
                int i = 0;
//                if((String.valueOf(l_xy).equals("")&&!String.valueOf(h_xy).equals(""))||(!String.valueOf(l_xy).equals("")&&String.valueOf(h_xy).equals(""))){
//                    i++;
//                    showToast("血压范围请填写完整！");
//                }
//                if((String.valueOf(l_xl)!=""&&(String.valueOf(h_xl)=="")||(l_xl==null&&h_xl!=null))){
//                    i++;
//                    showToast("心率范围请填写完整！");
//                }
//                if((l_tz!=null&&h_tz==null)||(l_tz==null&&h_tz!=null)){
//                    i++;
//                    showToast("体重范围请填写完整！");
//                }
//                if((l_xz!=null&&h_xz==null)||(l_xz==null&&h_xz!=null)){
//                    i++;
//                    showToast("血脂范围请填写完整！");
//                }

//                if(Integer.parseInt(String.valueOf(l_xy))>= Integer.parseInt(String.valueOf(h_xy))){
//                    i++;
//                    showToast("血压范围填写有误！");
//                }
//                if(Integer.parseInt(String.valueOf(l_xl))>= Integer.parseInt(String.valueOf(h_xl))){
//                    i++;
//                    showToast("心率范围填写有误！");
//                }
//                if(Integer.parseInt(String.valueOf(l_tz))>= Integer.parseInt(String.valueOf(h_tz))){
//                    i++;
//                    showToast("体重范围填写有误！");
//                }
//                if(Integer.parseInt(String.valueOf(l_xz))>= Integer.parseInt(String.valueOf(h_xz))){
//                    i++;
//                    showToast("血脂范围填写有误！");
//                }

                if(i==0) {
                    showToast("设置成功");
                }
                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}

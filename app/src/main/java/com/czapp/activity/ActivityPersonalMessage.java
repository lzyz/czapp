package com.czapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.czapp.R;

/**
 * Created by admin on 2018/6/20.
 */

public class ActivityPersonalMessage extends FragmentActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_message);
    }

    @Override
    public void onClick(View v) {

    }
}

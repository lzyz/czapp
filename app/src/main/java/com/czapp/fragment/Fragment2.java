package com.czapp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.czapp.R;
import com.czapp.bean.EchartsDataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindCallback;


/**
 * Created by admin on 2018/4/30.
 */

public class Fragment2 extends Fragment implements View.OnClickListener{
    private  View view;
    private Button bt;
    private Button lc_tw;
    private Button lc_xl;
    private Button lc_xy;
    private Button lc_xz;
    private Button lc_tz;
    private String[] sj;
    private double[] xl;
    private double[] xy;
    private double[] tw;
    private double[] xz;
    private double[] tz;
    private ProgressDialog dialog;
    private WebView chartshowWb;
    private ImageView ivLeft;
    private ImageView ivRight;
    String TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_2, container, false);
//        bt = (Button)view.findViewById(R.id.create_database);
//        bt.setOnClickListener(this);

        lc_tw = (Button)view.findViewById(R.id.lc_tw);
        lc_tw.setOnClickListener(this);

        lc_xl = (Button)view.findViewById(R.id.lc_xl);
        lc_xl.setOnClickListener(this);

        lc_xy = (Button)view.findViewById(R.id.lc_xy);
        lc_xy.setOnClickListener(this);

        lc_xz = (Button)view.findViewById(R.id.lc_xz);
        lc_xz.setOnClickListener(this);

        lc_tz = (Button)view.findViewById(R.id.lc_tz);
        lc_tz.setOnClickListener(this);

//        initData();

        ivLeft = (ImageView) view.findViewById(R.id.iv_left);
        ivLeft.setOnClickListener(this);

        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        ivRight.setOnClickListener(this);

        chartshowWb = (WebView) view.findViewById(R.id.chartshow_wb);
        initWebview();
        return view;
    }

    @Override @SuppressLint("SetJavaScriptEnabled")
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //进行webwiev的一堆设置
        chartshowWb.getSettings().setAllowFileAccess(true);
        chartshowWb.getSettings().setJavaScriptEnabled(true);
        chartshowWb.loadUrl("file:///android_asset/echart/myechart.html");
        chartshowWb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

//                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //最好在这里调用js代码 以免网页未加载完成
//                chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson() + ");");
//                view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
            }
        });
		initWebview();
    }

	public void initWebview(){
			BmobQuery query = new BmobQuery("Health");
			BmobUser user = BmobUser.getCurrentUser(this.getContext());
			query.addWhereEqualTo("u",user);
                query.addQueryKeys("xt");

                query.findObjects(this.getContext(), new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //注意：查询结果是JSONArray
//                        showToast("查询成功：" + jsonArray.length());
                        xl = new double[jsonArray.length()];
                        sj = new String[jsonArray.length()];
//                        Log.i("添加数据：", String.valueOf(jsonArray));

                        try{
                            for (int i = 0; i < jsonArray.length(); i++) {//循环json数组
                                JSONObject ob  = (JSONObject) jsonArray.get(i);//得到json对象
                                xl[i] = ob.getDouble("xt");
                                sj[i] = ob.getString("updatedAt");
//                                Log.i("时间：", String.valueOf(sj[i]));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson(xl,sj) + ");");
                        view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showToast("查询失败：" + s);
                    }
                });
		
	}
    public void onClick(View v)
    {
//        LitePal.getDatabase();
        BmobQuery query = new BmobQuery("Health");
        BmobUser user = BmobUser.getCurrentUser(this.getContext());
        switch (v.getId())
        {
            case R.id.lc_tw:
//                chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson() + ");");
//                v.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                query.addWhereEqualTo("u",user);
                query.addQueryKeys("tw");

                query.findObjects(this.getContext(), new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //注意：查询结果是JSONArray
//                        showToast("查询成功：" + jsonArray.length());
                        tw = new double[jsonArray.length()];
                        sj = new String[jsonArray.length()];
//                        Log.i("添加数据：", String.valueOf(jsonArray));

                        try{
                            for (int i = 0; i < jsonArray.length(); i++) {//循环json数组
                                JSONObject ob  = (JSONObject) jsonArray.get(i);//得到json对象
                                tw[i] = ob.getDouble("tw");
                                sj[i] = ob.getString("updatedAt");
//                                Log.i("体温：", String.valueOf(tw[i]));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        chartshowWb.loadUrl("javascript:createChart('line3'," + EchartsDataBean.getInstance().getEchartsLineTWJson(tw,sj) + ");");
                        view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showToast("查询失败：" + s);
                    }
                });
                break;
            case R.id.lc_tz:
//                chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson() + ");");
//                v.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                query.addWhereEqualTo("u",user);
                query.addQueryKeys("tz");

                query.findObjects(this.getContext(), new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //注意：查询结果是JSONArray
//                        showToast("查询成功：" + jsonArray.length());
                        tz = new double[jsonArray.length()];
                        sj = new String[jsonArray.length()];
//                        Log.i("添加数据：", String.valueOf(jsonArray));

                        try{
                            for (int i = 0; i < jsonArray.length(); i++) {//循环json数组
                                JSONObject ob  = (JSONObject) jsonArray.get(i);//得到json对象
                                tz[i] = ob.getDouble("tz");
                                sj[i] = ob.getString("updatedAt");
//                                Log.i("体温：", String.valueOf(tz[i]));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        chartshowWb.loadUrl("javascript:createChart('line4'," + EchartsDataBean.getInstance().getEchartsLineTZJson(tz,sj) + ");");
                        view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showToast("查询失败：" + s);
                    }
                });
                break;
            case R.id.lc_xl:

//                double[] xl;
//
//                BmobQuery<HealthBean> query = new BmobQuery<HealthBean>();
                query.addWhereEqualTo("u",user);
                query.addQueryKeys("xt");

                query.findObjects(this.getContext(), new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //注意：查询结果是JSONArray
//                        showToast("查询成功：" + jsonArray.length());
                        xl = new double[jsonArray.length()];
                        sj = new String[jsonArray.length()];
//                        Log.i("添加数据：", String.valueOf(jsonArray));

                        try{
                            for (int i = 0; i < jsonArray.length(); i++) {//循环json数组
                                JSONObject ob  = (JSONObject) jsonArray.get(i);//得到json对象
                                xl[i] = ob.getDouble("xt");
                                sj[i] = ob.getString("updatedAt");
//                                Log.i("时间：", String.valueOf(sj[i]));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson(xl,sj) + ");");
                        view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showToast("查询失败：" + s);
                    }
                });
//                chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson(xl) + ");");
//                v.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                break;
            case R.id.lc_xy:
//                chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson() + ");");
//                v.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                query.addWhereEqualTo("u",user);
                query.addQueryKeys("xy");

                query.findObjects(this.getContext(), new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //注意：查询结果是JSONArray
//                        showToast("查询成功：" + jsonArray.length());
                        xy = new double[jsonArray.length()];
                        sj = new String[jsonArray.length()];
//                        Log.i("添加数据：", String.valueOf(jsonArray));

                        try{
                            for (int i = 0; i < jsonArray.length(); i++) {//循环json数组
                                JSONObject ob  = (JSONObject) jsonArray.get(i);//得到json对象
                                xy[i] = ob.getDouble("xy");
                                sj[i] = ob.getString("updatedAt");
//                                Log.i("体温：", String.valueOf(tw[i]));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        chartshowWb.loadUrl("javascript:createChart('line1'," + EchartsDataBean.getInstance().getEchartsLineXYJson(xy,sj) + ");");
                        view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showToast("查询失败：" + s);
                    }
                });
                break;
            case R.id.lc_xz:
//                chartshowWb.loadUrl("javascript:createChart('line'," + EchartsDataBean.getInstance().getEchartsLineJson() + ");");
//                v.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                query.addWhereEqualTo("u",user);
                query.addQueryKeys("xz");

                query.findObjects(this.getContext(), new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //注意：查询结果是JSONArray
//                        showToast("查询成功：" + jsonArray.length());
                        xz = new double[jsonArray.length()];
                        sj = new String[jsonArray.length()];
//                        Log.i("添加数据：", String.valueOf(jsonArray));

                        try{
                            for (int i = 0; i < jsonArray.length(); i++) {//循环json数组
                                JSONObject ob  = (JSONObject) jsonArray.get(i);//得到json对象
                                xz[i] = ob.getDouble("xz");
                                sj[i] = ob.getString("updatedAt");
//                                Log.i("体温：", String.valueOf(tw[i]));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        chartshowWb.loadUrl("javascript:createChart('line2'," + EchartsDataBean.getInstance().getEchartsLineXZJson(xz,sj) + ");");
                        view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showToast("查询失败：" + s);
                    }
                });
                break;
            case R.id.iv_left:
                dealwithLeft();
                break;
            case R.id.iv_right:
                dealwithRight();
                break;
        }
    }

    private void initData() {
//        dialog = new ProgressDialog(getActivity());
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setMessage("玩儿命加载中...");
//
//        TAG = this.getClass().getName();
    }

    /**
     * start 和 end 意为拖放的起始点 范围均为  0-100;
     * js中设置的默认初始值 , 和 activity中设置的默认值   两者必须一致, 不然会有错乱
     */
    int start = 20, end = 85;

    private void dealwithLeft() {
        if (start >= 5) {
            start -= 5;
            if (end <= 105 && end >= start + 15) {
                end -= 5;
            }
            if (!ivRight.isEnabled()) {
                ivRight.setEnabled(true);
                ivRight.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.right_blue_bg));
            }
            chartshowWb.loadUrl("javascript:updateZoomState(" + start + "," + end + ");");
        } else {
            if (ivLeft.isEnabled()) {
                ivLeft.setEnabled(false);
                ivLeft.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.left_gray_bg));
            }
            Log.i(TAG, "start == " + start + "  end== " + end);
        }
    }

    private void dealwithRight() {
        if (end <= 100) {
            end += 5;
            if (start < end - 15) {
                start += 5;
            }
            if (!ivLeft.isEnabled()) {
                ivLeft.setEnabled(true);
                ivLeft.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.left_blue_bg));
            }
            chartshowWb.loadUrl("javascript:updateZoomState(" + start + "," + end + ");");
        } else {
            if (ivRight.isEnabled()) {
                ivRight.setEnabled(false);
                ivRight.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.right_gray_bg));
            }
            Log.i(TAG, "start == " + start + "  end== " + end);
        }
    }

    private void showToast(String msg){
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

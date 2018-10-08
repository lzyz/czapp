package com.czapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.czapp.R;

/**
 * Created by admin on 2018/5/4.
 */

public class ActivityHealthGuide extends FragmentActivity {

    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_guide);

        web = (WebView)findViewById(R.id.wv_hg);
        web.loadUrl("https://m.baidu.com/from=1001703a/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40320_1001%2Cta%40iphone_2_7.1_18_4.5/baiduid=F01E826D5743E88B6D0F62F2C59F8C2D/w=0_10_/t=iphone/l=3/tc?ref=www_iphone&lid=8314536522301924552&order=1&fm=alop&tj=www_normal_1_0_10_title&vit=osres&m=8&srd=1&cltj=cloud_title&asres=1&title=%E6%B1%BD%E8%BD%A6%E5%AE%89%E5%85%A8%E9%A9%BE%E9%A9%B6%E6%8A%80%E5%B7%A7%2C%E5%AE%89%E5%85%A8%E9%A9%BE%E9%A9%B6%E7%9F%A5%E8%AF%86%2C%E9%A9%BE%E9%A9%B6%E5%91%98%E5%AE%89%E5%85%A8%E7%9F%A5%E8%AF%86...&dict=32&wd=&eqid=73632aea2bd16400100000015b2b53b0&w_qd=IlPT2AEptyoA_yk59eccvei6BllOa8O&tcplug=1&sec=30679&di=0be5620ca42d84e9&bdenc=1&tch=124.411.279.410.2.331&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IHBuURiZRLGfc6ZXfxP4kHREsRDngMWbTUS3&clk_info=%7B%22srcid%22%3A1599%2C%22tplname%22%3A%22www_normal%22%2C%22t%22%3A1529566134556%2C%22xpath%22%3A%22div-a-h3%22%7D&sfOpen=1");

        web.getSettings().setJavaScriptEnabled(true);  //加上这一行网页为响应式的

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if(url == null) return false;
                try {
                    if (url.startsWith("http:") || url.startsWith("https:"))
                    {
                        view.loadUrl(url);
                        return true;
                    }
                    else
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }
            }
        });
    }
}

package com.oterman.njubbs.activity.expore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.oterman.njubbs.R;
import com.oterman.njubbs.activity.MyActionBarActivity;
import com.oterman.njubbs.utils.MyToast;
import com.umeng.analytics.MobclickAgent;

public class ColleageContentActivity extends MyActionBarActivity implements OnKeyListener, OnClickListener{

	
	private WebView webview;
//	private ProgressDialog dialog; 
	private ProgressBar pb;
	private ImageView ivBack;
	private ImageView ivForward;
	private ImageView ivRefresh;
	private ImageView ivBrowser;
	private String url; 

	@Override
	protected String getBarTitle() {
		String name=getIntent().getStringExtra("name");
		return name;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		webview.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		webview.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		webview.destroy();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//showDialog(0);
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_webview2);
		
		url = getIntent().getStringExtra("url");
		
		webview = (WebView) this.findViewById(R.id.webview);
		pb = (ProgressBar) this.findViewById(R.id.progress);
		
		ivBack = (ImageView) this.findViewById(R.id.browser_back);
		ivForward = (ImageView) this.findViewById(R.id.browser_forward);
		ivRefresh = (ImageView) this.findViewById(R.id.browser_refresh);
		ivBrowser = (ImageView) this.findViewById(R.id.browser_system_browser);
		
		ivBack.setOnClickListener(this);
		ivForward.setOnClickListener(this);
		ivRefresh.setOnClickListener(this);
		ivBrowser.setOnClickListener(this);
		
		pb.setVisibility(View.VISIBLE);
		WebSettings settings = webview.getSettings();
		
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		
		settings.setDisplayZoomControls(false);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(true);
		
		webview.loadUrl(url);
		
		webview.setOnKeyListener(this);
		
		webview.setWebViewClient(new MyWebViewClient());
		
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		if(event.getAction()==KeyEvent.ACTION_DOWN){
			if(keyCode==KeyEvent.KEYCODE_BACK&&webview.canGoBack()){
				webview.goBack();
				return true;
			}
		}
		return false;
	}
    
	class MyWebViewClient extends WebViewClient{
        @Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            view.loadUrl(url);  
            return true;  
        }  
        @Override  
        public void onPageFinished(WebView view, String url) {  
        	//  dialog.dismiss();  
        	 pb.setVisibility(View.INVISIBLE);
        }  
        @Override  
        public void onReceivedError(WebView view, int errorCode,  
                String description, String failingUrl) {  
            super.onReceivedError(view, errorCode, description, failingUrl);  
//            dialog.dismiss();  
            pb.setVisibility(View.INVISIBLE);
        } 
	}

@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.browser_forward:
		webview.goForward();
		break;
	case R.id.browser_back:
		webview.goBack();
		break;
	case R.id.browser_refresh:
		webview.loadUrl(webview.getUrl());
		break;
	case R.id.browser_system_browser:
        try {
            // 启用外部浏览器
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            this.startActivity(it);
        } catch (Exception e) {
           MyToast.toast("出错了");
        }
		break;

	default:
		break;
	}
}
}

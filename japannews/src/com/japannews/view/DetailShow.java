package com.japannews.view;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.japannews.entity.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

public class DetailShow extends Activity {

	private final RssInfo detail = new RssInfo();
	private WebView wbShow;
	private ProgressDialog pdiDialog;
	
	private AdView adView;
	private Button btnHome;
	/**
	 * Ìí¼Ógoogle¹ã¸æ
	 * */
	private void AddGoogleAd(){
		adView = new AdView(DetailShow.this, AdSize.BANNER,
				getResources().getString(R.string.app_key));
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout02);
		layout.addView(adView);
		// getListView().addHeaderView(layout);
		adView.loadAd(new AdRequest());
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		CheckNetWork();
	}



	private void CheckNetWork(){
		if(!you.lib.common.NetWorkTool.isNetworkAvailable(this)){
			Intent intent = new Intent(DetailShow.this,MainActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_show);
		wbShow = (WebView)findViewById(R.id.wbShow);
		Intent pIntent = getIntent();
		if (pIntent != null) {
			Bundle pBundle = pIntent.getExtras();
			detail.setTitle(pBundle.getString("title"));
			detail.setDescription(pBundle.getString("description"));
			detail.setLink(pBundle.getString("link"));
			detail.setCategory(pBundle.getString("category"));
			detail.setPubDate(pBundle.getString("pubdate"));
			
			pdiDialog = new ProgressDialog(this);
			pdiDialog.setCancelable(true);
			pdiDialog.setMessage(getResources().getString(R.string.dialog_waiting));
			pdiDialog.show();
			wbShow.getSettings().setJavaScriptEnabled(true);
			
			//wbShow.getSettings().setCacheMode(wbShow.getSettings().LOAD_CACHE_ELSE_NETWORK);
			wbShow.setWebChromeClient(new WebChromeClient(){

				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					// TODO Auto-generated method stub
					if (newProgress == 100) {
						pdiDialog.dismiss();
					}
					super.onProgressChanged(view, newProgress);
				}
				
			});
			wbShow.setWebViewClient(new WebViewClient(){
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					// TODO Auto-generated method stub
					pdiDialog.dismiss();
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					view.loadUrl(url);
					return true;
				}
				
			});
			wbShow.loadUrl(detail.getLink());
			AddGoogleAd();
		}
		
		btnHome = (Button)findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DetailShow.this,MainActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(DetailShow.this,MainActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}




	private class AsyncProcess extends AsyncTask<String, Void, Void>{

//		private Context context;
//		public AsyncProcess(Context context){
//			this.context = context;
//		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			wbShow.loadUrl(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			pdiDialog.dismiss();
		}
		
		
		
	}
}

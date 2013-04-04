package com.japannews.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class WelCome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		Handler x = new Handler();
		x.postDelayed(new WelComeHandler(), 2000);
	}
	
	private class WelComeHandler implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			startActivity(new Intent(getApplication(),MainActivity.class));
			WelCome.this.finish();
		}
		
	}

}

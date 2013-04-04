package com.japannews.menu;

import java.util.ArrayList;

import com.japannews.entity.SlideMenuInfo;
import com.japannews.view.MainActivity;
import com.japannews.view.R;
import com.japannews.view.ShowActivityList;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideMenuLayout {
	// 包含菜单的ArrayList
	private ArrayList<TextView> menuList = null;
	
	private Activity activity;
	private LocalActivityManager activityManager;
	private TextView textView = null;
	private SlideMenuInfo menuUtil = null;
	
	public SlideMenuLayout(Activity activity){
		this.activity = activity;
		menuList = new ArrayList<TextView>();
		menuUtil = new SlideMenuInfo();
	}
	
	public void SetLocalActivityManager(LocalActivityManager activityManager){
		this.activityManager=activityManager;
	}
	
	/**
	 * 顶部滑动菜单布局
	 * @param menuTextViews
	 * @param layoutWidth
	 */
	public View getSlideMenuLinerLayout(String[] menuTextViews,int layoutWidth){
		// 包含TextView的LinearLayout
		LinearLayout menuLinerLayout = new LinearLayout(activity);
		menuLinerLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		// 参数设置
		LinearLayout.LayoutParams menuLinerLayoutParames = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT,
				1);
		menuLinerLayoutParames.gravity = Gravity.CENTER_HORIZONTAL;
		
		// 添加TextView控件
		for(int i = 0;i < menuTextViews.length; i++){
			TextView tvMenu = new TextView(activity);
			// 设置标识值
			tvMenu.setTag(menuTextViews[i]);
			tvMenu.setLayoutParams(new LayoutParams(layoutWidth / 4,30));
			tvMenu.setPadding(10, 14, 10, 10);
			tvMenu.setText(menuTextViews[i]);
			tvMenu.setTextColor(Color.WHITE);
			tvMenu.setGravity(Gravity.CENTER_HORIZONTAL);
			tvMenu.setOnClickListener(SlideMenuOnClickListener);
			
			// 菜单项计数
			menuUtil.count ++;

			// 设置第一个菜单项背景
			if(menuUtil.count == 1){
				tvMenu.setBackgroundResource(R.drawable.menu_bg);
			}
			
			menuLinerLayout.addView(tvMenu,menuLinerLayoutParames);
			menuList.add(tvMenu);
		}

		return menuLinerLayout;
	}
	
	// 单个菜单事件
	OnClickListener SlideMenuOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String menuTag = v.getTag().toString();
			
			if(v.isClickable()){
				textView = (TextView)v;
				textView.setBackgroundResource(R.drawable.menu_bg);
				
				for(int i = 0;i < menuList.size();i++){
					if(!menuTag.equals(menuList.get(i).getText())){
						menuList.get(i).setBackgroundDrawable(null);
					}
				}
				
		        // 点击菜单时改变内容
				slideMenuOnChange(menuTag);
			}
		}
	};
	
	// 点击时改内容
	private void slideMenuOnChange(String menuTag){
		ViewGroup llc = (ViewGroup)activity.findViewById(R.id.linearLayoutContent);
		llc.removeAllViews();
		Intent intent = new Intent(activity, ShowActivityList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		int type=0;
		
		if (menuTag.equals(activity.getResources().getString(R.string.item_top))) {
			type=1;
		}else if (menuTag.equals(activity.getResources().getString(R.string.item_business))) {
			type=2;
		}else if (menuTag.equals(activity.getResources().getString(R.string.item_world))) {
			type=3;
		}else if (menuTag.equals(activity.getResources().getString(R.string.item_entertainment))) {
			type=4;
		}else if (menuTag.equals(activity.getResources().getString(R.string.item_technology))) {
			type=5;
		}else if (menuTag.equals(activity.getResources().getString(R.string.item_sports))) {
			type=6;
		}else if (menuTag.equals(activity.getResources().getString(R.string.item_titbits))) {
			type=7;
		}
		intent.putExtra("type", type);
		Window subActivity = activityManager.startActivity("subActivity", intent);
		llc.addView(subActivity.getDecorView());

		/*if(menuTag.equals(SlideMenuInfo.ITEM_MOBILE)){
			llc.addView(inflater.inflate(R.layout.item_mobile, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_WEB)){
			llc.addView(inflater.inflate(R.layout.item_web, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_CLOUD)){
			llc.addView(inflater.inflate(R.layout.item_cloud, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_DATABASE)){
			llc.addView(inflater.inflate(R.layout.item_database, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_EMBED)){
			llc.addView(inflater.inflate(R.layout.item_embed, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_SERVER)){
			llc.addView(inflater.inflate(R.layout.item_server, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_DOTNET)){
			llc.addView(inflater.inflate(R.layout.item_dotnet, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_JAVA)){
			llc.addView(inflater.inflate(R.layout.item_java, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_SAFE)){
			llc.addView(inflater.inflate(R.layout.item_safe, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_DOMAIN)){
			llc.addView(inflater.inflate(R.layout.item_domain, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_RESEASRCH)){
			llc.addView(inflater.inflate(R.layout.item_research, null));
		}else if(menuTag.equals(SlideMenuUtil.ITEM_MANAGE)){
			llc.addView(inflater.inflate(R.layout.item_manage, null));
		}*/
	}
}
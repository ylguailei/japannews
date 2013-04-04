package com.japannews.view;

import java.util.ArrayList;

import you.lib.common.AlertHelper;

import com.japannews.entity.ViewHolder;
import com.japannews.menu.MyHorizontalScrollView;
import com.japannews.menu.MyHorizontalScrollView.SizeCallback;
import com.japannews.menu.SlideMenuLayout;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ActivityGroup {
	
	/*
	 * 日志TAG
	 * */
	private final String TAG = "JapanNews.MainActivity";
	MyHorizontalScrollView scrollView;
	// 当前ViewPager索引
	private int pagerIndex = 0; 
	private ArrayList<View> menuViews = null;
	/* 
	 * 主界面的ViewGroup
	 * */
	private ViewGroup main = null;
	private ViewPager viewPager = null;
	// 左右导航图片按钮
	private ImageView imagePrevious = null;
	private ImageView imageNext = null;
	private Window subActivity =  null;
	private SlideMenuLayout menu=null;
	
	private ImageView btnSlide;
	private View app;
	/*
	 * 左侧Item菜单列表
	 * */
	private View left_item_menu;
	private myListViewAdapter leftAda;
	private ListView leftLv;
	LayoutInflater inflater;
	
	private final String[] left_List_Items={
			"123",
			"123",
			"123",
			"123",
			"123",
			"123",
			"123",
			"123",
			"123"
	};
	
//	private Handler checkUpdateHandler = new Handler();
//	private UpdateHelper updateHelper;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// ===============此处为自动检查更新部分==================
//		AsyncUpdateProgram auto = new AsyncUpdateProgram(this);
//		auto.execute(getResources().getString(R.string.apk_Server),
//				getResources().getString(R.string.apk_Name),
//				getResources().getString(R.string.apk_verjson),
//				getResources().getString(R.string.apk_SaveName));
//		checkUpdateHandler.post(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				// TODO Auto-generated method stub
//			    updateHelper =  new UpdateHelper(getApplicationContext().getString(R.string.apk_Server),
//			           getApplicationContext().getString(R.string.apk_Name),
//			           getApplicationContext().getString(R.string.apk_verjson),
//			           getApplicationContext().getString(R.string.apk_SaveName));
//				if (updateHelper.CheckNewVersion(MainActivity.this, R.string.pagename))
//				{
//				    updateHelper.ExcuteUpdateVersion(MainActivity.this, R.string.pagename);
//				}
//			}
//		});
		// ======================================================
		//取得窗口属性
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        
        inflater = getLayoutInflater();  
        menuViews = new ArrayList<View>(); 
        menu = new SlideMenuLayout(this);
        
        /*
         * 顶部可滑动菜单
         * */
        String[][] menus = {
   			 {getResources().getString(R.string.item_top),
   			  getResources().getString(R.string.item_business)},
   			 {getResources().getString(R.string.item_world),
   			  getResources().getString(R.string.item_entertainment)},
   			 {getResources().getString(R.string.item_technology),
   			  getResources().getString(R.string.item_sports)},
   			 {getResources().getString(R.string.item_titbits)}
   	    };
        for(int i = 0;i < menus.length;i++){
        	menuViews.add(menu.getSlideMenuLinerLayout(menus[i],screenWidth));
        }
        
        setContentView(inflater.inflate(R.layout.left_menu_item, null));
        
        
        scrollView = (MyHorizontalScrollView) findViewById(R.id.myScrollView);
        
        
        /*设置list*/
        app = inflater.inflate(R.layout.main, null);
        left_item_menu = findViewById(R.id.menu);
        //left_item_menu.setFocusable(true);
        
        leftAda = new myListViewAdapter(this);
        leftLv = (ListView)left_item_menu.findViewById(R.id.lv_Left_Type);
        leftLv.setAdapter(leftAda);
        
        
        btnSlide = (ImageView)app.findViewById(R.id.ivPreviousButton);
        
        // 左右导航图片按钮
        imagePrevious = (ImageView)app.findViewById(R.id.ivPreviousButton);
        imageNext = (ImageView) app.findViewById(R.id.ivNextButton);
        imagePrevious.setOnClickListener(new ImagePreviousOnclickListener());
        imageNext.setOnClickListener(new ImageNextOnclickListener());
        
        if(menuViews.size() > 1){
        	imageNext.setVisibility(View.VISIBLE);
        }
        viewPager = (ViewPager)app.findViewById(R.id.slideMenu);  
        viewPager.setAdapter(new SlideMenuAdapter());  
        viewPager.setOnPageChangeListener(new SlideMenuChangeListener());  
        

		// Create a transparent view that pushes the other views in the HSV to the right.
        // This transparent view allows the menu to be shown when the HSV is scrolled.
        View transparent = new TextView(this);
        //在这里,将左边的菜单栏设置成为了透明的
        transparent.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        final View[] children = new View[] { transparent, app };
     // Scroll to app (view[1]) when layout finished.
        int scrollToViewIdx = 1;
        scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu());
	}
	
	/**
     * Helper that remembers the width of the 'slide' button, so that the 'slide' button remains in view, even when the menu is
     * showing.
     */
    class SizeCallbackForMenu implements SizeCallback {
        int btnWidth;
        //View btnSlide;

//        public SizeCallbackForMenu(View btnSlide) {
//            super();
//            this.btnSlide = btnSlide;
//        }
        public SizeCallbackForMenu() {
            super();
        }

        @Override
        public void onGlobalLayout() {
            btnWidth = 50;
            System.out.println("btnWidth=" + btnWidth);
        }

        @Override
        public void getViewSize(int idx, int w, int h, int[] dims) {
            dims[0] = w;
            dims[1] = h;
            final int menuIdx = 0;
            if (idx == menuIdx) {
                dims[0] = w - btnWidth;
            }
        }
    }
    
    
    /**
     * Menu must NOT be out/shown to start with.
     */
    boolean menuOut = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			Context context = left_item_menu.getContext();
	         int menuWidth =left_item_menu.getMeasuredWidth();
	         if (!menuOut) {
	             // Scroll to 0 to reveal menu
	             int left = 0;
	             scrollView.smoothScrollTo(left, 0);
	             left_item_menu.setVisibility(View.VISIBLE);
	         } else {
	             // Scroll to menuWidth so menu isn't on screen.
	             int left = menuWidth;
	             scrollView.smoothScrollTo(left, 0);
	             left_item_menu.setVisibility(View.INVISIBLE);
	             
	             /* 
	              * 这里他娘的非要让app这个View获取焦点,不然他妈个比再次按menu的时候不会响应keyDown事件,因为焦点丢了
	              * */
	             /********************************************/
	             app.setFocusable(true);
	             app.setFocusableInTouchMode(true);
	             app.requestFocus();
	             /********************************************/
	         }
	         menuOut = !menuOut;
			break;

		default:
			Log.v(TAG,String.valueOf(keyCode));
			break;
		}
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertHelper.AlertDialog_YesorNo(this, 
				"",
				getResources().getString(R.string.exit_content), 
				getResources().getString(R.string.exit_ok), 
				getResources().getString(R.string.exit_cancel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						System.exit(0);
					}
				}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BindListView();
	}

	/*
	 * 绑定列表
	 */
	private void BindListView(){
		if(!you.lib.common.NetWorkTool.isNetworkAvailable(this)){
        	you.lib.common.AlertHelper.AlertDialog_YesorNo(this, this.getResources().getString(R.string.NoNetWork_Tips_Title),
        			this.getResources().getString(R.string.NoNetWork_Tips_Content), 
        			this.getResources().getString(R.string.NoNetWork_Tips_Setting), 
        			this.getResources().getString(R.string.NoNetWork_Tips_Out), settingListener,cancelListener);
        }else {
        	// 加载移动菜单下内容
            ViewGroup llc = (ViewGroup)findViewById(R.id.linearLayoutContent);
            
    		Intent intent = new Intent(MainActivity.this, ShowActivityList.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.putExtra("type", 1);
    		
    		LocalActivityManager localActivityManager =getLocalActivityManager(); 
    		subActivity = localActivityManager.startActivity("subActivity", intent);
    		menu.SetLocalActivityManager(localActivityManager);
    		
    		llc.addView(subActivity.getDecorView());
		}
	}
	
	/*
	 * 设置网络
	 */
	private DialogInterface.OnClickListener settingListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Intent intent=null;
            //判断手机系统的版本  即API大于10 就是3.0或以上版本 
            if(android.os.Build.VERSION.SDK_INT>10){
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            }else{
                intent = new Intent();
                ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            getApplicationContext().startActivity(intent);
		}
	};
	
	/*
	 * 终止应用
	 */
	private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
	};

	
	// 滑动菜单数据适配器
    class SlideMenuAdapter extends PagerAdapter {  
  	  
        @Override  
        public int getCount() {
            return menuViews.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public int getItemPosition(Object object) {  
            // TODO Auto-generated method stub  
            return super.getItemPosition(object);  
        }  
  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(menuViews.get(arg1));  
        }  
  
        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
            // TODO Auto-generated method stub  
        	((ViewPager) arg0).addView(menuViews.get(arg1));
            
            return menuViews.get(arg1);  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
    } 
    
    // 滑动菜单更改事件监听器
    class SlideMenuChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
        	
        }  
  
        @Override  
        public void onPageSelected(int arg0) {
        	int pageCount = menuViews.size() - 1;
        	pagerIndex = arg0;
        	
        	// 显示右边导航图片
        	if(arg0 >= 0 && arg0 < pageCount){
        		imageNext.setVisibility(View.VISIBLE);
        	}else{
        		imageNext.setVisibility(View.INVISIBLE);
        	}
        	
        	// 显示左边导航图片
        	if(arg0 > 0 && arg0 <= pageCount){
        		imagePrevious.setVisibility(View.VISIBLE);
        	}else{
        		imagePrevious.setVisibility(View.INVISIBLE);
        	}
        }  
    }  
    
    // 右导航图片按钮事件
    class ImageNextOnclickListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		pagerIndex ++;
    		viewPager.setCurrentItem(pagerIndex);
    	}
    }
    
    // 左导航图片按钮事件
    class ImagePreviousOnclickListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		pagerIndex --;
    		viewPager.setCurrentItem(pagerIndex);
    	}
    }

    
    /*
	 * 列表中自定义的适配器
	 */
	public class myListViewAdapter extends BaseAdapter{
		
		private LayoutInflater mInflater;
		public myListViewAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return left_List_Items.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder hoder;
			if (convertView == null) {
				hoder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_detail_left,null);
				hoder.text = (TextView)convertView.findViewById(R.id.left_item_text);
				convertView.setTag(hoder);
			}else {
				hoder = (ViewHolder)convertView.getTag();
			}
			hoder.text.setText(left_List_Items[position]);
			hoder.text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Intent detailShow = new Intent(mInflater.getContext(),DetailShow.class);
//					Bundle bund = new Bundle();
//					bund.putString("title", totalList.get(position).getTitle());
//					bund.putString("description", totalList.get(position).getDescription());
//					bund.putString("link", totalList.get(position).getLink());
//					bund.putString("category", totalList.get(position).getCategory());
//					bund.putString("pubdate", totalList.get(position).getPubDate());
//					detailShow.putExtras(bund);
//					startActivity(detailShow);
				}
			});
			//Toast.makeText(mInflater.getContext(), myList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
			return convertView;
		}
		
	}
}

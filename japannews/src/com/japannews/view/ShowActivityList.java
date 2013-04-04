package com.japannews.view;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.japannews.component.RssHandler;
import com.japannews.component.RssManager;
import com.japannews.entity.RssInfo;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.japannews.entity.*;

public class ShowActivityList extends Activity {
	
	private ListView lView=null;
	private List<RssInfo> totalList =new ArrayList<RssInfo>(){
		public String ToString(String curStr){
			if (curStr.length() > 42)
			{
				return curStr.substring(0, 42) + "...";
			}
			return curStr;
		}
	};
	private int urlIndex=0;
	private int listCount=0;
	private String[] url0=null;
	private myListViewAdapter adapter=null;
	
	private AdView adView;
	/**
	 * 添加google广告
	 * */
	private void AddGoogleAd(){
		adView = new AdView(ShowActivityList.this, AdSize.BANNER,
				getResources().getString(R.string.app_key));
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout01);
		layout.addView(adView);
		adView.setGravity(Gravity.CENTER_HORIZONTAL);
		// getListView().addHeaderView(layout);
		adView.loadAd(new AdRequest());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);
		
		Bundle bundle = getIntent().getExtras();
		int param = 0;
		if(bundle != null){
			param = bundle.getInt("type");
		}
		lView = (ListView)this.findViewById(R.id.lvShow);
		lView.setOnItemClickListener(listener);
		url0 = getResources().getStringArray(GetNewsTabResourceId(param));
		if (url0.length > 0) {
			AsyncProcess aProcess = new AsyncProcess(this);
			aProcess.execute(url0[urlIndex]);
		}
	}
	
	/*
	 * 通过参数传递获取的rss资源编号
	 */
	private static int GetNewsTabResourceId(int pid){
		int reValue=R.array.rss_item_top;
		switch (pid) {
		case 1:
			reValue = R.array.rss_item_top;
			break;
		case 2:
			reValue = R.array.rss_item_business;
			break;
		case 3:
			reValue = R.array.rss_item_world;
			break;
		case 4:
			reValue = R.array.rss_item_entertainment;
			break;
		case 5:
			reValue = R.array.rss_item_technology;
			break;
		case 6:
			reValue = R.array.rss_item_sports;
			break;
		case 7:
			reValue = R.array.rss_item_titbits;
			break;
		default:
			break;
		}
		return reValue;
	}
	
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/*
	 * 解析rss中获取的数据并绑定到ListView
	 * */
	public class AsyncProcess extends AsyncTask<String, Integer, RssManager> {

		private Context context;
		private ProgressDialog pdiDialog;
		public AsyncProcess(Context context){
			this.context=context;
			pdiDialog = new ProgressDialog(context);
			pdiDialog.setCancelable(false);
			
			pdiDialog.setMessage(context.getString(R.string.dialog_waiting));
			//pdiDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pdiDialog.show();
		}
		
		@Override
		protected RssManager doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				if (arg0.length > 0) {
					return getFeed(arg0[0]);	
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}
		
		/*
		 * 获取rss资源并解析成实体
		 */
		private RssManager getFeed(String urlToRssFeed)
	    {
			HttpURLConnection  connection=null;
			InputSource is=null;
	    	try
	    	{
	    		// setup the url获取感兴趣的 RSS 提要  
	    	   URL url = new URL(urlToRssFeed);
	    	    connection = (HttpURLConnection) url.openConnection();
	    	    connection.setRequestProperty("Accept-Encoding", "identity");
	    	    connection.setConnectTimeout(5000);
	    	    connection.setRequestMethod("GET");
	    	    connection.connect();
	    	    InputStream iStream =connection.getInputStream();
		        is = new InputSource(iStream);
	    	    
	           // create the factory
	           SAXParserFactory factory = SAXParserFactory.newInstance();
	           // create a parser
	           SAXParser parser = factory.newSAXParser();

	           // create the reader (scanner)
	           XMLReader xmlreader = parser.getXMLReader();
	           // instantiate our handler
	           RssHandler theRssHandler = new RssHandler();
	           // assign our handler
	           xmlreader.setContentHandler(theRssHandler);
	          
	           // get our data via the url class
	           
	           // perform the synchronous parse           
	           xmlreader.parse(is);
	           // get the results - should be a fully populated RSSFeed instance, or null on error
	           return theRssHandler.getFeed();
	    	}
	    	catch (Exception ee)
	    	{
	    		ee.printStackTrace();
	    		// if we have a problem, simply return null
	    		return null;
	    	}
	    	finally{
	    		connection.disconnect();
	    	}
	    }

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(RssManager result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			if (result != null) {
				
				urlIndex++;
				/*
		         * 这里为ListView添加底部更多选项
		         */
				if (lView.getFooterViewsCount() == 0) {
					View footer = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_footer,null,false);
			        lView.addFooterView(footer);
			        TextView tvFooter = (TextView)footer.findViewById(R.id.txtFooter);
			        tvFooter.setOnClickListener(tvFooterListener);
				}
				if (totalList.isEmpty()) {
					totalList = result.getAllItems();
				}else {
					totalList.addAll(result.getAllItems());
				}
				
				listCount = totalList.size();
				//SimpleAdapter sAdapter = new SimpleAdapter(ShowActivityList.this, result.getAllItems(), R.layout.item_detail,new String[]{"txtName"},new int[]{R.id.testname});
		        //ArrayAdapter<RssInfo> adapter = new ArrayAdapter<RssInfo>(context,R.layout.item_detail,result.getAllItems());
				if (adapter == null) {
					adapter = new myListViewAdapter(ShowActivityList.this);
					lView.setAdapter(adapter);
				}else {
					
					adapter.notifyDataSetChanged();
				}
				AddGoogleAd();
			}
			pdiDialog.dismiss();
		}
		
		/*
		 * ListView的Footer点击更多事件
		 */
		private View.OnClickListener tvFooterListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (url0.length > urlIndex) {
					AsyncProcess aProcess = new AsyncProcess(context);
					aProcess.execute(url0[urlIndex]);
				}else {
					Toast.makeText(context, context.getString(R.string.toast_nomore_data), Toast.LENGTH_SHORT).show();
				}
			}
		};

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
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
			return listCount;
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
				convertView = mInflater.inflate(R.layout.item_detail,null);
				hoder.text = (TextView)convertView.findViewById(R.id.newsTitle);
				convertView.setTag(hoder);
			}else {
				hoder = (ViewHolder)convertView.getTag();
			}
			hoder.text.setText(totalList.get(position).getTitle());
			hoder.text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent detailShow = new Intent(mInflater.getContext(),DetailShow.class);
					Bundle bund = new Bundle();
					bund.putString("title", totalList.get(position).getTitle());
					bund.putString("description", totalList.get(position).getDescription());
					bund.putString("link", totalList.get(position).getLink());
					bund.putString("category", totalList.get(position).getCategory());
					bund.putString("pubdate", totalList.get(position).getPubDate());
					detailShow.putExtras(bund);
					startActivity(detailShow);
				}
			});
			//Toast.makeText(mInflater.getContext(), myList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
			return convertView;
		}
		
	}

}
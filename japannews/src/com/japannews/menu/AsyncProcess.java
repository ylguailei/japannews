package com.japannews.menu;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.japannews.component.RssHandler;
import com.japannews.component.RssManager;
import com.japannews.entity.RssInfo;

import android.os.AsyncTask;

public class AsyncProcess extends AsyncTask<String, Integer, RssManager> {

	@Override
	protected RssManager doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try {
			if (arg0.length > 1) {
				return getFeed(arg0[0]);	
			}
			
//			HttpClient hClient = new DefaultHttpClient();
//			HttpGet get = new HttpGet(arg0[0]);
//			HttpResponse httpResponse = hClient.execute(get);
//			HttpEntity entity = httpResponse.getEntity();
//			InputStream iStream = entity.getContent();
//			
//			long length = entity.getContentLength();
//			String s=null;
//			if (iStream != null) {
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				byte[] buf = new byte[128];
//				int ch=-1;
//				int count=0;
//				while ((ch = iStream.read(buf)) != -1) {
//					baos.write(buf,0,ch);
//					count += ch;
//					if (length > 0) {
//						publishProgress((int) ((count / (float) length) * 100));
//					}
//					Thread.sleep(100);
//				}
//				
//			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	private RssManager getFeed(String urlToRssFeed)
    {
    	try
    	{
    		// setup the url获取感兴趣的 RSS 提要  
    	   URL url = new URL(urlToRssFeed);

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
           InputSource is = new InputSource(url.openStream());
           // perform the synchronous parse           
           xmlreader.parse(is);
           // get the results - should be a fully populated RSSFeed instance, or null on error
           return theRssHandler.getFeed();
    	}
    	catch (Exception ee)
    	{
    		// if we have a problem, simply return null
    		return null;
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
		super.onPostExecute(result);
	}

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

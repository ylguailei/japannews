package com.japannews.component;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.japannews.entity.RssInfo;

import android.util.Log;

//RSSHandler ��ʵ�� SAX �������ص�����
public class RssHandler extends DefaultHandler 
{
	
	RssManager _feed;
	RssInfo _item;
	String _lastElementName = "";
	boolean bFoundChannel = false;
	final int RSS_TITLE = 1;
	final int RSS_LINK = 2;
	final int RSS_DESCRIPTION = 3;
	final int RSS_CATEGORY = 4;
	final int RSS_PUBDATE = 5;
	
	int depth = 0;
	int currentstate = 0;
	/*
	 * Constructor 
	 */
	public RssHandler()
	{
	}
	
	/*
	 * getFeed - this returns our feed when all of the parsing is complete
	 */
	public RssManager getFeed()
	{
		return _feed;
	}
	
	
	public void startDocument() throws SAXException
	{
		// initialize our RssManager object - this will hold our parsed contents
		_feed = new RssManager();
		// initialize the RssInfo object - we will use this as a crutch to grab the info from the channel
		// because the channel and items have very similar entries..
		_item = new RssInfo();

	}
	public void endDocument() throws SAXException
	{
	}
	public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException
	{
		depth++;
		if (localName.equals("channel"))
		{
			currentstate = 0;
			return;
		}
		if (localName.equals("image"))
		{
			// record our feed data - we temporarily stored it in the item :)
			_feed.setTitle(_item.getTitle());
			_feed.setPubDate(_item.getPubDate());
		}
		if (localName.equals("item"))
		{
			// create a new item
			_item = new RssInfo();
			return;
		}
		if (localName.equals("title"))
		{
			currentstate = RSS_TITLE;
			return;
		}
		if (localName.equals("description"))
		{
			currentstate = RSS_DESCRIPTION;
			return;
		}
		if (localName.equals("link"))
		{
			currentstate = RSS_LINK;
			return;
		}
		if (localName.equals("category"))
		{
			currentstate = RSS_CATEGORY;
			return;
		}
		if (localName.equals("pubDate"))
		{
			currentstate = RSS_PUBDATE;
			return;
		}
		// if we don't explicitly handle the element, make sure we don't wind up erroneously 
		// storing a newline or other bogus data into one of our existing elements
		currentstate = 0;
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		depth--;
		if (localName.equals("item"))
		{
			// add our item to the list!
			_feed.addItem(_item);
			return;
		}
	}
	 
	public void characters(char ch[], int start, int length)
	{
		String theString = new String(ch,start,length);
		Log.i("RSSReader","characters[" + theString + "]");
		
		switch (currentstate)
		{
			case RSS_TITLE:
				_item.setTitle(theString);
				currentstate = 0;
				break;
			case RSS_LINK:
				_item.setLink(theString);
				currentstate = 0;
				break;
			case RSS_DESCRIPTION:
				_item.setDescription(theString);
				currentstate = 0;
				break;
			case RSS_CATEGORY:
				_item.setCategory(theString);
				currentstate = 0;
				break;
			case RSS_PUBDATE:
				_item.setPubDate(theString);
				currentstate = 0;
				break;
			default:
				return;
		}
		
	}
}

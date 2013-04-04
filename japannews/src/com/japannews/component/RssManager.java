package com.japannews.component;
import java.util.List;
import java.util.Vector;
import com.japannews.entity.RssInfo;

public class RssManager {
	private String _title = null;
	private String _pubdate = null;
	private int _itemcount = 0;
	private List<RssInfo> _itemlist;
	
	
	RssManager()
	{
		_itemlist = new Vector(0); 
	}
	public int addItem(RssInfo item)
	{
		_itemlist.add(item);
		_itemcount++;
		return _itemcount;
	}
	public RssInfo getItem(int location)
	{
		return _itemlist.get(location);
	}
	public List<RssInfo> getAllItems()
	{
		return _itemlist;
	}
	public int getItemCount()
	{
		return _itemcount;
	}
	public void setTitle(String title)
	{
		_title = title;
	}
	public void setPubDate(String pubdate)
	{
		_pubdate = pubdate;
	}
	public String getTitle()
	{
		return _title;
	}
	public String getPubDate()
	{
		return _pubdate;
	}
	
	
}
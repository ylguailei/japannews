package com.japannews.entity;

public class RssInfo{
	
		//从 XML 数据流提取得到的 RSS 提要必须使用一种有用的格式保存。
		//两个 helper 类：RSSFeed 和 RSSItem
		private String _title = null;
		private String _description = null;
		private String _link = null;
		private String _category = null;
		private String _pubdate = null;

		
		public RssInfo()
		{
		}
		public void setTitle(String title)
		{
			_title = title;
		}
		public void setDescription(String description)
		{
			_description = description;
		}
		public void setLink(String link)
		{
			_link = link;
		}
		public void setCategory(String category)
		{
			_category = category;
		}
		public void setPubDate(String pubdate)
		{
			_pubdate = pubdate;
		}
		public String getTitle()
		{
			return _title.length() > 15?_title.substring(0, 15) + "...":_title;
		}
		public String getDescription()
		{
			return _description;
		}
		public String getLink()
		{
			return _link;
		}
		public String getCategory()
		{
			return _category;
		}
		public String getPubDate()
		{
			return _pubdate;
		}
		public String toString()
		{
			// limit how much text we display
			if (_title.length() > 42)
			{
				return _title.substring(0, 42) + "...";
			}
			return _title;
		}
	}
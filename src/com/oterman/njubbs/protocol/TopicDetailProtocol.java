package com.oterman.njubbs.protocol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.oterman.njubbs.bean.TopicDetailInfo;
import com.oterman.njubbs.utils.UiUtils;


public class TopicDetailProtocol  extends BaseProtocol<TopicDetailInfo>{
	String loadMoreUrl;

	/**
	 * 解析html   
	 * @param doc
	 * @return
	 */
	public List<TopicDetailInfo> parseHtml(Document doc,String url) {
		List<TopicDetailInfo> list = new ArrayList<>();
		
		loadMoreUrl=null;
		Elements aEles = doc.select("a");
		for (int i = aEles.size()-1; i >0 ; i--) {
			Element aEle=aEles.get(i);
			if("本主题下30篇".equals(aEle.text())){
				loadMoreUrl=aEle.attr("href");
			}
		}

		Elements tableEles = doc.select("tbody");
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (int i = 0; i < tableEles.size(); i++) {
			
			Elements tdEles = tableEles.get(i).select("td");
			
			//回复本文链接
			Element aEle = tdEles.get(0).select("a").get(1);
			String replyUrl= aEle.attr("href");
			
			int floorth=Integer.parseInt(tdEles.get(1).text());
			
			String str=tdEles.get(2).text();
			
			//指定.可以匹配所有字符 包括行结束符
//			Pattern p1=Pattern.compile("发信人:(.+?),.*小百合站 \\s*\\((.+?\\d{4})\\)*(.+)--.*",Pattern.DOTALL);
			Pattern p1=Pattern.compile("发信人:(.+?),.*标\\s*题:(.*?)发信站.*小百合站 \\s*\\((.+?\\d{4})\\)*(.+)--.*",Pattern.DOTALL);
			Matcher matcher = p1.matcher(str);
			if(matcher.find()){
				handleData2(list, dateFormat, floorth, matcher,replyUrl);
			}else{//帖子有修改 格式变化
//				p1=Pattern.compile("发信人:(.+?),.*小百合站\\s*\\((.+?\\d{4})\\)*(.+)",Pattern.DOTALL);
				p1=Pattern.compile("发信人:(.+?),.*标\\s*题:(.*?)发信站.*小百合站\\s*\\((.+?\\d{4})\\)*(.+)",Pattern.DOTALL);
				matcher = p1.matcher(str);
				
				if(matcher.find()){
					handleData2(list, dateFormat, floorth, matcher,replyUrl);
				}else{//自动发信
					p1=Pattern.compile("发信人:(.+?),.*自动发信系统\\s*\\((.+?\\d{4})\\)(.+)",Pattern.DOTALL);
					matcher = p1.matcher(str);
					if(matcher.find()){
						handleData(list, dateFormat, floorth, matcher,replyUrl);
					}else{
						p1=Pattern.compile("发信人:(.+?),.*BBS\\s*\\((.+?\\d{4})\\)(.+)",Pattern.DOTALL);
						matcher = p1.matcher(str);
						if(matcher.find()){
							handleData(list, dateFormat, floorth, matcher,replyUrl);
						}
					}
				}
			}
		}
		return list;
	}

	private void handleData2(
			List<TopicDetailInfo> list,
			SimpleDateFormat dateFormat, 
			int floorth, Matcher matcher,String replyUrl) {
		String author=matcher.group(1).trim();
		String title=matcher.group(2).trim();
		String pubTime=matcher.group(3).trim();
		pubTime=pubTime.replaceAll("\\)", "").trim();
		pubTime=dateFormat.format(new Date(pubTime));
		
		String content=matcher.group(4).trim();
		
		content=UiUtils.deleteNewLineMark(content);
		
		content=content.replaceAll("\\[/*uid\\]", "").trim();
		//[1;35mSent From 南大小百合  by MI NOTE LTE[m
//		content=content.replaceAll("\\[1;35m", "<font color='purple'>").replaceAll("\\[m", "</font>");
		
		
		content=content.replaceAll("*\\[1;37m", "<font color='black'>").replaceAll("\\[m", "</font>");
		content=content.replaceAll("*\\[1;31m", "<font color=\"#E00000\">").replaceAll("\\[m", "</font>");
		content=content.replaceAll("*\\[1;32m", "<font color=\"#008000\">").replaceAll("\\[m", "</font>");
		content=content.replaceAll("*\\[1;33m", "<font color=\"#808000\">").replaceAll("\\[m", "</font>");
		content=content.replaceAll("*\\[1;34m", "<font color=\"#0000FF\">").replaceAll("\\[m", "</font>");
		content=content.replaceAll("*\\[1;35m", "<font color='#D000D0'>").replaceAll("\\[m", "</font>");
		content=content.replaceAll("*\\[1;36m", "<font color=\"#33A0A0\">").replaceAll("\\[m", "</font>");
		
		content=content.replaceAll("\\[.*?m", "");
		
//		content=content.replaceAll("http.*?(jpg|jpeg|png|JPG|JPEG|PNG|gif|GIF)", "<br><img src=\""+"$0"+"\"/><br>");
		content=content.replaceAll("http.*?(jpg|jpeg|png|JPG|JPEG|PNG|gif|GIF)", "<img src=\""+"$0"+"\"/>");
		
		
		content=content.replaceAll("\\n", "<br>");
		
		TopicDetailInfo info=new TopicDetailInfo(author, floorth+"", pubTime, content, loadMoreUrl,replyUrl,title);
		list.add(info);
		
	}
	private void handleData(
			List<TopicDetailInfo> list,
			SimpleDateFormat dateFormat, 
			int floorth, Matcher matcher,String replyUrl) {
		
		String author=matcher.group(1).trim();
		String pubTime=matcher.group(2).trim();
		pubTime=pubTime.replaceAll("\\)", "").trim();
		pubTime=dateFormat.format(new Date(pubTime));
		
		String content=matcher.group(3).trim();
		
		content=UiUtils.deleteNewLineMark(content);
		
		content=content.replaceAll("\\[/*uid\\]", "").trim();
		
		//[1;35mSent From 南大小百合  by MI NOTE LTE[m
		
//		content=content.replaceAll("\\[1;37m", "<font color='black'>").replaceAll("\\[m", "</font>");
//		content=content.replaceAll("\\[1;31m", "<font color=\"#E00000\">").replaceAll("\\[m", "</font>");
//		content=content.replaceAll("\\[1;32m", "<font color=\"#008000\">").replaceAll("\\[m", "</font>");
//		content=content.replaceAll("\\[1;33m", "<font color=\"#808000\">").replaceAll("\\[m", "</font>");
//		content=content.replaceAll("\\[1;34m", "<font color=\"#0000FF\">").replaceAll("\\[m", "</font>");
//		content=content.replaceAll("\\[1;35m", "<font color='purple'>").replaceAll("\\[m", "</font>");
//		content=content.replaceAll("\\[1;36m", "<font color=\"#33A0A0\">").replaceAll("\\[m", "</font>");
		
		content=content.replaceAll("\\[1;37m", "<font color='black'>").replaceAll("\\[m", "</font>");
		content=content.replaceAll("\\[1;31m", "<font color=\"red\">").replaceAll("\\[m", "</font>");
		content=content.replaceAll("\\[1;32m", "<font color=\"#green\">").replaceAll("\\[m", "</font>");
		content=content.replaceAll("\\[1;33m", "<font color=\"#808000\">").replaceAll("\\[m", "</font>");
		content=content.replaceAll("\\[1;34m", "<font color=\"blue\">").replaceAll("\\[m", "</font>");
		content=content.replaceAll("\\[1;35m", "<font color='purple'>").replaceAll("\\[m", "</font>");
		content=content.replaceAll("\\[1;36m", "<font color=\"#33A0A0\">").replaceAll("\\[m", "</font>");
		
		content=content.replaceAll("\\[.*?m", "");
		
		content=content.replaceAll("http.*?(jpg|jpeg|png|JPG|JPEG|PNG|gif|GIF)", "<br><img src=\""+"$0"+"\"/><br>");
		
		
		content=content.replaceAll("\\n", "<br>");
		
		TopicDetailInfo info=new TopicDetailInfo(author, floorth+"", pubTime, content, loadMoreUrl,replyUrl);
		list.add(info);
	}
	/*
	 [1;37m哈哈哈[m  #000000
	[1;31m哈哈哈[m  #E00000
	[1;32m哈哈哈[m  #008000
	[1;33m哈哈哈[m  #808000
	[1;34m哈哈哈[m  #0000FF
	[1;35m哈哈哈[m  #D000D0
	[1;36m哈哈哈[m  #33A0A0
	 * 
	 * 	
	 */	
	  public static String ToSBC(String input) { 
	        char c[] = input.toCharArray(); 
	        for (int i = 0; i < c.length; i++) { 
	            if (c[i] == ' ') { 
	                c[i] = '\u3000'; 
	            } else if (c[i] < '\177') { 
	                c[i] = (char) (c[i] + 65248); 
	            } 
	        } 
	        return new String(c); 
	    } 

}

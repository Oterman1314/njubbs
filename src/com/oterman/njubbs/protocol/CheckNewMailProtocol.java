package com.oterman.njubbs.protocol;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;

import com.oterman.njubbs.BaseApplication;
import com.oterman.njubbs.utils.LogUtil;
/**
 * 检查是否有新的站内信  如果有，返回新的封数
 */
public class CheckNewMailProtocol {


	/**
	 * 从服务器加载十大数据，解析并返回
	 */
	public int checkFromServer(String url,Context context) {
		try {
			String cookie = BaseApplication.getCookie();
			if (cookie == null) {
				cookie=BaseApplication.autoLogin(context,false);
			}

			//	"_U_NUM=xx;_U_UID=xx;_U_KEY=xx
			String[] strs = cookie.split(";");
			if(strs.length==3){
				Map<String, String> cookies=new HashMap<>();
				cookies.put("_U_NUM",strs[0].split("=")[1]);
				cookies.put("_U_UID",strs[1].split("=")[1]);
				cookies.put("_U_KEY",strs[2].split("=")[1]);
				
				Document doc= Jsoup.connect(url).cookies(cookies).get();
				if(doc.select("td").size()==0){
					BaseApplication.autoLogin(context,false);
					cookie = BaseApplication.getCookie();
					cookies.put("_U_NUM",strs[0].split("=")[1]);
					cookies.put("_U_UID",strs[1].split("=")[1]);
					cookies.put("_U_KEY",strs[2].split("=")[1]);
					
					doc= Jsoup.connect(url).cookies(cookies).get();
				}
				
				String result=doc.html();
				//LogUtil.d("检查新的站内信结果："+result);
				
				if (!result.contains("您尚未登录")) {
					return parseHtml(doc);
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}


	private int parseHtml(Document doc) {
		int count=doc.select("tr").size()-1;
		LogUtil.d("新站内信数："+count);
		
		return count;
	}
}

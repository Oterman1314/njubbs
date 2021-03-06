package com.oterman.njubbs.protocol;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.oterman.njubbs.bean.UserInfo;
import com.oterman.njubbs.utils.Constants;
import com.oterman.njubbs.utils.LogUtil;

public class UserProtocol {

	public UserInfo getUserInfoFromServer(String userId) {

		Document doc;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy年MM月dd日 HH:mm:ss");
		try {
			doc = Jsoup.connect(Constants.getUserUrl(userId)).get();
			if (doc.select("td").size() != 0) {
				String result = doc.select("td").get(0).text();
				
				//处理性别
				String result2=result.substring(0,result.indexOf("到本站一游"));
				
				result = result.replaceAll("\\[.*?m", "");

				/*
				 * IFme (Mico) 共上站 682 次，发表文章 70 篇 [处女座]上次在 [Tue May 10 21:31:32
				 * 2016] 从 [112.21.166.24] 到本站一游。 信箱：[⊙] 经验值：[562](中级站友)
				 * 表现值：[38](很好) 生命力：[368]。 目前不在站上, 上次离站时间 [(不详)]
				 */

				Pattern p = Pattern.compile(
						"(.*?)\\((.*?)\\).*?共上站(.*?)次.*?文章(.*?)篇.*?"
								+ "\\[(.*?)\\].*?\\[(.*?)\\].*?\\[(.*?)\\].*?"
								+ "经验值：(.*?)表现值：(.*?)生命力：(.*?)。.*",
						Pattern.DOTALL);
				Matcher m = p.matcher(result);

				if (m.find()) {
					String id = m.group(1).trim();
					String nickname = m.group(2).trim();
					String totalVisit = m.group(3).trim();
					String totalPub = m.group(4).trim();
					String xingzuo = m.group(5).trim();
					String lastVisitTime = m.group(6).trim();
					String lastVisitIP = m.group(7).trim();
					String jingyan = m.group(8).trim();
					String biaoxian = m.group(9).trim();
					String life = m.group(10).trim();

					if (lastVisitIP == null || lastVisitIP.length() < 3) {
						lastVisitIP = lastVisitTime;
						lastVisitTime = xingzuo;
						xingzuo = "未知";
					}

					lastVisitTime = dateFormat.format(new Date(lastVisitTime));

					UserInfo info = new UserInfo(id, nickname, totalVisit,
							totalPub, xingzuo, lastVisitTime, lastVisitIP,
							jingyan, biaoxian, life, false);
					if (result.contains("目前在站上")) {
						info.isOnline = true;
					} else {
						info.isOnline = false;
					}
					
					if(result2.contains("1;36m")){//男
						info.gender="男";
					}else if(result2.contains("1;35m")){//女
						info.gender="女";
					}else{//未知
						System.out.println("性别：未知");
						info.gender="未知";
					}
					LogUtil.d("从网上获取用户数据："+info.id);
					return info;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}
}

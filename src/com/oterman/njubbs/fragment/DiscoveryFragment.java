package com.oterman.njubbs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oterman.njubbs.R;
import com.oterman.njubbs.activity.expore.AddFriendActivity;
import com.oterman.njubbs.activity.expore.ColleagesActivity;
import com.oterman.njubbs.activity.expore.FindBoardActivity;
import com.oterman.njubbs.activity.expore.FindTopicActivity;
import com.oterman.njubbs.activity.expore.FindTopicTotalActivity;
import com.oterman.njubbs.activity.expore.FriendsActivity;
import com.oterman.njubbs.activity.expore.TopicHisActivity;
import com.oterman.njubbs.activity.mail.MailNewActivity;
import com.oterman.njubbs.activity.mail.MailSpecialActivity;
import com.oterman.njubbs.utils.MyToast;
import com.oterman.njubbs.utils.SPutils;


/**
 * 
String str="njubbs_v0.5 \n " +
		"更新日志：" +
		"\n 1.实现了显示表情" +
		"\n 2.勉强实现了获取收藏的版面";

 str="njubbs_v0.6 \n " +
		"更新日志：" +
		"\n 1.实现了发帖" +
		"\n 2.优化了版面帖子列表";
 str="njubbs_v0.7 \n " +
		"更新日志：" +
		"\n 1.实现了长按帖子删除功能" +
		"\n 2.实现了发帖时添加表情功能";
 str="njubbs_v0.8 \n " +
		"更新日志：" +
		"\n 1.实现了回帖" +
		"\n 2.实现了修改回帖";
 str="njubbs_v0.9 \n " +
		"更新日志：" +
		"\n 1.站内信的查看" +
		"\n 2.站内信的发送，回复，删除";
 *
 */
public class DiscoveryFragment extends Fragment implements OnClickListener {
	

	private LinearLayout llColleages;
	private LinearLayout llFindUser;
	private LinearLayout llFindBoard;
	private LinearLayout llFindTopic;
	
//	private LinearLayout llFriends;
//	private LinearLayout llMyTopic;
	
	private LinearLayout llMoney;
	private LinearLayout llFeedback;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = View.inflate(getContext(), R.layout.fragment_explore, null);
		llColleages = (LinearLayout) view.findViewById(R.id.ll_colleages);
		llMoney = (LinearLayout) view.findViewById(R.id.ll_money);
		llFindUser=(LinearLayout) view.findViewById(R.id.ll_find_user);
		llFindBoard=(LinearLayout) view.findViewById(R.id.ll_find_board);
		llFindTopic=(LinearLayout) view.findViewById(R.id.ll_find_topic);
		llFeedback=(LinearLayout) view.findViewById(R.id.ll_feedback);
		
		//设置监听
		llColleages.setOnClickListener(this);
		llMoney.setOnClickListener(this);
		llFindUser.setOnClickListener(this);
		llFindBoard.setOnClickListener(this);
		llFindTopic.setOnClickListener(this);
		llFeedback.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_colleages:
			Intent intent=new Intent(getContext(),ColleagesActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_money://打赏
			//判断登陆用户
			String id = SPutils.getFromSP("id").trim();
			if(TextUtils.isEmpty(id)){
				MyToast.toast("请先登录哦");
				return ;
			}
			if("395440772".equals(id)){
				MyToast.toast("就不打赏");
				return ;
			}
			//跳转到求祝福页面
			Intent intent2=new Intent(getContext(),MailSpecialActivity.class);
			startActivity(intent2);
			break;
		case R.id.ll_find_user:
			Intent findIntent=new Intent(getContext(),AddFriendActivity.class);
			startActivity(findIntent);
			break;
			
		case R.id.ll_find_board:
			Intent findBoardIntent=new Intent(getContext(),FindBoardActivity.class);
			startActivity(findBoardIntent);
			break;
		case R.id.ll_find_topic:
//			Intent findTopicIntent=new Intent(getContext(),FindTopicActivity.class);
			Intent findTopicIntent=new Intent(getContext(),FindTopicTotalActivity.class);
			startActivity(findTopicIntent);
			break;
		case R.id.ll_feedback://反馈
//			Intent myIntent=new Intent(getContext(),MyTopicActivity.class);
//			startActivity(myIntent);
			Intent intent3=new Intent(getContext(),MailNewActivity.class);
			intent3.putExtra("receiver","oterman");
			intent3.putExtra("title","反馈");
			startActivity(intent3);
			break;

		default:
			break;
		}
		
	}
	
	


}

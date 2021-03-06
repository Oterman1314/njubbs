package com.oterman.njubbs.holders;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oterman.njubbs.R;
import com.oterman.njubbs.activity.expore.TopicHisActivity;
import com.oterman.njubbs.utils.MyToast;
import com.oterman.njubbs.utils.SPutils;
import com.oterman.njubbs.utils.UiUtils;

public class OptionsDialogHolder implements OnClickListener {


	View rootView;
	Context context;
	private Button btnAuthorDetail;
	private Button btnMail;
	private Button btnDelete;
	private Button btnModify;
	private String ownerId;
	
	MyOnclickListener listener;
	private Button btnReply;
	
	private boolean isInsideTopic;//是否在帖子详情长按
	private boolean isFromMail=false;//是否从站内长按
	private Button btnTopicHis;
	
	public void setListener(MyOnclickListener listener) {
		this.listener = listener;
	}

	public OptionsDialogHolder(Context context,String ownerId,boolean isInsideTopic) {
		rootView = View.inflate(UiUtils.getContext(), R.layout.item_long_click, null);
		this.context=context;
		this.ownerId=ownerId;
		this.isInsideTopic=isInsideTopic;
		initViews();
	}
	public OptionsDialogHolder(Context context,String ownerId,boolean isInsideTopic,boolean isFromMail) {
		rootView = View.inflate(UiUtils.getContext(), R.layout.item_long_click, null);
		this.context=context;
		this.ownerId=ownerId;
		this.isInsideTopic=isInsideTopic;
		this.isFromMail=isFromMail;
		initViews();
	}
	
	

	private void initViews() {
		
		btnAuthorDetail = (Button) rootView.findViewById(R.id.btn_author_detail);
		
		btnMail = (Button) rootView.findViewById(R.id.btn_mail_to_author);//站内
		btnDelete = (Button) rootView.findViewById(R.id.btn_delete_topci);//删帖
		btnModify = (Button) rootView.findViewById(R.id.btn_modify_topic);//修改
		btnReply = (Button) rootView.findViewById(R.id.btn_reply_floor);//回帖
		
		btnTopicHis = (Button) rootView.findViewById(R.id.btn_topic_his);

		btnAuthorDetail.setOnClickListener(this);
		btnMail.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnModify.setOnClickListener(this);
		btnReply.setOnClickListener(this);
		
		btnTopicHis.setOnClickListener(this);
		
		if(!isFromMail){//不是从站内信处长按
			updateVisibility();
		}else{
			updateMailViews();//从站内信处长按
		}
		
	}

	private void updateMailViews() {
		//保留看作者信息，看作者发帖，以及删除，其他都
		btnAuthorDetail.setText("发件人信息");
		btnTopicHis.setText("发件人发帖记录");
		btnDelete.setText("删除站内信");
		
		btnMail.setVisibility(View.GONE);
		btnModify.setVisibility(View.GONE);
		btnReply.setVisibility(View.GONE);
		btnDelete.setVisibility(View.GONE);
		
	}

	private void updateVisibility() {
		String logedId = SPutils.getFromSP("id");//登陆作者的id
		
		//比较当前id与登陆作者id的区别
		if(this.ownerId.equals(logedId)){//查看帖子的作者和当前登录的是同一人
			this.btnDelete.setVisibility(View.VISIBLE);
			this.btnModify.setVisibility(View.VISIBLE);
		}else{//不是同一人
			this.btnDelete.setVisibility(View.GONE);
			this.btnModify.setVisibility(View.GONE);
		}
		
		if(isInsideTopic){//帖子内部点击
			this.btnReply.setVisibility(View.VISIBLE);
		}else{//帖子外部点击  不显示修改
			this.btnReply.setVisibility(View.GONE);
			this.btnModify.setVisibility(View.GONE);
		}
		
	}

	public View getRootView() {
		return rootView;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_author_detail:
			if(listener!=null){
				listener.OnQueryAuthurDetail();
			}
			
			break;
		case R.id.btn_mail_to_author:
			if(listener!=null){
				listener.OnMailTo();
			}
			break;
		case R.id.btn_delete_topci:
			if(listener!=null){
				listener.onDelete();
			}
			break;
		case R.id.btn_modify_topic:
			if(listener!=null){
				listener.OnModify();
			}
			break;
		case R.id.btn_reply_floor:
			if(listener!=null){
				listener.onReplyFloor();
			}
			break;
			
		case R.id.btn_topic_his:
//			MyToast.toast("点击了");
			if(listener!=null){
				listener.onQueryTopicHis();
			}
			break;

		default:
			break;
		}
	}
	
	public interface MyOnclickListener{
		
		public void onDelete();
		public void OnQueryAuthurDetail();
		public void OnModify();
		public void OnMailTo();
		public void onReplyFloor();
		
		public void onQueryTopicHis();
		
	}

}

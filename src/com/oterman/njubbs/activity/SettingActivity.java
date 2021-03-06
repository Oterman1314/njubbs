package com.oterman.njubbs.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.oterman.njubbs.R;
import com.oterman.njubbs.dialog.EditTailDialog;
import com.oterman.njubbs.dialog.MailRemindDialog;
import com.oterman.njubbs.dialog.SettingByAuthorDialog;
import com.oterman.njubbs.dialog.SettingTitlelDialog;
import com.umeng.analytics.MobclickAgent;
@SuppressLint("NewApi")
public class SettingActivity  extends MyActionBarActivity implements OnClickListener {


	private LinearLayout llTail;
	private LinearLayout llTitleSearch;
	private LinearLayout llAuthorSearch;
	private LinearLayout llAuthorTips;
	private LinearLayout llAboutme;
	private LinearLayout llRemind;
	private LinearLayout llAccounts;

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		
		llTail = (LinearLayout) this.findViewById(R.id.ll_post_tail);
		llTitleSearch = (LinearLayout) this.findViewById(R.id.ll_title_search);
		llAuthorSearch = (LinearLayout) this.findViewById(R.id.ll_author_search);
		llAuthorTips= (LinearLayout) this.findViewById(R.id.ll_author_tips);
		llAboutme = (LinearLayout) this.findViewById(R.id.ll_about_me);
		llRemind = (LinearLayout) this.findViewById(R.id.ll_remind);
		llAccounts = (LinearLayout) this.findViewById(R.id.ll_accounts);

		
		llAccounts.setOnClickListener(this);
		llRemind.setOnClickListener(this);
		llTail.setOnClickListener(this);
		llTitleSearch.setOnClickListener(this);
		llAuthorTips.setOnClickListener(this);
		llAboutme.setOnClickListener(this);
		llAuthorSearch.setOnClickListener(this);
		
		
	}
	
	@Override
	protected String getBarTitle() {
		return "设置";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_post_tail:
			EditTailDialog dialog=new EditTailDialog(this);
			dialog.show();
//			MyToast.toast("小尾巴");
			break;
		case R.id.ll_remind:
			MailRemindDialog remindDialog=new MailRemindDialog(this);
			remindDialog.show();
			
			
			break;
		case R.id.ll_author_search:
			SettingByAuthorDialog authorDialog=new SettingByAuthorDialog(this);
			authorDialog.show();
			break;
		case R.id.ll_title_search:
			SettingTitlelDialog titleDialog=new SettingTitlelDialog(this);
			titleDialog.show();
			break;
		case R.id.ll_about_me:
//			MyToast.toast("我就是我，不一样的烟火!");
			Uri uri = Uri.parse("market://details?id="+getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.ll_author_tips:
			//使用技巧
			AlertDialog.Builder builder=new Builder(this);
			
			builder.setTitle("小技巧");
			builder.setMessage("1.在十大列表页面，长按帖子有惊喜!\n2.在版面帖子列表页面，长按惊喜依旧!\n3.在帖子详情列表页面，长按更多惊喜！\n4.某些页面需要手动下拉刷新哦。");
			builder.setPositiveButton("确定", null);
			
			builder.show();
			
			break;
		case R.id.ll_accounts:
			//跳转到账号管理页面
//			MyToast.toast("账户管理");
			Intent accountIntent=new Intent(this,ManageAccountsActivity.class);
			
			startActivity(accountIntent);
			
			break;
		default:
			break;
		}
		
	}



}

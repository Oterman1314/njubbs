package com.oterman.njubbs.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.oterman.njubbs.R;
import com.oterman.njubbs.dialog.EditTailDialog;
import com.oterman.njubbs.utils.MyToast;
@SuppressLint("NewApi")
public class SettingActivity  extends MyActionBarActivity implements OnClickListener {


	private LinearLayout llTail;
	private LinearLayout llTitleSearch;
	private LinearLayout llAuthorSearch;
	private LinearLayout llAboutme;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		
		llTail = (LinearLayout) this.findViewById(R.id.ll_post_tail);
		llTitleSearch = (LinearLayout) this.findViewById(R.id.ll_title_search);
		llAuthorSearch = (LinearLayout) this.findViewById(R.id.ll_author_search);
		llAboutme = (LinearLayout) this.findViewById(R.id.ll_about_me);

		
		llTail.setOnClickListener(this);
		llTitleSearch.setOnClickListener(this);
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
		case R.id.ll_author_search:
			MyToast.toast("作者搜贴设置");
			break;
		case R.id.ll_title_search:
			MyToast.toast("按标题搜贴设置");
			break;
		case R.id.ll_about_me:
			MyToast.toast("关于我");
			break;

		default:
			break;
		}
		
	}



}

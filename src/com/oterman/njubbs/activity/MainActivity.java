package com.oterman.njubbs.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.oterman.njubbs.R;
import com.oterman.njubbs.R.color;
import com.oterman.njubbs.R.id;
import com.oterman.njubbs.R.layout;
import com.oterman.njubbs.R.menu;
import com.oterman.njubbs.fragment.AboutMeFragment;
import com.oterman.njubbs.fragment.BaseFragment;
import com.oterman.njubbs.fragment.DiscoveryFragment;
import com.oterman.njubbs.fragment.factory.FragmentFactory;
import com.oterman.njubbs.protocol.CheckNewMailProtocol;
import com.oterman.njubbs.utils.Constants;
import com.oterman.njubbs.utils.LogUtil;
import com.oterman.njubbs.utils.ThreadManager;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {

	private ViewPager vpPages;
	private RadioGroup rgGroup;
	private int newMailCount;
	private RadioButton rbMe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//更改状态栏的颜色
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.green));
		}
		initViews();
		
		//检查是否有新邮件
		//checkHasNewMail();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//检查新邮件
		checkHasNewMail();
		LogUtil.d("onResume  检查新邮件");
	}
	
	//检查是否有新的站内
	public void checkHasNewMail() {
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {
			@Override
			public void run() {
				LogUtil.d("检查是否有新邮件啦");
				CheckNewMailProtocol protocol=new CheckNewMailProtocol();
				String url=Constants.HAS_NEW_MAIL_URL;
				newMailCount = protocol.checkFromServer(url);
				LogUtil.d("检查结果："+newMailCount);
				if(newMailCount>0){//有新邮件
					//更新状态
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Drawable drawable = getResources().getDrawable(R.drawable.tab_me_selector_newmail);
							drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
							rbMe.setCompoundDrawables(null, drawable, null, null);
						}
					});
				}else{//没有新邮件
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Drawable drawable = getResources().getDrawable(R.drawable.tab_me_selector);
							drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
							rbMe.setCompoundDrawables(null, drawable, null, null);
						}
					});
				}
			}
		});
		
	}


	public void initViews() {
		
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle("haha  this is title.");
		
		//初始化viewpager
		vpPages = (ViewPager) this.findViewById(R.id.vp_pages);
		vpPages.setAdapter(new MyPageAdapter(getSupportFragmentManager()));

		//初始化radiogroup
		rgGroup = (RadioGroup) this.findViewById(R.id.rg_bottom_group);
		rbMe = (RadioButton) this.findViewById(R.id.rb_me);
		
		rgGroup.check(R.id.rb_hottopic);
		
		
		//设置监听 同步viewpager
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_hottopic:
					vpPages.setCurrentItem(0);
					break;
				case R.id.rb_boards:
					vpPages.setCurrentItem(1);
					break;
				case R.id.rb_find:
					vpPages.setCurrentItem(2);
					break;
				case R.id.rb_me:
					vpPages.setCurrentItem(3);
					break;
				}
			}
		});
		
		//设置监听 同步radiobutton
		vpPages.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				//加载数据

				Fragment fragment = FragmentFactory.creatFragment(position);
				
//				if(fragment instanceof BaseFragment){
//					//((BaseFragment)fragment).showViewFromServer();
//				}
//				
//				if(fragment instanceof AboutMeFragment){
//					((AboutMeFragment)fragment).updateViews();
//				}
				
				//选中页面时，切换到对应的radiobutton;
				switch (position) {
				case 0:
					rgGroup.check(R.id.rb_hottopic);
					break;
				case 1:
					rgGroup.check(R.id.rb_boards);
					break;
				case 2:
					rgGroup.check(R.id.rb_find);
					break;
				case 3:
					rgGroup.check(R.id.rb_me);
					
					
					break;
				default:
					break;
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
		
	}

	class MyPageAdapter extends FragmentPagerAdapter{

		public MyPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			Fragment fragment = FragmentFactory.creatFragment(position);
			
			return fragment;
		}

		@Override
		public int getCount() {
			return 4;
		}
		
	}
	
	
}

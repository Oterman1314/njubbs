package com.oterman.njubbs.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oterman.njubbs.R;
import com.oterman.njubbs.bean.TopicDetailInfo;
import com.oterman.njubbs.protocol.TopicDetailProtocol;
import com.oterman.njubbs.utils.LogUtil;
import com.oterman.njubbs.view.LoadingView;
import com.oterman.njubbs.view.LoadingView.LoadingState;

@SuppressLint("NewApi")
public class TopicDetailActivity extends FragmentActivity {

	LoadingView loadingView;
	private List<TopicDetailInfo> list;
	private TopicDetailAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//更改状态栏的颜色
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.green));
		}
		
		if(loadingView==null){
			loadingView=new LoadingView(getApplicationContext()){

				@Override
				protected LoadingState loadDataFromServer() {
					return TopicDetailActivity.this.loadDataFromServer();
				}

				@Override
				protected View createSuccessView() {
					return TopicDetailActivity.this.createSuccessView();
				}
			};
		}
		loadingView.showViewFromServer();
		setContentView(loadingView);
	}

	protected View createSuccessView() {
		ListView lv=new ListView(getApplicationContext());
//		lv.setDivider(new ColorDrawable(Color.GRAY));  
//		lv.setDividerHeight(1); 
		lv.setDividerHeight(0);
		adapter = new TopicDetailAdapter();
		lv.setAdapter(adapter);
		return lv;
	}

	protected LoadingState loadDataFromServer() {
		String url=getIntent().getStringExtra("contentUrl");
		
		TopicDetailProtocol protocol=new TopicDetailProtocol();
		list = protocol.loadFromServer(url);
		LogUtil.d("结果："+list.toString());
		return list==null?LoadingState.LOAD_FAILED:LoadingState.LOAD_SUCCESS;
	}
	
	class TopicDetailAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;
			if(convertView==null){
				holder=new ViewHolder();
				convertView=View.inflate(getApplicationContext(), R.layout.list_item_topic_detial, null);
				
				holder.tvAuthor=(TextView) convertView.findViewById(R.id.tv_topic_detail_item_author);
				holder.tvContent=(TextView) convertView.findViewById(R.id.tv_topic_detail_item_content);
				holder.tvFloorth=(TextView) convertView.findViewById(R.id.tv_topic_detail_item_floorth);
				holder.tvPubTime=(TextView) convertView.findViewById(R.id.tv_topic_detail_item_pubtime);
				
				
				convertView.setTag(holder);
				
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			TopicDetailInfo info = list.get(position);
			holder.tvAuthor.setText(info.author);
			holder.tvContent.setText(info.content);
			holder.tvFloorth.setText("第"+info.floorth+"楼");
			holder.tvPubTime.setText(info.pubTime);

			if(position%2==0){
				convertView.setBackgroundColor(0xFFEBEBEB);
			}else{
				convertView.setBackgroundColor(0x88D0D0E0);
			}
			return convertView;
		}
		
		class ViewHolder{
			public TextView tvContent;
			public TextView tvAuthor;
			public TextView tvPubTime;
			public TextView tvFloorth;
			
		}
		
	}

}

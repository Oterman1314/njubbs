package com.oterman.njubbs.activity.mail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.oterman.njubbs.BaseApplication;
import com.oterman.njubbs.R;
import com.oterman.njubbs.activity.MyActionBarActivity;
import com.oterman.njubbs.bean.MailInfo;
import com.oterman.njubbs.dialog.WaitDialog;
import com.oterman.njubbs.smiley.SelectFaceHelper;
import com.oterman.njubbs.smiley.SelectFaceHelper.OnFaceOprateListener;
import com.oterman.njubbs.utils.Constants;
import com.oterman.njubbs.utils.LogUtil;
import com.oterman.njubbs.utils.MyToast;
import com.oterman.njubbs.utils.SmileyParser;
import com.oterman.njubbs.utils.ThreadManager;
import com.oterman.njubbs.utils.TopicUtils;
import com.oterman.njubbs.view.MyTagHandler;
import com.oterman.njubbs.view.URLImageParser;
import com.umeng.analytics.MobclickAgent;

public class MailReplyActivity extends MyActionBarActivity implements
		OnClickListener {

	private EditText etTitle;
	private EditText etReceiver;
	private EditText etContent;
	private WaitDialog dialog;
	private ImageButton ibSmiley;
	private View addFaceToolView;
	private SelectFaceHelper mFaceHelper;
	private ImageButton ibPost;
	boolean isVisbilityFace=false;
	private MailInfo mailInfo;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mail_reply);

		
		//actionbar的发送箭头
		ibPost = (ImageButton) actionBarView.findViewById(R.id.btn_post_topic);
		ibPost.setVisibility(View.VISIBLE);
		
		ibPost.setOnClickListener(this);
			
		etTitle = (EditText) this.findViewById(R.id.et_titile);//标题
		etReceiver=(EditText) this.findViewById(R.id.et_mailto);
		etContent = (EditText) this.findViewById(R.id.et_content);
		
		
		
		ibSmiley = (ImageButton) this.findViewById(R.id.iv_pic);
		ibSmiley.setOnClickListener(faceClick);
		
		ibChosePic = (ImageButton) this.findViewById(R.id.iv_chose_pic);
		ibChosePic.setOnClickListener(this);
		
		addFaceToolView=this.findViewById(R.id.add_tool);

		
		etContent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isVisbilityFace = false;
				addFaceToolView.setVisibility(View.GONE);
				return false;
			}
		});	
		
		etTitle.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isVisbilityFace = false;
				addFaceToolView.setVisibility(View.GONE);
				return false;
			}
		});	
		
		//回去要修改回帖的数据
		Intent intent = getIntent();
		mailInfo = (MailInfo) intent.getSerializableExtra("mailInfo");
		
		//初始化
		String author = mailInfo.author;
		author=author.substring(0,author.indexOf("("));
		
		etTitle.setText("Re:"+mailInfo.title);
		
		SmileyParser sp=SmileyParser.getInstance(getApplicationContext());
		StringBuffer sb=new StringBuffer();
		sb.append("<br><br><br>");
		sb.append("<font color=\"black\">");
		
		etReceiver.setText(author);
		
		sb.append("【 在").append(author).append("的来信中提到: 】<br>").append(":");
		String content=mailInfo.content;
		//content=content.substring(0, content.lastIndexOf('-'));//去掉小尾巴
		
		content=content.replace("<br><br>", "<br>");
		content=content.replaceAll("<img src=\"", "");
		content=content.replaceAll("\"/>", "");
		
		sb.append(content);
		sb.append("</font>");
		
		
		Spanned spanned = Html.fromHtml(sb.toString(), 
				new URLImageParser(etContent),
				new MyTagHandler(getApplicationContext()));
		
		etContent.setText(sp.strToSmiley(spanned));
		
		
	}

	//点击脸表情，调出所有表情
		View.OnClickListener faceClick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == mFaceHelper) {
					mFaceHelper = new SelectFaceHelper(MailReplyActivity.this, addFaceToolView);
					//点击表情时，设置监听
					mFaceHelper.setFaceOpreateListener(mOnFaceOprateListener2);
				}
				if (isVisbilityFace) {
					isVisbilityFace = false;
					addFaceToolView.setVisibility(View.GONE);
				} else {
					isVisbilityFace = true;
					addFaceToolView.setVisibility(View.VISIBLE);
					hideInputManager(MailReplyActivity.this);//隐藏软键盘
				}
			}
		};
		
		// 隐藏软键盘
		public void hideInputManager(Context ct) {
			try {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) ct)
						.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
				Log.e("", "hideInputManager Catch error,skip it!", e);
			}
		}
		
		
		//表情点击的监听事件
		OnFaceOprateListener mOnFaceOprateListener2 = new OnFaceOprateListener() {
			@Override
			public void onFaceSelected(SpannableString spanEmojiStr) {
				
				if (null != spanEmojiStr) {
					//在光标处插入表情
					String oriText=etContent.getText().toString();//原始文字
					int index=Math.max(etContent.getSelectionStart(),0);//获取光标处位置，没有光标，返回-1
					
					StringBuffer sb=new StringBuffer(oriText);
					sb.insert(index, spanEmojiStr);
					String string = sb.toString().replaceAll("\n", "<br>");
					
					Spanned spanned = Html.fromHtml(string);
					CharSequence text = SmileyParser.getInstance(getApplicationContext()).strToSmiley(spanned);
					etContent.setText(text);
					
					etContent.setSelection(index+spanEmojiStr.length());
//					etContent.append(spanEmojiStr);
				}
				
			}

			@Override
			public void onFaceDeleted() {
				int selection = etContent.getSelectionStart();
				String text = etContent.getText().toString();
				if (selection > 0) {
					String text2 = text.substring(selection - 1,selection);
					if ("]".equals(text2)) {
						int start = text.lastIndexOf("[");
						int end = selection;
						etContent.getText().delete(start, end);
						return;
					}
					etContent.getText().delete(selection - 1, selection);
				}
			}
		};
		private ImageButton ibChosePic;
		
		
	
	@Override
	protected String getBarTitle() {
		return "回复站内";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==100&&data!=null){//从图库中选图
			//从intent中得到选中图片的路径
	        String picturePath = TopicUtils.getPicPathFromUri(this,data);
	        //展示选中的图片,上传逻辑包含在其中
	        TopicUtils.showChosedPic(this,picturePath,etContent);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_post_topic:// 处理回信
			// 获取数据
			String content = etContent.getText().toString().trim();
			String title=etTitle.getText().toString().trim();
			String receiver=etReceiver.getText().toString().trim();
					
			if (TextUtils.isEmpty(content)) {
				MyToast.toast("请输入内容");
				return;
			}
			if (TextUtils.isEmpty(receiver)) {
				MyToast.toast("请输入收信人");
				return;
			}
			if (TextUtils.isEmpty(title)) {
				MyToast.toast("请输入标题");
				return;
			}

			// 处理发帖逻辑
			handleReplyMail(content,title,receiver);
			// MyToast.toast("发帖："+board);

			break;

		case R.id.iv_chose_pic://从图库选图
			Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			this.startActivityForResult(intent, 100);
			break;
			
		default:
			break;
		}

	}
	private void handleReplyMail( final String content, final String title, final String receiver) {
		dialog = new WaitDialog(this);
		dialog.setMessage("努力回信中。。。");
		dialog.show();
		
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {
			HttpUtils httpUtils = null;
			@Override
			public void run() {
				//回帖逻辑
				try {
					if(httpUtils==null){
						httpUtils=new HttpUtils();
					}
					RequestParams rp=new RequestParams("gbk");
					// delUrl=bbsdelmail?file=M.1463833076.A
					
					String delUrl=mailInfo.delUrl;
					String action=delUrl.substring(delUrl.indexOf("=")+1).trim();
					String pid = action.substring(action.indexOf(".")+1, action.lastIndexOf("."));
					
					
					rp.addQueryStringParameter("pid", pid);
					rp.addQueryStringParameter("userid",receiver);
					
					rp.addBodyParameter("signature", "1");
					rp.addBodyParameter("action", action);
					rp.addBodyParameter("userid", receiver);
					rp.addBodyParameter("title", title);
					rp.addBodyParameter("text", content);
					
					//添加cookie
					String cookie = BaseApplication.getCookie();
					if(cookie==null){//自动登陆
						cookie=BaseApplication.autoLogin(MailReplyActivity.this,true);
					}
					
					rp.addHeader("Cookie",cookie);
					
					ResponseStream stream = httpUtils.sendSync(HttpMethod.POST, Constants.REPLY_MAIL_URL, rp);
					
					String result = BaseApplication.StreamToStr(stream);
					LogUtil.d("回信结果："+result);
					
					if(result.contains("信件已寄给")){//成功
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (dialog != null) {
									dialog.dismiss();
								}
								MyToast.toast("回信成功！");
								finish();
							}
						});
					}else{//回信失败
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (dialog != null) {
									dialog.dismiss();
								}
								MyToast.toast("回信失败！重新登陆再试试");
							}
						});
					}
					
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (dialog != null) {
								dialog.dismiss();
							}
							MyToast.toast("发信失败！"+e.getMessage());
						}
					});
				}
				
			}
		});
		
	}

	/**
	 * 处理
	 * 
	 * @param title
	 * @param content
	 */
	private void handleReplyMail2( final String content) {

		dialog = new WaitDialog(this);
		dialog.setMessage("努力回信中。。。");
		dialog.show();

		ThreadManager.getInstance().createLongPool().execute(new Runnable() {
			HttpUtils httpUtils = null;
			
			@Override
			public void run() {

				try {
					
					if(httpUtils==null){
						httpUtils=new HttpUtils();
					}
					
					//服务器编码为gbk
					RequestParams params = new RequestParams("gbk");
					
				
					String modifyUrl=Constants.getModifyReplyUrl();
					ResponseStream stream = httpUtils.sendSync(HttpMethod.POST,modifyUrl, params);
					
					String result = BaseApplication.StreamToStr(stream);
					LogUtil.d("修改回帖结果：" + result);
					
					if(result.contains("发文间隔过密")){
						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (dialog != null) {
									dialog.dismiss();
								}
								MyToast.toast("修改成功！");
								//finish();
							}
						});
					}else{
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (dialog != null) {
									dialog.dismiss();
								}
								MyToast.toast("修改成功！");
//								Intent intent=new Intent(getApplicationContext(),BoardDetailActivity.class);
//								
//								intent.putExtra("boardUrl", boardUrl);
//								startActivity(intent);
								
								finish();
							}
						});
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

	}

}

package com.oterman.njubbs.dialog;

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.oterman.njubbs.R;
import com.oterman.njubbs.utils.MyToast;
import com.oterman.njubbs.utils.SPutils;

/**
 * 按作者搜贴天数
 *
 */
public class SettingByAuthorDialog {

	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private EditText etContent;
	private TextView tvDesc;

	private Context context;
	private CheckBox cbCheck;
	
	public SettingByAuthorDialog(Context context) {
		this.context=context;
		builder = new AlertDialog.Builder(context);
		View view = View.inflate(context, R.layout.dialog_edit_tail, null);

		etContent = (EditText) view.findViewById(R.id.et_content);
		
		etContent.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		
		tvDesc=(TextView) view.findViewById(R.id.tv_desc);
		cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
		
		tvDesc.setText("快捷搜索时，按作者默认搜索999天以内的内容，且搜索的标题包含Re。");
		etContent.setHint("请输入天数");
		
		//读取保存的值
		String byauthor_day=SPutils.getFromSP("byauthor_day");
		
		if(byauthor_day!=null&&!TextUtils.isEmpty(byauthor_day)){
			etContent.setText(byauthor_day);
		}else{
			etContent.setText("999");
		}
		
		//初始化checkbox
		cbCheck.setVisibility(View.VISIBLE);
		
		String byauthor_re=SPutils.getFromSP("byauthor_re");
		if(byauthor_re!=null&&!TextUtils.isEmpty(byauthor_re)){
			if("yes".equals(byauthor_re)){//包含re
				cbCheck.setChecked(true);
			}else{
				cbCheck.setChecked(false);
			}
		}else{//没有设置  默认不包含
			cbCheck.setChecked(true);
		}
		
		builder.setView(view);

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				String str = etContent.getText().toString();
				try {
					if (TextUtils.isEmpty(str)||!TextUtils.isDigitsOnly(str)) {

						Field field = dialog.getClass().getSuperclass()
								.getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, false); // false -不能关闭
						
						MyToast.toast("需要输入一个整数哦");
						return;
					}else{
						Field field = dialog.getClass().getSuperclass()
								.getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, true); // true　关闭对话框
						// 处理添加好友
						handleSaveData();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		builder.setNegativeButton("取消", null);
		builder.setTitle("按作者搜索");
		dialog = builder.create();
	}
	
	private void handleSaveData() {
		//保存起来
		String tail = etContent.getText().toString().trim();
		SPutils.saveToSP("byauthor_day", tail);
		
		if(cbCheck.isChecked()){//选中 包含
			SPutils.saveToSP("byauthor_re", "yes");
		}else{
			SPutils.saveToSP("byauthor_re", "no");
		}
		MyToast.toast("保存成功");
	}


	public void show() {
		builder.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}


}

package com.oterman.njubbs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class SPutils {
	static SharedPreferences sp;
	static{
		sp=UiUtils.getContext().getSharedPreferences("bbs_config", Context.MODE_PRIVATE);
	}

	public static void saveToSP(String key,String value){
		
		Editor editor = sp.edit();
		
		editor.putString(key, value);
		
		editor.commit();
		
	}
	
	public static String getFromSP(String key){
		
		return sp.getString(key, "");
	}
	
	//"\n-\n"+"sent from 小百合\n";
	public static String getTail(){
		String tail=getFromSP("tail");
		//[1;32m发送自 我的小百合Android客户端 by PE-TL20℡[m
		if(!TextUtils.isEmpty(tail)){
			return "\n-\n"+"[1;35mSent From "+tail+"[m\n";
		}else{
			String model = android.os.Build.MODEL;
			return "\n-\n[1;35mSent From 南大小百合 by "+model+"[m\n";
		}
//		if(!TextUtils.isEmpty(tail)){
//			return "\n-\n"+"<font color='purple'>Sent From  "+tail+"</font>\n";
//		}else{
//			return "\n-\n<font color='purple'>Sent From 南大小百合</font>\n";
//		}
	}
	
	public static String getTailNoColor(){
		String tail=getFromSP("tail");
		if(!TextUtils.isEmpty(tail)){
			return "Sent From "+tail;
		}else{
			String model = android.os.Build.MODEL;
			LogUtil.d(model);
			return "Sent From 南大小百合  by "+model;
		}
	}
}

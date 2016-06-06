package com.oterman.njubbs.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.oterman.njubbs.BaseApplication;
import com.oterman.njubbs.dialog.WaitDialog;

import cz.msebera.android.httpclient.Header;

public class NetUtils {
	
	
	public static void uploadFile(Context context,final WaitDialog waitDialog,File file){
		  
		AsyncHttpClient ahc=new AsyncHttpClient();
		
		String url=Constants.getUploadUrl();
	//	ahc.addHeader("Content-Type", "multipart/form-data");
		
		String cookie=BaseApplication.getCookie();
		if(cookie==null){
			cookie=BaseApplication.autoLogin(context, true);
		}
		//njubbs_upload36649831.jpg
		ahc.addHeader("Cookie", cookie);
		RequestParams rp=new RequestParams();
		try {
			rp.put("up", file);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		rp.put("exp", "");
		rp.put("ptext", "text");
		rp.put("board", "Pictures");
		rp.setForceMultipartEntityContentType(true);
		
		ahc.post(url, rp,new AsyncHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				/*
				responseBody is empty. but should not be empty. the responseBody shouled be like this:
				
					<meta http-equiv='Refresh' content='0; url=bbsupload2?board=Pictures
					&file=10104&name=LilyDroid0605184406.jpg&exp=UploadByLilyDroid
					&ptext=
					'>
					
				but i got nothing! so confused. somebody help me.
				*/
				LogUtil.d("�����"+statusCode);
				waitDialog.dismiss();
				if(statusCode==200){
					try {
						String result=new String(responseBody,"gb2312");
						LogUtil.d("result:"+result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			public void onFailure(int statusCode,
					Header[] headers,
					byte[] responseBody, Throwable error) {
				waitDialog.dismiss();
				LogUtil.d("�����"+statusCode+ new String(responseBody).toString());
				error.printStackTrace();
			}

		});
	}
	
	
	
	
	
	
	public static void uploadFile2(final Context context,final WaitDialog dialog,final File file){
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {
			@Override
			public void run() {
				HttpClient httpClient=new DefaultHttpClient();
				try {
					String url=Constants.getUploadUrl();
					HttpPost httpPost=new HttpPost(url);
					
					FileBody fb=new FileBody(file,"image/jpeg");
					String cookie=BaseApplication.getCookie();
					if(cookie==null){
						cookie=BaseApplication.autoLogin(context, true);
					}
					
					httpPost.setHeader("User-Agent","SOHUWapRebot");
					httpPost.addHeader("Cookie", cookie);
					httpPost.addHeader("Content-Type", "multipart/form-data");
					MultipartEntity me=new MultipartEntity();
					
					StringBody sb1=new StringBody("");
					StringBody sb2=new StringBody("text",Charset.forName("GB2312"));
					StringBody sb3=new StringBody("Pictures",Charset.forName("GB2312"));
					
					me.addPart("up",fb);
					me.addPart("exp", sb1);
					me.addPart("ptext",sb2);
					me.addPart("board", sb3);
					
					
					httpPost.setEntity(me);
					HttpResponse response=httpClient.execute(httpPost);
					
					int statusCode = response.getStatusLine().getStatusCode();  
				
					 if(statusCode == HttpStatus.SC_OK){  
			                System.out.println("������������Ӧ.....");  
			                HttpEntity resEntity = response.getEntity();  
			                InputStream inputStream = resEntity.getContent();
			                
			                BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,"gbk"));
			                //nju_bbs160605153704.jpg
			                String line=null;
			                StringBuffer sb=new StringBuffer();
			                
			                while((line=br.readLine())!=null){
			                	sb.append(line);
			                	sb.append("\n");
			                }
			                String result=sb.toString();
			                
			                LogUtil.d("result2:"+result);
			                
			               /**
<meta http-equiv='Refresh' content='0; url=bbsupload2?board=
&file=19068&name=njubbskdsadjkfa.jpg&exp=Content-Transfer-Encoding: 8bit
&ptext=Content-Disposition: form-data; name="p'>
			                */
//			                String result=EntityUtils.toString(resEntity);
			                result=result.replaceAll("\n", "");
			                //��ȡ &file=19068&name=njubbskdsadjkfa.jpg
			                int start= result.indexOf("&file=");
			                int end=result.indexOf("&exp=");
			                result = result.substring(start, end);
			                
			                //bbsupload2?board=Pictures&file=2672&name=1.jpg&exp=&ptext=text HTTP/1.1
			                String url2="http://bbs.nju.edu.cn/bbsupload2?board=Pictures"+
			                		result+"&exp=&ptext=text";
			                LogUtil.d("url2:"+url2);
			                
			              handleUpload2(context,url2,dialog);
			                
			            }  
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		

	}

	protected static void handleUpload2(Context  context,String url2,final WaitDialog waitDialog) {
		AsyncHttpClient ahc=new SyncHttpClient();
		String cookie=BaseApplication.getCookie();
		if(cookie==null){
			cookie=BaseApplication.autoLogin(context, true);
		}
		
		ahc.addHeader("Cookie", cookie);
		ahc.get(url2,new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				//���������ִ�У����ǵõ���responsebodyû��ֵ������Ӧ����ֵ�á�
				
				LogUtil.d("�����"+statusCode);//Content-Encoding: gzip
				if(statusCode==200){
					try {
						String result=new String(responseBody,"gb2312");
						LogUtil.d("result:"+result);
						
						UiUtils.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								waitDialog.dismiss();
							}
						});
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
					byte[] responseBody, Throwable error) {
				LogUtil.d("�����"+statusCode+ new String(responseBody).toString());
				error.printStackTrace();
			}

		});
		
		
	}


	
}
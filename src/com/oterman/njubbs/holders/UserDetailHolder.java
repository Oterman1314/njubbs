package com.oterman.njubbs.holders;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.oterman.njubbs.R;
import com.oterman.njubbs.bean.UserInfo;
import com.oterman.njubbs.protocol.UserProtocol;
import com.oterman.njubbs.utils.Constants;
import com.oterman.njubbs.utils.LogUtil;
import com.oterman.njubbs.utils.ThreadManager;
import com.oterman.njubbs.utils.UiUtils;

public class UserDetailHolder {

	private TextView tvId;
	private TextView tvNickname;
	private TextView tvXingzuo;
	private TextView tvJingyan;
	private TextView tvBiaoxian;
	private TextView tvLife;
	private TextView tvTotalVisit;
	private TextView tvTotalPost;
	private TextView tvLastVisitTime;
	private TextView tvLastVisitIp;
	private TextView tvOnline;

	View rootView;

	public UserDetailHolder(int layoutId) {
		rootView = View.inflate(UiUtils.getContext(), layoutId, null);

		initViews();
	}

	private void initViews() {
		tvId = (TextView) rootView.findViewById(R.id.tv_user_detail_id);
		tvNickname = (TextView) rootView
				.findViewById(R.id.tv_user_detail_nickname);
		tvXingzuo = (TextView) rootView
				.findViewById(R.id.tv_user_detail_xingzuo);

		tvJingyan = (TextView) rootView
				.findViewById(R.id.tv_user_detail_jingyan);
		tvBiaoxian = (TextView) rootView
				.findViewById(R.id.tv_user_detail_biaoxian);
		tvLife = (TextView) rootView.findViewById(R.id.tv_user_detail_life);

		tvTotalVisit = (TextView) rootView
				.findViewById(R.id.tv_user_detail_totalvisit);
		tvTotalPost = (TextView) rootView
				.findViewById(R.id.tv_user_detail_totalpost);
		tvLastVisitTime = (TextView) rootView
				.findViewById(R.id.tv_user_detail_lastvisittime);

		tvLastVisitIp = (TextView) rootView
				.findViewById(R.id.tv_user_detail_lastvisitip);
		tvOnline = (TextView) rootView.findViewById(R.id.tv_user_detail_online);
	}

	public View getRootView() {
		return rootView;
	}

	public void updateStatus(final String userId) {
		// �¿��̣߳���������
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {
			private UserInfo info;

			@Override
			public void run() {

				UserProtocol protocol = new UserProtocol();
				info = protocol.getUserInfoFromServer(userId);

				// ����ip��ȡ��ַ
				if (info!=null&&info.lastVistiIP != null) {
					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.GET,
							Constants.getQueryIpUrl(info.lastVistiIP),
							new RequestCallBack<String>() {
								@Override
								public void onSuccess(
										ResponseInfo<String> responseInfo) {
									String result = responseInfo.result;
									if (result.contains("�й�")) {
										result = result.replaceAll("�й�", "")
												.trim();
									}
									LogUtil.d(info.lastVistiIP + ":" + result);

									tvLastVisitIp.setText(Html.fromHtml(UiUtils
											.getString(R.string.lastVistiIP)
											+ info.lastVistiIP
											+ "("
											+ result
											+ ")"));

								}

								@Override
								public void onFailure(HttpException error,
										String msg) {

								}
							});
				}

				// ���½���
				UiUtils.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (info != null) {
							String html = "<font color='purple' size='22px'>"
									+ info.id
									+ "</font><font size='16px' color='grey'>��ϸ��Ϣ</font>";

							// tvId.setText(info.id+"��ϸ��Ϣ");
							tvId.setText(Html.fromHtml(html));

							if (info.nickname != null) {
								tvNickname.setText(Html.fromHtml(UiUtils
										.getString(R.string.nickname)
										+ info.nickname));
							}
							if (info.xingzuo != null) {
								tvXingzuo.setText(Html.fromHtml(UiUtils
										.getString(R.string.xingzuo)
										+ info.xingzuo));
							}
							if (info.jingyan != null) {
								tvJingyan.setText(Html.fromHtml(UiUtils
										.getString(R.string.jingyan)
										+ info.jingyan));
							}
							if (info.biaoxian != null)
								tvBiaoxian.setText(Html.fromHtml(UiUtils
										.getString(R.string.biaoxian)
										+ info.biaoxian));

							if (info.life != null)
								tvLife.setText(Html.fromHtml(UiUtils
										.getString(R.string.life) + info.life));

							if (info.totalVisit != null)
								tvTotalVisit.setText(Html.fromHtml(UiUtils
										.getString(R.string.totalVisit)
										+ info.totalVisit + "��"));

							if (info.totalPub != null)
								tvTotalPost.setText(Html.fromHtml(UiUtils
										.getString(R.string.totalPub)
										+ info.totalPub + "ƪ"));

							if (info.lastVisitTime != null)
								tvLastVisitTime.setText(Html.fromHtml(UiUtils
										.getString(R.string.lastVisitTime)
										+ info.lastVisitTime));

							if (info.lastVistiIP != null)
								tvLastVisitIp.setText(Html.fromHtml(UiUtils
										.getString(R.string.lastVistiIP)
										+ info.lastVistiIP));

							if (info.isOnline) {
								tvOnline.setText(Html
										.fromHtml("<font color='grey'>�Ƿ����ߣ�</font>"
												+ "<font color='green'>��</font>"));
							} else {
								tvOnline.setText(Html
										.fromHtml("<font color='grey'>�Ƿ����ߣ�</font>"
												+ "<font color='red'>��</font>"));
							}
						}

					}
				});

			}

		});

	}

}
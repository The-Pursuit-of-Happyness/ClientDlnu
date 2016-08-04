package com.mess.order;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.UserMessageInfo;
import com.mess.ordermess.utils.GetAPPMessage;

public class MyWallet extends Activity {

	private SharedPreferences sp;
	private TextView order_wallet_tip, my_wallet_money, wallet_data,
			wallet_data1,icon_version;
	private LinearLayout my_wallet_lin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywallet);

		wallet_data1 = (TextView) this.findViewById(R.id.wallet_data1);
		wallet_data = (TextView) this.findViewById(R.id.wallet_data);
		my_wallet_money = (TextView) this.findViewById(R.id.my_wallet_money);
		order_wallet_tip = (TextView) this.findViewById(R.id.order_wallet_tip);
		icon_version = (TextView) this.findViewById(R.id.icon_version);
		my_wallet_lin = (LinearLayout) this.findViewById(R.id.my_wallet_lin);

		fillData();
	}

	private void fillData() {
		Bmob.initialize(this, Constants.BMOB_API_ID);

		UserMessageInfo bmobUser = BmobUser.getCurrentUser(MyWallet.this,
				UserMessageInfo.class);
		if (bmobUser == null) {
			my_wallet_lin.setVisibility(View.INVISIBLE);
			order_wallet_tip.setVisibility(View.VISIBLE);
		} else {
			my_wallet_lin.setVisibility(View.VISIBLE);
			sp = getSharedPreferences("config", MODE_PRIVATE);
			String userId = sp.getString("User_ID", null);
			my_wallet_money.setText("获取中...");
			wallet_data.setText("获取中...");
			wallet_data1.setText("获取中...");
			icon_version.setText("Version:"+GetAPPMessage.getVersion(this));
			getUserMessage(userId);
		}
	}

	public void getUserMessage(final String userName) {
		BmobQuery<UserMessageInfo> query = new BmobQuery<UserMessageInfo>();
		query.addWhereEqualTo("username", userName);
		query.findObjects(this, new FindListener<UserMessageInfo>() {
			@Override
			public void onSuccess(List<UserMessageInfo> object) {
				for (UserMessageInfo user : object) {
					if (user.getUsername().equals(userName)) {
						my_wallet_money.setText("xxxx" + ".00元");
						wallet_data.setText(user.getUsername());
						wallet_data1.setText("xxxx");
						break;
					}
				}

			}

			@Override
			public void onError(int code, String msg) {
				Toast.makeText(getApplicationContext(), "获取用户信息失败！", 0).show();
			}
		});
	}

}

package com.mess.ordermess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.mess.clientdlnu.R;
import com.mess.order.AboutUs;
import com.mess.order.AccomplishOrder;
import com.mess.order.MyMessage;
import com.mess.order.MyOrder;
import com.mess.order.MyWallet;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.UserMessageInfo;
import com.mess.publicmethod.Setting;

public class MyOrderActivity extends Activity {

	private SharedPreferences sp;
	private LinearLayout my_order_lin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order_mess_activity);

		TableRow tablerow = (TableRow) findViewById(R.id.tablerow);
		my_order_lin = (LinearLayout) this.findViewById(R.id.my_order_lin);
		
		my_order_lin.setBackgroundResource(R.drawable.navigationbg);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);

		/* initialize bmob java bean */
		Bmob.initialize(this, Constants.BMOB_API_ID);

		tablerow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserMessageInfo bmobUser = BmobUser.getCurrentUser(
						MyOrderActivity.this, UserMessageInfo.class);
				if (bmobUser == null) {
					logining();
					return;
				}
				Intent intent = new Intent(MyOrderActivity.this,
						MyMessage.class);
				startActivity(intent);
			}
		});

		final ListView listview = (ListView) findViewById(R.id.listview1);
		int imageid[] = new int[] { R.drawable.accomplishorder,
				R.drawable.myorder, R.drawable.mywallet,
				R.drawable.setting, R.drawable.aboutus };
		String itemtitle[] = getResources().getStringArray(R.array.itemtitle);

		List<Map<String, Object>> listitems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imageid.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", imageid[i]);
			map.put("title", itemtitle[i]);
			listitems.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, listitems,
				R.layout.activity_mymainitem,
				new String[] { "title", "image" }, new int[] { R.id.title,
						R.id.image });
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case 0:
					intent = new Intent(MyOrderActivity.this, MyOrder.class);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(MyOrderActivity.this,
							AccomplishOrder.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(MyOrderActivity.this, MyWallet.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(MyOrderActivity.this,Setting.class);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(MyOrderActivity.this, AboutUs.class);
					startActivity(intent);
					break;
					default:
						break;
				}
			}
		});
	}

	public void logining() {
		AlertDialog.Builder dialog = new Builder(MyOrderActivity.this);
		dialog.setTitle("询问");
		dialog.setMessage("你还没有登录，是否现在登录！");
		dialog.setCancelable(false);
		dialog.setPositiveButton("现在登录", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dia, int which) {
				Intent intent = new Intent(getApplicationContext(),
						UserLogin.class);
				startActivity(intent);
			}
		});
		dialog.setNeutralButton("狠心拒绝", null);
		dialog.show();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
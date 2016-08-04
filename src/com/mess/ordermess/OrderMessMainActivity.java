package com.mess.ordermess;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;

import com.mess.clientdlnu.R;
import com.mess.order.MyMessage;
import com.mess.ordermess.constant.Constants;

public class OrderMessMainActivity extends TabActivity {

	private TabHost tabHost;

	private SharedPreferences sp;
	private boolean isInit = true;
	private long exitTime=0;// 退出时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.tabhosts);
		tabHost = this.getTabHost();

		sp = getSharedPreferences("config", MODE_PRIVATE);

		TabWidget tabWidget = this.getTabWidget();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.BOTTOM);

		adddTabHostMenu();
		addChangeListener();
	}

	/**
	 * 为Tabhost添加菜单选项卡
	 */
	private void adddTabHostMenu() {
		// Home Tab
		Intent intent0 = new Intent(this, NewsActivity.class);
		addMyStyleTab(Constants.HOME, R.drawable.new_activity_press, intent0);

		// Order Mess Tab
		Intent intent1 = new Intent(this, OrderMessActivity.class);
		addMyStyleTab(Constants.ORDER_MESS, R.drawable.shop, intent1);

		// Order Mess Tab
		Intent intent2 = new Intent(this, ClassifyNavigationActivity.class);
		addMyStyleTab(Constants.ORDER_CLASSIFY, R.drawable.navigation, intent2);

		// My Order Mess Tab
		Intent intent3 = new Intent(this, MyOrderActivity.class);
		addMyStyleTab(Constants.MY_ORDER_MESS, R.drawable.my_order, intent3);
	}

	/**
	 * 自定义tabhost选项卡风格
	 * 
	 * @param label
	 *            设定提示的文本
	 * @param drawableId
	 *            要显示的图标
	 * @param intent
	 *            要显示的界面
	 */
	private void addMyStyleTab(String label, int drawableId, Intent intent) {
		TabHost.TabSpec spec = tabHost.newTabSpec(label);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tabhost_item_style, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.tv_tab_tip);
		title.setText(label);
		if (isInit) {
			title.setTextColor(Color.rgb(238, 156, 12));
			isInit = false;
		}
		ImageView icon = (ImageView) tabIndicator
				.findViewById(R.id.img_tab_icon);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

	/**
	 * 当选项卡改变时，改变图标
	 */
	public void addChangeListener() {
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				View view;
				ImageView iv;
				TextView tv;

				view = tabHost.getTabWidget().getChildAt(0);
				iv = (ImageView) view.findViewById(R.id.img_tab_icon);// 获取控件imageView
				tv = (TextView) view.findViewById(R.id.tv_tab_tip);
				if (Constants.HOME.equals(tabId)) {
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.new_activity_press));// 改变我们需要的图标
					tv.setTextColor(Color.rgb(238, 156, 12));
				} else {
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.new_activity));// 改变我们需要的图标
					tv.setTextColor(Color.rgb(140, 140, 140));
				}

				view = tabHost.getTabWidget().getChildAt(1);
				iv = (ImageView) view.findViewById(R.id.img_tab_icon);
				tv = (TextView) view.findViewById(R.id.tv_tab_tip);
				if (Constants.ORDER_MESS.equals(tabId)) {
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.shop_press));
					tv.setTextColor(Color.rgb(137, 195, 31));
				} else {
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.shop));
					tv.setTextColor(Color.rgb(140, 140, 140));
				}

				view = tabHost.getTabWidget().getChildAt(2);
				iv = (ImageView) view.findViewById(R.id.img_tab_icon);
				tv = (TextView) view.findViewById(R.id.tv_tab_tip);
				if (Constants.ORDER_CLASSIFY.equals(tabId)) {
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.navigation_press));
					tv.setTextColor(Color.rgb(248, 52, 52));
				} else {
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.navigation));
					tv.setTextColor(Color.rgb(140, 140, 140));
				}

				view = tabHost.getTabWidget().getChildAt(3);
				iv = (ImageView) view.findViewById(R.id.img_tab_icon);
				tv = (TextView) view.findViewById(R.id.tv_tab_tip);
				if (Constants.MY_ORDER_MESS.equals(tabId)) {
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.my_order_press));
					tv.setTextColor(Color.rgb(51, 181, 229));
				} else {
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.my_order));
					tv.setTextColor(Color.rgb(140, 140, 140));
				}
			}

		});
	}
	
	@Override
	public void onBackPressed() {
		
	}
	
	@Override
	protected void onDestroy() {
		boolean isAutoLogin = sp.getBoolean("is_auto_login", true);
		if(!isAutoLogin){
			BmobUser.logOut(OrderMessMainActivity.this);
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
		// TODO 按两次返回键退出应用程序
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 判断间隔时间 大于2秒就退出应用
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				// 应用名
				String applicationName = getResources().getString(
						R.string.app_name);
				String msg = "再按一次返回键退出" + applicationName;
				Toast.makeText(OrderMessMainActivity.this, msg, 0).show();
				exitTime = System.currentTimeMillis();
			} else {
//				moveTaskToBack(false); 
//				return false;
			}
			
		}
		return false;
	}
}

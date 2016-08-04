package com.mess.publicmethod;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.NewsActivityAdapter;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.MessMenu;
import com.mess.ordermess.dao.NavigationContent;
import com.mess.ordermess.net.NetUntils;
import com.mess.ordermess.ui.TagsGridView;

public class PublicShowMessListActivity extends Activity {
	
	private LinearLayout shop_mess_Loading;
	private GridView lv_shop_mess;
	private String mess_Type;
	private NewsActivityAdapter adapter;
	private List<MessMenu> infos;
	private TextView tv_nav_title;

	private int maxTemp = 15;
	private int offset = 0;
	private int maxNumber = maxTemp;
	protected int firstX;
	protected int firstY;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_navigation_mess_activity);

		this.lv_shop_mess = (GridView) this.findViewById(R.id.lv_shop_mess);
		this.shop_mess_Loading = (LinearLayout) this
				.findViewById(R.id.shop_mess_Loading);
		tv_nav_title = (TextView) this.findViewById(R.id.tv_nav_title);

		receiveData();
		int type = Integer.parseInt(mess_Type);
		tv_nav_title.setText(NavigationContent.menuNames[type]);

		infos = new ArrayList<MessMenu>();
		
		fillShopData();
		addListener();
	}

	/**
	 * 返回上一个页面
	 */
	public void navigationBack(View view) {
		this.finish();
	}

	/**
	 * 添加listView的监听事件
	 */
	private void addListener() {
		lv_shop_mess.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getApplicationContext(),
						PublicShowMessMsgActivity.class);
				intent.putExtra("com.mess.ordermess.dao.MessMenu",
						(Parcelable) infos.get(position));
				startActivity(intent);
			}

		});

		lv_shop_mess.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
					int lastPos = lv_shop_mess.getLastVisiblePosition();
					if (NetUntils
							.isNetworkAvailable(PublicShowMessListActivity.this)) {
						if (lastPos == (infos.size() - 1)) {
							offset++;
							fillShopData();
							shop_mess_Loading.setVisibility(View.VISIBLE);
						}
					} else {
						Toast.makeText(PublicShowMessListActivity.this,
								"网络连接失败！", 0).show();
					}
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 惯性滑动状态
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 手指触摸状态
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}

		});
		
		lv_shop_mess.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean result = false;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					firstX = (int) event.getX();
					firstY = (int) event.getY();
					break;
				case MotionEvent.ACTION_UP:
					int distanceX = (int) (firstX - event.getX());
					int distanceY = (int) Math.abs(firstY - event.getY());
					if (distanceY > Math.abs(distanceX))
						return false;
					if (distanceX < -20) {
						PublicShowMessListActivity.this.finish();
						result = true;
					} else {
						result = false;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				}
				return result;
			}

		});
	}

	/**
	 * 得到OrderMessActivity传来的数据
	 */
	public void receiveData() {
		Intent intent = this.getIntent();
		mess_Type = intent.getStringExtra("mess_Type");
	}

	/**
	 * 填充数据
	 */
	private void fillShopData() {
		shop_mess_Loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				getMessMessage(offset, maxNumber, mess_Type);
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void getMessMessage(int offset, int maxNumber, String mess_type) {
		final BmobQuery<MessMenu> query = new BmobQuery<MessMenu>();
		query.addWhereEqualTo("mess_Type", mess_Type);
		query.setLimit(maxNumber);
		query.setSkip(maxNumber * offset);
		query.findObjects(PublicShowMessListActivity.this,
			new FindListener<MessMenu>() {

				@Override
				public void onError(int arg0, String arg1) {
					shop_mess_Loading.setVisibility(View.INVISIBLE);	
					Toast.makeText(PublicShowMessListActivity.this,
							"获取数据失败!", 0).show();
				}

				@Override
				public void onSuccess(List<MessMenu> messMenu) {
					if (messMenu.size() > 0) {
						if (infos == null) {
							infos = messMenu;
						} else {
							infos.addAll(messMenu);
						}
						
						if (adapter == null) {
							adapter = new NewsActivityAdapter(
									PublicShowMessListActivity.this,
									Constants.GRIDVIEW_CLASSIFY_ADAPTER,infos);
							lv_shop_mess.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
					} else {
						Toast.makeText(getApplicationContext(), "没有更多数据了!",0).show();
					}
					shop_mess_Loading.setVisibility(View.INVISIBLE);				
				}
			}
		);
	}
}

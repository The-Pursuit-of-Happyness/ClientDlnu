package com.mess.ordermess;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.GalleryAdapter;
import com.mess.ordermess.adapter.LoadImage;
import com.mess.ordermess.adapter.NewsActivityAdapter;
import com.mess.ordermess.adapter.LoadImage.GetImageBitmap;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.BitmapResource;
import com.mess.ordermess.dao.MessMenu;
import com.mess.ordermess.dao.ShopActivityInfo;
import com.mess.ordermess.dao.UserMessageInfo;
import com.mess.ordermess.ui.TagsGridView;
import com.mess.ordermess.utils.DensityUtils;
import com.mess.publicmethod.PublicShowMessMsgActivity;
import com.mess.publicmethod.ShowShopActivity;

public class NewsActivity extends Activity {

	public int index = 0;
	private ImageView news_user_login;
	private TextView news_data_loading;
	private MyBoradcastReceiver myReceiver;
	private SharedPreferences sp;
	private Gallery gallery;
	private TagsGridView youlovelist;
	private List<ShopActivityInfo> shopActivity;
	private List<MessMenu> messInfos;
	private GalleryAdapter adapter;
	private NewsActivityAdapter messAdapter;
	private Timer timer;
	private List<BitmapResource> imageResource;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_mess_main);
		news_user_login = (ImageView) this.findViewById(R.id.news_user_login);
		youlovelist = (TagsGridView) this.findViewById(R.id.youlovelist);
		news_data_loading = (TextView) this.findViewById(R.id.news_data_loading);
		gallery = (Gallery) findViewById(R.id.gallery);


		/* initialize bmob java bean */
		Bmob.initialize(this, Constants.BMOB_API_ID);

		initialize();
	}

	private void initialize() {
		loginState();
		fillData();

		timer = new Timer();
		timer.schedule(task, 5000, 5000);

		myReceiver = new MyBoradcastReceiver();
		registerReceiver(myReceiver, new IntentFilter(
				"com.dlnu.ordermess.UserLogin"));
		
	}

	/**
	 * 加载广告数据
	 */
	private void fillData() {
		news_data_loading.setText("数据加载中...");
		imageResource = new ArrayList<BitmapResource>();
		messInfos = new ArrayList<MessMenu>();
		new Thread() {
			public void run() {
				getActivityData();
				getMessData();
			}
		}.start();

		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < shopActivity.size(); i++) {
					
					if(DensityUtils.judgeShopActivity(shopActivity.get(i).getImage_Url(), 
							imageResource.get(position).getBitmapName())){
						Intent intent = new Intent(NewsActivity.this,ShowShopActivity.class);
						intent.putExtra("com.mess.publicmethod.ShowShopActivity",
								(Parcelable) shopActivity.get(i));
						startActivity(intent);
						break;
					}					
				}
			}
		});
		
		youlovelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						PublicShowMessMsgActivity.class);
				intent.putExtra("com.mess.ordermess.dao.MessMenu",
						(Parcelable) messInfos.get(position));
				startActivity(intent);
			}
		});
	}

	protected void getMessData() {
		final BmobQuery<MessMenu> query = new BmobQuery<MessMenu>();
		query.setLimit(Constants.SQL_COUNT);
		query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		query.findObjects(NewsActivity.this, new FindListener<MessMenu>() {
			@Override
			public void onSuccess(List<MessMenu> object) {
				if (object.size() > 0) {
					if (messInfos == null) {
						messInfos = object;
					} else {
						messInfos.addAll(object);
					}
				}
				Message handlerMsg = new Message();
				handlerMsg.what = Constants.GAIN_MESS_MSG_SUCCESS;
				handler.sendMessage(handlerMsg);
			}

			@Override
			public void onError(int code, String msg) {
			}
		});
	}

	protected void getActivityData() {
		BmobQuery<ShopActivityInfo> query = new BmobQuery<ShopActivityInfo>();
		query.findObjects(this, new FindListener<ShopActivityInfo>() {

			@Override
			public void onSuccess(List<ShopActivityInfo> object) {
				shopActivity = object;
				Message handlerMsg = new Message();
				handlerMsg.what = Constants.SHOP_ACTIVITY_DATA;
				handler.sendMessage(handlerMsg);
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	/**
	 * 定时器，实现自动播放
	 */
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			Message message = new Message();
			message.what = Constants.GALLERY_REFRESH;
			index = gallery.getSelectedItemPosition();
			index++;
			handler.sendMessage(message);
		}
	};

	/**
	 * 对画廊组件数据自动刷新
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.GALLERY_REFRESH:
				if (imageResource.size() > 0) {
					count++;
					Animation animation = new AlphaAnimation(0.2f, 1f);
					animation.setDuration(1000); 
					gallery.setAnimation(animation);
					gallery.setSelection(index % imageResource.size());
					if(count <= Constants.SQL_COUNT / 5 && messAdapter != null){
						messAdapter.notifyDataSetChanged();
					}
				}
				break;
			case Constants.SHOP_ACTIVITY_DATA:
				for (int i = 0; i < shopActivity.size(); i++) {
					downLoadImage(i);
				}
				break;
			case Constants.DOWNLOAD_ACTIVITY_IMAGE:
				if (null == adapter) {
					adapter = new GalleryAdapter(NewsActivity.this,imageResource);
					gallery.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				break;
			case Constants.GAIN_MESS_MSG_SUCCESS:
				if (null == messAdapter) {
					messAdapter = new NewsActivityAdapter(NewsActivity.this,
							Constants.GRIDVIEW_NEWS_ADAPTER,messInfos);
					youlovelist.setAdapter(messAdapter);
				} else {
					messAdapter.notifyDataSetChanged();
				}
				news_data_loading.setText("已经到底啦!");
				break;
			default:
				break;
			}
		}
	};

	private class MyBoradcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			loginState();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(myReceiver);
		myReceiver = null;
		timer = null;
	}

	protected void downLoadImage(final int i) {
		LoadImage.LoadImageFile(NewsActivity.this, Constants.LOADING_BIG_IMAGE,
				shopActivity.get(i).getImage_Url(), new GetImageBitmap() {

					@Override
					public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
						BitmapResource res = new BitmapResource();
						res.setBitmapName(cacheUrl);
						res.setBitmap(bitmap);
						imageResource.add(res);
						Message msg = new Message();
						msg.what = Constants.DOWNLOAD_ACTIVITY_IMAGE;
						handler.sendMessage(msg);
					}

				});
	}

	private void loginState() {
		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		if (bmobUser != null) {
			news_user_login.setBackgroundResource(R.drawable.user_loging);
		} else {
			news_user_login.setBackgroundResource(R.drawable.userlogo);
		}
	}

	/**
	 * 用户登录函数
	 * 
	 * @param view
	 */
	public void userLogonActivity(View view) {
		UserMessageInfo bmobUser = BmobUser.getCurrentUser(this,
				UserMessageInfo.class);
		if (bmobUser != null) {
			Toast.makeText(getApplicationContext(), "你已经登录过了！", 1).show();
		} else {
			Intent intent = new Intent(getApplicationContext(), UserLogin.class);
			startActivity(intent);
		}
	}
}

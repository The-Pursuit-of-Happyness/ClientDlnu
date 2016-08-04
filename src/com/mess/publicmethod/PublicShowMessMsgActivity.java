package com.mess.publicmethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.mess.clientdlnu.R;
import com.mess.ordermess.UserLogin;
import com.mess.ordermess.adapter.LoadImage;
import com.mess.ordermess.adapter.LoadImage.GetImageBitmap;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.MessMenu;
import com.mess.ordermess.dao.UserMessageInfo;

public class PublicShowMessMsgActivity extends Activity {

	private MessMenu receiveData;
	private TextView show_mess_name, show_mess_price, show_mess_sale,
			show_mess_describe, show_mess_feature, show_mess_effect;
	private RatingBar show_mess_star;
	private ImageView show_mess_img;
	private Button show_mess_Btn, show_mess_back;
	private SharedPreferences sp;
	private ScrollView show_mess_message_Lin;
	private WindowManager windowManager;
	
	private float screenWidth;
	private float screenHeight;

	private int firstX;
	private int firstY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_mess_message_public);

		this.show_mess_name = (TextView) this.findViewById(R.id.show_mess_name);
		this.show_mess_price = (TextView) this
				.findViewById(R.id.show_mess_price);
		this.show_mess_sale = (TextView) this.findViewById(R.id.show_mess_sale);
		this.show_mess_star = (RatingBar) this
				.findViewById(R.id.show_mess_star);
		this.show_mess_img = (ImageView) this.findViewById(R.id.show_mess_img);
		this.show_mess_describe = (TextView) this
				.findViewById(R.id.show_mess_describe);
		this.show_mess_feature = (TextView) this
				.findViewById(R.id.show_mess_feature);
		this.show_mess_effect = (TextView) this
				.findViewById(R.id.show_mess_effect);
		this.show_mess_Btn = (Button) this.findViewById(R.id.show_mess_Btn);
		this.show_mess_back = (Button) this.findViewById(R.id.show_mess_back);
		this.show_mess_message_Lin = (ScrollView) this
				.findViewById(R.id.show_mess_message_Lin);
		
		windowManager = (WindowManager) getSystemService(this.WINDOW_SERVICE);
		this.screenWidth = windowManager.getDefaultDisplay().getWidth();
		this.screenHeight = windowManager.getDefaultDisplay().getHeight();

		/* initialize bmob java bean */
		Bmob.initialize(this, Constants.BMOB_API_ID);

		gainData();
		fillData();
		addListener();
	}

	/**
	 * 得到数据
	 */
	public void gainData() {
		Intent intent = getIntent();
		receiveData = intent
				.getParcelableExtra("com.mess.ordermess.dao.MessMenu");
	}

	/**
	 * 填充数据
	 */
	public void fillData() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		String messName = receiveData.getMess_Name();
		this.show_mess_name.setText(messName);
		this.show_mess_price.setText("商品价格:" + receiveData.getMess_Price()
				+ "元");
		this.show_mess_sale.setText("月销售量:" + receiveData.getSales_Volume());
		int progress = receiveData.getMess_Start();
		this.show_mess_star.setProgress(progress);

		this.show_mess_img.setBackgroundResource(R.drawable.loading);

		LoadImage.LoadImageFile(PublicShowMessMsgActivity.this,
				Constants.LOADING_SAMLL_IMAGE, receiveData.getMess_Graph(),
				new GetImageBitmap() {
					@Override
					public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
						show_mess_img.setBackgroundColor(Color.WHITE);
						show_mess_img.setImageBitmap(bitmap);
					}
				});

		this.show_mess_describe.setText(receiveData.getMess_Describe());
		this.show_mess_feature.setText(receiveData.getMess_Feature());
		this.show_mess_effect.setText(receiveData.getMess_Effect());
	}

	/**
	 * 事件监听
	 */
	public void addListener() {
		show_mess_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new Builder(
						PublicShowMessMsgActivity.this);
				View view = View.inflate(PublicShowMessMsgActivity.this,
						R.layout.show_mess_image, null);
				final ImageView iv_img = (ImageView) view
						.findViewById(R.id.show_mess_image);
				final LinearLayout order_mess_Loading = (LinearLayout) view.findViewById(R.id.order_mess_Loading);
				order_mess_Loading.setVisibility(View.VISIBLE);				
				LoadImage.LoadImageFile(PublicShowMessMsgActivity.this,
						Constants.LOADING_BIG_IMAGE, receiveData.getMess_Graph(),
						new GetImageBitmap() {
							@Override
							public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
								order_mess_Loading.setVisibility(View.INVISIBLE);
								iv_img.setBackgroundColor(Color.WHITE);
								iv_img.setImageBitmap(bitmap);
										
							}
						});
				
				builder.setView(view);
				AlertDialog dialog = builder.show();
				WindowManager.LayoutParams params = dialog.getWindow()
						.getAttributes();
				params.width = (int) (screenWidth * Constants.DIALOG_SCREEN_WIDTH_SCALE);
				params.height = (int) (screenHeight * Constants.DIALOG_SCREEN_HEIGHT_SCALE);
				dialog.getWindow().setAttributes(params);
				dialog.setView(view, 0, 0, 0, 0);
			}
		});

		show_mess_back.setOnClickListener(listener);
		show_mess_Btn.setOnClickListener(listener);

		this.show_mess_message_Lin.setOnTouchListener(new OnTouchListener() {

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
						PublicShowMessMsgActivity.this.finish();
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

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			if (btn == show_mess_Btn) {
				UserMessageInfo bmobUser = BmobUser.getCurrentUser(
						PublicShowMessMsgActivity.this, UserMessageInfo.class);
				if (bmobUser == null) {
					logining();
					return;
				}
				Intent intent = new Intent(getApplicationContext(),
						PublicByOrder.class);
				intent.putExtra("com.dlnu.ordermess.dao.MessMenu",
						(Parcelable) receiveData);
				startActivity(intent);
			} else if (show_mess_back == btn) {
				PublicShowMessMsgActivity.this.finish();
			}
		}
	};

	public void logining() {
		AlertDialog.Builder dialog = new Builder(PublicShowMessMsgActivity.this);
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

	/**
	 * 点击查看用户评论
	 */
	public void clickEnterShop(View view) {
		Intent intent = new Intent(this, DisplayUserComment.class);
		intent.putExtra("com.dlnu.ordermess.DisplayUserComment",
				receiveData.getMess_Id());
		startActivity(intent);
	}

}

package com.mess.publicmethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mess.clientdlnu.R;
import com.mess.ordermess.UserLogin;
import com.mess.ordermess.dao.MessMenu;

public class PublicMessMsgActivity extends Activity {

	private MessMenu receiveData;
	private TextView show_mess_name, show_mess_price, show_mess_sale,
			show_mess_describe, show_mess_feature, show_mess_effect;
	private RatingBar show_mess_star;
	private ImageView show_mess_img;
	private Button show_mess_Btn, show_mess_back;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_mess_shop_public);

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

		sp = getSharedPreferences("config", MODE_PRIVATE);
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
				.getParcelableExtra("com.dlnu.ordermess.dao.OrderMessMsgInfo");
	}

	/**
	 * 填充数据
	 */
	public void fillData() {
		String messName = receiveData.getMess_Name();
		this.show_mess_name.setText(messName);
		this.show_mess_price
				.setText("商品价格:" + receiveData.getMess_Price() + "元");
		this.show_mess_sale.setText("月销售量:" + receiveData.getSales_Volume());
		int progress = receiveData.getMess_Start();
		this.show_mess_star.setProgress(progress);
		
		////////////////////////////////////////////////////////////
		
		this.show_mess_img
					.setBackgroundResource(R.drawable.loading);
		
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
						PublicMessMsgActivity.this);
				View view = View.inflate(PublicMessMsgActivity.this,
						R.layout.show_mess_image, null);
				ImageView iv_img = (ImageView) view
						.findViewById(R.id.show_mess_image);
				//////////////////////////////////////////////////////////////////
				iv_img.setImageResource(R.drawable.loading);
				builder.setView(view);
				builder.show();
			}
		});

		show_mess_back.setOnClickListener(listener);
		show_mess_Btn.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			if (btn == show_mess_Btn) {
				boolean isLogin = sp.getBoolean("isLogin", false);
				if (!isLogin) {
					logining();
					return;
				}
				Intent intent = new Intent(getApplicationContext(),
						PublicByOrder.class);
				intent.putExtra("com.dlnu.ordermess.dao.OrderMessMsgInfo",
						(Parcelable)receiveData);
				startActivity(intent);
			} else if (show_mess_back == btn) {
				PublicMessMsgActivity.this.finish();
			}
		}
	};

	public void logining() {
		AlertDialog.Builder dialog = new Builder(PublicMessMsgActivity.this);
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
}

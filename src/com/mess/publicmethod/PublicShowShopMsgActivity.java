package com.mess.publicmethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.LoadImage;
import com.mess.ordermess.adapter.LoadImage.GetImageBitmap;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.ShopMsgInfo;

public class PublicShowShopMsgActivity extends Activity {

	private TextView show_shop_title,show_shop_name,show_shop_visit,show_shop_type,
						show_shop_call,show_shop_address,show_shop_describe,
						show_shop_server_area,show_mess_server_purpose;
	private ImageView show_shop_img;
	private ShopMsgInfo receiveShop;
	private RatingBar show_shop_star;
	private RelativeLayout show_shop_relCall;
	private ScrollView show_shop_message_scr;
	
	private int firstX;
	private int firstY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_shop_message_public);
		
		this.show_shop_name = (TextView) this.findViewById(R.id.show_shop_name);
		this.show_shop_title = (TextView) this.findViewById(R.id.show_shop_title);
		this.show_shop_visit = (TextView) this.findViewById(R.id.show_shop_visit);
		this.show_shop_img = (ImageView) this.findViewById(R.id.show_shop_img);
		this.show_shop_type = (TextView) this.findViewById(R.id.show_shop_type);
		this.show_shop_star = (RatingBar) this.findViewById(R.id.show_shop_star);
		this.show_shop_call = (TextView) this.findViewById(R.id.show_shop_call);
		this.show_shop_address = (TextView) this.findViewById(R.id.show_shop_address);
		this.show_shop_relCall = (RelativeLayout) this.findViewById(R.id.show_shop_relCall);
		this.show_mess_server_purpose = (TextView) this.findViewById(R.id.show_mess_server_purpose);
		this.show_shop_describe = (TextView) this.findViewById(R.id.show_shop_describe);
		this.show_shop_server_area = (TextView) this.findViewById(R.id.show_shop_server_area);
		this.show_shop_message_scr = (ScrollView) this.findViewById(R.id.show_shop_message_scr);
		
		receiveData();
		fillData();
		addListener();
	}
	
	/**
	 * 得到OrderMessActivity传来的数据
	 */
	public void receiveData(){
		Intent intent = this.getIntent();
		receiveShop = intent.getParcelableExtra("com.mess.ordermess.dao.ShopMsgInfo");
	}
	
	/**
	 * 填充数据
	 */
	public void fillData(){
		this.show_shop_title.setText("欢迎光临"+receiveShop.getShop_Name());
		this.show_shop_name.setText(receiveShop.getShop_Name());
		this.show_shop_visit.setText("历史访问量:"+receiveShop.getShop_visit());
		if(receiveShop.getIsPopular()){
			this.show_shop_type.setText("店铺类型:大众");
		}else{
			this.show_shop_type.setText("店铺类型:清真");
		}
		this.show_shop_star.setProgress(receiveShop.getShop_Star());
		
		this.show_shop_img.setImageResource(R.drawable.loading);
		LoadImage.LoadImageFile(PublicShowShopMsgActivity.this, Constants.LOADING_SAMLL_IMAGE, 
				receiveShop.getLogoUrl(),new GetImageBitmap(){

					@Override
					public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
						show_shop_img.setBackgroundColor(Color.WHITE);
						show_shop_img.setImageBitmap(bitmap);
					}
			
		});
		
		this.show_shop_call.setText(receiveShop.getShop_Call());
		this.show_shop_address.setText(receiveShop.getShop_Address());
		this.show_shop_describe.setText(receiveShop.getShop_Describe());
		this.show_shop_server_area.setText(receiveShop.getServer_Area());
		this.show_mess_server_purpose.setText(receiveShop.getServer_Purpose());
	}
	
	/**
	 * 事件监听
	 */
	public void addListener(){
		show_shop_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PublicShowShopMsgActivity.this.finish();
			}
		});
		show_shop_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(PublicShowShopMsgActivity.this);
				View view = View.inflate(PublicShowShopMsgActivity.this, R.layout.show_mess_image, null);
				final ImageView iv_img = (ImageView) view.findViewById(R.id.show_mess_image);
				iv_img.setImageResource(R.drawable.loading);
				LoadImage.LoadImageFile(PublicShowShopMsgActivity.this, Constants.LOADING_BIG_IMAGE, 
						receiveShop.getLogoUrl(),new GetImageBitmap(){

							@Override
							public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
								iv_img.setBackgroundColor(Color.WHITE);
								iv_img.setImageBitmap(bitmap);
							}
					
				});
				builder.setView(view);
				AlertDialog dialog = builder.show();
				WindowManager.LayoutParams params = dialog.getWindow()
						.getAttributes();
				params.width = 820;
				params.height = 600;
				dialog.getWindow().setAttributes(params);
				dialog.setView(view, 0, 0, 0, 0);
			}
		});
		
		this.show_shop_relCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(PublicShowShopMsgActivity.this);
				builder.setCancelable(false);
				builder.setTitle("询问");
				builder.setMessage("你确定要拨打吗？");
				builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:"+receiveShop.getShop_Call()));
						PublicShowShopMsgActivity.this.startActivity(intent);
					}
					
				});
				builder.setNegativeButton("取消", null);
				builder.show();				
			}
		});
		
		this.show_shop_message_scr.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean result = false;
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					firstX = (int)event.getX();
					firstY = (int) event.getY();
					break;
				case MotionEvent.ACTION_UP:
					int distanceX = (int)(firstX - event.getX());
					int distanceY = (int) Math.abs(firstY - event.getY());
					if(distanceY > Math.abs(distanceX))
						return false;
					if(distanceX < -10){
						System.out.println("OnTouchEvent TRUE");
						PublicShowShopMsgActivity.this.finish();
						result = true;
					}else{
						System.out.println("OnTouchEvent FALSE");
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
	 * 进入菜谱页面
	 */
	public void clickEnterMess(View view){
		Intent intent = new Intent(PublicShowShopMsgActivity.this,PublicShowShopMessListActivity.class);
		intent.putExtra("com.dlnu.ordermess.dao.ShopMsgInfo", receiveShop.getShop_Id());
		startActivity(intent);
	}
}

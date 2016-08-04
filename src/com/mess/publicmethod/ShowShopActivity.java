package com.mess.publicmethod;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.LoadImage;
import com.mess.ordermess.adapter.LoadImage.GetImageBitmap;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.ShopActivityInfo;

public class ShowShopActivity extends Activity {
	
	private ImageView show_shop_activity;
	private TextView tv_activity,tv_activity_content;
	private ShopActivityInfo shopActivity;
	private WindowManager winManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_shop_acitvity);
		
		show_shop_activity = (ImageView) this.findViewById(R.id.show_shop_activity);
		tv_activity = (TextView) this.findViewById(R.id.tv_activity);
		tv_activity_content = (TextView) this.findViewById(R.id.tv_activity_content);
		
		winManager = (WindowManager) this.getSystemService(this.WINDOW_SERVICE);
		
		getData(); 
		fillData();
	}

	private void fillData() {
		LoadImage.LoadImageFile(this, Constants.LOADING_BIG_IMAGE, shopActivity.getImage_Url(), 
				new GetImageBitmap() {
			@Override
			public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
				Matrix matrix = new Matrix();
				// º∆À„Àı∑≈±»¿˝
				float screenWidth = winManager.getDefaultDisplay().getWidth();
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				float sx = screenWidth / width;
				matrix.postScale(sx, sx);
				Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				show_shop_activity.setImageBitmap(newbmp);
			}
		});
		tv_activity.setText(shopActivity.getActivity_Name());
		tv_activity_content.setText(shopActivity.getActivity_Content());
	}

	private void getData() {
		Intent intent = this.getIntent();
		shopActivity = intent.getParcelableExtra("com.mess.publicmethod.ShowShopActivity");
	}
	
	public void navigationBack(View view){
		this.finish();
	}
}

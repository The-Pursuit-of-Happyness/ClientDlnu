package com.mess.ordermess.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.mess.ordermess.dao.BitmapResource;

public class GalleryAdapter extends BaseAdapter {
	
	private Activity context;
	private List<BitmapResource> imageResource;
	private WindowManager winManager;
	
	public GalleryAdapter(Activity context,List<BitmapResource> imageResource){
		this.context = context;
		this.imageResource = imageResource;
		winManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageResource.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageView imageview;
		if (convertView == null) {
			imageview = new ImageView(context);
			imageview.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		} else {
			imageview = (ImageView) convertView;
		}

		Matrix matrix = new Matrix();
		// º∆À„Àı∑≈±»¿˝
		float screenWidth = winManager.getDefaultDisplay().getWidth();
		Bitmap bitmap = imageResource.get(position).getBitmap();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float sx = screenWidth / width;
		matrix.postScale(sx, sx);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		imageview.setImageBitmap(newbmp);
		return imageview;
	}
}

package com.mess.ordermess.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mess.clientdlnu.R;
import com.mess.ordermess.adapter.LoadImage.GetImageBitmap;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.dao.ShopMsgInfo;
import com.mess.ordermess.utils.DensityUtils;

public class OrderShopAdapter extends BaseAdapter {

	private Activity context;
	private int type;
	private List<ShopMsgInfo> shopInfos;

	public OrderShopAdapter(Activity context, int type,
			List<ShopMsgInfo> shopInfos) {
		this.context = context;
		this.type = type;
		this.shopInfos = shopInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopInfos.size();
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
		return viewShop(position, convertView);
	}

	private View viewShop(int position, View convertView) {
		View view;
		final ViewShopHolder holder;
		if (convertView != null && convertView instanceof RelativeLayout) {
			view = convertView;
			holder = (ViewShopHolder) convertView.getTag();
		} else {
			view = View.inflate(context, R.layout.order_mess_list_item0, null);
			holder = new ViewShopHolder();
			holder.iv_mess_icon = (ImageView) view
					.findViewById(R.id.iv_mess_icon);
			holder.iv_mess_name = (TextView) view
					.findViewById(R.id.iv_mess_name);
			holder.rb_mess_star = (RatingBar) view
					.findViewById(R.id.rb_mess_star);
			holder.iv_mess_describe = (TextView) view
					.findViewById(R.id.iv_mess_describe);
			holder.mv_mess_state = (ImageView) view
					.findViewById(R.id.mv_mess_state);
			view.setTag(holder);
		}

		final ShopMsgInfo infos = shopInfos.get(position);
		
		holder.iv_mess_icon.setImageResource(R.drawable.loading);

		LoadImage.LoadImageFile(context, Constants.LOADING_SAMLL_IMAGE,
				infos.getLogoUrl(), new GetImageBitmap() {

					@Override
					public void getImageBitmap(Bitmap bitmap, String cacheUrl) {
						if(DensityUtils.judgeFileSame(infos.getLogoUrl(), cacheUrl)){
							holder.iv_mess_icon.setImageBitmap(bitmap);
						}
					}

				});

		holder.iv_mess_name
				.setText((position + 1) + "." + infos.getShop_Name());
		int progress = infos.getShop_Star();
		holder.rb_mess_star.setProgress(progress);
		holder.iv_mess_describe.setText(infos.getShop_Describe());
		if (progress <= 6) {
			holder.mv_mess_state.setAlpha(progress * 30);
			holder.mv_mess_state.setImageResource(R.drawable.greenarrows);
		} else {
			holder.mv_mess_state.setAlpha(progress * 30 - 80);
			holder.mv_mess_state.setImageResource(R.drawable.redarrows);
		}
		return view;
	}

}
